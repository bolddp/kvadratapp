package se.danielkonsult.www.kvadratab.services.scraper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses data on the summary page where the categories and
 * offices are listed.
 */
public class SummaryPageParser {

    // Private fields

    static final Pattern itemPattern = Pattern.compile("data-id=[\"|'](\\d*?)[\"|'].*?>(.*?)</a>");

    // Private methods

    private static SummaryPageData.OfficeData[] getOfficeDatas(SummaryPageData summaryPageData, String text) {
        final Pattern officeDivPattern = Pattern.compile("<div class=[\"|']filter-section[\"|'] id=[\"|']office-filter[\"|']>(.*?)</div>");

        List<SummaryPageData.OfficeData> officeDatas = new ArrayList<>();

        // Look up office data
        Matcher matcher = officeDivPattern.matcher(text);
        if (matcher.find()){
            String officeDiv = matcher.group(1);

            matcher = itemPattern.matcher(officeDiv);
            while (matcher.find()){
                int id = Integer.parseInt(matcher.group(1));
                String name = matcher.group(2);

                if (id > 0){
                    SummaryPageData.OfficeData od = summaryPageData.new OfficeData();
                    od.Id = id;
                    od.Name = name;

                    officeDatas.add(od);
                }
            }

            return officeDatas.toArray(new SummaryPageData.OfficeData[officeDatas.size()]);
        }
        else
            return new SummaryPageData.OfficeData[0];
    }

    private static SummaryPageData.TagData[] getTagDatas(SummaryPageData summaryPageData, String text) {
        final Pattern tagDivPattern = Pattern.compile("<div class=[\"|']filter-section[\"|'] id=[\"|']tag-filter[\"|']>(.*?)</div>");

        List<SummaryPageData.TagData> tagDatas = new ArrayList<>();

        // Look up tag data
        Matcher matcher = tagDivPattern.matcher(text);
        if (matcher.find()){
            String tagDiv = matcher.group(1);

            matcher = itemPattern.matcher(tagDiv);
            while (matcher.find()){
                int id = Integer.parseInt(matcher.group(1));
                String name = matcher.group(2);

                if (id > 0){
                    SummaryPageData.TagData td = summaryPageData.new TagData();
                    td.Id = id;
                    td.Name = name;

                    tagDatas.add(td);
                }
            }

            return tagDatas.toArray(new SummaryPageData.TagData[tagDatas.size()]);
        }
        else
            return new SummaryPageData.TagData[0];
    }

    /**
     * Locates office and tag data from web page data.
     * @param text
     * @return
     */
    public static SummaryPageData parse(String text){

        // Remove all newlines from the input
        text = text.replaceAll("\\r\\n|\\r|\\n", " ");
        text = text.replaceAll("\t", "");

        SummaryPageData result = new SummaryPageData();
        result.OfficeDatas = getOfficeDatas(result, text);
        result.TagDatas = getTagDatas(result, text);

        return result;
    }
}
