package com.example.lscodex.crimeapps.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.example.lscodex.crimeapps.R;
import com.example.lscodex.crimeapps.model.CrimeDec;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by lscodex on 1.08.2017.
 */

public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE_FRAG = "date";
    //Request code için
    public static final String EXTRA_DATE = "date";
    private DatePicker mDatePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Date date = (Date) getArguments().getSerializable(ARG_DATE_FRAG);

        final CrimeDec dateCrime = new CrimeDec();



        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
       /* calendar.setTime(date);*/

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        View v = LayoutInflater.from(getActivity()).inflate(R.layout.date_picker_layout, null);

        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { //buraya dialog interface giriyoruz sendresult method için

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();

                        Date date = new GregorianCalendar(year, month, day).getTime();


                        Log.d("date", "Alert date===>: "+date);
                        sendResult(Activity.RESULT_OK, date);


                    }

                }).create();

    }


    // data to 2 framgment each other
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE_FRAG, date);
        Log.d("args", "args =====>: " + args);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;

    }

    // set the date when user entered
    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
