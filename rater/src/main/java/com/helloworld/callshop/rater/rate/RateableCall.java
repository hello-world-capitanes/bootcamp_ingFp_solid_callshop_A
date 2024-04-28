package com.helloworld.callshop.rater.rate;


import java.time.Instant;
import java.time.LocalTime;

/**
 *
 * Structura de datos que representa una llamada que puede tarificarse
 *
 */
public class RateableCall {

    private final String internationalPhoneNumber;
    //Seconds
    private final int duration;

    private final LocalTime callStart;

    public RateableCall(String internationalPhoneNumber, int duration, LocalTime callStart) {
        this.internationalPhoneNumber = internationalPhoneNumber;
        this.duration = duration;
        this.callStart = callStart;
    }

    public String getInternationalPhoneNumber() {

        return internationalPhoneNumber;
    }

    public int getDuration() {
        return duration;
    }

    public LocalTime getCallStart() {
        return callStart;
    }


    @Override
    public String toString() {
        return "RateableCall{" +
                "internationalPhoneNumber='" + internationalPhoneNumber + '\'' +
                ", duration=" + duration +
                ", callStart=" + callStart +
                '}';
    }
}
