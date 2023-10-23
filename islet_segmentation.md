# Islet segmentation 

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
