package com.coderbd.smartlocationfinder.location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coderbd.smartlocationfinder.R;

import java.util.List;


public class LocationDataAdapter extends BaseAdapter {
    private final Context context;
    private List<LocationData> localeDataList;
    LayoutInflater inflater;
    public LocationDataAdapter(Context context, List<LocationData> localeDataList) {
        this.context = context;
        this.localeDataList = localeDataList;

    }

    @Override
    public int getCount() {
        return localeDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return localeDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=View.inflate(context, R.layout.location_list, null);
        // TextView ids=(TextView)v.findViewById(R.id.view_id);
        TextView cname=(TextView)v.findViewById(R.id.view_cname);
        TextView name=(TextView)v.findViewById(R.id.view_name);
        TextView lat=(TextView)v.findViewById(R.id.view_lat);
        TextView lon=(TextView)v.findViewById(R.id.view_lon);

        // set Text
        // ids.setText(contactList.get(i).getID());
        cname.setText(localeDataList.get(i).getCname());
        name.setText(localeDataList.get(i).getName());
        lat.setText(localeDataList.get(i).getLat());
        lon.setText(localeDataList.get(i).getLon());

        return v;
    }
}
