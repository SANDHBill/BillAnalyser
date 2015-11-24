package com.sandh.billanalyzer.utility;


import org.opencv.imgproc.Imgproc;
import org.opencv.core.*;

public class GaussianFixThresholdOCRTransformer extends AbstractTransformer implements Transformer{


	protected Mat prepareForOCR(Mat imageMatIn) {
				
		Mat grayScaledImageMat = convertToGrayScale(imageMatIn);
		Mat blackAndWhiteImageMat = blackAndWhiteImage(grayScaledImageMat);

		return blackAndWhiteImageMat;
	}


	private Mat convertToGrayScale(Mat imageMatIn) {
		Mat imageMatOut = new Mat();
		Imgproc.cvtColor(imageMatIn, imageMatOut, Imgproc.COLOR_BGR2GRAY, 1);
		return imageMatOut;
	}

	private Mat blackAndWhiteImage(Mat imageMatIn){
		Mat imageMatOut = new Mat();
		Imgproc.threshold(imageMatIn, imageMatOut, 100, 255, Imgproc.THRESH_BINARY );
		
		return imageMatOut;	
	}



}
