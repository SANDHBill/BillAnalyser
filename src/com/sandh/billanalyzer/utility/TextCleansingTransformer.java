package com.sandh.billanalyzer.utility;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class TextCleansingTransformer implements Transformer {
	
	private static final String encoding = "UTF-8";
	private volatile boolean debugMode = false;
	
	public boolean isDebugMode() {
		return debugMode;
	}
	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
	
	@Override
	public String transform(InputStream textStreamIn) throws IOException {
		
		String resultOriginal = IOUtils.toString(textStreamIn, encoding); 
		
		return transform( resultOriginal);

	}

	public String transform(String resultOriginal) throws IOException {
		String result = null;
		
		result = resultOriginal.replaceAll("[^\\x00-\\x7F]", "");
		
		return result;
	}
	@Override
	public String getParameters() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParameters(String parameters) {
		// TODO Auto-generated method stub
		
	}


}
