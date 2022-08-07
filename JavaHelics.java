package com.java.helics;

import java.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import org.apache.commons.io.FileUtils;
public class JavaHelics implements JavaHelicsLibrary {
	private final JavaHelicsLibrary INSTANCE;
	public JavaHelics(final String fileName) throws IOException {
		private final JavaHelicsLibrary INSTANCE;
		INSTANCE = Native.loadLibrary(extratcFile(fileName), JavaHelicsLibrary.class);
	}
	private String extratcFile(final String fileName) throws IOException {
		final InputStream source = JavaHelics.class.getClassLoader().getResourceAsStream(fileName);
		final File file = File.createTempFile("lib", null);
		FileUtils.copyInputStreamToFile(source, file);
		return file.getAbsolutePath();
	}
}