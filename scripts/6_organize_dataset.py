import os
import glob
import argparse
import numpy as np
import pandas as pd


def pad_along_axis(array: np.ndarray, target_length: int, axis: int = 0) -> np.ndarray:

    pad_size = target_length - array.shape[axis]

    if pad_size <= 0:
        return array

    npad = [(0, 0)] * array.ndim
    npad[axis] = (0, pad_size)

    return np.pad(array, pad_width=npad, mode='constant', constant_values=0)


def convert_text_to_csv(folder_path):
    r"""
        This function convert text file into csv file.
    """
    current_path = os.getcwd()
    path = os.path.join(current_path, folder_path)
    os.chdir(path)

    for txt_file in os.listdir():
        if txt_file.endswith('.txt'):
            f_path = f"{path}/{txt_file}"
            read_file = pd.read_csv(f_path, delimiter='\t')
            saved_fname = txt_file.split('.')[0]
            read_file.to_csv(f'{saved_fname}.csv', index='\t')
    print("File converted DONE!")


def organize_informations(convert_folder, organized_folder):
    r"""
        This function pull out the necessary information from *_areas.csv & *_cd3.csv.
        * Areas of Insulin, Glucagon, Backgrounds
        * Number of CD3+ cells

        Combine those informations into single file and save into 'final' folder.
    """
    # Define save folder
    current_path = os.getcwd()
    csv_path = f'{convert_folder}/{organized_folder}'
    saved_path = os.path.join(current_path, csv_path)
    os.makedirs(saved_path, exist_ok=True)    

    # Designate svs file
    read_path = os.path.join(current_path, convert_folder) 
    os.chdir(read_path)
    file_list = []
    for csv_file in os.listdir():
        if csv_file.endswith('_areas.csv'):
            file_name = csv_file.split('_')[0]
            file_list.append(file_name)
    
    for svs_fname in file_list:
        print(f"Processing {svs_fname}...")

        areas_fname = f'{read_path}/{svs_fname}_areas.csv'
        cd3_fname = f'{read_path}/{svs_fname}_cd3+.csv'
    
        # Read csv file
        areas_df = pd.read_csv(areas_fname)
        cd3_df = pd.read_csv(cd3_fname)
        
        # Extract class, islet, area
        areas_header = ['Class', 'Parent', 'Area µm^2', 'Length µm','Max diameter µm', 'Min diameter µm']
        cd3_header = ['Name', 'Num Positive']
        trimmed_area_df = areas_df[areas_header]
        trimmed_cd3_df = cd3_df[cd3_header]
        
        # Extract glucagon & insulin area
        glu_ins_df = trimmed_area_df[trimmed_area_df['Class'].isin(['Glucagon', 'Insulin', 'Background'])]
        # Except expanded islets
        final_glu_ins_df = glu_ins_df[~glu_ins_df['Parent'].str.contains('expanded')].set_index('Parent').reset_index()
        
        # Combine CD3+ cells islet + expanded
        combined_islet_name = []
        combined_cd3_num = []
        for i in range(len(trimmed_cd3_df['Name'])):
            islet_name = trimmed_cd3_df['Name'][i]            
            expanded_islet_name = islet_name + '_expanded'
            expanded_islet_df = trimmed_cd3_df[trimmed_cd3_df['Name'].str.contains(expanded_islet_name)]
            if len(expanded_islet_df) != 0:
                num_expanded_cd3 = expanded_islet_df['Num Positive'].values[0]
                num_cd3 = trimmed_cd3_df['Num Positive'][i]
                sum_num_cd3 = num_expanded_cd3 + num_cd3
                combined_islet_name.append(str(islet_name))
                combined_cd3_num.append(int(sum_num_cd3)) 
        final_combined_cd3 = pd.DataFrame(list(zip(combined_islet_name, combined_cd3_num)), columns = ['Name', 'Num Positive'])
        
        # Combine information of areas and cd3 cells
        numpy_glu_ins = final_glu_ins_df.to_numpy()
        numpy_combined_cd3 = final_combined_cd3.to_numpy()
    
        # Pad each indexes with 3 zeros 
        padded_numpy_combined_cd3 = pad_along_axis(numpy_combined_cd3, 8, axis=1)
        # Substitue zeros to areas of Insulin or Glucagon
        for i in range(len(numpy_glu_ins)):
            for j in range(len(padded_numpy_combined_cd3)):
                if numpy_glu_ins[i][0] == padded_numpy_combined_cd3[j][0]:
                    if numpy_glu_ins[i][1] == 'Glucagon':
                        padded_numpy_combined_cd3[j][2] = numpy_glu_ins[i][2] # Area
                        if numpy_glu_ins[i][3] > padded_numpy_combined_cd3[j][5]:
                            padded_numpy_combined_cd3[j][5] = numpy_glu_ins[i][3] # Length
                        if numpy_glu_ins[i][4] > padded_numpy_combined_cd3[j][6]:
                            padded_numpy_combined_cd3[j][6] = numpy_glu_ins[i][4] # Max_diameter
                        if numpy_glu_ins[i][5] > padded_numpy_combined_cd3[j][7]:
                            padded_numpy_combined_cd3[j][7] = numpy_glu_ins[i][5] # Min_diameter
                    elif numpy_glu_ins[i][1] == 'Insulin':
                        padded_numpy_combined_cd3[j][3] = numpy_glu_ins[i][2]
                        if numpy_glu_ins[i][3] > padded_numpy_combined_cd3[j][5]:
                            padded_numpy_combined_cd3[j][5] = numpy_glu_ins[i][3] # Length
                        if numpy_glu_ins[i][4] > padded_numpy_combined_cd3[j][6]:
                            padded_numpy_combined_cd3[j][6] = numpy_glu_ins[i][4] # Max_diameter
                        if numpy_glu_ins[i][5] > padded_numpy_combined_cd3[j][7]:
                            padded_numpy_combined_cd3[j][7] = numpy_glu_ins[i][5] # Min_diameter
                    elif numpy_glu_ins[i][1] == 'Background':
                        padded_numpy_combined_cd3[j][4] = numpy_glu_ins[i][2]
                        if numpy_glu_ins[i][3] > padded_numpy_combined_cd3[j][5]:
                            padded_numpy_combined_cd3[j][5] = numpy_glu_ins[i][3] # Length
                        if numpy_glu_ins[i][4] > padded_numpy_combined_cd3[j][6]:
                            padded_numpy_combined_cd3[j][6] = numpy_glu_ins[i][4] # Max_diameter
                        if numpy_glu_ins[i][5] > padded_numpy_combined_cd3[j][7]:
                            padded_numpy_combined_cd3[j][7] = numpy_glu_ins[i][5] # Min_diameter
        # Save ['islet_name', num_cd3+, glucagon_area, insulin_area] -> excel 
        results_df = pd.DataFrame(padded_numpy_combined_cd3, 
                                  columns=['Name', 'Num Positive', 'Glucagon', 'Insulin', 
                                           'Background', 'Length', 'Max_diameter','Min_diameter'])
        results_df.to_csv(f'{saved_path}/{svs_fname}.csv')
        print("Done\n")


