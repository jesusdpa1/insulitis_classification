# Workflow Instruction 
## 1. Color Deconvolution
- Run **_1_color-deconvolution.groovy_**
- Find proper red-ish, blue-ish, brown-ish stain
- Draw square box at each stains designate each stains with Insulin(red-ish), Glucagon(blue-ish), and CD3+(brown-ish) stain.

## 2. Construction ROI boxes
- Find proper islet and make squared ROI box.
- Apply SAM to the ROI box and designate as Islet Class.
- Make square box at the non-islet area and designate as Background Class.
- Train pixel classifier with changing the Channels(Insluin, Glucagon, CD3+) in Features edit.
- Save pixel classifier as '{slide_number}_classifier'.
- Remove all squares in image and apply trained classifier, and remove all the Background annotation on the pannels.
- Run **_2_get-bounding-box.groovy_** to get islet ROI boxes.
- Manual modification

## 3. Islet Segmentation using SAM
- Select all ROI boxes and apply SAM with the 20um mignification.
  
## 4. Segmentation Insulin and Glucagon
- Draw square to the Insulin(red-ish), Glucagon(blue-ish) and non-islet area, and designate them into Insulin, Glucagon, Background Class.
- Train pixel classifier with changing the Channels(Insluin, Glucagon, CD3+) in Features edit.
- Set resolution between 'High' or 'Very high'.
- Save pixel classifier as '{slide_number}_BGI'.
- Remove 3 compartment training squared box and run **_3_rename-islets.groovy'_**
- Apply trained pixel classifier
  
## 5. CD3+ Cell Detection
- Run **_4_expand-boundary-detection.groovy_** to get 20um boundary object named '{number}_islet_expanded'.
- Run **_5_positive-cell-detection_CD3+.groovy_** to quantify the number of CD3+ cells.
  
## 6. Export Measurement for Quantification
- For calculating the area of insulin and glucagon, select all detections (Objects -> Select -> Select detections -> Select all detections) and calculate the measurements(Analyze -> Calculate features -> Add shape features).
- Saving area of insulin and glucagon(Measure -> Show detection measurements), and save as '{slide_number}_areas.txt'.
- Saving the number of CD3+ cells(Measure -> Show annotation measurements), and save as '{slide_number}_cd3+.txt'.
- Run **_6_Quantification_measurement_** to combine measurements data and save it as CSV format.
