package com.example.lscodex.crimeapps.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.lscodex.crimeapps.R;
import com.example.lscodex.crimeapps.model.CrimeDec;
import com.example.lscodex.crimeapps.model.CrimeLab;
import com.example.lscodex.crimeapps.model.PictureUtils;


import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by lscodex on 1.08.2017.
 */

public class CrimeFragment extends Fragment {

    private static final String TAG = CrimeFragment.class.getName();
    private CrimeDec mCrime;


    private Button mButtonDate, mReportButton, mSuspectButton;
    private EditText mTitleEditText;
    private CheckBox mCheckBox;

    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    private File mPhotoFile;
    private Point mPhotoViewSize;


    //key for implicit INTENT !!!

    private static final int REQUEST_DATE = 0;          //Days
    private static final int REQUEST_CONTACT = 1;       //Contacts
    private static final int REQUEST_PHOTO = 2;         //Photos
    //KEY
    private static final String DIALOG_DATE = "dialog";
    //Extra Crime Id
    public static final String EXTRA_CRIME_ID = "com.example.cancakiroglu.starbuzz.Controller.CRIME_ID";

    private static final String EMPTY_TITLE = "Empty";

    //------------------------------------------------------------------------------------------------------<>
    //callbacks
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onCrimeUpdate();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    //extra_crime id  bundle
    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle arg = new Bundle();
        arg.putSerializable(EXTRA_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(arg);
        return fragment;

    }

    // get the intent uuid object
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mCrime = new CrimeDec();

        // uuid for objects
        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        Log.d(TAG, "onCreate: " + crimeId);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);

    }

    //  create view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.crime_fragment, container, false);

        mTitleEditText = (EditText) v.findViewById(R.id.c_ac_title);
        mTitleEditText.setText(mCrime.getTitle());
        String sTitle = mTitleEditText.getText().toString().trim();


        if (TextUtils.isEmpty(sTitle)) {

            mCrime.setTitle(EMPTY_TITLE);
            updateCrime();

        } else {
            mTitleEditText.setText(mCrime.getTitle());
        }

        mTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                mCrime.setTitle(charSequence.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });


        String sst = DateFormat.format("dd MMMM yyyy, EEEE", mCrime.getDate()).toString();
        mButtonDate = (Button) v.findViewById(R.id.c_ac_buttondate);
        mButtonDate.setText(sst);
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                //get date

                DialogFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());//datepicker.javadan gelen

                Log.d("getDAte ", "dialog = >>>> " + dialog);
                //request code
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);//button günü ile ilgili ,datetimepicker.java da
                dialog.show(fm, DIALOG_DATE);
                Log.d("getDAte ", "DIALOG_DATE = >>>> " + DIALOG_DATE);
                Log.d("getDAte ", "REQUEST_CODE = >>>> " + REQUEST_DATE);

            }

        });


        mCheckBox = (CheckBox) v.findViewById(R.id.c_ac_checkk);
        mCheckBox.setChecked(mCrime.isSolved());
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
                updateCrime();


            }
        });

        // add report button
        mReportButton = (Button) v.findViewById(R.id.c_ac_send_crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/pain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_suspect));
                sendIntent = Intent.createChooser(sendIntent, getString(R.string.send_report));
                startActivity(sendIntent);
            }
        });

        final Intent pickContect = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        mSuspectButton = (Button) v.findViewById(R.id.c_ac_choose_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContect, REQUEST_CONTACT);
            }
        });
        if (mCrime.getmSuspect() != null) {
            mSuspectButton.setText(mCrime.getmSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContect, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        //photo ya basınca dialogFragment da göstericez.
        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);
        mPhotoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boolean isFirstPass = (mPhotoViewSize == null);
                mPhotoViewSize = new Point();
                mPhotoViewSize.set(mPhotoView.getWidth(), mPhotoView.getWidth());
                if (isFirstPass) {
                    updatePhotoView();
                }
            }
        });
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DialogFragment dialogFragment = PhotoZoomFragment.newInstance(mPhotoFile.getPath());
                dialogFragment.setTargetFragment(CrimeFragment.this, REQUEST_PHOTO);
                dialogFragment.show(fm, "PHOTO");
            }
        });
        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera_button);

        // process handler
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        //todo - Çalışıyor...
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(), "com.example.cancakiroglu.starbuzz", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(String.valueOf(activity.activityInfo), uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });


        // update photo review
        updatePhotoView();

        return v;

    }

    // rechange button for date
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;

        }
        if (requestCode == REQUEST_DATE) {

            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateCrime();
            updateDate();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
            try {
                if (c.getCount() == 0) {
                    return;
                }
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setmSuspect(suspect);
                updateCrime();
                mSuspectButton.setText(suspect);
            } finally {
                c.close();
            }

        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.example.cancakiroglu.starbuzz", mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //todo updatePhoto -2  çağırdığımız yer
            updateCrime();
            updatePhotoView();
        }
    }

    // update crime
    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem subTitleMenu = menu.findItem(R.id.show_subtitle);
        MenuItem addMenu = menu.findItem(R.id.new_crime);
        subTitleMenu.setVisible(false);
        addMenu.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_crime:

                if (getActivity().findViewById(R.id.detail_fragment_container) == null) {
                    CrimeLab.get(getActivity()).deleteCrime(mCrime);
                    deleteCrimePhoto();
                    updateCrime();
                    getActivity().finish();
                } else { //for tablet
                    CrimeLab.get(getActivity()).deleteCrime(mCrime);
                    deleteCrimePhoto();
                    updateCrime();
                    getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }

    private void updateDate() {
        mButtonDate.setText(mCrime.getDate().toString());
    }

    // get crime report
    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getmSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }

    // photo utils recall
    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitMap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    private void deleteCrimePhoto() {
        if (mCrime.getPhotoFilename() != null) {

            getActivity().deleteFile(mCrime.getPhotoFilename());
            PictureUtils.cleanImageView(mPhotoView);
            Log.d(TAG, "deleteCrimePhoto: ");
        }
    }

    // when the delete crime and updating
    private void updateCrime() {
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdate();
    }
}
