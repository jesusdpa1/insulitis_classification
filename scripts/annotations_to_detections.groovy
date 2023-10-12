def annotations = getAnnotationObjects()
//change this line to subset = annotations if you want to convert ALL current annotations to detections. Otherwise adjust as desired.
def subset = annotations.findAll {it.getROI()&& it.getPathClass() == getPathClass("Islet")}

print(subset)
// Create corresponding detections (name this however you like)
def classification = getPathClass('Islet')


def detections = subset.collect {
    new qupath.lib.objects.PathDetectionObject(it.getROI(), classification)
}


// Remove ellipse annotations & replace with detections
removeObjects(subset, true)
addObjects(detections)