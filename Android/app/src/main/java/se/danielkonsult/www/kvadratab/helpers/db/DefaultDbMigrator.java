package se.danielkonsult.www.kvadratab.helpers.db;

import android.util.Log;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.helpers.Utils;

/**
 * Created by bigha on 2017-03-04.
 */

public class DefaultDbMigrator implements DbMigrator {
    @Override
    public void ensureGenderSet(KvadratDb db) {
        ConsultantData[] consultants = db.getConsultantDataRepository().getAll(false);
        for (ConsultantData cd : consultants) {
            if (Utils.isStringNullOrEmpty(cd.Gender)) {
                String gender = AppCtrl.getGenderHelper().getGender(cd.FirstName);
                db.getConsultantDataRepository().updateGender(cd.Id, gender);
            }
        }
    }
}
