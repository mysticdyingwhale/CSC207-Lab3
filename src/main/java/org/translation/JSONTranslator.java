package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {
    private final Map<String, List<String>> codeToLangMap;
    private final Map<String, Map<String, String>> translations;

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     *
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        codeToLangMap = new HashMap<>();
        translations = new HashMap<>();

        try {
            String jsonString = Files.readString(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject entry = jsonArray.getJSONObject(i);
                String countryCode = entry.getString("alpha3");

                Map<String, String> countryTranslations = new HashMap<>();
                List<String> languages = new ArrayList<>();

                for (String key : entry.keySet()) {
                    if (!List.of("id", "alpha2", "alpha3").contains(key)) {
                        String translatedName = entry.getString(key);
                        countryTranslations.put(key, translatedName);
                        languages.add(key);
                    }
                }

                translations.put(countryCode, countryTranslations);
                codeToLangMap.put(countryCode, languages);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Update the methods in JSONTranslator
    @Override
    public List<String> getCountryLanguages(String country) {
        return new ArrayList<>(codeToLangMap.getOrDefault(country, Collections.emptyList()));
    }

    @Override
    public List<String> getCountries() {
        return new ArrayList<>(translations.keySet());
    }

    @Override
    public String translate(String country, String language) {
        Map<String, String> countryTranslations = translations.get(country);
        if (countryTranslations == null) {
            return null;
        }
        return countryTranslations.get(language);
    }
}
