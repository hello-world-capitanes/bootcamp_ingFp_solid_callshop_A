package com.helloworld.callshop.rater.rate.factory;

public class RateBuilderException extends RuntimeException{
	public RateBuilderException() {
	}

	public RateBuilderException(String message) {
		super(message);
	}

	public RateBuilderException(String message, Throwable cause) {
		super(message, cause);
	}

	public RateBuilderException(Throwable cause) {
		super(cause);
	}

	public RateBuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
