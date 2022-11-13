package com.example.internallfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.internallfinal.Fragments.addFragments.addClientsFragment;
import com.example.internallfinal.Fragments.clientsFragment;
import com.example.internallfinal.Fragments.homeFragment;
import com.example.internallfinal.Fragments.mapsFragment;
import com.example.internallfinal.Fragments.ordersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    public BottomNavigationView railNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // rail nav
        railNav = findViewById(R.id.bottomNavigationView); // getting the view of bottom app bar navigation
        railNav.setOnItemSelectedListener(navListener); // setting the listener to the navigation
        railNav.setBackground(null); // setting the background to create the visual effect

        // setting home fragment as main fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,new homeFragment()).commit();

        // floating action button init
        fabStartActivity();
    }

    private void fabStartActivity() {
        FloatingActionButton myFab =findViewById(R.id.fab); // getting the view of floatingActionButton
        myFab.setOnClickListener(new View.OnClickListener() { // setting the onclick listener to handle the click
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, addActivity.class); // creating the intent to change activiyu from main to add
                startActivity(intent); // initializing the intent

            }
        });
    }

    private BottomNavigationView.OnItemSelectedListener navListener = new // creating the nav listener
            BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) { // when the item is selected

                    Fragment selectedFragment = null; // we create the fragment variable which will hold the selected items fragment

                    switch (item.getItemId()) { // we use switch to switch between the menu item cases
                        case R.id.home:
                            selectedFragment = new homeFragment();
                            break;

                        case R.id.clients:
                            selectedFragment = new clientsFragment();
                            break;

                        case R.id.orders:
                            selectedFragment = new ordersFragment();
                            break;

                        case R.id.map:
                            selectedFragment = new mapsFragment();
                            break;

                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                            selectedFragment).commit();

                    return true;

                }
            };

    public void replaceFragment() {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        mapsFragment mFrag = new mapsFragment();
        t.replace(R.id.fragment_layout, mFrag);
        t.commit();
        railNav = findViewById(R.id.bottomNavigationView);
        railNav.getMenu().getItem(3).setChecked(true);
    }
}