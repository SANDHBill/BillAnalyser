package com.sandh.billanalyzer.utility;


import org.opencv.imgproc.Imgproc;
import org.opencv.core.*;

public class GaussianAdaptiveOCRTransformer extends AbstractTransformer implements Transformer{


	protected Mat prepareForOCR(Mat imageMatIn) {
				
		Mat grayScaledImageMat = convertToGrayScale(imageMatIn);
		Mat deNoisedImageMat = removeNoise(grayScaledImageMat);
		Mat blackAndWhiteImageMat = blackAndWhiteImage(deNoisedImageMat);

		return blackAndWhiteImageMat;
	}


	private Mat convertToGrayScale(Mat imageMatIn) {
		Mat imageMatOut = new Mat();
		Imgproc.cvtColor(imageMatIn, imageMatOut, Imgproc.COLOR_BGR2GRAY, 1);
		return imageMatOut;
	}
	private Mat removeNoise(Mat imageMatIn){
		Mat imageMatOut = new Mat();
		Imgproc.GaussianBlur(imageMatIn, imageMatOut, new Size(11, 11), 0);
		return imageMatOut;		
	}
	private Mat blackAndWhiteImage(Mat imageMatIn){
		Mat imageMatOut = new Mat();
		Imgproc.adaptiveThreshold(
				imageMatIn, 
				imageMatOut, 
				255, 
				Imgproc.ADAPTIVE_THRESH_MEAN_C, 
				Imgproc.THRESH_BINARY, 
				5, 
				2);//ADAPTIVE_THRESH_MEAN_C
		
		return imageMatOut;	
	}



}
