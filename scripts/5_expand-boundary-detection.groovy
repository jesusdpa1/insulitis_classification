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

def suffixName = "expanded"
def counter = 1

double expandMarginMicrons = 20.0

// Extract the main info we need
def imageData = getCurrentImageData()
def hierarchy = imageData.getHierarchy()
def server = imageData.getServer()
// We need the pixel size
def cal = server.getPixelCalibration()
def plane = ImagePlane.getDefaultPlane()

if (!cal.hasPixelSizeMicrons()) {
    print 'We need the pixel size information here!'
    return
}

double expandPixels = expandMarginMicrons / cal.getAveragedPixelSizeMicrons()

def annotations = getAnnotationObjects()

def isletsList = annotations.findAll{it.getPathClass() == getPathClass("Islet")}

annotationToAdd = []

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

annotationToAdd

addObjects(annotationToAdd)