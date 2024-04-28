package com.helloworld.callshop.rater.rate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RatesRepository {

	public static final RatesRepository INSTANCE = new RatesRepository();

	private final Map<String, Rate> rates = new HashMap<>();

	public void addRate(Rate rate) {
		rates.put(rate.getName(), rate);
	}

	public Rate getRate(String name) {
		return rates.get(name);
	}

	public Map<String, Rate> getAllRates() {
		return Collections.unmodifiableMap(rates);
	}

}
