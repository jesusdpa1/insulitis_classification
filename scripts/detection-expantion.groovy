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


def isletsList = getAnnotationObjects().find{it.getPathClass() == getPathClass("Islet")}

annotationToAdd = []

isletsList.each{ it ->
        currentArea = it.getROI().getGeometry()
        areaExpansion = currentArea.buffer(expandPixels)

        roiExpansion = GeometryTools.geometryToROI(areaExpansion, plane)
        annotationExpansion = PathObjects.createAnnotationObject(roiExpansion, getPathClass("IsletExpanded"))
        annotationToAdd << annotationExpansion

}

annotationToAdd

addObjects(annotationToAdd)