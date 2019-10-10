package com.example.lscodex.crimeapps.controller;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lscodex.crimeapps.R;
import com.example.lscodex.crimeapps.model.CrimeDec;
import com.example.lscodex.crimeapps.model.CrimeLab;


import java.io.File;
import java.util.List;
import java.util.UUID;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;


public class CrimeListFragment extends Fragment {


    private static final String TAG = CrimeListFragment.class.getName();


    private RecyclerView mRecyclerView;
    private CrimeAdapter mAdapter;

    private Callbacks mCallback;
    private OnDeleteCrimeListener mDeleteCalLBack;

    private boolean mItemhasChange = false;
    private UUID mItemhasChangeId;

    private boolean mSubtitleVisible;

    //startActivityResult için key
    private static final int START_FOR_RESULT = 0;

    private Toast myToast;
    private File mPhotoFile;



    private int seleceted_position;
    // get resource for collor
    private Resources r;
    // zero flag when app opens in list position
    private boolean zeroFlag = false;

    //--------------------------------------------------->>>>>>>>>>>>>>>>>>>>

    //delete callback
    public interface OnDeleteCrimeListener {
        void onCrimeIdSelected(UUID crimeId);
    }

    //callback
    public interface Callbacks {
        void onCrimeSelected(CrimeDec crime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (Callbacks) context;
        mDeleteCalLBack = (OnDeleteCrimeListener) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
        mDeleteCalLBack = null;
    }

    // menus open
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    // update list
    @Override
    public void onResume() {
        super.onResume();

        updateUI();

    }

    // create fragment view
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.crime_activity_list, container, false);

