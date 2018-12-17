package com.rsi.homemaid;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.ViewAllReviewsRecyclerAdapter;
import bean.AllUsersRatingDataClass;
import bean.MaidList;
import io.techery.properratingbar.ProperRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by deepak.sharma on 9/8/2017.
 */

public class ViewAllReviewsActivity extends BaseActivity{

    private Toolbar toolbar;
    private TextView tv_name, tv_rating, tv_rating_count;
    private ProperRatingBar upperRatingBar;
    private RecyclerView rv_view_all_reviews;
    private ViewAllReviewsRecyclerAdapter viewAllReviewsRecyclerAdapter;
    private MaidList maid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_reviews);

        if (getIntent().getExtras().get("Maid") != null)
            maid = (MaidList) getIntent().getExtras().get("Maid");

        rv_view_all_reviews = (RecyclerView) findViewById(R.id.rv_view_all_reviews);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_rating = (TextView) findViewById(R.id.tv_rating);
        tv_rating_count = (TextView) findViewById(R.id.tv_rating_count);
        upperRatingBar = (ProperRatingBar) findViewById(R.id.upperRatingBar);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        empty_view = (TextView) findViewById(R.id.empty_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

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

        tv_name.setText(maid.getName());
        tv_rating.setText(""+maid.getRating());
        tv_rating_count.setText(String.valueOf(maid.getMaidRatingCount()) + " Ratings");
        upperRatingBar.setRating((int)maid.getRating());


        Call<AllUsersRatingDataClass> call = apiService.getAllUsersRating(getAllUsersRatingJson(maid.getMadeId(), "100").toString());
        progress_bar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<AllUsersRatingDataClass>() {
            @Override
            public void onResponse(Call<AllUsersRatingDataClass> call, Response<AllUsersRatingDataClass> response) {
                progress_bar.setVisibility(View.GONE);

                if (response.body() == null) return;

                if (response.body().getStatus().equalsIgnoreCase("Success")){

                    if (response.body().getMaidReviewList().isEmpty()) {
                        rv_view_all_reviews.setVisibility(View.GONE);
                        empty_view.setVisibility(View.VISIBLE);
                    }
                    else {
                        rv_view_all_reviews.setVisibility(View.VISIBLE);
                        empty_view.setVisibility(View.GONE);
                    }
                    viewAllReviewsRecyclerAdapter = new ViewAllReviewsRecyclerAdapter(ViewAllReviewsActivity.this, response.body().getMaidReviewList());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ViewAllReviewsActivity.this);
                    rv_view_all_reviews.setLayoutManager(mLayoutManager);
                    rv_view_all_reviews.addItemDecoration(new DividerItemDecoration(ViewAllReviewsActivity.this, DividerItemDecoration.VERTICAL));
                    rv_view_all_reviews.setAdapter(viewAllReviewsRecyclerAdapter);
                }
            }

            @Override
            public void onFailure(Call<AllUsersRatingDataClass> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
            }
        });

    }

    protected JSONArray getAllUsersRatingJson(String maidId, String count) {
        JSONObject paramObject = new JSONObject();
        JSONArray jsonArray =new JSONArray();
        try {
            paramObject.put("maidId", maidId);
            paramObject.put("count", count);
            jsonArray.put(paramObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
