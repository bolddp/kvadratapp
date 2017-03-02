package se.danielkonsult.www.kvadratab.helpers;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.R;

/**
 * Contains logic that tries to determine the gender of a consultant
 * by its first name, using a list of female names to compare.
 */

public class GenderHelper {

    private static final Gson _gson = new Gson();
    private static HashSet<String> _femaleNamesHash;

    private HashSet<String> loadNames() {
        try {
            HashSet<String> result = new HashSet<>();
            InputStream stream = AppCtrl.getApplicationContext().getResources().openRawResource(R.raw.swedish_female_names);
            try {
                InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
                String[] names = _gson.fromJson(reader, String[].class);
                for (String name : names) {
                    result.add(name.toLowerCase());
                }
                return result;
            } finally {
                stream.close();
            }
        } catch (IOException ex) {
            return new HashSet<>();
        }
    }

    /* Gets the suggested gender of a name, returning M for male
    and F for female names.
     */
    public String getGender(String fullName) {
        if (_femaleNamesHash == null) {
            _femaleNamesHash = loadNames();
        }
        String[] namePieces = fullName.split(" ");
        if ((namePieces.length > 0) && _femaleNamesHash.contains(namePieces[0].toLowerCase())) {
            return "F";
        }
        return "M";
    }
}
