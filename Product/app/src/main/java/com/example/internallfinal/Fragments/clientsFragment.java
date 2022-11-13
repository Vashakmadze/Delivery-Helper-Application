package com.example.internallfinal.Fragments;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.internallfinal.R;
import com.example.internallfinal.models.Clients;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class clientsFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private RecyclerView rvClients;
    private DatabaseReference clientsRef;



    public clientsFragment() {
        // Required empty public constructor
    }

    public static clientsFragment newInstance(String param1, String param2) {
        clientsFragment fragment = new clientsFragment();
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
        View view =  inflater.inflate(R.layout.fragment_clients, container, false);

        rvClients = view.findViewById(R.id.clientsRecyclerView);
        rvClients.setLayoutManager(new LinearLayoutManager(view.getContext()));
        clientsRef = FirebaseDatabase.getInstance().getReference().child("clients"); // getting database reference


        return view;
    }
    @Override
    public void onStart() { // on start method of fragment
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Clients>()
                .setQuery(clientsRef, Clients.class).build();

        FirebaseRecyclerAdapter<Clients, clientsViewHolder> adapter = new FirebaseRecyclerAdapter<Clients, clientsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull clientsViewHolder holder, int position, @NonNull Clients model) {

                String clientId = getRef(position).getKey(); // getting car id from database by getting the key

                clientsRef.child(clientId).addListenerForSingleValueEvent(new ValueEventListener() { // adding listener to listen for data change in car in database
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nameString = snapshot.child("name").getValue().toString(); // getting numberplate from database
                        String numberString = snapshot.child("number").getValue().toString(); // getting entrance date from database
                        String locationString = snapshot.child("location").getValue().toString(); // getting color from database


                        holder.name.setText(nameString);
                        holder.number.setText(numberString);
                        holder.location.setText(locationString);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

                holder.callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clientsRef.child(clientId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String number = snapshot.child("number").getValue(String.class);
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                callIntent.setData(Uri.parse("tel:"+number));
                                startActivity(callIntent);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });
                    }
                });

                holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clientsRef.child(clientId).removeValue();
                    }
                });

                holder.mapButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clientsRef.child(clientId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String location = snapshot.child("location").getValue(String.class);
                                if(location==null) {
                                    Toast.makeText(getContext(), "Address Not Found!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                    try {
                                        List<Address> listAddresss = geocoder.getFromLocationName(location, 1);
                                        if(listAddresss.size()>0) {
                                            Address address = listAddresss.get(0);
                                            Uri uri = Uri.parse(
                                                    "geo:"    + address.getLatitude() +
                                                            ","       + address.getLongitude() +
                                                            "?q="     + address.getLatitude() +
                                                            ","       + address.getLongitude() +
                                                            "("       + address + ")");
                                            Intent in = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(in);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });
                    }
                });


            }

            @NonNull
            @Override
            public clientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clients_layout, parent, false);
                clientsViewHolder viewHolder = new clientsViewHolder(view);

                return viewHolder;

            }
        };

        rvClients.setAdapter(adapter); // setting adapter for our recycler view
        adapter.startListening(); // starts listening for database changes and populates adapter

    }


    public static class clientsViewHolder extends RecyclerView.ViewHolder {

        TextView name, number, location;
        Button callButton, mapButton, deleteButton;


        public clientsViewHolder(@NonNull View itemView) {
            super(itemView);

            callButton = itemView.findViewById(R.id.callClient);
            mapButton = itemView.findViewById(R.id.mapClient);
            deleteButton = itemView.findViewById(R.id.deleteClient);
            name = itemView.findViewById(R.id.nameClient);
            number = itemView.findViewById(R.id.phoneClient);
            location = itemView.findViewById(R.id.locationOrder);


        }
    }


}