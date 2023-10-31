import static qupath.lib.gui.scripting.QPEx.*
import qupath.lib.images.servers.TransformedServerBuilder
import qupath.lib.images.ImageData
import qupath.lib.images.servers.ConcatChannelsImageServer

def imageData = getCurrentImageData()
def stains = imageData.getColorDeconvolutionStains()
def server = imageData.getServer()

//glucagon
def glucagonServer = new TransformedServerBuilder(server).deconvolveStains(stains, 1).build()
def glucagonImageData = new ImageData<>(glucagonServer, imageData.getHierarchy(), imageData.getImageType()) 


//insulin
def insulinServer = new TransformedServerBuilder(server).deconvolveStains(stains, 2).concatChannels(glucagonServer).build()
def insulinImageData = new ImageData<>(insulinServer, imageData.getHierarchy(), imageData.getImageType())

Platform.runLater {
    getCurrentViewer().setImageData(insulinImageData)
}