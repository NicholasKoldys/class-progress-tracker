package com.nicholaskoldys.collegetrackingapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.nicholaskoldys.collegetrackingapp.ui.fragmentControllers.HomeFragment;
import com.nicholaskoldys.collegetrackingapp.utility.Constants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i("MAIN", "Starting Application MainActivity");
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {

            /*
             * SavedInstance State is done automatically with fragments.
             * so when screen is rotated. nothing should be done after the
             * main activity is destroyed...
             */

        } else {
            getSupportActionBar();
            loadHomeScreen();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.mainHomeMenuButton:
                loadHomeScreen();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if(getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            return;
        } else {
            super.onBackPressed();
        }
    }

    public static FragmentTransaction getTransactionForDialog(Activity activity) {
        FragmentTransaction transaction = ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction();

        Fragment previousFragment = ((AppCompatActivity) activity).getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_DIALOG_TAG);

        if(previousFragment != null) {
            transaction.remove(previousFragment);
        }
        transaction.addToBackStack(null);

        return transaction;
    }

    private void loadHomeScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentViewHolder, homeFragment, "HOME");
        transaction.commit();
    }
}

