package com.helloworld.callshop.rater.rate.factory;

import java.util.function.Predicate;

/**
 * Estructura de datos que representa un parametro.
 *
 * Las factorias de tarifas usan esta estructura (mapeados a valores) para representar los parametros que se pasan
 * a los constructores de las tarifas.
 *
 * El nombre identifica el parametro, la descripcion puede utilizarse para informar al usuario del proposito del parametros
 * y el validador sirve para validar si un valor es adecuado para el parametro
 *
 */
public class Parameter {
    private final String name;
    private final String description;
    private final Predicate<Object> validator;


    public Parameter(String name, String description, Predicate<Object> validator) {
        this.name = name;
        this.description = description;
        this.validator = validator;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Predicate<Object> getValidator() {
        return validator;
    }
}
