package se.danielkonsult.www.kvadratab.fragments;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.flexbox.FlexboxLayout;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.entities.OfficeData;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultantFilterFragment extends Fragment {

    // Private methods

    private Listener _listener;
    private FlexboxLayout _layoutOfficeButtons;

    // Private methods

    private void setupOfficeButtons() {
        OfficeData[] offices = AppCtrl.getDataService().getOffices();
        for (OfficeData od : offices) {
            Button btn = new Button(getActivity());
            btn.setText(od.Name);
            btn.setTag(od.Id);

            _layoutOfficeButtons.addView(btn);
        }
    }

    // Public listener interface

    public interface Listener {
        void onClose();
    }

    // Constructor

    public ConsultantFilterFragment() {
        // Required empty public constructor
    }

    // Methods (Fragment)


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_consultant_filter, container, false);
        _layoutOfficeButtons = (FlexboxLayout) view.findViewById(R.id.layoutOfficeButtons);

        setupOfficeButtons();

        return  view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (_listener != null)
            _listener.onClose();
    }

    public void setListener(Listener listener){
        _listener = listener;
    }

    /**
     * Updates the contents of the GUI to reflect the state of the filter.
     */
    public void refresh(){

    }

}
