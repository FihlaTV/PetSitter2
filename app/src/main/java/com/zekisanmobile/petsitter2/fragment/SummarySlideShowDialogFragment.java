package com.zekisanmobile.petsitter2.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zekisanmobile.petsitter2.R;
import com.zekisanmobile.petsitter2.model.JobModel;
import com.zekisanmobile.petsitter2.util.Config;
import com.zekisanmobile.petsitter2.util.DateFormatter;
import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.PhotoUrl;
import com.zekisanmobile.petsitter2.vo.Summary;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class SummarySlideShowDialogFragment extends DialogFragment {

    private String TAG = SummarySlideShowDialogFragment.class.getSimpleName();
    private List<PhotoUrl> photos;
    private List<Summary> summaries;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount;
    private TextView tvText;
    private TextView tvDate;
    private int selectedPosition = 0;
    private Realm realm;
    private String jobId;
    private JobModel jobModel;
    private Job job;

    public static SummarySlideShowDialogFragment newInstance() {
        SummarySlideShowDialogFragment fragment = new SummarySlideShowDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        this.jobId = getArguments().getString(Config.JOB_ID);
        this.selectedPosition = getArguments().getInt(Config.SELECTED_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        lblCount = (TextView) v.findViewById(R.id.lbl_count);
        tvText = (TextView) v.findViewById(R.id.tv_text);
        tvDate = (TextView) v.findViewById(R.id.tv_date);

        realm = Realm.getDefaultInstance();
        jobModel = new JobModel(realm);
        job = jobModel.find(jobId);

        this.summaries = job.getSummaries();
        this.photos = new ArrayList<PhotoUrl>();
        for (Summary summary : summaries) {
            this.photos.add(summary.getPhotoUrl());
        }

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "photos size: " + photos.size());

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }

    @Override
    public void onDestroyView() {
        realm.close();
        super.onDestroyView();
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " de " + photos.size());

        Summary summary = summaries.get(position);
        tvText.setText(summary.getText());
        tvDate.setText(DateFormatter.formattedStringDateToView(summary.getCreatedAt()));
    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView ivPhotoPreview = (ImageView) view.findViewById(R.id.iv_photo_preview);

            PhotoUrl photo = photos.get(position);

            Picasso.with(getActivity())
                    .load(photo.getImage())
                    .into(ivPhotoPreview);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return photos.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
