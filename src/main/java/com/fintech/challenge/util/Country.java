package com.fintech.challenge.util;

import com.fintech.challenge.exceptions.client.InvalidCountryException;

public enum Country {
    ARGENTINA, BRAZIL, CHILE, COLOMBIA, PERU, URUGUAY, VENEZUELA, ECUADOR, PARAGUAY, BOLIVIA, MEXICO, EEUU;

    public static Country fromString(String countryName) {
        Country country;
        try {
            country = Country.valueOf(countryName);
        } catch (IllegalArgumentException e) {
            throw new InvalidCountryException(
                    String.format("Invalid country: %s", countryName));
        }
        return country;
    }
}
