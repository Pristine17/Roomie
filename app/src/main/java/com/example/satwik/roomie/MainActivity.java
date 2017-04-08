package com.example.satwik.roomie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    final String nav_home = "NAV_HOME";
    final String nav_about = "NAV_ABOUT";
    final String nav_settings = "NAV_SETTINGS";
    final String nav_account = "NAV_ACCOUNT";
    final String nav_cart = "NAV_CART";
    final String nav_logout = "NAV_LOGOUT";
    Fragment fragment;
    String tag = "";
    FragmentManager fragmentManager;
    private String email;
    final private String email_key="EMAIL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            onNavigationItemSelected(mNavigationView.getMenu().findItem(R.id.nav_home));
        }

        SharedPreferences sharedPreferences=this.getSharedPreferences(getString(R.string.pref_file_key), Context.MODE_PRIVATE);
        email=sharedPreferences.getString(email_key,"Oops");

        View hView =  mNavigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.headerName);
        nav_user.setText(email);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("TAG", tag);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        fragment = null;
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                fragment = fragmentManager.findFragmentByTag(nav_home);
                if (fragment == null) {
                    fragment = new PagerFragment();
                    tag = nav_home;
                }
                break;
            case R.id.nav_account:
                fragment = fragmentManager.findFragmentByTag(nav_account);
                if (fragment == null) {
                    fragment=new ProfileFragment();
                    tag = nav_account;
                }
                break;
            case R.id.nav_cart:
                fragment = fragmentManager.findFragmentByTag(nav_cart);
                if (fragment == null) {
                    fragment = new Cart();
                    tag = nav_cart;
                }
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
        }
        Log.e("YOLO", tag);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            try {
                fragmentTransaction.replace(R.id.content_frame, fragment, tag);
            } catch (IllegalStateException e) {
                fragmentTransaction.replace(R.id.content_frame, fragment);
            }
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        item.setChecked(true);
        mDrawerLayout.closeDrawers();
        return true;

    }

}





