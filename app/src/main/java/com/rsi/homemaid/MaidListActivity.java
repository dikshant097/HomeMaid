package com.rsi.homemaid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.MaidListRecyclerAdapter;
import bean.CategoryDataClass;
import bean.CategoryDetail;
import bean.MaidDataClass;
import bean.MaidList;
import bean.MaidReviewDataClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by deepak.sharma on 7/18/2017.
 */

public class MaidListActivity extends BaseActivity {

    public static final int REQUEST_CODE = 1;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MaidListRecyclerAdapter maidListRecyclerAdapter;
    private List<MaidList> maidLists;
    private int list_position;
    private ArrayList<String> maids=new ArrayList<>();
    private List<CategoryDetail> categoryDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maid_list);

        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.black)));


        getSupportActionBar().setTitle("Search Maids");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        empty_view = (TextView) findViewById(R.id.empty_view);

        Call<MaidDataClass> callMaid = apiService.getMaidsList(getMaidsJson("c104", dbHelper.getUserDetails().get(0).getUserId()).toString());
        callMaid.enqueue(new Callback<MaidDataClass>() {
            @Override
            public void onResponse(Call<MaidDataClass> call, Response<MaidDataClass> response) {
                progress_bar.setVisibility(View.GONE);

                if (response.body() == null) return;
                if (response.body().getStatus().equalsIgnoreCase("success")){

                    maidLists = response.body().getMaidList();
                    if (maidLists.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        empty_view.setVisibility(View.VISIBLE);
                    }
                    else {
                        recyclerView.setVisibility(View.VISIBLE);
                        empty_view.setVisibility(View.GONE);
                    }

                    maidListRecyclerAdapter = new MaidListRecyclerAdapter(MaidListActivity.this, response.body().getMaidList(), new MaidListRecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MaidList item, int position) {
                            list_position = position;

                            Intent mIntent = new Intent(MaidListActivity.this, MaidDetailsActivity.class);
                            mIntent.putExtra("Maid", item);
                            mIntent.putExtra("catId", getIntent().getExtras().getString("CatId"));
                            startActivityForResult(mIntent,REQUEST_CODE);
                        }
                    }, getIntent().getExtras().getString("CatId"));
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(maidListRecyclerAdapter);
                }
            }
            @Override
            public void onFailure(Call<MaidDataClass> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                showCustomToast(recyclerView, getString(R.string.err_message_retrofit));
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                MaidReviewDataClass maidReviewDataClass = (MaidReviewDataClass)data.getExtras().get("result");
                if (maidReviewDataClass != null){
                    maidLists.get(list_position).setRating(maidReviewDataClass.getAverageRating());
                    maidLists.get(list_position).setMaidRatingCount(maidReviewDataClass.getMaidRatingCount());
                    maidListRecyclerAdapter.notifyDataSetChanged();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_fav){
            startActivity(new Intent(MaidListActivity.this, FavoriteActivity.class));
            return true;
        }

        if(id == R.id.action_filter){
            startActivity(new Intent(MaidListActivity.this, FilterActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected JSONArray getMaidsJson(String categoryId, String userId) {
        JSONObject paramObject = new JSONObject();
        JSONArray jsonArray =new JSONArray();
        try {
            paramObject.put("categoryId", categoryId);
            paramObject.put("userId", userId);
            jsonArray.put(paramObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

}
