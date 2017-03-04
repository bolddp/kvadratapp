package se.danielkonsult.www.kvadratab.helpers.scraper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;

/**
 * Parses the contents of the main web page (where all consultants are listed)
 * and returns it as an array of ConsultantData.
 */
public class ConsultantDataParser {

    // Private methods

    private static void splitAndStoreName(ConsultantData consultantData, String fullName) {
        // Special treatment of people who have their previous last name listed inside parenthesis
        int index = fullName.indexOf("(");
        if (index >= 0)
            fullName = fullName.substring(0,index).trim();

        index = fullName.lastIndexOf(" ");
        if (index < 0) {
            consultantData.LastName = fullName;
            consultantData.FirstName = "-";
        }
        else {
            consultantData.FirstName = fullName.substring(0,index).trim();
            consultantData.LastName = fullName.substring(index).trim();
        }
    }

    // Public methods

    public static ConsultantData[] parse(String urlContents){
        final Pattern pattern = Pattern.compile("<a.*?/konsult/(\\d*).*?<div class='full-name'>(.*?)<.*?<");

        List<ConsultantData> result = new ArrayList<>();

        Matcher matcher = pattern.matcher(urlContents);
        while (matcher.find()){
            String id = matcher.group(1);
            String name = matcher.group(2);

            ConsultantData consultantData = new ConsultantData();

            consultantData.Id = Integer.parseInt(id);
            splitAndStoreName(consultantData, name);

            result.add(consultantData);
        }

        // Convert the list to an array
        return result.toArray(new ConsultantData[result.size()]);
    }
}
