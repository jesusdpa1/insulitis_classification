# CD3+ cell detection

> ⚠️ Warning 
> This step should be run after color deconvolution and cell[islet] segmentation.

1. Open the project in QuPath.
2. Select an image.
3. Make sure that the stain vectors are set properly. This means that glucagon insulin and CD3+ is available and calibrated.
   
    ![stain-vectors](/img/cd3+_cell-detection/image.png)
4. Make sure to have run color segmentation prior to this step.
   
    ![Color-segmentation applied](/img/cd3+_cell-detection/image-1.png)

5. GoTo **Show Script Editor** or press **CNTRL+[**
6. Open the script -> `rename-islets.groovy` this will update the name of your islets to **islet_[#]**.

    ![renaming-islets](/img/cd3+_cell-detection/image-2.png)

7. Select all the annotations and set the class to Islet.
    ![set-class-to-islet](/img/cd3+_cell-detection/image-3.png)

8. Next run the script -> `expand-boundary-detection` -> this will create additional annotations of the same islets with the expanded boundary. These islets will be name **islet_[#]_expanded**.

    ![expanded-boundary](/img/cd3+_cell-detection/image-4.png)

9. Next run the script -> `positive-cell-detection_CD3+` -> This will quantify CD3+ cells in the islets and in the expanded islet using the optical density sum image. 

![CD3+ cells](/img/cd3+_cell-detection/image-5.png)

10. All annotation measurements will be located in **Measure -> Annotations measurements**.

![Annotation-measurements](/img/cd3+_cell-detection/image-6.png)

