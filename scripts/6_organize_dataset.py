"""
Combine QuPath-exported area and CD3 quantification files.

This script keeps the original workflow:
1. Convert tab-delimited `.txt` files to `.csv`.
2. Combine per-islet area information with CD3-positive cell counts.
3. Remove rows with both Glucagon and Insulin area equal to zero.
4. Summarize per-slide INS/CD3 combinations.
"""

from __future__ import annotations

import argparse
from pathlib import Path
from typing import Iterable

import numpy as np
import pandas as pd


AREA_COLUMNS = [
    "Class",
    "Parent",
    "Area µm^2",
    "Length µm",
    "Max diameter µm",
    "Min diameter µm",
]

CD3_COLUMNS = ["Name", "Num Positive"]

OUTPUT_COLUMNS = [
    "Name",
    "Num Positive",
    "Glucagon",
    "Insulin",
    "Background",
    "Length",
    "Max_diameter",
    "Min_diameter",
]


def pad_along_axis(array: np.ndarray, target_length: int, axis: int = 0) -> np.ndarray:
    """Pad a NumPy array with zeros along one axis.

    This helper is retained for compatibility with the original script, although the
    cleaned workflow below no longer depends on it directly.
    """
    pad_size = target_length - array.shape[axis]
    if pad_size <= 0:
        return array

    pad_width = [(0, 0)] * array.ndim
    pad_width[axis] = (0, pad_size)

    return np.pad(array, pad_width=pad_width, mode="constant", constant_values=0)


def _resolve_path(path: str | Path, base: Path | None = None) -> Path:
    """Return an absolute path while preserving normal relative-path behavior."""
    path = Path(path)
    if path.is_absolute():
        return path
    return (base or Path.cwd()) / path


def _resolve_child_folder(parent: str | Path, child: str | Path) -> Path:
    """Resolve a child folder under a parent folder.

    The original script used values such as `"/organized"` as a suffix. To remain
    compatible, a leading slash in `child` is treated as a subfolder name rather
    than a filesystem root.
    """
    parent_path = _resolve_path(parent)
    child_str = str(child).strip()

    if child_str.startswith(("/", "\\")):
        return parent_path / child_str.lstrip("/\\")

    child_path = Path(child_str)
    return child_path if child_path.is_absolute() else parent_path / child_path


def _validate_columns(df: pd.DataFrame, required_columns: Iterable[str], file_path: Path) -> None:
    """Raise a clear error if a required column is missing."""
    missing_columns = [column for column in required_columns if column not in df.columns]
    if missing_columns:
        missing = ", ".join(missing_columns)
        raise ValueError(f"{file_path} is missing required column(s): {missing}")


def convert_text_to_csv(folder_path: str | Path) -> None:
    """Convert tab-delimited `.txt` files in `folder_path` to `.csv` files."""
    path = _resolve_path(folder_path)

    if not path.exists():
        raise FileNotFoundError(f"Input folder does not exist: {path}")

    for txt_file in sorted(path.glob("*.txt")):
        df = pd.read_csv(txt_file, delimiter="\t")
        df.to_csv(txt_file.with_suffix(".csv"), index=False)


def _combine_islet_and_expanded_cd3(cd3_df: pd.DataFrame) -> pd.DataFrame:
    """Combine CD3 counts from each islet and its matching `_expanded` region."""
    cd3_df = cd3_df[CD3_COLUMNS].copy()
    cd3_df["Num Positive"] = pd.to_numeric(cd3_df["Num Positive"], errors="coerce").fillna(0)

    expanded_df = cd3_df.rename(
        columns={
            "Name": "Expanded Name",
            "Num Positive": "Expanded Num Positive",
        }
    )

    base_df = cd3_df.copy()
    base_df["Expanded Name"] = base_df["Name"].astype(str) + "_expanded"

    combined = base_df.merge(expanded_df, on="Expanded Name", how="inner")
    combined["Num Positive"] = combined["Num Positive"] + combined["Expanded Num Positive"]

    return combined[["Name", "Num Positive"]]


