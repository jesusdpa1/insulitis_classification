import static qupath.lib.gui.scripting.QPEx.*

def move_left_corner = 10
def expand_box = move_left_corner * 2

def plane = getCurrentViewer().getImagePlane()

// select all the islets annotations adquired with threshold method

listOfIslets = []

getAnnotationObjects().each{
    if(it.getPathClass() == getPathClass("Islet")){

        listOfIslets << it

    }
}


boundingAnnotation = []

rois = getAnnotationObjects().collect{it.getROI()}

rois.each {
    
    boundingROI = ROIs.createRectangleROI(
            it.getBoundsX() - move_left_corner, //top left corner
            it.getBoundsY() - move_left_corner, //top left corner
            it.getBoundsWidth() + expand_box, //width
            it.getBoundsHeight() + expand_box, //height
            plane)
   boundingAnnotation << PathObjects.createAnnotationObject(boundingROI)
}

//boundingAnnotation = PathObjects.createAnnotationObject(boundingROI)
addObjects(boundingAnnotation)

removeObjects(listOfIslets, true)