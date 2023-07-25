package com.example.project01mvvm;

import android.app.Application;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;

public class ApplicationActivity extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

    }


}
