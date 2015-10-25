package com.markme.mmapp.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.markme.mmapp.R;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private ArrayList<String> list;
    private Context spinnerContext;

    public CustomSpinnerAdapter(Context context, int customViewResId, ArrayList<String> pList){
        super(context, customViewResId, pList);
        list = pList;
        spinnerContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) spinnerContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.custom_spinner_item_layout, parent, false);
        }
        TextView itemValue =  (TextView) convertView.findViewById(R.id.custom_spinner_main_content);
        if(position < 5)
            itemValue.setText(list.get(position));

        return convertView;
    }


    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource(resource);
    }
}
