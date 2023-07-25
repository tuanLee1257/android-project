package com.example.project01mvvm.mvvm.defaultScreen;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.project01mvvm.R;
import com.example.project01mvvm.databinding.ActivityDefaultBinding;
import com.example.project01mvvm.mvvm.downloadManagerScreen.DownloadManagerFragment;
import com.example.project01mvvm.mvvm.homeScreen.HomeFragment;
import com.example.project01mvvm.mvvm.searchScreen.SearchFragment;

public class DefaultActivity extends AppCompatActivity {
    ActivityDefaultBinding defaultScreenBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultScreenBinding = ActivityDefaultBinding.inflate(getLayoutInflater());
        setContentView(defaultScreenBinding.getRoot());

        replaceFragment(new HomeFragment());
        defaultScreenBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.search:
                    replaceFragment(new SearchFragment());
                    break;
                case R.id.user:
                    replaceFragment(new DownloadManagerFragment());
                    break;
            }
            return true;
        });
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}