package com.nvcreation.criminalintent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.provider.ContactsContract.*;
import static android.widget.CompoundButton.*;

public class CrimeFragment extends Fragment {

    private static final String DIALOG_DATE = "Dialog_Date";
    public static final String ARG_CRIME_ID = "crime_id";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final String DIALOG_TIME = "Dialog_Time";
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PERMISSION_READ_CONTACTS = 3;
    private static final int REQUEST_PHOTO = 4;
    private static final String DETAIL_PHOTO = "Dialog_Photo";

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton; //Chapter 12 - Challenge 1
    private CheckBox mSolvedCheckBox;
    private Button mSuspectButton;
    private Button mCallSuspectButton;
    private Button mReportButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }

    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mCrime = new Crime(); - commented in Chapter 10

        /* Using newInstance to get the Arguments instead of code mentioned below, to directly accessing
        UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID); //Fragment accessing intent directly
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);*/

        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_crime, container,false);

        //Wiring title Edit Text Field - activating onTextChanged
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getmTitle()); //Set in Chapter 10
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setmTitle(s.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Wiring Date Button - to get the current date

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        //   mDateButton.setEnabled(false); //Commented in Chapter 12 for Date Picker dialog

        mDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getmDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        //Wiring Time Button - Chapter 12 - Challenge 1

       /* mTimeButton = (Button) v.findViewById(R.id.crime_time);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getmTime());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });*/
        //Wiring Check box - activating onCheckChanged to check and set the crime to Solved

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.ismSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setmSolved(isChecked);
                updateCrime();
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
//        pickContact.addCategory(Intent.CATEGORY_HOME);
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if(mCrime.getmSuspect() != null){
            mSuspectButton.setText(mCrime.getmSuspect());
        }


        mCallSuspectButton = (Button) v.findViewById(R.id.call_suspect);
        if(mCrime.getmSuspect() == null){
            mCallSuspectButton.setEnabled(false);
            mCallSuspectButton.setText(R.string.call_suspect);
        } else {
            mCallSuspectButton.setText(getString(R.string.call_text) + mCrime.getmSuspect());
        }

        mCallSuspectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + mCrime.getmSuspectPhone()));
                startActivity(i);
            }
        });

        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null){
            mSuspectButton.setEnabled(false);
        }


        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//Solution of Chapter 15 - Challenge 1
                ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject))
                        .setChooserTitle(getString(R.string.send_report))
                        .startChooser();
                /*// Commented for Solution of Chapter 15 - Challenge 1
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);*/
            }
        });

        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = FileProvider.getUriForFile(getActivity(),"com.nvcreation.criminalintent.fileprovider", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);
        updatePhotoView();
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhotoFile != null && mPhotoFile.exists()) {
                    FragmentManager photoViewManager = getFragmentManager();
                    DetailImageFragment detailDialog = DetailImageFragment.newInstance(mPhotoFile.getPath());
                    detailDialog.show(photoViewManager, DETAIL_PHOTO);
                }

            }
        });

        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            updateCrime();
            updateDate();
        } else if (requestCode == REQUEST_CONTACT && data != null){
            String lookup;

            Uri contactUri = data.getData();

            String[] queryFields = new String[] {
                    Contacts.DISPLAY_NAME,
                    Contacts.LOOKUP_KEY
            };

            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

            try {
                if(c.getCount() == 0) {
                    return;
                }

                c.moveToFirst();

                lookup = c.getString(c.getColumnIndex(Contacts.LOOKUP_KEY));
                String suspect = c.getString(c.getColumnIndex(Contacts.DISPLAY_NAME));
                mCrime.setmSuspect(suspect);
                updateCrime();
                mSuspectButton.setText(suspect);
                mCallSuspectButton.setEnabled(true);
            } finally {
                c.close();
            }

            contactUri = CommonDataKinds.Phone.CONTENT_URI;
            queryFields = new String[]{
                    CommonDataKinds.Phone.NUMBER
            };

            c = getActivity().getContentResolver().query(contactUri, queryFields,
                    CommonDataKinds.Phone.LOOKUP_KEY + " =? ", new String[]{lookup},
                    null );

            try {
                if(c.getCount() == 0) {
                    return;
                }

                c.moveToFirst();
                String suspectPhone = c.getString(0);
                mCrime.setmSuspectPhone(suspectPhone);
                updateCrime();
                mCallSuspectButton.setText(getString(R.string.call_text) + mCrime.getmSuspect());
            } finally {
                c.close();
            }


        } else if(requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.nvcreation.criminalintent.fileprovider", mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updateCrime();
            updatePhotoView();
        }

        /*if (requestCode == REQUEST_TIME){
            Date time = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setmTime(time);
            updateTime();

        }*/
    }

    private void updateCrime(){
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }

   private void updateDate() {
        mDateButton.setText(mCrime.getmDate().toString());
    }

    /*private void updateTime() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
        mTimeButton.setText((df.format(mCrime.getmTime())).toString());
    }*/

    private String getCrimeReport(){
        String solvedString = null;
        if(mCrime.ismSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }
        else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM, dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getmDate()).toString();

        String suspect = mCrime.getmSuspect();
        if(suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        }
        else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getmTitle(), dateString, solvedString, suspect);

        return report;
    }

    private void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        } else {

            ViewTreeObserver observer = mPhotoView.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int width = mPhotoView.getMeasuredWidth();
                    int height = mPhotoView.getMeasuredHeight();

                    Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), width, height);
                    //Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity()); // Commented for Chapter 166 challenge 2
                    mPhotoView.setImageBitmap(bitmap);
                }
            });

        }
    }

}
