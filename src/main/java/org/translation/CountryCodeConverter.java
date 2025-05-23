package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides the service of converting country codes to their names.
 */
public class CountryCodeConverter {
    // Number of countries and thereby number of entries in files are fairly static.
    public static final int TOTAL_COUNTRIES = 260;
    public static final int ALPHA3_IDX = 3;
    public static final int JSON_PARSE_SIZE = 4;
    public static final String DELIMITER = "\t";
    private final Map<String, String> countryToAlphaCode;
    private final Map<String, Integer> countryToId;
    // Reverse map,
    private final Map<String, String> alphaCodeToCountry;

    /**
     * Default constructor which will load the country codes from "country-codes.txt"
     * in the resources folder.
     */
    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the country code data from.
     *
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public CountryCodeConverter(String filename) {
        countryToAlphaCode = new HashMap<>(TOTAL_COUNTRIES);
        countryToId = new HashMap<>(TOTAL_COUNTRIES);
        alphaCodeToCountry = new HashMap<>(TOTAL_COUNTRIES);

        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            // Skip header line (index 0)
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(DELIMITER);
                if (parts.length >= JSON_PARSE_SIZE) {
                    String countryName = parts[0].trim();
                    String alphaCode = parts[2].trim().toUpperCase();
                    try {
                        int countryNum = Integer.parseInt(parts[ALPHA3_IDX].trim());
                        this.countryToAlphaCode.put(countryName.toLowerCase(), alphaCode);
                        this.countryToId.put(countryName, countryNum);
                        this.alphaCodeToCountry.put(alphaCode, countryName);
                    }
                    catch (NumberFormatException ex) {
                        System.err.println("Skipping invalid numeric code for: " + countryName);
                    }
                }
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the name of the country for the given country code.
     *
     * @param code the 3-letter code of the country
     * @return the name of the country corresponding to the code
     */
    public String fromCountryCode(String code) {
        String codeUpper = code.trim().toUpperCase();
        return this.alphaCodeToCountry.getOrDefault(codeUpper, "Country Code Not Supported.");
    }

    /**
     * Returns the code of the country for the given country name.
     *
     * @param country the name of the country
     * @return the 3-letter code of the country
     */
    public String fromCountry(String country) {
        return this.countryToAlphaCode.get(country.toLowerCase());
    }

    /**
     * Returns how many countries are included in this code converter.
     *
     * @return how many countries are included in this code converter.
     */
    public int getNumCountries() {
        return this.countryToId.size();
    }
}
