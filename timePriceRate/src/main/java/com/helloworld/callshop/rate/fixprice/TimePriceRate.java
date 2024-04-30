public class TimePriceRate extends AbstractRate implements Rate {

    // De 4AM a 12PM
    private LocalTime inicioFranjaMañana;
    private Rate franjaMañana;

    // De 1pm a 8PM
    private LocalTime inicioFranjaTarde;
    private Rate franjaTarde;

    // De 9PM a 3PM
    private LocalTime inicioFranjaNoche;
    private Rate franjaNoche;

    public TimePriceRate(LocalTime inicioFranjaMañana, Rate franjaMañana, LocalTime inicioFranjaTarde, Rate franjaTarde,  LocalTime inicioFranjaNoche, Rate franjaNoche) {
        this.inicioFranjaMañana = inicioFranjaMañana;
        this.franjaMañana = franjaMañana;
        this.inicioFranjaTarde = inicioFranjaTarde;
        this.franjaTarde = franjaTarde;
        this.inicioFranjaNoche = inicioFranjaNoche;
        this.franjaNoche = franjaNoche;
    }

    @Override
    public BigDecimal calculatePrice(RateableCall rateableCall) {
        BigDecimal duration = new BigDecimal(rateableCall.getDuration()); //duration in seconds
        if (rateableCall.getCallStart() < inicioFranjaTarde) {
            // Franja Mañana
            return franjaMañana.calculatePrice(rateableCall);
        }  else if (rateableCall.getCallStart() >= inicioFranjaTarde && rateableCall.getCallStart() < inicioFranjaNoche) {
            // Franja Tarde
            return franjaTarde.calculatePrice(rateableCall);
        } else if (rateableCall.getCallStart() >= inicioFranjaNoche && rateableCall.getCallStart() <= 24) {
            // Franja Noche
            return franjaNoche.calculatePrice(rateableCall);
        } else {
            throw new IllegalArgumentExceptio("Franja horaria no disponible")
        }
    }
}
