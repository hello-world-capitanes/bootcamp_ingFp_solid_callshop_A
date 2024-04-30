package com.helloworld.callshop.engine;

import com.helloworld.callshop.rater.rate.Rate;
import com.helloworld.callshop.rater.rate.RatesRepository;
import com.helloworld.callshop.rater.rate.factory.RateBuilderException;
import com.helloworld.callshop.rater.rate.factory.RateFactoriesContainer;
import com.helloworld.callshop.rater.rate.factory.RateFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Map;
import java.util.stream.Collectors;

public class JSONEngine {

    private RateFactoriesContainer factoriesContainer;
    private JSONObject listaTarifas;

    public static void main(String[] args) throws Exception{
        JSONEngine engine = new JSONEngine();
        engineProvisioning(engine);
        engine.run();
    }

    public static void engineProvisioning(JSONEngine engine) throws Exception{
        String json = readJsonFile();
        engine.listaTarifas = new JSONObject(json);

        engine.factoriesContainer = new RateFactoriesContainer();

        XMLConfigReader xmlConfigReader = new XMLConfigReader();
        xmlConfigReader.readConfiguration("configuration.xml");
        engine.factoriesContainer.createFactories(xmlConfigReader);
    }

    private static String readJsonFile() {
        String json;
        try (FileReader fr = new FileReader("tarifas.json")) {
            BufferedReader br = new BufferedReader(fr);
            json = br.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    public void run(){
        String selectedFactory = "";

        JSONArray tarifas = listaTarifas.getJSONArray("tarifas");

        for (int i = 0; i < tarifas.length(); i++) {
            JSONObject tarifa = tarifas.getJSONObject(i);

            JSONParametersReader paramsReader = new JSONParametersReader(tarifa);
            
            try{
                selectedFactory = selectRateFactory(tarifa);
            } catch (RateBuilderException e) {
                System.err.println("Error al seleccionar factoría de Rate: "+e.getMessage());
                System.exit(1);
            }

            RateFactory factory = factoriesContainer.getFactories().get(selectedFactory);
            try {
                Rate rate = factory.makeRate(paramsReader);
                RatesRepository.INSTANCE.addRate(rate);
            } catch (Exception e) {
                System.err.println("Error al crear la tarifa: " + e.getMessage());
                System.exit(1);
            }
        }


        for (Map.Entry<String, Rate> tarifa : RatesRepository.INSTANCE.getAllRates().entrySet()) {
            System.out.println(tarifa.getKey()+":"+tarifa.getValue());
        }

    }

    private String selectRateFactory(JSONObject tarifa) throws RateBuilderException {

        Map<String, RateFactory> factoryMap = factoriesContainer.getFactories();

        String selectedOption = tarifa.getString("tipo_tarifa");

        if(!factoryMap.containsKey(selectedOption)) throw new RateBuilderException("La factoría "+selectedOption+" no existe");

        return selectedOption;
    }

}
