package com.helloworld.callshop.jsonreader;

import com.helloworld.callshop.engine.XMLConfigReader;
import com.helloworld.callshop.jsonreader.impl.JSONParametersReader;
import com.helloworld.callshop.rater.rate.Rate;
import com.helloworld.callshop.rater.rate.RatesRepository;
import com.helloworld.callshop.rater.rate.factory.RateFactoriesContainer;
import com.helloworld.callshop.rater.rate.factory.RateFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Map;
import java.util.stream.Collectors;

public class JSONEngine {

    private final RateFactoriesContainer container;
    private JSONObject object;

    public static void main(String[] args) {
        JSONEngine engine = getJsonEngine();

        try{
            XMLConfigReader xmlConfigReader = new XMLConfigReader();
            xmlConfigReader.readConfiguration("configuration.xml");
            engine.container.createFactories(xmlConfigReader);

        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }

        engine.run();
    }

    private static JSONEngine getJsonEngine() {

        String json;
        try (FileReader fr = new FileReader("tarifas.json")) {
            BufferedReader br = new BufferedReader(fr);
            json = br.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return new JSONEngine(new JSONObject(json), new RateFactoriesContainer());
    }

    public JSONEngine(JSONObject object, RateFactoriesContainer container){
        this.container = container;
        this.object = object;
    }

    public void run(){
        String selectedFactory;

        JSONArray tarifas = object.getJSONArray("tarifas");

        for (int i = 0; i < tarifas.length(); i++) {
            JSONObject tarifa = tarifas.getJSONObject(i);

            JSONParametersReader paramsReader = new JSONParametersReader(tarifa);

            selectedFactory = selectRateFactory(tarifa);
            
            RateFactory factory = container.getFactories().get(selectedFactory);
            try {
                Rate rate = factory.makeRate(paramsReader);
                RatesRepository.INSTANCE.addRate(rate);
            } catch (Exception e) {
                System.out.println("Error al crear la tarifa: " + e.getMessage());
                System.exit(1);
            }
        }


        for (Map.Entry<String, Rate> tarifa : RatesRepository.INSTANCE.getAllRates().entrySet()) {
            System.out.println(tarifa.getKey()+":"+tarifa.getValue());
        }

    }

    private String selectRateFactory(JSONObject tarifa) {

        Map<String, RateFactory> factoryMap = container.getFactories();

        String selectedOption = tarifa.getString("tipo_tarifa");

        if(!factoryMap.containsKey(selectedOption)){
            System.err.println("La opción en el JSON no existe");
            System.exit(1);
        }

        return selectedOption;
    }

}