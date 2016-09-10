package se.danielkonsult.www.kvadratab.services.scraper;

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

    public static ConsultantData[] parse(String urlContents){
        final Pattern pattern = Pattern.compile("<a.*?/profil/\\?id=(\\d*).*?<div class='full-name'>(.*?)<.*?<");

        List<ConsultantData> result = new ArrayList<>();

        Matcher matcher = pattern.matcher(urlContents);
        while (matcher.find()){
            String id = matcher.group(1);
            String name = matcher.group(2);

            ConsultantData mpwd = new ConsultantData();
            mpwd.Id = Integer.parseInt(id);
            mpwd.Name = name;

            result.add(mpwd);
        }

        // Convert the list to an array
        return result.toArray(new ConsultantData[result.size()]);
    }
}
