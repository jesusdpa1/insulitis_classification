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
   * **Feature:** Channels -> Glucagon, Insulin, CD3+
   * 

   
