# [Islet segmentation Basic]

After creating a project in QuPath and uploading the images, we can start segmenting the islets.


1. Open one image
![main](/img/islet-segm_main.png)
2. Using the rectangle tool, select the islets **[Press -> R]**
![first-crop](/img/iselt-segm_example-first-crop.png)
3. Continue choosing all the islets that will be run through the segmentation algorithm
![multi-example](/img/iselt-segm_example-multi-crop.png)
4. After selecting all the islets, open the **SAM API** extension
![SAM](/img/islet-segm_SAM.png)
5. Select all the annotations
![SAM-select-all](img/iselt-segm_SAM-ALL.png)
6. Press **Run for Selected**
7. Wait for the segmentation to finish
8. Save the data
![SAM-segmentation](/img/iselt-segm_SAM-ALL-Process.png)


# Islet segmentation Advanced

1. Open one image
2. Go to **Classify** -> **Pixel classification** -> **Create Threshold**
3. create a new Threshold where the class is Islet and the 
   * Adjust properly the values where the majority of the islets are selected
![threshold](/img/islet_create-threshold.png)
4. Go through the image and remove non islet objects, create annotations for those islets using the previous method
5. Repeat steps 4-8 from the basic segmentation [this step will take time]
![fast-SAM-output](img/iselt-segm_example-fast-SAM.png)
![fast-SAM-output_zoom](img/iselt-segm_example-fast-SAM-zoom.png)
### As you can see, the output is not as good as the basic segmentation, but it is faster and can be used as a starting point for the basic segmentation