package com.helloworld.callshop.rate.fixprice;

import com.helloworld.callshop.rater.rate.AbstractRate;
import com.helloworld.callshop.rater.rate.Rate;
import com.helloworld.callshop.rater.rate.RateableCall;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FixPriceRate extends AbstractRate implements Rate {

    private final static BigDecimal  SECONDS_PER_MINUTE = new BigDecimal(60);

    private final BigDecimal connectFee;
    private final BigDecimal minutePrice;

    public FixPriceRate(String name, BigDecimal connectFee, BigDecimal minutePrice) {
        super(name);
        this.connectFee = connectFee;
        this.minutePrice = minutePrice;
    }

    @Override
    public BigDecimal calculatePrice(RateableCall rateableCall) {
        BigDecimal duration = new BigDecimal(rateableCall.getDuration()); //duration in seconds
		return duration.divide(SECONDS_PER_MINUTE, 6, RoundingMode.HALF_EVEN).multiply(minutePrice).add(connectFee);
    }

    @Override
    public String toString() {
        return "FixPriceRate{" +
                "name=" + getName() +
                ", connectFee=" + connectFee +
                ", minutePrice=" + minutePrice +
                '}';
    }
}
