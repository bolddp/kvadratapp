package se.danielkonsult.www.kvadratab.helpers;

/**
 * Created by Daniel on 2016-10-08.
 */
public class Constants {
    public static final java.lang.String EXTRA_CONSULTANT_ID = "consultant_id";

    public static final long CONSULTANT_DETAILS_EXPIRY_HOURS = 48;

    public static final int REFRESHER_INTENT_REQUEST_CODE = 1122;
    public static final String REFRESHER_INTENT_ACTION = "se.danielkonsult.www.kvadratab.refresher";
    public static final long REFRESHER_CONSULTANT_IMAGE_COMPARISON_INTERVAL_HOURS = 168; // Once a week the consultant images should be compared
}
