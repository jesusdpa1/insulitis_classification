import static qupath.lib.gui.scripting.QPEx.*
import qupath.lib.images.servers.TransformedServerBuilder
import qupath.lib.images.ImageData;
import qupath.imagej.tools.IJTools;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.EDM;
import ij.process.ByteProcessor;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;


def imageData = getCurrentImageData()
def stains = imageData.getColorDeconvolutionStains()
def server = imageData.getServer()

imgpath = getCurrentServer().getPath()
downsample = 1
def roi = getSelectedROI()
def chServer = new TransformedServerBuilder(server).deconvolveStains(stains, 3).build()
RegionRequest request = RegionRequest.createInstance(imgpath, downsample, roi)


imp = IJTools.convertToImagePlus(chServer, request).getImage()
impOriginal = imp.duplicate()
ImageConverter converter = new ImageConverter(imp);

converter.convertToGray8();
IJ.setAutoThreshold(imp, "Default");

// Convert to mask
impOriginal.show()
imp.show()
//
//EDM edmProcessor = new EDM();
//edmProcessor.toEDM(imp.getProcessor());

// Apply the Euclidean Distance Map (EDM)

 
//// Apply the Euclidean Distance Map (EDM)
//def ip = imagePlus.getProcessor()
//EDM edm = new EDM()
//edm.setup("", imagePlus)
//edm.run(ip)
//







// Apply Gaussian Blur
double sigma = 2.0 // Adjust this value for the desired level of blurring
IJ.run(imp, "Gaussian Blur...", "sigma=" + sigma)
imp.show()
//// Apply the Watershed algorithm
//IJ.run(imp, "Watershed", "")
//imp.show()
//// Convert to Mask
//IJ.run(imp, "Convert to Mask", "")
//
//// Label the particles (regions)
//ResultsTable rt = new ResultsTable()
//int measurements = ParticleAnalyzer.SHOW_OUTLINES
//int options = ParticleAnalyzer.ADD_TO_MANAGER
//double minSize = 0
//double maxSize = Double.POSITIVE_INFINITY
//ParticleAnalyzer pa = new ParticleAnalyzer(options, measurements, rt, minSize, maxSize)
//pa.analyze(imp)
//
//// Display the labeled image
//imp.updateAndDraw()
//imp.show()
//
//// Display the image
