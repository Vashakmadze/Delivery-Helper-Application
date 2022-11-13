package com.example.internallfinal.Fragments.addFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.internallfinal.MainActivity;
import com.example.internallfinal.R;
import com.example.internallfinal.models.Clients;
import com.example.internallfinal.models.Orders;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class addClientsFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private DatabaseReference clientsRef;
    private Button submitButton;


    public addClientsFragment() {
        // Required empty public constructor
    }


    public static addClientsFragment newInstance(String param1, String param2) {
        addClientsFragment fragment = new addClientsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_clients, container, false);

        clientsRef = FirebaseDatabase.getInstance().getReference().child("clients"); // getting the reference for the clients from the database

        submitButton = view.findViewById(R.id.submit_client); // getting the id of the submit button
        submitButton.setOnClickListener(new View.OnClickListener() { // setting the on click listener on the submit button
            @Override
            public void onClick(View v) {
                EditText clientNameInput = view.findViewById(R.id.clientNameInput); // getting the client name input id
                String name = clientNameInput.getText().toString(); // getting the input from the client name edit text

                EditText clientLocationInput = view.findViewById(R.id.clientLocationInput); // getting the client location input id
                String location = clientLocationInput.getText().toString();// getting the input from the client location edit text

                EditText clientNumberInput = view.findViewById(R.id.clientNumberInput); // getting the client number input id
                String number = clientNumberInput.getText().toString(); // getting the input from the client number edit text

                Clients client = new Clients(location, name, number); // creating a new client

                clientsRef.push().setValue(client); // pushing the new client into the database

                Intent intent = new Intent(view.getContext(), MainActivity.class); // creating the intent to go from addActivity to mainActivity
                startActivity(intent); // initializing intent
            }
        });

        return view;
    }
}