/// import libraries
import qupath.lib.objects.PathAnnotationObject
import qupath.lib.objects.PathObject
import static qupath.lib.scripting.QP.*
import static qupath.lib.gui.scripting.QPEx.*


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

//
def annotations = hierarchy.getAnnotationObjects()


//
def jsonParams = """{
    "detectionImageBrightfield": "Optical density sum",
    "requestedPixelSizeMicrons": 1.0,
    "backgroundRadiusMicrons": 8.0,
    "backgroundByReconstruction": true,
    "medianRadiusMicrons": 0.0,
    "sigmaMicrons": 1.5,
    "minAreaMicrons": 10.0,
    "maxAreaMicrons": 400.0,
    "threshold": 0.1,
    "maxBackground": 2.0,
    "watershedPostProcess": true,
    "cellExpansionMicrons": 5.0,
    "includeNuclei": true,
    "smoothBoundaries": true,
    "makeMeasurements": true,
    "thresholdCompartment": "Cell: CD3+ OD mean",
    "thresholdPositive1": 0.1,
    "thresholdPositive2": 0,
    "thresholdPositive3": 0,
    "singleThreshold": true
}"""

selectAnnotations();
runPlugin('qupath.imagej.detect.cells.PositiveCellDetection', jsonParams)

//
def cells = getCellObjects()

// Filter to get only 'Negative' cells
def negativeCells = cells.findAll { it.getPathClass() == getPathClass("Negative") }
removeObjects(negativeCells, true)
//

fireHierarchyUpdate()