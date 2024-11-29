package com.example.hospitalapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;


// Displaying a list of appointments
public class AppointmentsAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> appointments;

    public AppointmentsAdapter(Context context, ArrayList<String> appointments) {
        super(context, 0, appointments);
        this.context = context;
        this.appointments = appointments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView appointmentTextView = convertView.findViewById(android.R.id.text1);
        appointmentTextView.setText(appointments.get(position));

        return convertView;
    }
}
