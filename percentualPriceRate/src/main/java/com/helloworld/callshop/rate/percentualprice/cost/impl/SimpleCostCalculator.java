package com.helloworld.callshop.rate.percentualprice.cost.impl;

import com.helloworld.callshop.rate.percentualprice.cost.CostCalculator;
import com.helloworld.callshop.rater.rate.RateableCall;

import java.math.BigDecimal;

public class SimpleCostCalculator implements CostCalculator {
    @Override
    public BigDecimal getCost(RateableCall rateableCall) {
        double cost;
        if (rateableCall.getInternationalPhoneNumber().startsWith("34")) {
            cost = rateableCall.getDuration() * 0.0015d;
        } else {
            cost = rateableCall.getDuration() * 0.003d;
        }
        return BigDecimal.valueOf(cost);
    }

    @Override
    public String toString() {
        return "SimpleCostCalculator{ Spain->0.09, Rest-> 0.18}";
    }
}