def get_ins_cd3_info(origin_folder, organized_folder, final_folder):
    r"""
        This function get rid of Glucagon & Insulin numbered 0.
    """
    current_path = os.getcwd()
    
    # Make file path
    save_path = os.path.join(current_path, f'{origin_folder}/{final_folder}')
    organized_path = os.path.join(current_path, f'{origin_folder}/{organized_folder}')    
    os.makedirs(save_path, exist_ok=True)

    organized_file_list = os.listdir(organized_path)

    for files in organized_file_list:
        # Filter Glucagon & Insulin = 0
        read_organized_file = os.path.join(organized_path, files)
        quan_df = pd.read_csv(read_organized_file)
        filtered_index = quan_df[(quan_df['Glucagon'] == 0) & (quan_df['Insulin'] == 0)].index
        quan_df.drop(filtered_index, inplace=True)
    
        quan_df2 = quan_df.drop(quan_df.columns[0],axis=1)
        quan_df2.to_csv(f'{save_path}/organized_{files}')


def add_combination_ins_cd3_info(main_path, filtered_folder):
    current_path = os.getcwd()
    filtered_path = main_path
    path = os.path.join(current_path, filtered_path)
    
    saved_path = os.path.join(path, 'per_slide_data')
    os.makedirs(saved_path, exist_ok=True)
    os.chdir(path)

    new_df = pd.DataFrame(columns=['Image id', 'Islets',
                                   'ins+ cd3+', 'ins+ cd3-', 'ins- cd3+', 'ins- cd3-',
                                   'Insulitis'])
    for csv_file in os.listdir():
        if csv_file.endswith('.csv'):
            f_path = f"{path}/{csv_file}"
            df = pd.read_csv(f_path)
            data_list = []
            data_list.append(csv_file.split('.')[0].split('_')[-1])

            ins_p_cd_p = df[(df['Num Positive']>6) & (df['Insulin']>0)].index
            ins_p_cd_n = df[(df['Num Positive']<7) & (df['Insulin']>0)].index
            ins_n_cd_p = df[(df['Num Positive']>6) & (df['Insulin']==0)].index
            ins_n_cd_n = df[(df['Num Positive']<7) & (df['Insulin']==0)].index
           
            num_insulitis = df[df['Num Positive']>6].index
 
            data_list.append(len(df))
            data_list.append(len(ins_p_cd_p))
            data_list.append(len(ins_p_cd_n))
            data_list.append(len(ins_n_cd_p))
            data_list.append(len(ins_n_cd_n))
            data_list.append(len(num_insulitis))

            new_df = new_df._append(pd.Series(data_list, index=['Image id', 'Islets',
                                                               'ins+ cd3+', 'ins+ cd3-', 'ins- cd3+', 'ins- cd3-',
                                                               'Insulitis']), ignore_index=True)
    new_df.to_csv(f'{saved_path}/per_slide_info.csv')

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--origin_folder", type= str, default= 'T1D',
                        help="folder path for convert text to csv file")
    parser.add_argument("--organized_csv_folder", type= str, default= 'combined',
                        help="pulled out folder name")
    parser.add_argument("--final_folder", type= str, default= 'final',
                        help="filtered folder name")
    args = parser.parse_args()

    ### per slide data ###
    # Convert text files (areas + cd3) into combined csv file
    convert_text_to_csv(args.origin_folder)
    # Re-organize the data shape what we want to see
    organize_informations(args.origin_folder, args.organized_csv_folder)
    # Get rid of islets that contains the area of ins==0 & glu==0
    get_ins_cd3_info(args.origin_folder, args.organized_csv_folder, args.final_folder)

    ### per donor data ###
    # Pull out information of INS+/- & CD3+ +/- 
    add_combination_ins_cd3_info(args.final_folder, args.final_folder)
