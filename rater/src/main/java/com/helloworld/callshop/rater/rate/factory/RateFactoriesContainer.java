package com.helloworld.callshop.rater.rate.factory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RateFactoriesContainer {

    private final Map<String, RateFactory> factoriesMapping = new HashMap<>();


    public Map<String, RateFactory> getFactories() {
        return factoriesMapping;
    }

    public void createFactories(RateFactoriesConfigReader factoriesConfigReader) {
        List<String> names = factoriesConfigReader.readRateFactoryNames();
        for (String name : names) {
            try {
                Class<RateFactory> klass = (Class<RateFactory>)Class.forName(name);
                Constructor<RateFactory> factoryConstructor =  klass.getConstructor();
                RateFactory factory = factoryConstructor.newInstance();
                factoriesMapping.put(factory.getId(), factory);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



}
