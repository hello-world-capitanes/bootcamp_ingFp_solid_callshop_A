package com.helloworld.callshop.rate.timeprice;

import com.helloworld.callshop.rater.rate.AbstractRate;
import com.helloworld.callshop.rater.rate.Rate;
import com.helloworld.callshop.rater.rate.RateableCall;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public class TimePriceRate extends AbstractRate implements Rate {

    private ZonedDateTime inicioFranjaMañana;
    private Rate franjaMañana;

    private ZonedDateTime inicioFranjaTarde;
    private Rate franjaTarde;

    private ZonedDateTime inicioFranjaNoche;
    private Rate franjaNoche;

    public TimePriceRate(String name, LocalTime inicioFranjaMañana, Rate franjaMañana, LocalTime inicioFranjaTarde, Rate franjaTarde, LocalTime inicioFranjaNoche, Rate franjaNoche) {
        super(name);
        this.inicioFranjaMañana = inicioFranjaMañana;
        this.franjaMañana = franjaMañana;
        this.inicioFranjaTarde = inicioFranjaTarde;
        this.franjaTarde = franjaTarde;
        this.inicioFranjaNoche = inicioFranjaNoche;
        this.franjaNoche = franjaNoche;
    }

    @Override
    public String toString() {
        return "PercentualPriceRate{" +
                "name=" + getName() +
                ", inicioFranjaMañana=" + inicioFranjaMañana.toString() +
                ", rate=" + franjaMañana.toString() +
                ", inicioFranjaTarde=" + inicioFranjaTarde.toString() +
                ", rate=" + franjaTarde.toString() +
                ", inicioFranjaNoche=" + inicioFranjaNoche.toString() +
                ", rate=" + franjaNoche.toString() +
                '}';
    }

    @Override
    public BigDecimal calculatePrice(RateableCall rateableCall) {
        BigDecimal duration = new BigDecimal(rateableCall.getDuration()); //duration in seconds
        LocalTime hora = rateableCall.getCallStart();
        if ((hora.isAfter(inicioFranjaMañana) || hora.equals(inicioFranjaMañana)) && hora.isBefore(inicioFranjaTarde)) {
            return franjaMañana.calculatePrice(rateableCall);
        }  else if ((hora.isAfter(inicioFranjaTarde) || hora.equals(inicioFranjaTarde)) && hora.isBefore(inicioFranjaNoche)) {
            return franjaTarde.calculatePrice(rateableCall);
        } else if ((hora.isAfter(inicioFranjaNoche) || hora.equals(inicioFranjaNoche)) && hora.isBefore(inicioFranjaMañana)) {
            return franjaNoche.calculatePrice(rateableCall);
        } else {
            //throw new IllegalArgumentExceptio("Franja horaria no disponible");
        }
        return null;
    }
}
