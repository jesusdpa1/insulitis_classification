import static qupath.lib.gui.scripting.QPEx.*

// Define the base name
def baseName = "islet"

// Get the current project
def imageData = getCurrentImageData()
def hierarchy = imageData.getHierarchy()

// Get the root annotation hierarchy
//def hierarchy = project.getHierarchy()

// Create a counter for the sequence number
def counter = 1

def annotations = hierarchy.getAnnotationObjects()

annotations.each { it -> 
    def newName = "${baseName}_${counter}"
    
    it.setName(newName)
    counter++

}

fireHierarchyUpdate()