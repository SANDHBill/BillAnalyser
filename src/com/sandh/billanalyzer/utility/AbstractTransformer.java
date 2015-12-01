package com.sandh.billanalyzer.utility;

import static org.bytedeco.javacpp.lept.pixRead;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.bytedeco.javacpp.lept;
import org.bytedeco.javacpp.tesseract;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;


public abstract class AbstractTransformer implements Transformer{
	private volatile boolean debugMode = false;
	private String parameters ="";
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	public boolean isDebugMode() {
		return debugMode;
	}
	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
	@Override
	public String transform(InputStream imageIn) throws IOException {
		String item =null;
				
		 Mat imageMat = readInputStreamIntoMat(imageIn);		 
		 Mat preparedImageMat = prepareForOCR(imageMat);
		 item = applyOCRToImage(preparedImageMat)[0];
		
		return item;
	}
	private String[] applyOCRToImage(Mat preparedImageMat) {
		String items[] =new String[1];
		
		String ocrOutPutString = applyOCR(matToInputStream(preparedImageMat));
		items[0]=ocrOutPutString;
		return items;
	}

	protected abstract Mat prepareForOCR(Mat imageMatIn);

	private String applyOCR(InputStream imageStreamIn){
		String outText=null;
		
		tesseract.TessBaseAPI api = new tesseract.TessBaseAPI();
		if (api.Init(".", "ENG") != 0) {
			throw new RuntimeException("Unable to initialise OCR lib");
		}

		File tempFile = workAroundTempFile(imageStreamIn);
		
		// Open input image with leptonica library
		lept.PIX image = pixRead(tempFile.getAbsolutePath());
		api.SetImage(image);

		
		// Get OCR result
		outText = api.GetUTF8Text().getString();
		
		return outText;
	}
	private File workAroundTempFile(InputStream imageStreamIn) {
		File tempFile = null;
		try {
			byte[] imageBytes = IOUtils.toByteArray(imageStreamIn);	
			tempFile = File.createTempFile("sandh_"+this.getClass().getName()+"_"+this.getParameters()+"_", String.valueOf(System.currentTimeMillis()+".png"), null);
			FileOutputStream fos = new FileOutputStream(tempFile);
			fos.write(imageBytes);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(isDebugMode()){
			System.out.println("File loc:"+tempFile.getAbsolutePath());
		}else{
			tempFile.deleteOnExit();
		}
		return tempFile;
	}
	private  Mat readInputStreamIntoMat(InputStream inputStream) throws IOException {		
		byte[] temporaryImageInMemory = IOUtils.toByteArray(inputStream);
	    // Decode into mat. Use any IMREAD_ option that describes your image appropriately
	    Mat outputImage = Imgcodecs.imdecode(new MatOfByte(temporaryImageInMemory), Imgcodecs.IMREAD_COLOR);
	    return outputImage;
	}

	private InputStream matToInputStream(Mat imageMatIn) {
		// create a temporary buffer
		MatOfByte buffer = new MatOfByte();
		// encode the frame in the buffer, according to the PNG format
		Imgcodecs.imencode(".png", imageMatIn, buffer);
		// build and return an Image created from the image encoded in the
		// buffer
		return new ByteArrayInputStream(buffer.toArray());
	}
	
}