def _summarize_area_information(areas_df: pd.DataFrame) -> pd.DataFrame:
    """Summarize Glucagon, Insulin, Background, and size information by islet."""
    areas_df = areas_df[AREA_COLUMNS].copy()

    valid_classes = ["Glucagon", "Insulin", "Background"]
    mask = areas_df["Class"].isin(valid_classes)
    mask &= ~areas_df["Parent"].astype(str).str.contains("expanded", case=False, na=False)
    areas_df = areas_df.loc[mask].copy()

    numeric_columns = ["Area µm^2", "Length µm", "Max diameter µm", "Min diameter µm"]
    for column in numeric_columns:
        areas_df[column] = pd.to_numeric(areas_df[column], errors="coerce").fillna(0)

    area_wide = (
        areas_df.pivot_table(
            index="Parent",
            columns="Class",
            values="Area µm^2",
            aggfunc="last",
            fill_value=0,
        )
        .reset_index()
        .rename_axis(columns=None)
    )

    for column in ["Glucagon", "Insulin", "Background"]:
        if column not in area_wide.columns:
            area_wide[column] = 0

    size_summary = (
        areas_df.groupby("Parent", as_index=False)
        .agg(
            Length=("Length µm", "max"),
            Max_diameter=("Max diameter µm", "max"),
            Min_diameter=("Min diameter µm", "max"),
        )
    )

    return area_wide.merge(size_summary, on="Parent", how="left")


def organize_informations(convert_folder: str | Path, organized_folder: str | Path) -> None:
    """Combine `*_areas.csv` and `*_cd3.csv` files into per-islet organized files."""
    read_path = _resolve_path(convert_folder)
    save_path = _resolve_child_folder(read_path, organized_folder)
    save_path.mkdir(parents=True, exist_ok=True)

    if not read_path.exists():
        raise FileNotFoundError(f"Input folder does not exist: {read_path}")

    area_files = sorted(read_path.glob("*_areas.csv"))

    for areas_file in area_files:
        svs_fname = areas_file.name.removesuffix("_areas.csv")
        cd3_file = read_path / f"{svs_fname}_cd3.csv"

        if not cd3_file.exists():
            print(f"Skipping {svs_fname}: missing {cd3_file.name}")
            continue

        print(f"Processing {svs_fname}...")

        areas_df = pd.read_csv(areas_file)
        cd3_df = pd.read_csv(cd3_file)

        _validate_columns(areas_df, AREA_COLUMNS, areas_file)
        _validate_columns(cd3_df, CD3_COLUMNS, cd3_file)

        combined_cd3 = _combine_islet_and_expanded_cd3(cd3_df)
        area_summary = _summarize_area_information(areas_df)

        results_df = combined_cd3.merge(
            area_summary,
            left_on="Name",
            right_on="Parent",
            how="left",
        ).drop(columns=["Parent"], errors="ignore")

        for column in OUTPUT_COLUMNS:
            if column not in results_df.columns:
                results_df[column] = 0

        results_df = results_df[OUTPUT_COLUMNS].fillna(0)
        results_df.to_csv(save_path / f"{svs_fname}.csv", index=False)

        print("Done\n")


def get_ins_cd3_info(organized_folder: str | Path, final_folder: str | Path) -> None:
    """Remove rows where both Glucagon and Insulin are zero."""
    organized_path = _resolve_path(organized_folder)
    save_path = _resolve_path(final_folder)
    save_path.mkdir(parents=True, exist_ok=True)

    if not organized_path.exists():
        raise FileNotFoundError(f"Organized folder does not exist: {organized_path}")

    for csv_file in sorted(organized_path.glob("*.csv")):
        df = pd.read_csv(csv_file)

        # Backward compatibility with CSVs generated with an index column.
        unnamed_columns = [column for column in df.columns if column.startswith("Unnamed")]
        if unnamed_columns:
            df = df.drop(columns=unnamed_columns)

        _validate_columns(df, ["Glucagon", "Insulin"], csv_file)

        df["Glucagon"] = pd.to_numeric(df["Glucagon"], errors="coerce").fillna(0)
        df["Insulin"] = pd.to_numeric(df["Insulin"], errors="coerce").fillna(0)

        filtered_df = df.loc[~((df["Glucagon"] == 0) & (df["Insulin"] == 0))].copy()
        filtered_df.to_csv(save_path / f"organized_{csv_file.name}", index=False)


