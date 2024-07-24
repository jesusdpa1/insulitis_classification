1. Color Deconvolution
   a. Run '1_color-deconvolution.groovy'
   b. Find proper red-ish, blue-ish, brown-ish stain
   c. Draw square box at each stains designate each stains with Insulin(red-ish), Glucagon(blue-ish), and CD3+(brown-ish) stain.
2. Construction ROI boxes
   a. Find proper islet and make squared ROI box.
   b. Apply SAM to the ROI box and designate as Islet Class.
   c. Make square box at the non-islet area and designate as Background Class.
   d. Train pixel classifier with changing the Channels(Insluin, Glucagon, CD3+) in Features edit. [FIGURE]
   e. Save pixel classifier as '{slide_number}_classifier'.
   e. Remove all squares in image and apply trained classifier, and remove all the Background annotation on the pannels.
   f. Run '2_get-bounding-box.groovy' to get islet ROI boxes.
   g. Manual modification
3. Islet Segmentation using SAM
   a. Select all ROI boxes and apply SAM with the 20um mignification.
4. Segmentation Insulin and Glucagon
   a. Draw square to the Insulin(red-ish), Glucagon(blue-ish) and non-islet area, and designate them into Insulin, Glucagon, Background Class.
   b. Train pixel classifier with changing the Channels(Insluin, Glucagon, CD3+) in Features edit.
   c. Set resolution between 'High' or 'Very high'.
   d. Save pixel classifier as '{slide_number}_BGI'.
   e. Remove 3 compartment training squared box and run '3_rename-islets.groovy'.
   f. Apply trained pixel classifier
5. CD3+ Cell Detection
   a. Run '4_expand-boundary-detection.groovy' to get 20um boundary object named '{number}_islet_expanded'.
   b. Run '5_positive-cell-detection_CD3+.groovy' to quantify the number of CD3+ cells.
6. Export Measurement for Quantification
   a. For calculating the area of insulin and glucagon, select all detections (Objects -> Select -> Select detections -> Select all detections) and calculate the measurements(Analyze -> Calculate features -> Add shape features).
   b. Saving area of insulin and glucagon(Measure -> Show detection measurements), and save as '{slide_number}_areas.txt'.
   c. Saving the number of CD3+ cells(Measure -> Show annotation measurements), and save as '{slide_number}_cd3+.txt'.
   d. Run '6_Quantification_measurement' to combine measurements data and save it as CSV format.
