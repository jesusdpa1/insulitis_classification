# Color extraction

> [!WARNING]
> ⚠️ For this step you will need to have all the islets annotated using SAM, and should be run after color deconvolution

1. first, lets create the NN to extract the colors
    ![Image Opend](/img/color-extraction/image.png)
    > [!NOTE]
    > 📝 This step is only needed once per project and requires for you to know the color scheme
    > In other words, what color corresponds to each biological marker.
    > EX. Insulin = red, Glucagon = Blue, CD3+ = Brown
2. create an annotation for each color and assign the color to a class. **Remember to also include a class for what is consider the background**
    ![Annotation with wand tool](/img/color-extraction/image-1.png)
    ![Annotation finished](/img/color-extraction/image-2.png)
3. GoTo **Classify** -> **Train Pixel Classifier** or **CNTRL+SHIFT+P**
    ![Alt text](/img/color-extraction/image-3.png)
4. Select the following options:
   * **Classifier:** ANN_MLP
   * **Resolution:** Very High
   * **Output type:** Classification
   * **Region:** Any annotation bounds
5. GoTo **Features** and press **Edit**
    ![Alt text](/img/color-extraction/image-4.png)
6. Select the following options:
   * **Channels:** Glucagon, Insulin, CD3+
   * **Scales:** 1
   * **Features:** 1-> Gaussian
   * **Local normalization scale:** 8
7. Press -> **Live Prediction** to evaluate the result and compare the output with the original image using the C scroll bar option 
    ![Alt text](/img/color-extraction/image-5.png)
8. After the result has been evaluated, proceed to choose the name by the following rule:
   * [Colors]_[Slide_Number]
    > Warning
    > ⚠️ There is a chance that the classifier might not be optimal for different slides of similar colors due to small difference, an easy solution is to create a new classifier for that slide and add the slide number to the name. A more advance solution is to give the classifier more examples of the color you want to extract by including **Load training**.
9. After the classifier has been save, proceed to close the window. Delete the annotations created and load the Islet annotations
    ![Alt text](/img/color-extraction/image-6.png)
10.  GoTo **Classify** -> **Load Pixel Classifer**
11. Choose the classifier you want to use
   ![Alt text](/img/color-extraction/image-7.png)
12. and choose the following options 
    ![Alt text](/img/color-extraction/image-8.png)
    > Warning 
    > ⚠️ Avoid splitting objects since we want to measure the total area of the different markers withing the islet 
13. Press **OK** and wait for the classifier to finish and evaluate the result 
    ![Alt text](/img/color-extraction/image-9.png)

