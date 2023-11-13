import static qupath.lib.gui.scripting.QPEx.*
import qupath.lib.images.servers.TransformedServerBuilder
import qupath.lib.images.ImageData
import qupath.lib.images.servers.ConcatChannelsImageServer

def imageData = getCurrentImageData()
def stains = imageData.getColorDeconvolutionStains()
def server = imageData.getServer()

//cd3+
def cd3Server = new TransformedServerBuilder(server).deconvolveStains(stains, 3).build()
def cd3ImageData = new ImageData<>(cd3Server, imageData.getHierarchy(), imageData.getImageType()) 


//Platform.runLater {
//    getCurrentViewer().setImageData(cd3ImageData)
//}