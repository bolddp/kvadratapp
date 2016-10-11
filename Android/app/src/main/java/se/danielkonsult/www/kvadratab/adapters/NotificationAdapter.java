package se.danielkonsult.www.kvadratab.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.services.notification.ConsultantInsertedNotification;
import se.danielkonsult.www.kvadratab.services.notification.Notification;

/**
 * Created by Daniel on 2016-10-11.
 */
public class NotificationAdapter extends ArrayAdapter<Notification> {

    private final Context _context;

    public NotificationAdapter(Context context, Notification[] objects) {
        super(context, R.layout.adapter_notification_list, objects);
        _context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_consultant_list, null);
        }

        Notification item = getItem(position);

        ImageView imgMain = (ImageView) convertView.findViewById(R.id.imgMain);
        TextView tvHeader = (TextView) convertView.findViewById(R.id.tvHeader);
        TextView tvDetails = (TextView) convertView.findViewById(R.id.tvDetails);

        Bitmap bitmap = item.getBitmap();
        if (bitmap == null){
            imgMain.setVisibility(View.INVISIBLE);
        }
        else {
            imgMain.setImageBitmap(bitmap);
            imgMain.setVisibility(View.VISIBLE);
        }

        tvHeader.setText(item.getHeader());
        tvDetails.setText(item.getDetails());

        return convertView;
    }
}
