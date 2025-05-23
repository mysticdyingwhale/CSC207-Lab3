package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class provides the service of converting language codes to their names.
 */
public class LanguageCodeConverter {
    // Number of countries and thereby number of entries in files are (again) fairly static.
    public static final int TOTAL_LANGUAGES = 200;
    public static final String DELIMITER = "\t";
    private final Map<String, String> langNameToCode;
    // Reverse map,
    private final Map<String, String> langCodeToName;

    /**
     * Default constructor which will load the language codes from "language-codes.txt"
     * in the resources folder.
     */
    public LanguageCodeConverter() {
        this("language-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the language code data from.
     *
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public LanguageCodeConverter(String filename) {
        langNameToCode = new HashMap<>(TOTAL_LANGUAGES);
        langCodeToName = new HashMap<>(TOTAL_LANGUAGES);

        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));
            lines.size();
            Iterator<String> iterator = lines.iterator();
            while (iterator.hasNext()) {
                String line = iterator.next();
                String[] parts = line.split(DELIMITER);
                if (parts.length >= 2) {
                    String langName = parts[0].trim();
                    String langCode = parts[1].trim();
                    this.langNameToCode.put(langName, langCode);
                    this.langCodeToName.put(langCode, langName);
                }
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the name of the language for the given language code.
     *
     * @param code the language code
     * @return the name of the language corresponding to the code
     */
    public String fromLanguageCode(String code) {
        return this.langCodeToName.getOrDefault(code, "Language Not Supported.");
    }

    /**
     * Returns the code of the language for the given language name.
     *
     * @param language the name of the language
     * @return the 2-letter code of the language
     */
    public String fromLanguage(String language) {
        return this.langNameToCode.getOrDefault(language, "Language Not Supported.");
    }

    /**
     * Returns how many languages are included in this code converter.
     *
     * @return how many languages are included in this code converter.
     */
    public int getNumLanguages() {
        return langNameToCode.size();
    }
}
