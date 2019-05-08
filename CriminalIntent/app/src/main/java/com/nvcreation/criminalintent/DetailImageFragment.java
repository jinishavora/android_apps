package com.nvcreation.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class DetailImageFragment extends DialogFragment {

    private static final String ARGS_PHOTO = "photo";

    private ImageView mDetailImageView;

    public static DetailImageFragment newInstance(String photopath) {
        Bundle args = new Bundle();
        args.putString(ARGS_PHOTO, photopath);

        DetailImageFragment fragment = new DetailImageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String photopath = getArguments().getString(ARGS_PHOTO);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_detail_image, null);

        mDetailImageView = (ImageView) v.findViewById(R.id.detailImage);

        Bitmap bitmap = PictureUtils.getScaledBitmap(photopath, getActivity());
        mDetailImageView.setImageBitmap(bitmap);

        return new AlertDialog.Builder(getActivity()).setView(v).create();
    }
}
