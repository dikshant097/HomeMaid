package com.rsi.homemaid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.ViewAllReviewsRecyclerAdapter;
import bean.AllUsersRatingDataClass;
import bean.MaidList;
import bean.MaidReviewDataClass;
import bean.MaidReviewList;
import bean.Status;
import dialog.CustomDialogFragment;
import helper.RoundedImageView;
import io.techery.properratingbar.ProperRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.HelperMethods;

/**
 * Created by deepak.sharma on 7/20/2017.
 */

// Commit from RG residency
// commit from Rsystems

    /*compile 'me.zhanghai.android.materialprogressbar:library:1.1.0'
            compile 'com.jakewharton:butterknife:8.5.1'
            annotationProcessor 'com.jakewharton:butterknife-compiler:8.5.1'*/
public class MaidDetailsActivity extends BaseActivity implements View.OnClickListener{

    public static final int REQUEST_CODE = 1;

    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private ImageView iv_pic_maid, iv_pricing_information, iv_verification_information, iv_verified;
    private LikeButton heart_button;
    private TextView tv_name, tv_rating, tv_rating_count, tv_address, tv_religion,tv_age, tv_experience, tv_available_time, tv_veg, tv_non_veg, tv_permanent, tv_temporary, tv_currently_working, tv_no_review_available ;
    private ProperRatingBar upperRatingBar;
    private LinearLayout ll_call, ll_rating, ll_share;
    private MaidList maid;
    private MaidReviewDataClass maidReviewDataClass;
    private String catId;
    private String star = "★";
    private LayoutInflater inflater;
    private ViewGroup parent;
    private View view;
    private Button btn_view_all_reviews;
    private List<MaidReviewList> maidReviewLists = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maid_details);

        if (getIntent().getExtras().get("Maid") != null)
        maid = (MaidList) getIntent().getExtras().get("Maid");

        if (getIntent().getExtras().get("catId") != null)
        catId = getIntent().getExtras().getString("catId");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRatingDataToMaidListActivityCallBack();
                finish();
            }
        });

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(maid.getName());
        toolbarTextAppearance();

        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        iv_verified = (ImageView) findViewById(R.id.iv_verified);
        heart_button = (LikeButton) findViewById(R.id.heart_button);
        iv_pic_maid = (RoundedImageView) collapsingToolbarLayout.findViewById(R.id.iv_pic_maid);
        iv_pricing_information = (ImageView) findViewById(R.id.iv_pricing_information);
        iv_verification_information = (ImageView) findViewById(R.id.iv_verification_information);
        if (maid.getVerification().equalsIgnoreCase("verified")){
            iv_verification_information.setVisibility(View.VISIBLE);
        }else {
            iv_verification_information.setVisibility(View.INVISIBLE);
        }

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_rating = (TextView) findViewById(R.id.tv_rating);
        tv_rating_count = (TextView) findViewById(R.id.tv_rating_count);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_religion = (TextView) findViewById(R.id.tv_religion);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_experience = (TextView) findViewById(R.id.tv_experience);
        tv_available_time = (TextView) findViewById(R.id.tv_available_time);
        tv_veg = (TextView) findViewById(R.id.tv_veg);
        tv_non_veg = (TextView) findViewById(R.id.tv_non_veg);
        tv_permanent = (TextView) findViewById(R.id.tv_permanent);
        tv_temporary = (TextView) findViewById(R.id.tv_temporary);
        tv_currently_working = (TextView) findViewById(R.id.tv_currently_working);
        tv_no_review_available = (TextView) findViewById(R.id.tv_no_review_available);
        upperRatingBar = (ProperRatingBar) findViewById(R.id.upperRatingBar);
        ll_call = (LinearLayout) findViewById(R.id.ll_call);
        ll_rating = (LinearLayout) findViewById(R.id.ll_rating);
        ll_share = (LinearLayout) findViewById(R.id.ll_share);
        btn_view_all_reviews = (Button) findViewById(R.id.btn_view_all_reviews);

        ll_call.setOnClickListener(this);
        ll_rating.setOnClickListener(this);
        ll_share.setOnClickListener(this);

        Picasso.with(this)
                .load(maid.getPhoto())
                .into(iv_pic_maid);


        for (int i = 0; i< maid.getMaidCostList().size(); i++){
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            parent = (ViewGroup)findViewById(R.id.ll_pricing);
            view = inflater.inflate(R.layout.pricing_dynamic_view, parent);
            TextView tv_category = (TextView) view.findViewById(R.id.tv_category);
            TextView tv_cost = (TextView) view.findViewById(R.id.tv_cost);
            tv_category.setId(i + 100);
            tv_cost.setId(i + 200);

            tv_category.setText(maid.getMaidCostList().get(i).getCategoryName());
            tv_cost.setText(getResources().getString(R.string.Rs) + " " + maid.getMaidCostList().get(i).getCost() + "");

        }


        iv_verified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(MaidDetailsActivity.this, DocumentsActivity.class);
                mIntent.putExtra("Maid", maid);
                startActivity(mIntent);
            }
        });

        btn_view_all_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(MaidDetailsActivity.this, ViewAllReviewsActivity.class);
                mIntent.putExtra("Maid", maid);
                startActivity(mIntent);

            }
        });

        heart_button.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                Call<Status> call = apiService.addFav(getFavJson(dbHelper.getUserDetails().get(0).getUserId(), maid.getMadeId(), "True", catId).toString());
                call.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {

                        if (response.body().getStatus().equalsIgnoreCase("success")){
                            heart_button.setLiked(true);
                        }
                    }
                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        heart_button.setLiked(false);
                        showCustomToast(tv_address, getString(R.string.err_message_retrofit));
                    }
                });

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Call<Status> call = apiService.addFav(getFavJson(dbHelper.getUserDetails().get(0).getUserId(), maid.getMadeId(), "False", catId).toString());
                call.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        if (response.body().getStatus().equalsIgnoreCase("success")){
                            heart_button.setLiked(false);
                        }
                    }
                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        heart_button.setLiked(true);
                        showCustomToast(tv_address, getString(R.string.err_message_retrofit));

                    }
                });
            }
        });

        iv_pricing_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Message", getResources().getString(R.string.pricing_inform));
                customDialogFragment.setArguments(bundle);
                customDialogFragment.show(getSupportFragmentManager(), "information");

            }
        });

        iv_verification_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Message", getResources().getString(R.string.verification_inform));
                customDialogFragment.setArguments(bundle);
                customDialogFragment.show(getSupportFragmentManager(), "Information");
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSms(getResources().getString(R.string.sms_text), maid.getPhoneNumber());
            }
        });

        FloatingActionButton fab_call = (FloatingActionButton) findViewById(R.id.fab_call);
        fab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callPhone(maid.getPhoneNumber());
            }
        });

        tv_name.setText(maid.getName());
        tv_rating.setText(""+maid.getRating());
        tv_rating_count.setText(String.valueOf(maid.getMaidRatingCount()) + " Ratings");
        tv_address.setText(maid.getLocalAddress());
        tv_religion.setText(maid.getReligion());
        tv_age.setText(maid.getAge()+ " years old");
        tv_experience.setText(maid.getExperience() + " Years Experience");
        tv_available_time.setText(maid.getWorkTime().replace("\\n", System.getProperty("line.separator")));


        if (maid.getVerification().equalsIgnoreCase("Verified"))
            iv_verified.setVisibility(View.VISIBLE);
        else
            iv_verified.setVisibility(View.INVISIBLE);

        if (maid.getIsFavourite().equalsIgnoreCase("true")){
            heart_button.setLiked(true);
        }else {
            heart_button.setLiked(false);
        }

        if (maid.getCookingType().equalsIgnoreCase("Veg")){
            tv_veg.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_tick ), null, null, null);
            tv_non_veg.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_cross ), null, null, null);
        }else if (maid.getCookingType().equalsIgnoreCase("Non-Veg")){
            tv_veg.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_cross ), null, null, null);
            tv_non_veg.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_tick ), null, null, null);
        }else if (maid.getCookingType().equalsIgnoreCase("Both")){
            tv_veg.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_tick ), null, null, null);
            tv_non_veg.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_tick ), null, null, null);
        }else {
            tv_veg.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_cross ), null, null, null);
            tv_non_veg.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_cross ), null, null, null);
        }

        if (maid.getWorkStyle().equalsIgnoreCase("permanent")){
            tv_permanent.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_tick ), null, null, null);
            tv_temporary.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_cross ), null, null, null);
        }else if (maid.getWorkStyle().equalsIgnoreCase("temporary")){
            tv_permanent.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_cross ), null, null, null);
            tv_temporary.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_tick ), null, null, null);
        }else if (maid.getWorkStyle().equalsIgnoreCase("Both")){
            tv_permanent.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_tick ), null, null, null);
            tv_temporary.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_tick ), null, null, null);
        }else {
            tv_permanent.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_cross ), null, null, null);
            tv_temporary.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_cross ), null, null, null);
        }

        tv_currently_working.setText(maid.getWorkingFlat().replace(",", "\n"));
        upperRatingBar.setRating((int) maid.getRating());


        getReviewList();

    }

    private void toolbarTextAppearance() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ll_call :

                callPhone(maid.getPhoneNumber());
                break;

            case R.id.ll_rating :

                Intent mIntent = new Intent(MaidDetailsActivity.this, RatingActivity.class);
                mIntent.putExtra("Maid", maid);
                startActivityForResult(mIntent, REQUEST_CODE);
                break;

            case R.id.ll_share :
                share();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                maidReviewDataClass = (MaidReviewDataClass)data.getExtras().get("result");
                tv_rating.setText(""+maidReviewDataClass.getAverageRating());
                tv_rating_count.setText(""+maidReviewDataClass.getMaidRatingCount());
                upperRatingBar.setRating((int)(float)maidReviewDataClass.getAverageRating());

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }
    }

    @Override
    public void onBackPressed() {
      setRatingDataToMaidListActivityCallBack();
    }

    private void setRatingDataToMaidListActivityCallBack(){

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", maidReviewDataClass);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    private void callPhone(String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    private void sendSms(String message, String phoneNumber){
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , new String (phoneNumber));
        smsIntent.putExtra("sms_body"  , message);

        try {
            startActivity(smsIntent);
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MaidDetailsActivity.this,
                    "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    private void share(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hi, \nYour friend "+ dbHelper.getUserDetails().get(0).getFullName()+ " has sent you a reference of maid/cook whose name is " + maid.getName() + ". \nYou can contact her at " + maid.getPhoneNumber() + "\nYou can also get details of so many maids/cooks available in your area. \nDownload the app now. \nhttps://play.google.com/store/apps/details?id=com.rsi.homemaid");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private List<MaidReviewList> getReviewList(){

        Call<AllUsersRatingDataClass> call = apiService.getAllUsersRating(getAllUsersRatingJson(maid.getMadeId(), "2").toString());
        progress_bar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<AllUsersRatingDataClass>() {
            @Override
            public void onResponse(Call<AllUsersRatingDataClass> call, Response<AllUsersRatingDataClass> response) {
                progress_bar.setVisibility(View.GONE);

                if (response.body() == null) return;

                if (response.body().getStatus().equalsIgnoreCase("Success")){

                    maidReviewLists = response.body().getMaidReviewList();

                    for (int i = 0; i < maidReviewLists.size(); i++){

                        btn_view_all_reviews.setVisibility(View.VISIBLE);
                        tv_no_review_available.setVisibility(View.GONE);

                        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        parent = (ViewGroup)findViewById(R.id.ll_users_ratings);
                        view = inflater.inflate(R.layout.user_ratings_dynamic_view, parent);
                        RoundedImageView iv_users_ratings = (RoundedImageView) view.findViewById(R.id.iv_users_ratings);
                        ProperRatingBar upperRatingBar = (ProperRatingBar) view.findViewById(R.id.upperRatingBar);
                        TextView tv_user_rating_name = (TextView) view.findViewById(R.id.tv_user_rating_name);
                        TextView tv_user_rating_review = (TextView) view.findViewById(R.id.tv_user_rating_review);
                        TextView tv_user_rating_date = (TextView) view.findViewById(R.id.tv_user_rating_date);

                        iv_users_ratings.setId(i + 400);
                        tv_user_rating_name.setId(i + 100);
                        tv_user_rating_review.setId(i + 200);
                        tv_user_rating_date.setId(i + 300);
                        upperRatingBar.setId(i + 500);
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tv_user_rating_name.getLayoutParams();
                        lp.addRule(RelativeLayout.RIGHT_OF, iv_users_ratings.getId());
                        tv_user_rating_name.setLayoutParams(lp);

                        tv_user_rating_name.setText(maidReviewLists.get(i).getUserName());
                        if (maidReviewLists.get(i).getReview().equalsIgnoreCase(""))
                            tv_user_rating_review.setVisibility(View.GONE);
                        tv_user_rating_review.setText(maidReviewLists.get(i).getReview());
                        upperRatingBar.setRating(maidReviewLists.get(i).getRating());
                        if (maidReviewLists.get(i).getUserPhoto() != "")
                            Picasso.with(MaidDetailsActivity.this)
                                    .load(maidReviewLists.get(i).getUserPhoto())
                                    .into(iv_users_ratings);

                        tv_user_rating_date.setText("Reviewed on " + HelperMethods.getDate(maidReviewLists.get(i).getDate(), "dd"));

                    }
                }
            }

            @Override
            public void onFailure(Call<AllUsersRatingDataClass> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                showCustomToast(tv_address, getString(R.string.err_message_retrofit));
            }
        });

        return maidReviewLists;

    }

    private JSONArray getFavJson(String userId, String maidId, String action, String categoryId) {
        JSONObject paramObject = new JSONObject();
        JSONArray jsonArray =new JSONArray();
        try {
            paramObject.put("userId", userId);
            paramObject.put("maidId", maidId);
            paramObject.put("action", action);
            paramObject.put("categoryId", categoryId);
            jsonArray.put(paramObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }


    private JSONArray getAllUsersRatingJson(String maidId, String count) {
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
