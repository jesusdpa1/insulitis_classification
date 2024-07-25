# Color Deconvolution

1. Open the project → [Project name] → Open image
2. Run the script `1_color-deconvolution.groovy`
3. Go to the image tab
   
   ![Untitled](/img/islet-segmentation-advance/Untitled.png)
    
4. Create a square annotation around each red-ish(insulin), blue-ish(glucagon), brown-ish(cd3+) color and select the annotation that correspond to each channel.

    ![color_deconvolution_1](https://github.com/user-attachments/assets/33635abd-2fd7-4fa9-9b6a-f38e986318b2)

    ![Untitled](/img/islet-segmentation-advance/Untitled2.png)
    
5. Double click each channel and update the values, repeat this step until you have the three colors stain vectors
    
    ![Untitled](/img/islet-segmentation-advance/Untitled3.png)
    
6. Remove all the annotations
