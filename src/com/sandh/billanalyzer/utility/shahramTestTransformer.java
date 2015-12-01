package com.sandh.billanalyzer.utility;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.*;

public class shahramTestTransformer extends AbstractTransformer  implements Transformer{

	@Override
	protected Mat prepareForOCR(Mat imageMatIn) {
		
		Mat grayScaledImageMat = convertToGrayScale(imageMatIn);
		Mat deNoisedImageMat = removeNoise(grayScaledImageMat);
		Mat blackAndWhiteImageMat = blackAndWhiteImage(deNoisedImageMat);
		Mat erodeDilatedImageMat = morphologicalOperations(blackAndWhiteImageMat);
		Mat removedDots = clearSmallBlackDots(erodeDilatedImageMat, 300, 0.9);
		return removedDots;
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

	private Mat morphologicalOperations(Mat imageMatIn){
		Mat imageMatOut = new Mat();
		Mat kernel = Mat.ones(3, 3, CvType.CV_8UC1);
		System.out.println("Kernel Mat"+kernel.dump());
		//Imgproc.dilate(imageMatIn, imageMatOut, kernel, new Point(-1,-1),1);
		
		//Imgproc.threshold(imageMatOut, imageMatOut, 220, 255, Imgproc.THRESH_BINARY );
		//Imgproc.dilate(imageMatOut, imageMatOut, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
		//Imgproc.erode(imageMatIn, imageMatOut, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8,8)));    
		Imgproc.erode(imageMatIn, imageMatOut, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5))); 
		//Imgproc.dilate(imageMatOut, imageMatOut, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4,4)));
		this.setParameters("adaptive5x2erode40x40");
		return imageMatOut;		
	}
	
	private Mat clearSmallBlackDots(Mat imageMatIn,int countourSize, double threshold)
	{
		Mat imageMatOut = new Mat();
		imageMatIn.copyTo(imageMatOut);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
	    Imgproc.findContours(imageMatIn, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		
	    for(MatOfPoint contour : contours) 
	    {
	        Rect R = Imgproc.boundingRect(contour);
	        if( R.width*R.height < countourSize )
	        {
	            Mat roi = new Mat(imageMatIn,R);

	            if (Core.countNonZero(roi) < R.width*R.height*threshold )
	            {
	            	Imgproc.rectangle(imageMatOut,R.tl(),R.br(),new Scalar(255,255,255));
	                Mat croi = new Mat(imageMatOut,R);
	                croi.setTo(new Scalar(255,255,255)); // this line is to clear small dots
	            }
	        }
	    }
	    return imageMatOut;
	}
	

}
