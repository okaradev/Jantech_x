package com.krypt.Hubble;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.krypt.Hubble.databinding.ActivityMainBinding;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomNavigationView navView;
    private SessionHandler session;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navView = findViewById(R.id.nav_view);
        session = new SessionHandler(this);
        user = session.getUserDetails();
//         Passing each menu ID as a set of Ids because each
//         menu should be considered as top level destinations.dr
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_ser,
                R.id.navigation_account,
                R.id.navigation_bookings

        ).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        if (session.isLoggedIn()) {

            if (!user.getUser_type().equals("Client")) {
                Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(in);
                finish();
           }
        }
       checkLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent s = new Intent(getApplicationContext(), SearchItem.class);
                startActivity(s);
                return true;
            case R.id.action_about:
                Intent ss = new Intent(getApplicationContext(), About.class);
                startActivity(ss);
                return true;
            case R.id.action_help:
                Intent f = new Intent(getApplicationContext(), Help_act.class);
                startActivity(f);
                return true;
            case R.id.Events:
                Intent ev= new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(ev);
                return true;
            case R.id.action_feedback:
               // if (session.isLoggedIn()) {

                    Intent fb = new Intent(getApplicationContext(), FeedbackActivity.class);
                    startActivity(fb);
//                } else {
//                    Toast.makeText(getApplicationContext(), "Please login", Toast.LENGTH_SHORT).show();
//                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkLogin() {
        navView.getMenu().findItem(R.id.navigation_account).setVisible(false);
        navView.getMenu().findItem(R.id.navigation_bookings).setVisible(false);
        navView.getMenu().findItem(R.id.navigation_home).setVisible(false);
        navView.getMenu().findItem(R.id.navigation_ser).setVisible(false);

        if (session.isLoggedIn()) {
            navView.getMenu().findItem(R.id.navigation_ser).setVisible(false);
            navView.getMenu().findItem(R.id.navigation_home).setVisible(true);

            navView.getMenu().findItem(R.id.navigation_account).setVisible(true);
            navView.getMenu().findItem(R.id.navigation_bookings).setVisible(true);


        }
    }


}