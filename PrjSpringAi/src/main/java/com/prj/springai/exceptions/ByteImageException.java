package com.prj.springai.exceptions;

import java.io.IOException;

public class ByteImageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ByteImageException(IOException e) {
		super(e);
	}

}
