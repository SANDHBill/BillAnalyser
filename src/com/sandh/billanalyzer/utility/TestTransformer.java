package com.sandh.billanalyzer.utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

import org.opencv.core.Core;

public class TestTransformer {
	static String fileLoc = "/i1.jpg";
	
	public static void main(String[] args){
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
		
		Transformer transformerAdaptive = new GaussianAdaptiveOCRTransformer();
		Transformer transformerFix = new GaussianFixThresholdOCRTransformer();
		transformerFix.setDebugMode(true);
		
		String[] result = new String[2];
		
		URL url = ClassLoader.class.getResource(fileLoc);
		
	    try (InputStream imageIn = url.openStream()) {
	    	
	    	//result[0] = transformerAdaptive.transform(imageIn);
	    	
	    	result[1] = transformerFix.transform(imageIn);
	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		System.out.println(result[0]);
		System.out.println("-----");
		System.out.println(result[1]);
	}
	
	public static void printClassPath(){
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader)cl).getURLs();

        for(URL url: urls){
        	System.out.println(url.getFile());
        }  
	
	}

}
