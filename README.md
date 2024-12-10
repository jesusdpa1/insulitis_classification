# H&E Diabetes Slide Analysis Toolkit

This repo contains files with information on how to extract features to study insulitis in H&E slides.

The folder scripts contain the most recently developed scripts to analyze the images

The scripts have been developed using Groovy-Language

QuPath[^1] is the main tool for visualization and processing

## Publication

Leveraging pre-trained machine learning models for islet quantification in type 1 diabetes, Journal of Pathology Informatics, 16: 100406, ISSN 2153-3539 (2025)

https://doi.org/10.1016/j.jpi.2024.100406.

Abstract: Human islets display a high degree of heterogeneity in terms of size, number, architecture, and endocrine cell-type compositions. An ever-increasing number of immunohistochemistry-stained whole slide images (WSIs) are available through the online pathology database of the Network for Pancreatic Organ donors with Diabetes (nPOD) program at the University of Florida (UF). We aimed to develop an enhanced machine learning-assisted WSI analysis workflow to utilize the nPOD resource for analysis of endocrine cell heterogeneity in the natural history of type 1 diabetes (T1D) in comparison to donors without diabetes. To maximize usability, the user-friendly open-source software QuPath was selected for the main interface. The WSI data were analyzed with two pre-trained machine learning models (i.e., Segment Anything Model (SAM) and QuPath's pixel classifier), using the UF high-performance-computing cluster, HiPerGator. SAM was used to define precise endocrine cell and cell grouping boundaries (with an average quality score of 0.91 per slide), and the artificial neural network-based pixel classifier was applied to segment areas of insulin- or glucagon-stained cytoplasmic regions within each endocrine cell. An additional script was developed to automatically count CD3+ cells inside and within 20 μm of each islet perimeter to quantify the number of islets with inflammation (i.e., CD3+ T-cell infiltration). Proof-of-concept analysis was performed to test the developed workflow in 12 subjects using 24 slides. This open-source machine learning-assisted workflow enables rapid and high throughput determinations of endocrine cells, whether as single cells or within groups, across hundreds of slides. It is expected that the use of this workflow will accelerate our understanding of endocrine cell and islet heterogeneity in the context of T1D endotypes and pathogenesis.

## Resource to learn QuPath

[a] https://www.imagescientist.com/qupath-intro

[b] https://qupath.readthedocs.io/en/stable/docs/tutorials/index.html

## References

[^1]: https://qupath.github.io/
