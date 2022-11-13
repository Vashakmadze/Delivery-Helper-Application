package com.example.internallfinal.Fragments.addFragments;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.lang3.RandomStringUtils;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import com.example.internallfinal.MainActivity;
import com.example.internallfinal.R;
import com.example.internallfinal.addActivity;
import com.example.internallfinal.Adapters.clientsSpinnerAdapter;
import com.example.internallfinal.models.Clients;
import com.example.internallfinal.models.Orders;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addOrdersFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Spinner spinner;
    private DatabaseReference clientsRef;
    private DatabaseReference ordersRef;
    private ArrayList<Clients> mClientsList = new ArrayList<Clients>();
    private clientsSpinnerAdapter mAdapter;
    private Clients selectedClient;
    private Button submitButton;
    private Button addClient;

    public addOrdersFragment() {
        // Required empty public constructor
    }


    public static addOrdersFragment newInstance(String param1, String param2) {
        addOrdersFragment fragment = new addOrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_orders, container, false);

        spinner=view.findViewById(R.id.spinner_clients); // getting the id of the spinner
        clientsRef = FirebaseDatabase.getInstance().getReference().child("clients"); // getting the reference for the clients from the database
        ordersRef = FirebaseDatabase.getInstance().getReference().child("orders"); // getting the reference for the orders from the database

        clientsRef.addValueEventListener(new ValueEventListener() { // adding the listener to database
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Clients client = childSnapshot.getValue(Clients.class); // getting every client using for each loop
                    mClientsList.add(client); // adding the client to a list
                }
                mAdapter = new clientsSpinnerAdapter(view.getContext(), mClientsList); // creating a new clientsSpinnerAdapter and passing in the context and the clients list
                spinner.setAdapter(mAdapter); // setting the adapter to the spinner
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // setting the item selection listener on the spinner
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedClient = (Clients) parent.getItemAtPosition(position); // when the item is selected we equal that to selectedCleint
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitButton = view.findViewById(R.id.submit_order); // getting the id of submit button
        submitButton.setOnClickListener(new View.OnClickListener() { // setting the on click listener to submit button
            @Override
            public void onClick(View v) {
                TextInputLayout infoInput = view.findViewById(R.id.orderInfoInputLayout); // getting the id of info input
                String info = infoInput.getEditText().getText().toString(); // getting the text fron info input and saving that to a string
                String shortId = RandomStringUtils.randomNumeric(8); // generating a random short id using RandomStringUtils
                Orders order = new Orders(shortId, selectedClient, info); // creating a new order object
                ordersRef.push().setValue(order); // pushing the order to the ddatabase referebce that we  got earlier
                Intent intent = new Intent(view.getContext(), MainActivity.class); // creating the intent to go from addactivity to main activity
                startActivity(intent); // intilizaing the intent
            }
        });

        addClient = view.findViewById(R.id.add_clients_order); // getting the id of add client button
        addClient.setOnClickListener(new View.OnClickListener() { // setting the listener to add client button
            @Override
            public void onClick(View v) {
                ((addActivity)getActivity()).replaceFragment();
            } // on click we initilaize the method created in addActivity
        });

        return view;
    }


}