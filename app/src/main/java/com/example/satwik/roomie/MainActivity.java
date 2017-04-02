package com.example.satwik.roomie;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    final String nav_home="NAV_HOME";
    final String nav_about="NAV_ABOUT";
    final String nav_settings="NAV_SETTINGS";
    final String nav_account="NAV_ACCOUNT";
    final String nav_cart="NAV_CART";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FragmentManager fragmentManager=getSupportFragmentManager();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Fragment fragment=null;
                        String tag="";
                        int id=menuItem.getItemId();
                        switch (id)
                        {
                            case R.id.nav_home: fragment=fragmentManager.findFragmentByTag(nav_home);
                                                            if(fragment==null)
                                                            {
                                                                //fragment=new Cart();
                                                                tag=nav_home;
                                                            } break;
                            case R.id.nav_about:fragment=fragmentManager.findFragmentByTag(nav_about);
                                                            if(fragment==null)
                                                            {
                                                                //fragment=new Cart();
                                                                tag=nav_about;
                                                            } break;
                            case R.id.nav_settings:fragment=fragmentManager.findFragmentByTag(nav_settings);
                                                            if(fragment==null)
                                                            {
                                                                //fragment=new Cart();
                                                                tag=nav_settings;
                                                            } break;
                            case R.id.nav_account:fragment=fragmentManager.findFragmentByTag(nav_account);
                                                            if(fragment==null)
                                                            {
                                                                //fragment=new Cart();
                                                                tag=nav_account;
                                                            } break;
                            case R.id.nav_cart:fragment=fragmentManager.findFragmentByTag(nav_cart);
                                                            if(fragment==null)
                                                            {
                                                                fragment=new Cart();
                                                                tag=nav_cart;
                                                            } break;
                        }
                        Log.e("YOLO",tag);
                        if(fragment!=null) {
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment, tag);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });




    }




//    @Override
//    public void onBackPressed() {
//        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            getSupportFragmentManager().popBackStack();
//        } else {
//            super.onBackPressed();
//        }
//    }


}
