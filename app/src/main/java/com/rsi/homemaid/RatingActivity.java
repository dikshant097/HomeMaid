package com.rsi.homemaid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bean.MaidList;
import bean.MaidReviewDataClass;
import bean.SelfRatingDataClass;
import bean.Status;
import io.techery.properratingbar.ProperRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by deepak.sharma on 8/2/2017.
 */

public class RatingActivity extends BaseActivity {

    private Toolbar toolbar;
    private Button btn_submit;
    private TextView tv_name, tv_address;
    private ProperRatingBar lowerRatingBar;
    private EditText et_review;
    private MaidList maid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        if (getIntent().getExtras().get("Maid") != null)
            maid = (MaidList) getIntent().getExtras().get("Maid");

        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lowerRatingBar = (ProperRatingBar) findViewById(R.id.lowerRatingBar);
        lowerRatingBar.setClickable(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.black)));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_submit = (Button) findViewById(R.id.btn_submit);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        et_review = (EditText) findViewById(R.id.et_review);
        et_review.setEnabled(true);
        tv_name.setText(maid.getName());
        tv_address.setText(maid.getLocalAddress());

        checkSelfMaidRating();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<MaidReviewDataClass> call = apiService.maidRating(String.valueOf(getMaidRatingJson(dbHelper.getUserDetails().get(0).getUserId(), maid.getMadeId(), String.valueOf(lowerRatingBar.getRating()), et_review.getText().toString(), System.currentTimeMillis())));
                progress_bar.setVisibility(View.VISIBLE);
                call.enqueue(new Callback<MaidReviewDataClass>() {
                    @Override
                    public void onResponse(Call<MaidReviewDataClass> call, Response<MaidReviewDataClass> response) {

                        progress_bar.setVisibility(View.GONE);

                        if (response.body().getStatus().equalsIgnoreCase("success")){
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result",response.body());
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        }

                    }
                    @Override
                    public void onFailure(Call<MaidReviewDataClass> call, Throwable t) {
                        progress_bar.setVisibility(View.GONE);
                        showCustomToast(tv_address, getString(R.string.err_message_retrofit));
                    }
                });

            }
        });

    }

    protected JSONArray getMaidRatingJson(String userId, String maidId, String rating, String review, long date) {
        JSONObject paramObject = new JSONObject();
        JSONArray jsonArray =new JSONArray();
        try {
            paramObject.put("userId", userId);
            paramObject.put("maidId", maidId);
            paramObject.put("rating", rating);
            paramObject.put("review", review);
            paramObject.put("date", date);
            jsonArray.put(paramObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }


    protected JSONArray getMaidSelfRatingJson(String userId, String maidId) {
        JSONObject paramObject = new JSONObject();
        JSONArray jsonArray =new JSONArray();
        try {
            paramObject.put("userId", userId);
            paramObject.put("maidId", maidId);
            jsonArray.put(paramObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    private void checkSelfMaidRating(){

        Call<SelfRatingDataClass> call = apiService.getMaidSelfRating(String.valueOf(getMaidSelfRatingJson(dbHelper.getUserDetails().get(0).getUserId(), maid.getMadeId())));
        progress_bar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<SelfRatingDataClass>() {
            @Override
            public void onResponse(Call<SelfRatingDataClass> call, Response<SelfRatingDataClass> response) {

                progress_bar.setVisibility(View.GONE);

                if (response.body().getStatus().equalsIgnoreCase("success")){
                    et_review.setText(response.body().getReview());
                    lowerRatingBar.setRating(Integer.parseInt(response.body().getRating()));
                }
            }
            @Override
            public void onFailure(Call<SelfRatingDataClass> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                    showCustomToast(tv_address, getString(R.string.err_message_retrofit));
            }
        });

    }
}
 /* lowerRatingBar.setClickable(false);
                    et_review.setEnabled(false);
                    btn_submit.setEnabled(false);
                    btn_submit.setBackgroundColor(getResources().getColor(R.color.text_grey));
                    showCustomToast(tv_address, getString(R.string.already_rating_message));*/