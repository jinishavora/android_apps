package com.nvcreation.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.Callbacks {

    private ViewPager mViewPager;
    private Button mFirstCrime;
    private Button mLastCrime;
    private int mLastIndex;

    private List<Crime> mCrimes;

    public static final String EXTRA_CRIME_ID = "com.nvcreation.criminalintent.crime_id";

    public static Intent newIntent(Context packageContext, UUID crime_id){
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crime_id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);
        mFirstCrime = (Button) findViewById(R.id.crime_first);
        mLastCrime = (Button) findViewById(R.id.crime_last);

        mCrimes = CrimeLab.get(this).getCrimes();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                toggleButtons();
                return CrimeFragment.newInstance(crime.getmId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        //Chaper 11 - Challenge 2
        mFirstCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0, true);
                toggleButtons();
            }
        });

        mLastIndex = mCrimes.size()-1; //Chaper 11 - Challenge 2

        //Chaper 11 - Challenge 2
        mLastCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mLastIndex, true);
                toggleButtons();
            }
        });

        for(int i = 0; i < mCrimes.size(); i++){
            if(mCrimes.get(i).getmId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    //Chaper 11 - Challenge 2
    private void toggleButtons(){
        if (mViewPager.getCurrentItem() == 0){
            mFirstCrime.setEnabled(false);
        } else {
            mFirstCrime.setEnabled(true);
        }

        if (mViewPager.getCurrentItem() == mLastIndex){
            mLastCrime.setEnabled(false);
        } else {
            mLastCrime.setEnabled(true);
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {

    }
}
