package se.danielkonsult.www.kvadratab.fragments;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import se.danielkonsult.www.kvadratab.AppCtrl;
import se.danielkonsult.www.kvadratab.R;
import se.danielkonsult.www.kvadratab.entities.OfficeData;
import se.danielkonsult.www.kvadratab.services.data.ConsultantFilter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultantFilterFragment extends Fragment {

    // Private methods

    private final Handler handler = new Handler();

    private Listener _listener;
    private EditText _editName;
    private FlexboxLayout _layoutOfficeButtons;
    private List<Integer> _officeIds = new ArrayList<>();

    // Private methods

    private void setupOfficeButtons() {
        OfficeData[] offices = AppCtrl.getDataService().getOffices();
        for (OfficeData od : offices) {
            Button btn = new Button(getActivity());
            btn.setText(od.Name);
            btn.setTag(od.Id);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int officeId = (Integer) (view.getTag());
                    int listIndex = _officeIds.indexOf(officeId);
                    if (listIndex < 0) {
                        _officeIds.add(officeId);
                        view.setPressed(true);
                    }
                    else {
                        _officeIds.remove(listIndex);
                        view.setPressed(false);
                    }
                }
            });

            _layoutOfficeButtons.addView(btn);
        }
    }

    /**
     * Gets a value indicating whether the filter has changed
     * compared to the currently used filter.
     */
    public boolean getIsFilterDirty() {
        ConsultantFilter currentFilter = AppCtrl.getDataService().getFilter();

        boolean result = false;
        if (!_editName.getText().toString().trim().equals(currentFilter.getName()))
            result = true;

        // Not the same number of office ids = dirty
        if (currentFilter.getOfficeIds().size() != _officeIds.size())
            return true;

        for (Integer currentId : currentFilter.getOfficeIds()){
            if (!_officeIds.contains(currentId))
                return true;
        }

        return result;
    }

    // Public listener interface

    public interface Listener {
        void onShouldClose();
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
        _editName = (EditText) view.findViewById(R.id.editName);
        // Setup to listen for the Done button
        _editName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (_listener != null)
                                _listener.onShouldClose();
                        }
                    }, 500);
                    return false;
                }
                return false;
            }
        });

        setupOfficeButtons();

        view.setVisibility(View.GONE);
        return  view;
    }

    /**
     * Gets a filter instance based on the current state of the fragment.
     */
    public ConsultantFilter getFilter(){
        return new ConsultantFilter(_officeIds, _editName.getText().toString());
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

