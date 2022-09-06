package com.example.fruitgrowingapplication.ManagementActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.fruitgrowingapplication.HomeActivity.MainActivity;
import com.example.fruitgrowingapplication.ManagementActivity.Edit.EditOrchardFragment;
import com.example.fruitgrowingapplication.ManagementActivity.Info.InfoDialog;
import com.example.fruitgrowingapplication.ManagementActivity.Manage.ManageTreesFragment;
import com.example.fruitgrowingapplication.ManagementActivity.Statistics.StatisticsFragment;
import com.example.fruitgrowingapplication.R;
import com.google.android.material.navigation.NavigationView;

public class TreeManagementActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String orchardName;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ImageView ivActivityImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_management);
        getOrchardName();
        initializeActivityImage();
        setupNavigation();
        if (savedInstanceState == null) {
            setupInitialFragment();
        }
    }

    private void initializeActivityImage() {
        ivActivityImage = findViewById(R.id.iv_activity_image);
    }

    private void getOrchardName() {
        Bundle bundle = getIntent().getExtras();
        orchardName = bundle.getString("nameKey");
    }

    private void setupNavigation() {
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupInitialFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManageTreesFragment(orchardName, ivActivityImage)).commit();
        navigationView.setCheckedItem(R.id.home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.manage_trees:
                replaceFragment(new ManageTreesFragment(orchardName, ivActivityImage));
                break;
            case R.id.edit:
                replaceFragment(new EditOrchardFragment(orchardName, ivActivityImage));
                break;
            case R.id.statistics:
                replaceFragment(new StatisticsFragment(orchardName, ivActivityImage));
                break;
            case R.id.choosing_orchard:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.info:
                createInfoDialog();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment nextFragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nextFragment).commit();
    }


    private void createInfoDialog() {
        DialogFragment dialogFragment = new InfoDialog();
        dialogFragment.show(getSupportFragmentManager(), "info");

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