        r = getResources();
        mRecyclerView = (RecyclerView) v.findViewById(R.id.frecycle_view);
        LinearLayoutManager mLinear = new LinearLayoutManager(getActivity());
        runLayoutAnimation(mRecyclerView);
        mRecyclerView.setLayoutManager(mLinear);
        mRecyclerView.setHasFixedSize(true);
        // recycleview divider
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getActivity(), mLinear.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);


        setCrimeRecyclerViewItemTouchListener();


        return v;
    }


    //Recycle viewholder class
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        // TODO: 6.12.2017 , ek olarak eklenen, row silme özelliği
        public LinearLayout mLinerLayout;


        private CrimeDec mCrimeDec;
        private TextView mTxtTittle, mTxtDate;
        private ImageView mSolvedImage, mPhotoImage;


        public CrimeHolder(LayoutInflater inflater, ViewGroup view) {
            super(inflater.inflate(R.layout.crime_fragment_list, view, false));
            mTxtDate = (TextView) itemView.findViewById(R.id.c_acl_date_title);
            mTxtTittle = (TextView) itemView.findViewById(R.id.c_acl_crime_title);
            mSolvedImage = (ImageView) itemView.findViewById(R.id.c_acl_solved);
            mPhotoImage = (ImageView) itemView.findViewById(R.id.crime_photo);
            // TODO: 6.12.2017  devam-2
            mLinerLayout = (LinearLayout) view.findViewById(R.id.rowColor);

            itemView.setOnClickListener(this);
        }

        public void bind(CrimeDec crimDec) {
            mCrimeDec = crimDec;
            mTxtTittle.setText(mCrimeDec.getTitle());
            String dateFormat = DateFormat.format("dd MMMM yyyy, EEEE", mCrimeDec.getDate()).toString();
            mTxtDate.setText(dateFormat);
            mSolvedImage.setVisibility(mCrimeDec.isSolved() ? View.VISIBLE : View.GONE);

        }




        @Override
        public void onClick(View view) {

            if (myToast != null) {
                myToast.cancel();
            }

            Log.d(TAG, "onClick: " + getAdapterPosition());
            if (getAdapterPosition() == RecyclerView.NO_POSITION) {
                mAdapter.notifyItemChanged(2);
                zeroFlag=false;

                Log.d(TAG, "Not SELECTED ");
            } else {
                //selected list Item
                Log.d(TAG, "SELECETED");
                mAdapter.notifyItemChanged(seleceted_position);
                seleceted_position = getAdapterPosition();
                myToast = Toast.makeText(getActivity(), mCrimeDec.getTitle() + " clicked", Toast.LENGTH_SHORT);
                myToast.show();
                mAdapter.notifyItemChanged(seleceted_position);
                Log.d(TAG, "onClick: selected position" + seleceted_position);
                zeroFlag= true;
            }

            // callback another fragment
            mCallback.onCrimeSelected(mCrimeDec);

        }
    }

    //Recycle adapter
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<CrimeDec> mCrimeList;


        public CrimeAdapter(List<CrimeDec> crimeList) {
            mCrimeList = crimeList;
        }


        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inf = LayoutInflater.from(getActivity());
            return new CrimeHolder(inf, parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            CrimeDec crimeDec = mCrimeList.get(position);
            holder.bind(crimeDec);

            // FIXME: 8.12.2017 row seçilmeden rengi getiriyor
            if (seleceted_position == position && zeroFlag) {
                Log.d(TAG, "ZERO FLAG" +zeroFlag);
                holder.itemView.setBackgroundColor(r.getColor(R.color.colorPrimary));
            } else {
                Log.d(TAG, "ZERO FLAG" +zeroFlag);
                holder.itemView.setBackgroundColor(r.getColor(R.color.normal_color));
            }
        }

        @Override
        public int getItemCount() {
            return mCrimeList.size();
        }

        // initialize crimes
        public void setCrimes(List<CrimeDec> listCrime) {
            mCrimeList = listCrime;
        }


    }

    // refresh adapter
    public void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<CrimeDec> crimeList = crimeLab.getCrimeList();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimeList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(crimeList);
            mAdapter.notifyDataSetChanged();

        }

        updateSubtitle();

    }

    // add menus
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
       // visible and unvisible
        MenuItem subtitleMenu = menu.findItem(R.id.show_subtitle);
        MenuItem delMenu = menu.findItem(R.id.delete_crime);
        delMenu.setVisible(false);
        if (mSubtitleVisible) {
            subtitleMenu.setTitle(R.string.hide_subtitle);
        } else
            subtitleMenu.setTitle(R.string.show_subtitle);
    }

    // add new crime
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.new_crime:

                CrimeDec newCrime = new CrimeDec();
                CrimeLab.get(getActivity()).addCrime(newCrime);

                // update crime and call callback new Crime
                updateUI();
                mCallback.onCrimeSelected(newCrime);

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


    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimeList().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }


    //swap to dismiss

    public void setCrimeRecyclerViewItemTouchListener() {

        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;   //  I don't want support moving items up/down....
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                CrimeDec NNN = mAdapter.mCrimeList.get(position);
                Log.d(TAG, "onSwiped: " + NNN.getId());
                mDeleteCalLBack.onCrimeIdSelected(NNN.getId());

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                View itemview = viewHolder.itemView;
                Drawable deleteIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_menu_delete);
                float IcontHeight = deleteIcon.getIntrinsicHeight();
                float IconWidth = deleteIcon.getIntrinsicWidth();
                float itemHeight = itemview.getBottom() - itemview.getTop();
                // TODO: 7.12.2017 Swapte Bunu yaptık ve true değeri bize geldi... :)
                if (actionState == ACTION_STATE_SWIPE) {

                    // painting for red
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    RectF layout = new RectF(itemview.getLeft(), itemview.getTop(), itemview.getRight(), itemview.getBottom());
                    paint.setColor(r.getColor(R.color.delete_recyclerview_color));
                    c.drawRect(layout, paint);

                    // icon size configration
                    int deleteIconTop = (int) (itemview.getTop() + (itemHeight - IcontHeight) / 2);
                    int deleteIconBottom = (int) (deleteIconTop + IcontHeight);
                    int deleteIconMargin = (int) ((itemHeight - IcontHeight) / 2);
                    int deleteIconLeft = (int) (itemview.getRight() - deleteIconMargin - IconWidth);
                    int deleteIconRight = (int) itemview.getRight() - deleteIconMargin;
                    deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
                    deleteIcon.draw(c);


                    getDefaultUIUtil().onDraw(c, recyclerView, viewHolder.itemView, dX, dY, actionState, isCurrentlyActive);


                } else {

                }
            }

        };


        ItemTouchHelper iteItemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        iteItemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    public void deleteCrime(UUID crimeId) {
        CrimeDec crimeDec = CrimeLab.get(getActivity()).getCrime(crimeId);
        CrimeLab.get(getActivity()).deleteCrime(crimeDec);

    }

    // animation for recyclerView
    private void runLayoutAnimation(RecyclerView recyclerView) {

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layou_animation_fall_down);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
        recyclerView.invalidate();

    }


}
