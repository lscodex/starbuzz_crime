package com.example.lscodex.crimeapps.abstractclass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.lscodex.crimeapps.R;


/**
 * Created by lscodex on 8.11.2017.
 */

public abstract class SingleFragmentActivity  extends AppCompatActivity{
    private static final String TAG= SingleFragmentActivity.class.getName();

    protected  abstract Fragment createFragment();

    protected int getLayoutResId(){
        return  R.layout.crime_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }
}
