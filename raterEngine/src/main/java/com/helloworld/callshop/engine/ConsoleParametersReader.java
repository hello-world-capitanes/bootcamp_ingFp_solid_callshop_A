package com.helloworld.callshop.engine;

import com.helloworld.callshop.rater.rate.factory.ParametersReader;
import com.helloworld.callshop.rater.rate.factory.Parameter;
import com.helloworld.callshop.rater.rate.factory.ParametersMapper;

import java.util.List;
import java.util.Scanner;

public class ConsoleParametersReader implements ParametersReader {



    @Override
    public ParametersMapper readParameters(List<Parameter> parameters) {

        ParametersMapperImpl paramsMapper = new ParametersMapperImpl();

        Scanner consoleInput = new Scanner(System.in);

        for (Parameter parameter: parameters) {
            System.out.print(parameter.getDescription() + ":");
            String parameterValue = consoleInput.nextLine();
            while (!parameter.getValidator().test(parameterValue)) {
                System.out.print("El valor introducido es incorrecto. Repita por favor" + ":");
                parameterValue = consoleInput.nextLine();
            }
            paramsMapper.put(parameter.getName(), parameterValue);
        }

        return paramsMapper;
    }


}
