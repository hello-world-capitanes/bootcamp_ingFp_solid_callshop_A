package com.helloworld.callshop.rate.timeprice;

import com.helloworld.callshop.rater.rate.Rate;
import com.helloworld.callshop.rater.rate.RatesRepository;
import com.helloworld.callshop.rater.rate.factory.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public class TimePriceRateFactory implements RateFactory {

    private static final String DESCRIPTION = "Tarifa de basada en franjas horarias";

    private static final String ID = "TIME";
    private static final String START_TIME_ZONE_1 = "startTimeZone1";
    private static final String START_TIME_ZONE_2 = "startTimeZone2";
    private static final String START_TIME_ZONE_3 = "startTimeZone3";
    private static final String NAME_RATE_1 = "nameRate1";
    private static final String NAME_RATE_2 = "nameRate2";
    private static final String NAME_RATE_3 = "nameRate3";


    private final Predicate<Object> timeZoneParameterValidator = parameter -> {
        try {
            LocalTime value = parseLocalTime(parameter);
            return true;
        } catch (InvalidParameterValueException | NullPointerException e) {
            return false;
        }
    };

    private final Predicate<Object> nameRateParameterValidator = parameter -> {
        try {
            String value = parseString(parameter);
            Rate rate = RatesRepository.INSTANCE.getRate(value);
            if (rate!=null){
                return true;
            }
            return false;
        } catch (InvalidParameterValueException | NullPointerException e) {
            return false;
        }

    };

    private String parseString(Object parameter) {
        if (parameter == null) {
            throw new NullPointerException("Valor del parametro a null");
        } if (parameter instanceof String value) {
            try {
                return value;
            } catch (ClassCastException  e){
                throw new InvalidParameterValueException("Nombre no valido", e);
            }

        }
        throw new InvalidParameterValueException("Nombre no valido");
    }

    private final Parameter startTimeZone1 = new Parameter(START_TIME_ZONE_1, "Inicio de la primera franja horaria", timeZoneParameterValidator);
    private final Parameter nameRate1 = new Parameter(NAME_RATE_1, "Nombre de la tarifa asociada", nameRateParameterValidator);
    private final Parameter startTimeZone2 = new Parameter(START_TIME_ZONE_2, "Inicio de la segunda franja horaria", timeZoneParameterValidator);
    private final Parameter nameRate2 = new Parameter(NAME_RATE_2, "Nombre de la tarifa asociada", nameRateParameterValidator);

    private final Parameter startTimeZone3 = new Parameter(START_TIME_ZONE_3, "Inicio de la tercera franja horaria", timeZoneParameterValidator);

    private final Parameter nameRate3 = new Parameter(NAME_RATE_3, "Nombre de la tarifa asociada", nameRateParameterValidator);




    @Override
    public Rate makeRate(ParametersReader parametersReader) throws InvalidParameterValueException {

        List<Parameter> parameters = getBasicParameterList();
        parameters.addAll(Arrays.asList(startTimeZone1,nameRate1, startTimeZone2,nameRate2,startTimeZone3,nameRate3));

        ParametersMapper parametersMapper = parametersReader.readParameters(parameters);

        LocalTime startTimeZone1 = parseLocalTime(parametersMapper.getValue(START_TIME_ZONE_1));
        LocalTime startTimeZone2 = parseLocalTime(parametersMapper.getValue(START_TIME_ZONE_2));
        LocalTime startTimeZone3 = parseLocalTime(parametersMapper.getValue(START_TIME_ZONE_3));

        if(!isValidTimeZone(startTimeZone1,startTimeZone2,startTimeZone3)){
            throw new InvalidParameterValueException("Zonas horarias no validas");
        }

        String name1 = parseString(parametersMapper.getValue(NAME_RATE_1));
        String name2 = parseString(parametersMapper.getValue(NAME_RATE_2));
        String name3 = parseString(parametersMapper.getValue(NAME_RATE_3));

        Rate rate1 = RatesRepository.INSTANCE.getRate(name1);
        Rate rate2 = RatesRepository.INSTANCE.getRate(name2);
        Rate rate3 = RatesRepository.INSTANCE.getRate(name3);

        Object name = parametersMapper.getValue(RATE_NAME_NAME);

        if (!getNameValidator().test(name)) {
            throw new InvalidParameterValueException("Nombre no válido");
        }

        return new TimePriceRate((String)name, startTimeZone1, rate1,startTimeZone2,rate2,startTimeZone3,rate3);
    }

    private boolean isValidTimeZone(LocalTime startTimeZone1, LocalTime startTimeZone2, LocalTime startTimeZone3) {

        if (!isValidTimeZoneHour(startTimeZone1,startTimeZone2,startTimeZone3)){
            return false;
        }

        LocalDateTime finFranja1 = LocalDateTime.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth(),startTimeZone2.getHour(),startTimeZone2.getMinute());
        LocalDateTime finFranja2 = LocalDateTime.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth(),startTimeZone3.getHour(),startTimeZone3.getMinute());
        LocalDateTime finFranja3 = LocalDateTime.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth(),startTimeZone1.getHour(),startTimeZone1.getMinute());



        if (startTimeZone2.isBefore(startTimeZone1)){
            finFranja1.plusDays(1);
            finFranja2.plusDays(1);
        } else if (startTimeZone3.isBefore(startTimeZone2)) {
            finFranja3.plusDays(1);
        }

        System.out.println(finFranja1 + "\n" + finFranja2 + "\n" + finFranja3);
        return false;
    }

    private boolean isValidTimeZoneHour(LocalTime startTimeZone1, LocalTime startTimeZone2, LocalTime startTimeZone3) {

        if (startTimeZone1.until(startTimeZone2, ChronoUnit.HOURS) != 0 && startTimeZone2.until(startTimeZone3,ChronoUnit.HOURS) != 0 && startTimeZone3.until(startTimeZone1,ChronoUnit.HOURS) != 0)
            return true;

        return false;
    }

    private LocalTime parseLocalTime(Object parameter) {

        if (parameter == null) {
            throw new NullPointerException("Valor del parametro a null");
        }
        if (parameter instanceof LocalTime value) {
            return value;
        }
        if (parameter instanceof String value) {
            try {
                return LocalTime.parse(value, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e){
                throw new InvalidParameterValueException("Hora no valida", e);
            }

        }
        throw new InvalidParameterValueException("Hora no valida");
    }


    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    public String getId() {
        return ID;
    }


}
