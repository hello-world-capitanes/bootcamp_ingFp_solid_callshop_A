package com.helloworld.callshop.engine;

import com.helloworld.callshop.rater.rate.factory.*;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class JSONParametersReader implements ParametersReader {

    private JSONObject tarifa;
    private ParametersMapperImpl paramsMapper = new ParametersMapperImpl();

    public JSONParametersReader(JSONObject tarifa){
        this.tarifa = tarifa;
    }

    @Override
    public ParametersMapper readParameters(List<Parameter> parameters) {

        JSONObject jsonParametros = tarifa.getJSONObject("parametros");

        Map<String, Object> parametros = jsonParametros.toMap();

        for (Parameter p : parameters) {

            if(parametros.get(p.getName()) == null){
                System.err.println("No se ha especificado un parámetro esencial: " + p.getName());
                System.exit(1);
            } else{
                if(!p.getValidator().test(parametros.get(p.getName()))){
                    System.err.println("El parámetro "+p.getName()+" no ha pasado el test");
                    System.exit(1);
                }
            }

        }

        paramsMapper.putAll(parametros);

        return paramsMapper;
        
    }
}
