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
    public ParametersMapper readParameters(List<Parameter> parameters) throws InvalidParameterValueException{

        JSONObject jsonParametros = tarifa.getJSONObject("parametros");

        Map<String, Object> parametros = jsonParametros.toMap();

        for (Parameter p : parameters) {

            if(parametros.get(p.getName()) == null){
                throw new InvalidParameterValueException("No se ha especificado un parámetro esencial: "+p.getName());
            } else{
                if(!p.getValidator().test(parametros.get(p.getName()))){
                    throw new InvalidParameterValueException("El parámetro "+p.getName()+" no ha pasado el test");
                }
            }

        }

        paramsMapper.putAll(parametros);

        return paramsMapper;
        
    }
}
