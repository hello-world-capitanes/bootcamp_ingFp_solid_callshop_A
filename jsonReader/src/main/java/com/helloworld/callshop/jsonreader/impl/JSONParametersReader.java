package com.helloworld.callshop.jsonreader.impl;

import com.helloworld.callshop.engine.ParametersMapperImpl;
import com.helloworld.callshop.rater.rate.factory.*;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class JSONParametersReader implements ParametersReader {

    private JSONObject object;
    private ParametersMapperImpl paramsMapper = new ParametersMapperImpl();

    public JSONParametersReader(JSONObject object){
        this.object = object;
    }

    @Override
    public ParametersMapper readParameters(List<Parameter> parameters) {

        JSONObject jsonParametros = object.getJSONObject("parametros");

        Map<String, Object> parametros = jsonParametros.toMap();

        paramsMapper.putAll(parametros);

        return paramsMapper;
    }
}
