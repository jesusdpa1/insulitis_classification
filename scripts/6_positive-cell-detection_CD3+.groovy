/// import libraries
import qupath.lib.gui.commands.Commands;

import qupath.lib.objects.PathAnnotationObject
import qupath.lib.objects.PathDetectionObject
import qupath.lib.objects.PathObject
import qupath.lib.objects.PathCellObject
import qupath.lib.objects.classes.PathClassFactory

import qupath.lib.roi.interfaces.ROI

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

def pixelSizeMicrons = cal.getAveragedPixelSizeMicrons()
double expandPixels = expandMarginMicrons / pixelSizeMicrons


// Format the pixel size to two decimal places
def formattedPixelSize = String.format("%.2f", pixelSizeMicrons)
// Construct measurement names
def nameMean = "ROI: ${formattedPixelSize} µm per pixel: CD3+: Mean"
def nameStd = "ROI: ${formattedPixelSize} µm per pixel: CD3+: Std.dev."


def annotations = hierarchy.getAnnotationObjects()
def annotationIslet = annotations.findAll { it.getPathClass() == PathClassFactory.getPathClass("Islet") }
def annotationsIsletExpanded = annotations.findAll { it.getPathClass() == PathClassFactory.getPathClass("IsletExpanded") }
// prevents the glucagon/insulin/background detection from being removed
def allDetections = getDetectionObjects()
def nonCellObjects = allDetections.findAll { !(it instanceof PathCellObject) }

// JSON parameters for IntensityFeaturesPlugin
def jsonParamsIntensity = """{
    "pixelSizeMicrons": ${formattedPixelSize},
    "region": "ROI",
    "tileSizeMicrons": 25.0,
    "colorOD": false,
    "colorStain1": false,
    "colorStain2": false,
    "colorStain3": true,
    "colorRed": false,
    "colorGreen": false,
    "colorBlue": false,
    "colorHue": false,
    "colorSaturation": false,
    "colorBrightness": false,
    "doMean": true,
    "doStdDev": true,
    "doMinMax": true,
    "doMedian": true,
    "doHaralick": false,
    "haralickDistance": 1,
    "haralickBins": 32
}"""

selectObjectsByClassification("IsletExpanded")
runPlugin('qupath.lib.algorithms.IntensityFeaturesPlugin', jsonParamsIntensity)

// Aggregate results
double totalMean = 0
double totalStdDev = 0
int count = 0

print(nameMean)
annotationsIsletExpanded.each { it ->
    def measurements = it.getMeasurementList()
    totalMean += measurements.getMeasurementValue(nameMean)
    totalStdDev += measurements.getMeasurementValue(nameStd)
    count++
}

// Calculate overall mean and standard deviation
double overallMean = totalMean / count
double overallStdDev = totalStdDev / count
double cd3Threshold = overallMean + ((overallStdDev))
// Get the CD3+ cells by the std

// runs the Positive cell detection plugin
def jsonParams = """{
    "detectionImageBrightfield": "Optical density sum",
    "requestedPixelSizeMicrons": ${pixelSizeMicrons},
    "backgroundRadiusMicrons": 8.0,
    "backgroundByReconstruction": true,
    "medianRadiusMicrons": 0.0,
    "sigmaMicrons": 1.5,
    "minAreaMicrons": 10.0,
    "maxAreaMicrons": 400.0,
    "threshold": ${overallMean + ((overallStdDev * 2))},
    "maxBackground": 1,
    "watershedPostProcess": true,
    "cellExpansionMicrons": 5.0,
    "includeNuclei": true,
    "smoothBoundaries": true,
    "makeMeasurements": true,
    "thresholdCompartment": "Cell: CD3+ OD mean",
    "thresholdPositive1": ${cd3Threshold},
    "thresholdPositive2": 0,
    "thresholdPositive3": 0,
    "singleThreshold": true
}"""

selectObjectsByClassification("IsletExpanded")
runPlugin('qupath.imagej.detect.cells.PositiveCellDetection', jsonParams)

addObjects(annotationIslet)
addObjects(nonCellObjects)



// Filter to get only 'Negative' cells -> removal of objects should happend after solving hierarchy
def cells = getCellObjects()
def negativeCells = cells.findAll { it.getPathClass() == getPathClass("Negative") }

removeObjects(negativeCells, true)

selectAllObjects()
// updated the hierarchy of the objects
Commands.insertSelectedObjectsInHierarchy(imageData)

fireHierarchyUpdate()