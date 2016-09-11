package se.danielkonsult.www.kvadratab.services.scraper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.entities.TagData;

/**
 * Parses data on the summary page where the categories and
 * offices are listed.
 */
public class SummaryDataParser {

    // Private fields

    static final Pattern itemPattern = Pattern.compile("data-id=[\"|'](\\d*?)[\"|'].*?>(.*?)</a>");

    // Private methods

    private static OfficeData[] getOfficeDatas(SummaryData summaryPageData, String text) {
        final Pattern officeDivPattern = Pattern.compile("<div class=[\"|']filter-section[\"|'] id=[\"|']office-filter[\"|']>(.*?)</div>");

        List<OfficeData> officeDatas = new ArrayList<>();

        // Look up office data
        Matcher matcher = officeDivPattern.matcher(text);
        if (matcher.find()){
            String officeDiv = matcher.group(1);

            matcher = itemPattern.matcher(officeDiv);
            while (matcher.find()){
                int id = Integer.parseInt(matcher.group(1));
                String name = matcher.group(2);

                if (id > 0){
                    OfficeData od = new OfficeData();
                    od.Id = id;
                    od.Name = name;

                    officeDatas.add(od);
                }
            }

            return officeDatas.toArray(new OfficeData[officeDatas.size()]);
        }
        else
            return new OfficeData[0];
    }

    private static TagData[] getTagDatas(SummaryData summaryPageData, String text) {
        final Pattern tagDivPattern = Pattern.compile("<div class=[\"|']filter-section[\"|'] id=[\"|']tag-filter[\"|']>(.*?)</div>");

        List<TagData> tagDatas = new ArrayList<>();

        // Look up tag data
        Matcher matcher = tagDivPattern.matcher(text);
        if (matcher.find()){
            String tagDiv = matcher.group(1);

            matcher = itemPattern.matcher(tagDiv);
            while (matcher.find()){
                int id = Integer.parseInt(matcher.group(1));
                String name = matcher.group(2);

                if (id > 0){
                    TagData td = new TagData();
                    td.Id = id;
                    td.Name = name;

                    tagDatas.add(td);
                }
            }

            return tagDatas.toArray(new TagData[tagDatas.size()]);
        }
        else
            return new TagData[0];
    }

    /**
     * Locates office and tag data from web page data.
     * @param text
     * @return
     */
    public static SummaryData parse(String text){

        // Remove all newlines from the input
        text = text.replaceAll("\\r\\n|\\r|\\n", " ");
        text = text.replaceAll("\t", "");

        SummaryData result = new SummaryData();
        result.OfficeDatas = getOfficeDatas(result, text);
        result.TagDatas = getTagDatas(result, text);

        return result;
    }
}
