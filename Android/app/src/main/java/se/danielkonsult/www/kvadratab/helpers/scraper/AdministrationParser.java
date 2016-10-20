package se.danielkonsult.www.kvadratab.helpers.scraper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;

/**
 * Parses data related to the administration and sales of consultants.
 */
public class AdministrationParser {

    /**
     * Locates the office URL's in the Kvadrat Contact page
     */
    public static String[] parseOfficeUrls(String urlContents) {
        final Pattern pattern = Pattern.compile("page-item-\\d*\"><a href=\"(.*?)\"");

        List<String> result = new ArrayList<>();

        Matcher matcher = pattern.matcher(urlContents);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }

        return result.toArray(new String[result.size()]);
    }

    public static ConsultantData[] parseOfficeDetails(String urlContents){
        final Pattern pattern = Pattern.compile("<div class='content-column one_third'><p><a(.*?)</div>");

        List<ConsultantData> result = new ArrayList<>();

        Matcher matcher = pattern.matcher(urlContents);
        while (matcher.find()) {
            
        }

        return result.toArray(new ConsultantData[result.size()]);
    }
}
