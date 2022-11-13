package com.example.internallfinal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.internallfinal.R;
import com.example.internallfinal.models.Clients;

import java.util.ArrayList;
import java.util.List;

public class clientsSpinnerAdapter extends ArrayAdapter<Clients> {

   public clientsSpinnerAdapter(Context context, ArrayList<Clients> clientsList) {
       super(context, 0, clientsList);
   }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position,View convertView, ViewGroup parent) {
       if (convertView== null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.clients_spinner_row, parent,  false); // infating view with the created layout
       }
       TextView name = convertView.findViewById(R.id.client_view_name); // getting the id of the name in from the layout

       Clients currentClient = getItem(position); // getting current client by getting the position of the item

       if (currentClient!= null) {
           name.setText(currentClient.getName()); // getting the name from the client and setting that to layout

       }

       return convertView;
    }
}
