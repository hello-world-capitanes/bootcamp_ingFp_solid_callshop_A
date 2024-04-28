package com.helloworld.callshop.rate.percentualprice;

import com.helloworld.callshop.rate.percentualprice.cost.CostCalculator;
import com.helloworld.callshop.rate.percentualprice.cost.impl.SimpleCostCalculator;
import com.helloworld.callshop.rater.rate.Rate;
import com.helloworld.callshop.rater.rate.factory.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PercentualPriceRateFactory implements RateFactory {

    private static final String DESCRIPTION = "Tarifa porcentual (margen sobre en coste de la llamada)";

    private static final String ID = "PERC";
    private static final String PERCENT_NAME = "percent";

    private final CostCalculator costCalculator = new SimpleCostCalculator();


    private final Predicate<Object> percentValidator = parameter -> {
        try {
            int percent = parseIntegerParameter(parameter);
            return isValidPercentage(percent);
        } catch (InvalidParameterValueException | NullPointerException e) {
            return false;
        }
    };



    private final Parameter percentParam = new Parameter(PERCENT_NAME, "Margen sobre el coste (%)", percentValidator);


    @Override
    public Rate makeRate(ParametersReader parametersReader) throws InvalidParameterValueException{

        List<Parameter> parameters = getBasicParameterList();
        parameters.add(percentParam);

        ParametersMapper parametersMapper = parametersReader.readParameters(parameters);

        int percent = parseIntegerParameter(parametersMapper.getValue(PERCENT_NAME));
        if (!isValidPercentage(percent)) {
            throw new InvalidParameterValueException("Porcentaje no válido");
        }

        Object name = parametersMapper.getValue(RATE_NAME_NAME);

        if (!getNameValidator().test(name)) {
            throw new InvalidParameterValueException("Nombre no válido");
        }

        PercentualPriceRate rate = new PercentualPriceRate((String)name, percent);
        rate.setCostCalculator(costCalculator);

        return rate;
    }



    private int parseIntegerParameter(Object parameter) {
        if (parameter == null) {
            throw new NullPointerException("Valor del parametro a null");
        }
        if (parameter instanceof Integer value) {
            return value;
        }
        if (parameter instanceof BigInteger value) {
            try {
                return value.intValueExact();
            }catch (ArithmeticException e) {
                throw new InvalidParameterValueException("Número entero no válido", e);
            }
        }
        if(parameter instanceof Long value) {
            try {
                return Math.toIntExact(value);
            }catch (ArithmeticException e) {
                throw new InvalidParameterValueException("Número entero no válido", e);
            }
        }
        if (parameter instanceof Double value) {
            long  l = Math.round(value);
            if (l != value) {
                throw new InvalidParameterValueException("Número entero no válido");
            }
            try {
                return Math.toIntExact(l);
            }catch (ArithmeticException e) {
                throw new InvalidParameterValueException("Número entero no válido", e);
            }

        }
        if (parameter instanceof BigDecimal value) {
           try {
                return value.intValueExact();
           }catch (ArithmeticException e) {
               throw new InvalidParameterValueException("Número entero no válido", e);
           }
        }
        if (parameter instanceof String value) {
            try {
                BigDecimal decimal = new BigDecimal(value);
                return decimal.intValueExact();
            }catch(NumberFormatException | ArithmeticException e) {
                throw new InvalidParameterValueException("Número entero no válido", e);
            }
        }
        throw new InvalidParameterValueException("Número entero no válido");
    }

    private final boolean isValidPercentage(int percent) {
        return percent >= 0 && percent <= 100;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    public String getId() {
        return ID;
    }
}
