package se.danielkonsult.www.kvadratab.helpers.scraper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.entities.ConsultantDetails;
import se.danielkonsult.www.kvadratab.helpers.Utils;

public class ConsultantDetailsParser {

    // Methods

    /**
     * Parses the contents of a consultant details web page.
     */
    public static ConsultantDetails parse(String urlContents) {
        ConsultantDetails result = new ConsultantDetails();

        // Locate the competence areas
        List<String> competences = new ArrayList<>();
        final Pattern competenceAreasPattern = Pattern.compile("<ul id=['|\"]competence-areas['|\"]>(.*?)/ul>");
        Matcher areasMatcher = competenceAreasPattern.matcher(urlContents);
        if (areasMatcher.find()) {
            String competenceData = areasMatcher.group(1);

            final Pattern itemPattern = Pattern.compile("li>(.*?)<");
            Matcher itemMatcher = itemPattern.matcher(competenceData);

            while (itemMatcher.find()){
                String competence = itemMatcher.group(1);
                if (!Utils.isStringNullOrEmpty(competence)) {
                    competences.add(competence);
                }
            }
        }
        result.CompetenceAreas = competences.toArray(new String[competences.size()]);

        // Description pattern
        final Pattern descriptionPattern = Pattern.compile("<h2 class='small-heading'>Översikt</h2><p class='small-text'>(.*?)</p>");
        Matcher descriptionMatcher = descriptionPattern.matcher(urlContents);
        if (descriptionMatcher.find()){
            String description = descriptionMatcher.group(1);
            result.Description = description.replace("<br>", "\n");
        }

        // Overview pattern
        final Pattern overviewPattern = Pattern.compile("<h2 class='small-heading'>Egenskaper</h2><p class='small-text'>(.*?)</p>");
        Matcher overviewMatcher = overviewPattern.matcher(urlContents);
        if (overviewMatcher.find()){
            String overview = overviewMatcher.group(1);
            result.Overview = overview.replace("<br>", "\n");
        }

        return result;
    }
}
