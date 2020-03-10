package com.main.tapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        BrowseFragment.DataPassListener {

    private static final String TAG = "HomeActivity";
    private TextView mUsernameTV;
    private TextView mEmailTV;

    private FirebaseAuth mFireAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseUser mCurrentUser;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initViews(navigationView);
        initUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




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

    private void initUser() {
        mFireAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mCurrentUser = mFireAuth.getCurrentUser();
        if (mCurrentUser != null) {
            mEmailTV.setText(mCurrentUser.getEmail());
            DocumentReference ref = mFirestore.collection(
                    RegisterActivity.USER_COLLECTION).document(mCurrentUser.getUid());
            ref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    mUsernameTV.setText(String.valueOf(documentSnapshot.get(RegisterActivity.USER_FULLNAME)));
                }
            });
        } else {
            Toast.makeText(HomeActivity.this, "User login error", Toast.LENGTH_SHORT);
            Log.d(TAG,"initUser: User should be logged in but is not.");
        }
    }

    private void initViews(NavigationView navView) {
        //username+email not in this layout's activity but in header
        View header = navView.getHeaderView(0);

        mUsernameTV = header.findViewById(R.id.nav_header_name);
        mEmailTV= header.findViewById(R.id.nav_header_email);
        drawer = findViewById(R.id.drawer_layout);
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
    public void passViewJob(Job job) {
        ViewJobFragment viewJobFragment = new ViewJobFragment();
        Bundle args = new Bundle();
        args.putSerializable(ViewJobFragment.JOB_RECEIVE, job);
        viewJobFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, viewJobFragment)
                .commit();
    }

}
