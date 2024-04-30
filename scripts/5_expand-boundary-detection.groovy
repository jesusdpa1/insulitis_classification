/*
To determine the number of cd3+ cells interacting with an islet, 
after segmentation of islets using SAM we proceed to define the boundry
of interction through the expantion of the islet detected boundry by 
a define value (20um)

*/

// imports all the libraries needed to run this script

import qupath.lib.gui.commands.Commands
import org.locationtech.jts.geom.Geometry
import qupath.lib.common.GeneralTools
import qupath.lib.objects.PathObject
import qupath.lib.objects.PathObjects
import qupath.lib.roi.GeometryTools
import qupath.lib.roi.ROIs
import org.locationtech.jts.precision.GeometryPrecisionReducer
import org.locationtech.jts.geom.PrecisionModel
import java.awt.Rectangle
import java.awt.geom.Area

// definition of the suffix and margin boundry
def suffixName = "expanded"
def counter = 1

double expandMarginMicrons = 20.0

// Extract the main information from the image
def imageData = getCurrentImageData()
def hierarchy = imageData.getHierarchy()
def server = imageData.getServer()
// Extracts the pixel to microns information
def cal = server.getPixelCalibration()
def plane = ImagePlane.getDefaultPlane()

if (!cal.hasPixelSizeMicrons()) {
    print 'We need the pixel size information here!'
    return
}
// transforms the target um size expansion to pixel
double expandPixels = expandMarginMicrons / cal.getAveragedPixelSizeMicrons()

// gets all the annotation objects named Islet
def annotations = getAnnotationObjects()

def isletsList = annotations.findAll{it.getPathClass() == getPathClass("Islet")}

// creates a place holder to store all the new annotations
annotationToAdd = []

// base on each islet annotation, we proceed to get the expanded boundry
isletsList.each{ it ->
        def baseName = it.getName() 
        def newName = (baseName ? "${baseName}_$suffixName" : suffixName) // Generate new name
        currentArea = it.getROI().getGeometry()
        areaExpansion = currentArea.buffer(expandPixels)

        roiExpansion = GeometryTools.geometryToROI(areaExpansion, plane)
        annotationExpansion = PathObjects.createAnnotationObject(roiExpansion, getPathClass("IsletExpanded"))
        annotationExpansion.setName(newName)
        annotationToAdd << annotationExpansion

}
// adds all annotations to the image
annotationToAdd

addObjects(annotationToAdd)

selectAnnotations()
// updated the hierarchy of the objects
Commands.insertSelectedObjectsInHierarchy(imageData)

fireHierarchyUpdate()