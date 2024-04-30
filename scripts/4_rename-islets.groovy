// Script to standardize the names of the islets and assign an number (1...N)

import static qupath.lib.gui.scripting.QPEx.*

// Define the base name
def baseName = "islet"

// Get the current project
def imageData = getCurrentImageData()
def hierarchy = imageData.getHierarchy()

// Create a counter for the sequence number
def counter = 1

def annotations = hierarchy.getAnnotationObjects()
// renames all the anotations
annotations.each { it -> 
    def newName = "${baseName}_${counter}"
    
    it.setName(newName)
    counter++

}
// updates the values on the image information 
fireHierarchyUpdate()