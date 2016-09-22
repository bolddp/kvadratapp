package se.danielkonsult.www.kvadratab.services.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2016-09-22.
 */
public class ConsultantFilter {

    // Private variables

    private List<Integer> _officeIds = new ArrayList<>();
    private String _name = "";

    // Constructor

    public ConsultantFilter() {
    }

    public ConsultantFilter(List<Integer> officeIds, String name) {
        this._officeIds = officeIds;
        this._name = name;
    }

    // Getters and setters

    public List<Integer> getOfficeIds() {
        return _officeIds;
    }

    public void setOfficeIds(List<Integer> officeIds) {
        this._officeIds = officeIds;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }
}