def add_combination_ins_cd3_info(main_path: str | Path, filtered_folder: str | Path | None = None) -> None:
    """Create a per-slide summary of INS+/− and CD3+/− islet counts.

    `filtered_folder` is kept only for compatibility with the original function
    signature. The original implementation did not use this argument.
    """
    path = _resolve_path(main_path)
    save_path = path / "per_slide_data"
    save_path.mkdir(parents=True, exist_ok=True)

    if not path.exists():
        raise FileNotFoundError(f"Filtered folder does not exist: {path}")

    rows: list[dict[str, int | str]] = []

    for csv_file in sorted(path.glob("*.csv")):
        df = pd.read_csv(csv_file)

        unnamed_columns = [column for column in df.columns if column.startswith("Unnamed")]
        if unnamed_columns:
            df = df.drop(columns=unnamed_columns)

        _validate_columns(df, ["Num Positive", "Insulin"], csv_file)

        df["Num Positive"] = pd.to_numeric(df["Num Positive"], errors="coerce").fillna(0)
        df["Insulin"] = pd.to_numeric(df["Insulin"], errors="coerce").fillna(0)

        image_id = csv_file.stem.split("_")[-1]
        cd3_positive = df["Num Positive"] > 6
        insulin_positive = df["Insulin"] > 0

        rows.append(
            {
                "Image id": image_id,
                "Islets": len(df),
                "ins+ cd3+": int((insulin_positive & cd3_positive).sum()),
                "ins+ cd3-": int((insulin_positive & ~cd3_positive).sum()),
                "ins- cd3+": int((~insulin_positive & cd3_positive).sum()),
                "ins- cd3-": int((~insulin_positive & ~cd3_positive).sum()),
                "Insulitis": int(cd3_positive.sum()),
            }
        )

    summary_df = pd.DataFrame(
        rows,
        columns=[
            "Image id",
            "Islets",
            "ins+ cd3+",
            "ins+ cd3-",
            "ins- cd3+",
            "ins- cd3-",
            "Insulitis",
        ],
    )
    summary_df.to_csv(save_path / "per_slide_info.csv", index=False)


def parse_args() -> argparse.Namespace:
    """Parse command-line arguments."""
    parser = argparse.ArgumentParser(
        description="Combine area and CD3 quantification files into per-islet and per-slide CSVs."
    )
    parser.add_argument(
        "--text_folder",
        type=str,
        default="additional/T1D",
        help="Folder containing text files and converted CSV files.",
    )
    parser.add_argument(
        "--organized_csv_folder",
        type=str,
        default="/organized",
        help="Output subfolder for organized per-islet CSV files.",
    )
    parser.add_argument(
        "--organized_folder",
        type=str,
        default="additional/T1D/organized",
        help="Folder containing organized per-islet CSV files.",
    )
    parser.add_argument(
        "--final_folder",
        type=str,
        default="additional/T1D/final",
        help="Output folder for filtered per-islet CSV files.",
    )
    return parser.parse_args()


def main() -> None:
    """Run the full original workflow."""
    args = parse_args()

    # Per-slide data
    convert_text_to_csv(args.text_folder)
    organize_informations(args.text_folder, args.organized_csv_folder)
    get_ins_cd3_info(args.organized_folder, args.final_folder)

    # Per-donor / per-slide summary
    add_combination_ins_cd3_info(args.final_folder, args.final_folder)


if __name__ == "__main__":
    main()
