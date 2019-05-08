package com.nvcreation.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class CrimeListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private int mClickedItemPosition;
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;

    private static final String TAG = "CrimeListFragment";

    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
            }

        });

        itemTouchHelper.attachToRecyclerView(mCrimeRecyclerView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
             //   Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getmId()); //Commented for Chapter 17
             //   startActivity(intent); //Commented for Chapter 17
                updateUI();
                mCallbacks.onCrimeSelected(crime);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);

        if(!mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    public void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else{
            //mAdapter.notifyDataSetChanged(); commented for Chapter 10 Challenge to implement notifyItemChanged
            mAdapter.setCrimes(crimes);
            mAdapter.notifyItemChanged(mClickedItemPosition);
        }
        updateSubtitle();

    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Button mContactPoliceButton; // Added in Chapter 8 Challenge
        private ImageView mSolvedImageView; // Added in Chapter 9
        private Crime mCrime;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent, int layoutId) {
            super(inflater.inflate(layoutId, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved); // Added in Chapter 9

            if(layoutId == R.layout.list_item_crime_police) {
                mContactPoliceButton = (Button) itemView.findViewById(R.id.btn_contact_police);
                mContactPoliceButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "Police Contacted for : " + mCrime.getmTitle(), Toast.LENGTH_SHORT).show();
                        }
                });
            }
        }

        public void bind(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getmTitle());
            //java.text.DateFormat df = getDateInstance(java.text.DateFormat.FULL); // Using java.text.DateFormat
            mDateTextView.setText(DateFormat.format("EEEE, MMM dd, yyyy",mCrime.getmDate())); // Chapter 9 Challenge - using android.text.format.DateFormat can use this as well - DateFormat.getLongDateFormat(getContext()).format(mCrime.getmDate())
            mSolvedImageView.setVisibility(mCrime.ismSolved() ? View.VISIBLE : View.GONE); // Added in Chapter 9
            //Continuing Chapter 8 Challenge with Chapter 9 changes....If Contact Police is required, it will check whether the crime has been solved, if solved the button will be disabled, else enabled.
            if(mCrime.ismRequiresPolice()) {
                mContactPoliceButton.setEnabled(!mCrime.ismSolved());
            }
        }


        @Override
        public void onClick(View v) {

            //Chapter 8 - Toast.makeText(getActivity(), mCrime.getmTitle() + " clicked!", Toast.LENGTH_SHORT).show();

            //Chapter 10 - Challenge - notifyItemChanged
            mClickedItemPosition = getAdapterPosition();

            //Chapter 10 - Starting Activity from Fragment
            /* Can use this but below approach for expected parameters and prevent run time errors, Intent intent = new Intent(getActivity(),CrimeActivity.class); intent.putExtra("EXTRA_CRIME_ID", mCrime.getmId());*/
            //Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getmId()); //Commented for Chapter 11

            //Chapter 11 - Removal of Crime Activity and adding CrimePagerActivity instead
            //Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getmId()); //Commented for Chapter 17
            //startActivity(intent); //Commented for Chapter 17
            mCallbacks.onCrimeSelected(mCrime);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, viewGroup, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder crimeHolder, int position) {
            Crime crime = mCrimes.get(position);
            crimeHolder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes){
            mCrimes = crimes;
        }

        @Override
        public int getItemViewType(int position) {
            Crime crime = mCrimes.get(position);
            // Added in Chapter 8 Challenge
            if(crime.ismRequiresPolice()){
                return R.layout.list_item_crime_police;
            } else{
                return R.layout.list_item_crime;
            }
        }

        public void onItemDismiss(int position){
            Crime crime = mCrimes.get(position);
            CrimeLab.get(getActivity()).removeCrime(crime);
        }

    }
}
