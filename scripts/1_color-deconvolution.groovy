// Sets the image type to BRIGHTFIELD_OTHER
setImageType('BRIGHTFIELD_OTHER');
/* Set the deconvolution values to standardize names and ranges based on 
Glucagon -> Blue
Insulin -> Red
CD3+ -> Brown
Tissue & Background -> anything else

WARNING: 
Colors must be adjusted depending on the image used, some images might have 
different colors for each of the stains
*/
setColorDeconvolutionStains("""{
    "Name" : "HE_Insulitis",
    "Stain 1" : "Glucagon",
    "Values 1" : "0.694 0.634 0.342",
    "Stain 2" : "Insulin",
    "Values 2" : "0.322 0.778 0.54",
    "Stain 3" : "CD3+",
    "Values 3" : "0.38 0.579 0.721",
    "Stain 4" : "Tissue",
    "Values 4" : "0.38 0.579 0.721",
    "Background" : "255 255 255"
}""");