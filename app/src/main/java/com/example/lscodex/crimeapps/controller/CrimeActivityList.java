package com.example.lscodex.crimeapps.controller;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.lscodex.crimeapps.R;
import com.example.lscodex.crimeapps.abstractclass.SingleFragmentActivity;
import com.example.lscodex.crimeapps.model.CrimeDec;


import java.util.UUID;


public class CrimeActivityList extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks, CrimeListFragment.OnDeleteCrimeListener {
    private static final String TAG = CrimeActivityList.class.getName();


    @Override
    protected Fragment createFragment() {
        Log.d(TAG, "==================== BURDA ==================");
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }


    @Override
    public void onCrimeSelected(CrimeDec crime) {
        //Crime select and getLayout from that...
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = new Intent(this, CrimeActivity.class);
            intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
            startActivity(intent);

        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment oldDetail = fm.findFragmentById(R.id.detail_fragment_container);
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());


            if (oldDetail != null) {
                ft.remove(oldDetail);
            }
            ft.add(R.id.detail_fragment_container, newDetail);
            ft.commit();

        }

    }

    @Override
    public void onCrimeUpdate() {
        FragmentManager fm = getSupportFragmentManager();
        CrimeListFragment listFragment = (CrimeListFragment) fm.findFragmentById(R.id.fragment_container);
        listFragment.updateUI();


    }

    @Override
    public void onCrimeIdSelected(UUID crimeId) {

            CrimeFragment crimeFragment = (CrimeFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container);
            CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            listFragment.deleteCrime(crimeId);
        Log.d(TAG, "SİLİNDİİİİİİİ -- ");
            listFragment.updateUI();
            if(crimeFragment==null){

                // FIXME: 6.12.2017 burda olursa hata alıyorum o yüzden boş bıraktım..


            }else {
                listFragment.getActivity().getSupportFragmentManager().beginTransaction().remove(crimeFragment).commit();
            }



    }
}
