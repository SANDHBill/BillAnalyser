package com.sandh.billanalyzer.utility;

import java.io.IOException;
import java.io.InputStream;

public interface Transformer {
	public String transform(InputStream imageIn) throws IOException;
	public boolean isDebugMode();
	public void setDebugMode(boolean debugMode);
	public String getParameters();
	public void setParameters(String parameters);
}
