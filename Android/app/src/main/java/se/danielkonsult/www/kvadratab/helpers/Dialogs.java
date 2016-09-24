package se.danielkonsult.www.kvadratab.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Daniel on 2015-11-08.
 */
public class Dialogs {

    /*
 * Displays an error-message in an alert-dialog with an OK button
 */
    public static void displayError(Context context, String header, String message, String okButtonText, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(header).setCancelable(false)
                .setNeutralButton(okButtonText, listener);
        builder.create().show();
    }

    public static void displayError(Context context, int headerResId, int msgResId, int okButtonResId, DialogInterface.OnClickListener listener) {
        displayError(context, context.getString(headerResId), context.getString(msgResId), context.getString(okButtonResId), listener);
    }

    /*
     * Displays an info-message in an alert-dialog with an OK button
     */
    public static void displayInfo(Context context, String header, String message, String okButtonText, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(header).setCancelable(false)
                .setNeutralButton(okButtonText, listener);
        builder.create().show();
    }

    public  static void displayInfo(Context context, int headerResId, int msgResId, int okButtonResId, DialogInterface.OnClickListener listener){
        displayInfo(context, context.getString(headerResId), context.getString(msgResId), context.getString(okButtonResId), listener);
    }

    /*
    Displays a dialog with a Yes and No button.
     */
    public static void displayYesNo(Context context, String header, String message,
                                    String yesButtonText, String noButtonText,
                                    DialogInterface.OnClickListener yesListener,
                                    DialogInterface.OnClickListener noListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(header).setCancelable(false)
                .setPositiveButton(yesButtonText, yesListener)
                .setNegativeButton(noButtonText, noListener);

        builder.create().show();
    }

    /*
    Displays a dialog with a Yes and No button.
     */
    public static void displayYesNo(Context context, int headerResId, int msgResId,
                                    int yesButtonResId, int noButtonResId,
                                    DialogInterface.OnClickListener yesListener,
                                    DialogInterface.OnClickListener noListener){
        displayYesNo(context, context.getString(headerResId), context.getString(msgResId),
                context.getString(yesButtonResId), context.getString(noButtonResId),
                yesListener, noListener);
    }


}
