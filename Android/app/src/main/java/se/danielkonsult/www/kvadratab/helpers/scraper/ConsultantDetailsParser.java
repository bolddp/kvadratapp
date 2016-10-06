package se.danielkonsult.www.kvadratab.helpers.scraper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;

/**
 * Created by Daniel on 2016-10-06.
 */
public class ConsultantDetailsParser {

    // Methods

    public static ConsultantData parse(String urlContents) {
        ConsultantData result = new ConsultantData();

        // Locate the competence areas
        List<String> competences = new ArrayList<>();
        final Pattern competenceAreasPattern = Pattern.compile("<ul id=['|\"]competence-areas['|\"]>(.*?)/ul>");
        Matcher areasMatcher = competenceAreasPattern.matcher(urlContents);
        if (areasMatcher.find()) {
            String competenceData = areasMatcher.group(1);

            final Pattern itemPattern = Pattern.compile("li>(.*?)<");
            Matcher itemMatcher = itemPattern.matcher(competenceData);

            while (itemMatcher.find()){
                competences.add(itemMatcher.group(1));
            }
        }
        result.CompetenceAreas = competences.toArray(new String[competences.size()]);

        // Overview pattern
        final Pattern overviewPattern = Pattern.compile("<h2 class='small-heading'>Översikt</h2><p class='small-text'>(.*?)</p>");
        Matcher overviewMatcher = overviewPattern.matcher(urlContents);
        if (overviewMatcher.find()){
            String overview = overviewMatcher.group(1);
            result.Overview = overview.replace("<br>", "\n");
        }

        // Overview2 pattern
        final Pattern overview2Pattern = Pattern.compile("<h2 class='small-heading'>Egenskaper</h2><p class='small-text'>(.*?)</p>");
        Matcher overview2Matcher = overview2Pattern.matcher(urlContents);
        if (overview2Matcher.find()){
            String overview2 = overview2Matcher.group(1);
            result.Overview2 = overview2.replace("<br>", "\n");
        }

        return result;
    }
}
