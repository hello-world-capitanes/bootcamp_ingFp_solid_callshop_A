package com.helloworld.callshop.rater.rate.factory;

import com.helloworld.callshop.rater.rate.Rate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public interface RateFactory {

    String RATE_NAME_NAME = "NAME";

    Rate makeRate(ParametersReader parametersReader) throws InvalidParameterValueException, RateBuilderException;

    String getDescription();

    String getId();

    default List<Parameter> getBasicParameterList(){
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter(RATE_NAME_NAME, "Nombre de la tarifa", getNameValidator()));
        return parameters;
    }

    default Predicate<Object> getNameValidator(){
        return parameter -> (parameter instanceof String value) && value.matches("^[a-zA-Z0-9_]+$");
    };


}

