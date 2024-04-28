package com.helloworld.callshop.rater.rate.factory;

import java.util.List;

public interface ParametersReader {

    ParametersMapper readParameters(List<Parameter> parameters);
}
