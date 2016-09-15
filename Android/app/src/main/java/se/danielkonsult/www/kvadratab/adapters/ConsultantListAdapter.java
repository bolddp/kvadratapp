package se.danielkonsult.www.kvadratab.adapters;

import android.content.Context;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.entities.ConsultantData;
import se.danielkonsult.www.kvadratab.helpers.scraper.ImageHelper;

/**
 * Created by Daniel on 2016-09-15.
 */
public class ConsultantListAdapter extends ArrayAdapter<ConsultantData> {

    // Private variables

    private final Context _context;

    // Constructor


    public ConsultantListAdapter(Context context, ConsultantData[] objects) {
        super(context, R.layout.adapter_consultant_list, objects);
        _context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_consultant_list, null);
        }

        ConsultantData item = getItem(position);
        String officeText = "";
        if (item.Office != null)
            officeText = item.Office.Name;

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvOffice = (TextView) convertView.findViewById(R.id.tvOffice);
        ImageView imgConsultant = (ImageView) convertView.findViewById(R.id.imgConsultant);

        tvName.setText(String.format("%s %s", item.FirstName, item.LastName));
        tvOffice.setText(officeText);

        imgConsultant.setImageBitmap(ImageHelper.getConsultantBitmapFromFile(item.Id));

        return convertView;
    }
}
