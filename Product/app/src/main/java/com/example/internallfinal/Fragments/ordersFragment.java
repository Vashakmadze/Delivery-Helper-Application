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

import com.example.internallfinal.MainActivity;
import com.example.internallfinal.R;
import com.example.internallfinal.addActivity;
import com.example.internallfinal.models.Clients;
import com.example.internallfinal.models.Orders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class ordersFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private RecyclerView rvOrders;
    private DatabaseReference ordersRef;
    private DatabaseReference clientsRef;


    public ordersFragment() {
        // Required empty public constructor
    }


    public static ordersFragment newInstance(String param1, String param2) {
        ordersFragment fragment = new ordersFragment();
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
        View view =  inflater.inflate(R.layout.fragment_orders, container, false);


        rvOrders = view.findViewById(R.id.ordersRecyclerView); // getting the id of orders recycler view
        rvOrders.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ordersRef = FirebaseDatabase.getInstance().getReference().child("orders"); // getting orders database reference

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Orders>() // we create the firebase recycler options and pass ordersRef and orders.class as arguments
                .setQuery(ordersRef, Orders.class).build();

        FirebaseRecyclerAdapter<Orders, ordersFragment.ordersViewHolder> adapter = new FirebaseRecyclerAdapter<Orders, ordersFragment.ordersViewHolder>(options) { // we then create the firebaserecycler adapter and pass Orders calss and ordersFragment viewholder as arguments
            @Override
            protected void onBindViewHolder(@NonNull ordersFragment.ordersViewHolder holder, int position, @NonNull Orders model) {

                String orderId = getRef(position).getKey(); // we get the uniqe key of order by referencing the database and passing the position which is set as an argument in onBindViewHolder
                ordersRef.child(orderId).addListenerForSingleValueEvent(new ValueEventListener() { // we add the listener for ordersRef and passin the unique key to listen to the single order
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String id = snapshot.child("id").getValue().toString(); // we get the id in strings
                        String infoString = snapshot.child("orderInfo").getValue().toString(); // we get the order info in strings
                        String locationString = snapshot.child("client").child("location").getValue().toString(); // and we get the location of the order in strings by accessing the child client in the order and gettuing the location from that

                        holder.id.setText("Order Id: " + id); // setting the order it text
                        holder.info.setText(infoString); // setting the info text
                        holder.location.setText(locationString); // setting the text
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                holder.callButton.setOnClickListener(new View.OnClickListener() { // setting the on click listener on call button
                    @Override
                    public void onClick(View v) {
                        ordersRef.child(orderId).addListenerForSingleValueEvent(new ValueEventListener() { // setting a value listener on order
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String number = snapshot.child("client").child("number").getValue(String.class); // we get the number of the client
                                Intent callIntent = new Intent(Intent.ACTION_DIAL); // we create an intent to go to action dial
                                callIntent.setData(Uri.parse("tel:"+number)); // we put in the number
                                startActivity(callIntent); // we initiate the intent
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

                holder.finish.setOnClickListener(new View.OnClickListener() { // setting the on click listener on finish button
                    @Override
                    public void onClick(View v) {
                        ordersRef.child(orderId).removeValue();
                    } // removing the order from the databsae
                });

                holder.mapButton.setOnClickListener(new View.OnClickListener() { // setting the on click listener on map button
                    @Override
                    public void onClick(View v) {

                        ordersRef.child(orderId).child("client").addListenerForSingleValueEvent(new ValueEventListener() { // setting a value listener on orders client
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String location = snapshot.child("location").getValue(String.class); // getting the location of the client from the databsae
                                if(location==null) {
                                    Toast.makeText(getContext(), "Address Not Found!", Toast.LENGTH_SHORT).show(); // toasting if the address equals to null
                                } else {
                                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault()); // creating the geocoder
                                    try {
                                        List<Address> listAddresss = geocoder.getFromLocationName(location, 1); // creating the list of addresses for our adress
                                        if(listAddresss.size()>0) {
                                            Address address = listAddresss.get(0); // we get the address by getting the first element from the list
                                            Uri uri = Uri.parse( // we parse the uri by follwoing parsing sequence
                                                    "geo:"    + address.getLatitude() +
                                                            ","       + address.getLongitude() +
                                                            "?q="     + address.getLatitude() +
                                                            ","       + address.getLongitude() +
                                                            "("       + address + ")");
                                            Intent in = new Intent(Intent.ACTION_VIEW, uri); // creating the intent to open up google maps
                                            startActivity(in); // initializing the intent
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

            }

            @NonNull
            @Override
            public ordersFragment.ordersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                ordersFragment.ordersViewHolder viewHolder = new ordersFragment.ordersViewHolder(view);

                return viewHolder;

            }
        };

        rvOrders.setAdapter(adapter); // setting the adapter to recycler view orders
        adapter.startListening(); // telling the adapter to start listening

    }


    public static class ordersViewHolder extends RecyclerView.ViewHolder { // creating the ordersviewholder where all the textviews and buttons will be gathered

        TextView id, info, location;
        Button callButton, mapButton, finish;
        public ordersViewHolder(@NonNull View itemView) {
            super(itemView);

            callButton = itemView.findViewById(R.id.callButton);
            mapButton = itemView.findViewById(R.id.mapButton);
            finish = itemView.findViewById(R.id.finishButton);
            id = itemView.findViewById(R.id.orderID);
            info = itemView.findViewById(R.id.orderInfo);
            location = itemView.findViewById(R.id.locationOrder);


        }
    }
}