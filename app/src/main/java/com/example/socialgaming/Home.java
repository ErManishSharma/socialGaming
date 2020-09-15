package com.example.socialgaming;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import Fragments.AddpostFragment;
import Fragments.DashboardFragment;
import Fragments.HomeFragment;
import Fragments.ProfileFragment;
import Fragments.notificationFragment;

public class Home extends AppCompatActivity {
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadFragment(new HomeFragment());
        navView = (BottomNavigationView) findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        fragment = new HomeFragment();
                        break;

                    case R.id.navigation_dashboard:
                        fragment = new DashboardFragment();
                        break;

                    case R.id.navigation_notifications:
                        fragment = new notificationFragment();
                        break;

                    case R.id.navigation_profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.nav_add_post:
                        fragment = new AddpostFragment();
                        break;
                }

                return loadFragment(fragment);
            }
        });

    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
