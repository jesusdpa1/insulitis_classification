# QuPath - Islet segmentation

Created time: October 26, 2023 1:12 PM

> [!WARNING]
> ⚠️ Remember to save the files while working with them!

1. Open the project → Project… → Open
2. Run the script `color-deconvolution.groovy`
3. Go to the image tab 
    ![Untitled](/img/islet-segmentation-advance/Untitled.png)
    
4. Create a square annotation around each distinct color and select the annotation that correspond to each channel. Each annotation has to have only the stain vector
    
    ![Untitled](/img/islet-segmentation-advance/Untitled1.png)
    
    ![Untitled](/img/islet-segmentation-advance/Untitled2.png)
    
5. Double click each channel and update the values, repeat this step until you have the three colors stain vectors
    
    ![Untitled](/img/islet-segmentation-advance/Untitled3.png)
    
6. Remove the annotations made
7. Now Run the script `color-extraction.groovy`
8. open the new image
9. Go to Classify → Pixel Classification → create threshold 
    1. set: 
    2. **Resolution** → Very high
    3. **Channel** → Average channels
    4. **Pre-filter** → Morphological closing
    5. `Adjust the remaining values accordingly` 
    6. **Above threshold** → Islet
    7. **Below threshold** → Unclassified
    8. **Classifier name** → assign a classifier name
        
        ![Untitled](/img/islet-segmentation-advance/Untitled4.png)
        

10. Press create objects, and adjust the values according to the image:
    
    ![Untitled](/img/islet-segmentation-advance/Untitled5.png)
    
11. Output result should look something like this 
    
    ![Untitled](/img/islet-segmentation-advance/Untitled6.png)
    
12. Now, go through each annotation by zooming in the image and pressing double click on the annotation name, this will take you to the location of the annotation
13. Using the scroll bar on the top remove and reinstate the delineation of the detection
    
    ![Untitled](/img/islet-segmentation-advance/Untitled7.png)
    
14. If its not an islet or a positive cell delete the annotation
15. Remember to save throughout this process and the end of the analysis
