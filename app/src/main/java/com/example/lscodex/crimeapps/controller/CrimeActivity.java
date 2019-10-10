package com.example.lscodex.crimeapps.controller;

import android.support.v4.app.Fragment;

import com.example.lscodex.crimeapps.abstractclass.SingleFragmentActivity;
import com.example.lscodex.crimeapps.R;

import java.util.UUID;


public class CrimeActivity extends SingleFragmentActivity implements CrimeFragment.Callbacks  {

    private static final String TAG = CrimeActivity.class.getName();

    //Intent key
    public static final String FIRST_INTENT = "first intent";
    public static final String EXTRA_CRIME_ID = "com.example.cancakiroglu.starbuzz.Controller.CRIME_ID";

    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return new CrimeFragment().newInstance(crimeId);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }


    @Override
    public void onCrimeUpdate() {

    }
}
