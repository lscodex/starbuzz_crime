package com.example.lscodex.crimeapps.controller;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lscodex.crimeapps.R;
import com.example.lscodex.crimeapps.model.PictureUtils;


/**
 * Created by lscodex on 7.11.2017.
 */

public class PhotoZoomFragment extends DialogFragment {

    private static final String BUNDLE_FOR_PHOTO_KEY = "com.example.cancakiroglu.starbuzz";


    private ImageView mPhotoView;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        String path = (String) getArguments().getSerializable(BUNDLE_FOR_PHOTO_KEY);     // get the path
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.photo_picker_dialog, null);

        mPhotoView = (ImageView) view.findViewById(R.id.photo_zoom);
        Bitmap bitmap = PictureUtils.getScaledBitMap(path, getActivity());
        mPhotoView.setImageBitmap(bitmap);
        if (mPhotoView.getParent() != null) {
            ((ViewGroup) mPhotoView.getParent()).removeView(mPhotoView);
            dialog.setContentView(mPhotoView);
        }

        return dialog;
    }

    // bundle the arguments
    public static PhotoZoomFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_FOR_PHOTO_KEY, path);
        PhotoZoomFragment fragment = new PhotoZoomFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
