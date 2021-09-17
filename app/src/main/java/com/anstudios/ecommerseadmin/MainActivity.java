package com.anstudios.ecommerseadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.anstudios.ecommerseadmin.fragments.HomeFragment;
import com.anstudios.ecommerseadmin.fragments.OrdersFragment;
import com.anstudios.ecommerseadmin.fragments.PincodeFragment;
import com.anstudios.ecommerseadmin.fragments.ProductsFragment;
import com.anstudios.ecommerseadmin.fragments.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic("admins");
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.apptheme));
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, null,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        TextView navName=findViewById(R.id.nav_bar_name);
        TextView navEmail=findViewById(R.id.nav_bar_email);
        navName.setText(SplashScreen.sharedPreferences.getString("name","John Doe"));
        navEmail.setText(SplashScreen.sharedPreferences.getString("email","johnDoe@gmail.com"));
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_main);
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.frame_layout, new PincodeFragment())
                .commit();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_nav_pincode:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .replace(R.id.frame_layout, new PincodeFragment())
                            .commit();
                    break;

                case R.id.bottom_nav_products:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .replace(R.id.frame_layout, new ProductsFragment())
                            .commit();
                    break;

                case R.id.bottom_nav_orders:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .replace(R.id.frame_layout, new OrdersFragment())
                            .commit();
                    break;
                case R.id.bottom_nav_settings:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .replace(R.id.frame_layout, new SettingFragment())
                            .commit();
                    break;
                case R.id.bottom_nav_home:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .replace(R.id.frame_layout, new HomeFragment())
                            .commit();
                    break;

            }
            return true;
        });
        addNavListener();
    }

    private void addNavListener() {
        findViewById(R.id.nav_bar_home_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);

                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frame_layout, new HomeFragment())
                        .commit();
            }
        });
        findViewById(R.id.nav_bar_products_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);

                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frame_layout, new ProductsFragment())
                        .commit();
            }
        });
        findViewById(R.id.nav_bar_pincode_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);

                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frame_layout, new PincodeFragment())
                        .commit();
            }
        });
        findViewById(R.id.nav_bar_orders_btn).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);

            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.frame_layout, new OrdersFragment())
                    .commit();
        });
        findViewById(R.id.nav_bar_settings).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);

            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.frame_layout, new SettingFragment())
                    .commit();
        });
        findViewById(R.id.nav_bar_logout_btn).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
    }


    public void openNavDrawer(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
}