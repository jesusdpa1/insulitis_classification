# QuPath Pipeline

1. Open QuPath
2. Create a project and import the images


## Analysis

1. Go to **View** > **Brightness/Contrast** 
   * Select -> Optical Density Sum
   * Adjust min/max and gamma values to see the tissue
 * Go to **Extensions** > **SAM**
   * Select -> create a square ROI around the islet
   * press run 



## Change Annotations to Detections

It is important to modify the annotations of the islets to detections to be able to use the measurements of the detections and additional features of QuPath

A script has been develop to do this automatically, 

1. Assign the label Islet to the annotations of the islets
2. Go to **Automate** > **Scripts Editor**
3. Copy and paste the script name **scripts/annotations_to_detections.groovy**
4. Click on **Run**