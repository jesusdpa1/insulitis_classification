/*
Get bounding box 

Color base thresholding and NN base detection of islet are not precise, yielding segmentation boundries 
that include non islet features but allows to find the location of the islet in the IHC image.
This script allows to extract the bounding box of the islet proving a NN base detection of an islet

This is later use with SAM to segmentate the islet.
*/


/* Loads the libraries needed to run this script */
import static qupath.lib.gui.scripting.QPEx.*

//defines the bounding box size
def move_left_corner = 10
def expand_box = move_left_corner * 2

// gets the image to process
def plane = getCurrentViewer().getImagePlane()

// select all the islets annotations adquired with threshold method

// creates a place holder list to store all the islets for iteration
listOfIslets = []

// saves all the annotations information define as islet
getAnnotationObjects().each{
    if(it.getPathClass() == getPathClass("Islet")){

        listOfIslets << it

    }
}

// creates a place holder list to store all the bounding box information
boundingAnnotation = []

rois = getAnnotationObjects().collect{it.getROI()}
// iteretes through each annotation and gets the bounding box
rois.each {
    
    boundingROI = ROIs.createRectangleROI(
            it.getBoundsX() - move_left_corner, //top left corner
            it.getBoundsY() - move_left_corner, //top left corner
            it.getBoundsWidth() + expand_box, //width
            it.getBoundsHeight() + expand_box, //height
            plane)
   boundingAnnotation << PathObjects.createAnnotationObject(boundingROI)
}

//adds all the new bounding box objects to the image
addObjects(boundingAnnotation)
// removes the previously define islet object
removeObjects(listOfIslets, true)