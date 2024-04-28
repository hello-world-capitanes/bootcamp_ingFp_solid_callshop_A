package com.helloworld.callshop.engine;


import com.helloworld.callshop.rater.rate.Rate;
import com.helloworld.callshop.rater.rate.RateableCall;
import com.helloworld.callshop.rater.rate.RatesRepository;
import com.helloworld.callshop.rater.rate.factory.ParametersReader;
import com.helloworld.callshop.rater.rate.factory.RateFactoriesConfigReader;
import com.helloworld.callshop.rater.rate.factory.RateFactoriesContainer;
import com.helloworld.callshop.rater.rate.factory.RateFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RatesEngine {

    private Scanner consoleInput;

    private ParametersReader consoleParametersReader;

    private RateFactoriesContainer factoriesContainer;


    public static void main(String[] args) throws Exception{
        RatesEngine engine = new RatesEngine();
        engineProvisioning(engine);
        engine.run();

    }

    public static void engineProvisioning(RatesEngine engine) throws Exception{

        engine.consoleInput =  new Scanner(System.in);

        engine.consoleParametersReader = new ConsoleParametersReader();

        engine.factoriesContainer = new RateFactoriesContainer();

        XMLConfigReader xmlConfigReader = new XMLConfigReader();
        xmlConfigReader.readConfiguration("configuration.xml");
        engine.factoriesContainer.createFactories(xmlConfigReader);
    }

    private String selectRateFactory() {

        System.out.println("Seleccione una opcion o 'x' para terminar:");

        Map<String, RateFactory> factoryMap = factoriesContainer.getFactories();

        String selectedOption = "";

        while (!factoryMap.containsKey(selectedOption) && !selectedOption.equalsIgnoreCase("x")) {

            factoryMap.forEach((id, factory) ->
                System.out.println(id + " --> " + factory.getDescription())
            );

            selectedOption = consoleInput.nextLine();
        }

        return selectedOption;
    }

    public void run() throws Exception{


        String selectedFactory = "";

        while (!(selectedFactory = selectRateFactory()).equalsIgnoreCase("x")) {
            RateFactory factory = factoriesContainer.getFactories().get(selectedFactory);
            try {
                Rate rate = factory.makeRate(consoleParametersReader);
                RatesRepository.INSTANCE.addRate(rate);
            } catch (Exception e) {
                System.out.println("Error al crear la tarifa: " + e.getMessage());
            }

        }


        //TESTEAMOS LAS TARIFAS CREADAS. DE AQUÍ HACIA ABAJO, ES SIMPLEMENTE DEMOSTRACIÓN DE FUNCIONAMIENTO Y QUEDA FUERA
        testRates();

    }

    private void testRates() {
        RateableCall callSpain = new RateableCall("34915465454", 120, LocalTime.now());

        System.out.println("Comprobando las tarifas creadas para " + callSpain);


        for (Rate rate : RatesRepository.INSTANCE.getAllRates().values()) {
            BigDecimal charge = rate.calculatePrice(callSpain);
            System.out.println(rate + "-->" + charge);
        }

        System.out.println();

        RateableCall callUk = new RateableCall("444567345674", 120, LocalTime.now());

        System.out.println("Comprobando las tarifas creadas para " + callUk);

        for (Rate rate : RatesRepository.INSTANCE.getAllRates().values()) {
            BigDecimal charge = rate.calculatePrice(callUk);
            System.out.println(rate + "-->" + charge);
        }
    }

}
