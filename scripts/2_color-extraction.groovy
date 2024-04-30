/*imports all the main libraries needed to excecute the script*/
import static qupath.lib.gui.scripting.QPEx.*
import qupath.lib.images.servers.TransformedServerBuilder
import qupath.lib.images.ImageData
import qupath.lib.images.servers.ConcatChannelsImageServer

// Gets the image information from the current view
def imageData = getCurrentImageData()
// 
def stains = imageData.getColorDeconvolutionStains()
//
def server = imageData.getServer()

/*
creates a new image just with the glucagon and insulin information. 
For this IHC image the islet can be define as a combination of only the glucagon and insulin stains
Therefore we proceed to deconvolve the image into a subset of only the glucagon and insulin stains
*/

//Deconvolves the images to extract the glucagon stain data from the RGB image
def glucagonServer = new TransformedServerBuilder(server).deconvolveStains(stains, 1).build()
def glucagonImageData = new ImageData<>(glucagonServer, imageData.getHierarchy(), imageData.getImageType()) 


//Deconvolves the images to extract the insulin stain data from the RGB image
def insulinServer = new TransformedServerBuilder(server).deconvolveStains(stains, 2).concatChannels(glucagonServer).build()
def insulinImageData = new ImageData<>(insulinServer, imageData.getHierarchy(), imageData.getImageType())


Platform.runLater {
    getCurrentViewer().setImageData(insulinImageData)
}