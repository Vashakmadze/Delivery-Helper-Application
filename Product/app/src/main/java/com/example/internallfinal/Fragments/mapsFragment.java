package com.example.internallfinal.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.internallfinal.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
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

public class mapsFragment extends Fragment {

    private DatabaseReference ordersRef;



    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            ordersRef = FirebaseDatabase.getInstance().getReference().child("orders"); // getting the database reference for orders
            ordersRef.addValueEventListener(new ValueEventListener() { // setting value listener on clients in database
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) { // getting every single order

                        String location = childSnapshot.child("client").child("location").getValue(String.class); // getting location fo the order
                        String name = childSnapshot.child("client").child("name").getValue(String.class); // getting name of the order

                        if(location==null) {
                            Toast.makeText(getContext(), "Address Not Found!", Toast.LENGTH_SHORT).show();
                        } else { // creating goecoder
                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                            try {
                                List<Address> listAddresss = geocoder.getFromLocationName(location, 1);
                                if(listAddresss.size()>0) {
                                    Address address = listAddresss.get(0); // turnign into address
                                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude()); // turning adress into latLng using get methods
                                    MarkerOptions markerOptions = new MarkerOptions(); // creating markerOptions
                                    markerOptions.title(name + "'s order: " + location); // setting title to the marker
                                    markerOptions.position(latLng); // setting marker position to the latlng we created
                                    googleMap.addMarker(markerOptions); // addding marker to google maps
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    LatLng tbilisiGeneral = new LatLng(41.716667, 44.783333); // setting default zoom in position
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(tbilisiGeneral, 15); // creating aniumati0on for zooming in
                    googleMap.animateCamera(cameraUpdate); // initiaing the animation
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            googleMap.getUiSettings().setZoomControlsEnabled(true); // we enable zooming in and zooming out

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);

        }

    }
}