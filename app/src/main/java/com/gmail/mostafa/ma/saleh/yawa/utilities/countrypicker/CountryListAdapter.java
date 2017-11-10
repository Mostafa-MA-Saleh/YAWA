package com.gmail.mostafa.ma.saleh.yawa.utilities.countrypicker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gmail.mostafa.ma.saleh.yawa.R;
import com.gmail.mostafa.ma.saleh.yawa.R.drawable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

class CountryListAdapter extends BaseAdapter {

    private List<Country> countries;
    private LayoutInflater inflater;

    /**
     * Constructor
     *
     * @param context context
     * @param countries a days of countries
     */
    CountryListAdapter(Context context, List<Country> countries) {
        super();
        this.countries = countries;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * The drawable image name has the format "flag_$countryCode". We need to
     * load the drawable dynamically from country code. Code from
     * http://stackoverflow.com/
     * questions/3042961/how-can-i-get-the-resource-id-of
     * -an-image-if-i-know-its-name
     *
     * @param drawableName name of the drawable
     * @return resId of the drawable
     */
    private int getResId(String drawableName) {

        try {
            Class<drawable> res = R.drawable.class;
            Field field = res.getField(drawableName);
            return field.getInt(null);
        } catch (Exception e) {
            Log.e("COUNTRYPICKER", "Failure to get drawable id.", e);
        }
        return -1;
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    /**
     * Return country_picker_row for each country
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cellView = convertView;
        TextView cell;
        Country country = countries.get(position);

        if (convertView == null) {
            cellView = inflater.inflate(R.layout.country_picker_row, parent, false);
            cell = cellView.findViewById(R.id.row_title);
            cellView.setTag(cell);
        } else {
            cell = (TextView) cellView.getTag();
        }

        cell.setText(country.getName());

        // Load drawable dynamically from country code
        String drawableName = "flag_"
                + country.getCode().toLowerCase(Locale.ENGLISH);
        cell.setCompoundDrawablesWithIntrinsicBounds(getResId(drawableName), 0, 0, 0);
        return cellView;
    }

}