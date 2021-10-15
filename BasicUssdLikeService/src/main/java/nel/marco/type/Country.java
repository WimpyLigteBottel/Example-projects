package nel.marco.type;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Optional;


public enum Country {

    //Ideally the currency will be stored in database or properties file so that it can be dynamically updated
    //HARD CODED CURRENCY IS BAD IDEA!!!
    KENYA("KES", 6.10),
    MALAWI("MWK", 42.50);


    private final String currency;
    private final BigDecimal currencyAmount;

    Country(String currency, double currencyAmount) {
        this.currency = currency;
        //When working with amounts you always want to work with BigDecimal do to floating points in double
        this.currencyAmount = BigDecimal.valueOf(currencyAmount).setScale(2,RoundingMode.FLOOR);
    }

    public String getCurrency() {
        return currency;
    }

    public static Country parseValue(String value) {

        Optional<Country> countriesFound = Arrays.stream(Country.values())
                .filter(country -> value.equalsIgnoreCase(country.name()))
                .findFirst();


        if (countriesFound.isPresent()) {
            return countriesFound.get();
        }

        throw new RuntimeException(String.format("Invalid currency supplied [value=%s]", value));
    }

    public static BigDecimal convertAmount(BigDecimal randAmount, Country country) {
        //Added the setScale so it defaults to x.00 points and not x.0000
        return randAmount.multiply(country.currencyAmount).setScale(2, RoundingMode.FLOOR);
    }
}
