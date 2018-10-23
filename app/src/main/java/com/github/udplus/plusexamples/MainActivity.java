/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.github.udplus.plusexamples;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = MainActivity.class.getSimpleName();
    private boolean mDarkTheme = false;
    private SharedPreferences mSharedPref;
    private SwitchCompat mDarkThemeDrawerSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //-------------------------------
        // done: start activity SettingsActivity
        // done: add action menu action_dark_theme

        // PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        // String preferencesName = this.getPreferenceManager().getSharedPreferencesName();

        // ------------------------------
        mSharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        // Boolean switchPref = mSharedPref.getBoolean
        //         (SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);
        mDarkTheme = mSharedPref.getBoolean
                (SettingsActivity.KEY_PREF_DARK_THEME_SWITCH, false);

        //--------------------------------------
        // change theme
        // setTheme(R.style.AppTheme);
        if (mDarkTheme) {
            // setTheme(android.R.style.Theme_Black);
            // setTheme(darkTheme ? R.style.AppThemeDark : R.style.AppThemeLight);
            setTheme(R.style.AppThemeDark_NoActionBar);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Flow.main_coCreate();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //-- drawer --
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //-- navigation view --
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setOnCreateContextMenuListener(new NavigationView.OnCreateContextMenuListener() {
                  @Override
                  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                      Log.d(TAG, "navigationView.OnCreateContextMenuListener");
                  }
              }
        );
        //
        mDarkThemeDrawerSwitch = (SwitchCompat) navigationView.getMenu().findItem(R.id.nav_dark_theme).getActionView();
        mDarkThemeDrawerSwitch.setChecked(mDarkTheme);
        mDarkThemeDrawerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(MainActivity.this, "click: " + isChecked, Toast.LENGTH_SHORT).show();
                mDarkTheme = isChecked;
                if (isChecked) {
                    // do stuff
                } else {
                    // do other stuff
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");

        mDarkTheme = mSharedPref.getBoolean
                (SettingsActivity.KEY_PREF_DARK_THEME_SWITCH, false);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        Log.d(TAG, "onCreateOptionsMenu");

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu");

        // set dark theme
        MenuItem menuItem = menu.findItem(R.id.action_dark_theme);
        menuItem.setChecked(mDarkTheme);
        mDarkThemeDrawerSwitch.setChecked(mDarkTheme);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_dark_theme) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            if(item.isChecked()){
                // If item already checked then unchecked it
                item.setChecked(false);
            }else{
                // If item is unchecked then checked it
                item.setChecked(true);
            }
            mDarkThemeDrawerSwitch.setChecked(item.isChecked());
            sharedPref.edit().putBoolean(SettingsActivity.KEY_PREF_DARK_THEME_SWITCH, item.isChecked()).commit();

            Toast.makeText(this, "item.isChecked: "+ item.isChecked(), Toast.LENGTH_SHORT).show();
            recreate();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_dark_theme) {
            Log.d(TAG, "nav dark theme");
            // todo: selectTheme()


        } else if (id == R.id.nav_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        // config


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onHello(View view) {
        Log.d(TAG, "Hello Word! 하이 윈도우 7");

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean
                (SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);

        // Toast.makeText(this, switchPref.toString(), Toast.LENGTH_SHORT).show();
        if (switchPref) {
            Toast.makeText(this, "Hello World! 하이 윈도우7 - 스위치 온", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Hello World! 하이 윈도우7 - 스위치 오프", Toast.LENGTH_SHORT).show();

        }

        changeDarkTheme();
    }

    private void changeDarkTheme() {
        // How to change current Theme at runtime in Android
        if (mDarkTheme) {
            Toast.makeText(this, "Dark", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Light", Toast.LENGTH_SHORT).show();
        }
        recreate();
    }
}
