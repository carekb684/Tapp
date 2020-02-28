package com.main.tapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        BrowseFragment.DataPassListener {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                R.string.nav_open_drawer, R.string.nav_close_drawer);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        if(savedInstanceState == null) {
            //start with this fragment selected
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new BrowseFragment()).commit();
        }
    }

    /** If drawer is open and backPressed - close drawer
     */
    @Override
    public void onBackPressed() {
        if (!closeDrawer()) {
            super.onBackPressed();
        }
    }

    private boolean closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.nav_calender:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CalenderFragment()).commit();
                break;

                case R.id.nav_browse:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BrowseFragment()).commit();
                break;

                case R.id.nav_create_job:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CreateJobFragment()).commit();
                break;

                case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();
                break;
        }

        closeDrawer();
        return true;
    }

    //for opening drawer when clicking hamburger
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return true for ActionBarToggle to handle the touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //for opening drawer when clicking hamburger
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void passData(String data) {
        ViewJobFragment viewJobFragment = new ViewJobFragment();
        Bundle args = new Bundle();
        args.putString(ViewJobFragment.DATA_RECEIVE, data);
        viewJobFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ViewJobFragment())
                .commit();
    }

}
