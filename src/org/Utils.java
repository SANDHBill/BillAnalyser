package org;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;


import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class Utils {
	public static Image mat2Image(Mat frame) {
		// create a temporary buffer
		MatOfByte buffer = new MatOfByte();
		// encode the frame in the buffer, according to the PNG format
		Imgcodecs.imencode(".png", frame, buffer);
		// build and return an Image created from the image encoded in the
		// buffer
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}
	
	public static Mat matify(Image fxImage) {
		return matify(convertToAwtImage(fxImage));
	}

	private static BufferedImage convertToAwtImage(Image fxImage) {
		return SwingFXUtils.fromFXImage(fxImage, null);
	}

	private static Mat matify(BufferedImage in) {
		Mat out;
		byte[] data;
		

		int widthSize = in.getWidth();
		int heightSize = in.getHeight();

		if (in.getType() == BufferedImage.TYPE_INT_RGB) {
			out = new Mat(heightSize, widthSize, CvType.CV_8UC3);
			data = new byte[heightSize * widthSize * (int) out.elemSize()];
			int[] dataBuff = in.getRGB(0, 0, widthSize, heightSize, null, 0, widthSize);
			for (int i = 0; i < dataBuff.length; i++) {
				data[i * 3] = (byte) ((dataBuff[i] >> 16) & 0xFF);
				data[i * 3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
				data[i * 3 + 2] = (byte) ((dataBuff[i] >> 0) & 0xFF);
			}
		} else {
			 BufferedImage convertedImg = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_RGB);
			 convertedImg.getGraphics().drawImage(in, 0, 0, null);
			 return matify(convertedImg);
			
			 /*int r, g, b;
		    out = new Mat(heightSize, widthSize, CvType.CV_8UC1);
			data = new byte[widthSize * heightSize * (int) out.elemSize()];
			int[] dataBuff = in.getRGB(0, 0, widthSize, heightSize, null, 0, widthSize);
			for (int i = 0; i < dataBuff.length; i++) {
				r = (byte) ((dataBuff[i] >> 16) & 0xFF);
				g = (byte) ((dataBuff[i] >> 8) & 0xFF);
				b = (byte) ((dataBuff[i] >> 0) & 0xFF);
				data[i] = (byte) ((0.21 * r) + (0.71 * g) + (0.07 * b)); // luminosity
			}*/
		}
		out.put(0, 0, data);
		return out;
	}

	

}
