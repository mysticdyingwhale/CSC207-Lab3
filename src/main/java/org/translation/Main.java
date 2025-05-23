package org.translation;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     *
     * @param args not used by the program
     */
    public static void main(String[] args) {
        Translator translator = new JSONTranslator();
        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     *
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        String breakStr = "quit";
        CountryCodeConverter countryConverter = new CountryCodeConverter();
        LanguageCodeConverter langConverter = new LanguageCodeConverter();

        while (true) {
            String countryName = promptForCountry(translator);
            if (breakStr.equals(countryName)) {
                break;
            }

            String countryCode = countryConverter.fromCountry(countryName);
            String languageName = promptForLanguage(translator, countryCode);
            if (breakStr.equals(languageName)) {
                break;
            }

            String languageCode = langConverter.fromLanguage(languageName);
            String translation = translator.translate(countryCode, languageCode);

            if (translation != null) {
                System.out.println(countryName + " in " + languageName + " is "
                        + translation);
            }
            else {
                System.out.println(countryName + " in " + languageName + " is "
                        + "No translation available");
            }

            System.out.println("Press enter to continue or quit to exit.");
            Scanner s = new Scanner(System.in);
            if (breakStr.equals(s.nextLine())) {
                break;
            }
        }
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForCountry(Translator translator) {
        List<String> countries = translator.getCountries();
        CountryCodeConverter countryConverter = new CountryCodeConverter();
        countries.replaceAll(countryConverter::fromCountryCode);

        // Sort countries,
        Collections.sort(countries);

        countries.forEach(System.out::println);
        System.out.println("select a country from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForLanguage(Translator translator, String country) {
        LanguageCodeConverter langConverter = new LanguageCodeConverter();
        List<String> langCodes = translator.getCountryLanguages(country);
        langCodes.replaceAll(langConverter::fromLanguageCode);

        Collections.sort(langCodes);

        langCodes.forEach(System.out::println);
        System.out.println("select a language from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
}
