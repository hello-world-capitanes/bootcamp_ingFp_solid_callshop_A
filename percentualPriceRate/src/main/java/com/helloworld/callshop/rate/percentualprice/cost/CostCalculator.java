package com.helloworld.callshop.rate.percentualprice.cost;

import com.helloworld.callshop.rater.rate.RateableCall;

import java.math.BigDecimal;

public interface CostCalculator {
    BigDecimal getCost(RateableCall rateableCall);
}
