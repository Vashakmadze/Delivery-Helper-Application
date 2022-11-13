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
import com.example.internallfinal.Fragments.addFragments.addOrdersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class addActivity extends AppCompatActivity {
    BottomNavigationView railNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        back(); // returns to main activity

        // rail nav
        railNav = findViewById(R.id.topNavigationViewAdd); // getting the view of top (bottom) app bar navigation
        railNav.setOnItemSelectedListener(navListener);  // setting the listener to the navigation
        railNav.setBackground(null); // setting the background to create the visual effect

        // setting addOrders fragment as main fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.addFragmentLayout,new addOrdersFragment()).commit();



    }
    public void back() {
        FloatingActionButton backFab =  (FloatingActionButton) findViewById(R.id.backButton); // getting the id of floating action button
        backFab.setOnClickListener(new View.OnClickListener() { // setting the listener on floating action button
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addActivity.this, MainActivity.class); // creating the intent to go from addActivity to mainActivity
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
                        case R.id.addOrdersTab:
                            selectedFragment = new addOrdersFragment();
                            break;

                        case R.id.addClientsTab:
                            selectedFragment = new addClientsFragment();
                            break;

                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.addFragmentLayout,
                            selectedFragment).commit();

                    return true;

                }
            };

    public void replaceFragment() {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction(); // getting the fragment transaction
        addClientsFragment mFrag = new addClientsFragment(); // getting the target fragment
        t.replace(R.id.addFragmentLayout, mFrag); // replacing the layout to target fragmebt
        t.commit(); // commiting
        railNav = findViewById(R.id.topNavigationViewAdd); // getting the id of the navigation bar
        railNav.getMenu().getItem(1).setChecked(true); // setting the navigation bar item active
    }

}
