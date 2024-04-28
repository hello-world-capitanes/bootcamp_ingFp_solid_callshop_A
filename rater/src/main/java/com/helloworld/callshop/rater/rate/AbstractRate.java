package com.helloworld.callshop.rater.rate;

public abstract class AbstractRate implements Rate{

	private String name;

	public AbstractRate(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
