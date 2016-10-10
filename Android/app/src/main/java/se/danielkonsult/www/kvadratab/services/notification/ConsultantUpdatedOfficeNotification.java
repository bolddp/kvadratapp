package se.danielkonsult.www.kvadratab.services.notification;

/**
 * Created by Daniel on 2016-10-10.
 */
public class ConsultantUpdatedOfficeNotification extends Notification {

    // Constructor


    public ConsultantUpdatedOfficeNotification() {
    }

    public ConsultantUpdatedOfficeNotification(int consultantId, String firstName, String lastName, String newOffice) {
        ConsultantId = consultantId;
        FirstName = firstName;
        LastName = lastName;
        NewOffice = newOffice;
    }

    public int ConsultantId;
    public String FirstName;
    public String LastName;
    public String NewOffice;
}
