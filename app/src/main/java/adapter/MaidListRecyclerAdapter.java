package adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.rsi.homemaid.MaidDetailsActivity;
import com.rsi.homemaid.MaidListActivity;
import com.rsi.homemaid.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bean.MaidDataClass;
import bean.MaidList;
import bean.Status;
import database.DatabaseHelper;
import helper.RoundedImageView;
import retrofit.ApiClient;
import retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by deepak.sharma on 7/18/2017.
 */

public class MaidListRecyclerAdapter extends RecyclerView.Adapter<MaidListRecyclerAdapter.MyViewHolder> {

    private List<MaidList> maidLists;
    private Context context;
    private final OnItemClickListener listener;
    private ApiInterface apiService;
    private DatabaseHelper databaseHelper;

    private String catId;
    public MaidListRecyclerAdapter(Context context, List<MaidList> maidLists, OnItemClickListener listener, String catId) {
        this.maidLists = maidLists;
        this.context = context;
        this.listener = listener;
        apiService = ApiClient.getClient().create(ApiInterface.class);
        databaseHelper = DatabaseHelper.getInstance(context);
        this.catId = catId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_maid_list_row_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.bind(maidLists.get(position), listener);
        holder.setIsRecyclable(false);
        holder.tv_name.setText(maidLists.get(position).getName());
        holder.tv_religion.setText(maidLists.get(position).getReligion());
        holder.tv_rating.setText(""+maidLists.get(position).getRating());
        holder.tv_total_ratings.setText("(" + String.valueOf(maidLists.get(position).getMaidRatingCount() + ")"));
        if (maidLists.get(position).getVerification().equalsIgnoreCase("Verified"))
            holder.iv_verified.setVisibility(View.VISIBLE);
        else
            holder.iv_verified.setVisibility(View.INVISIBLE);
        holder.tv_rs.setText(context.getResources().getString(R.string.Rs) + " " + maidLists.get(position).getCost() + "");
        holder.tv_experience.setText(maidLists.get(position).getExperience() + " years experience");
        holder.tv_available_time.setText(maidLists.get(position).getWorkTime().replace("\\n", System.getProperty("line.separator")));

        Picasso.with(context)
                .load(maidLists.get(position).getPhoto())
                .placeholder(R.drawable.thumbnail_profile)
                .into(holder.iv_pic_maid);
        if (maidLists.get(position).getCookingType().equalsIgnoreCase("veg")){
            holder.iv_veg.getDrawable().setColorFilter(context.getResources().getColor(R.color.darkgreen), PorterDuff.Mode.SRC_IN);
            holder.iv_veg.getBackground().setColorFilter(context.getResources().getColor(R.color.darkgreen), PorterDuff.Mode.SRC_IN);
        }else if (maidLists.get(position).getCookingType().equalsIgnoreCase("Non-Veg")){
            holder.iv_veg.getDrawable().setColorFilter(context.getResources().getColor(R.color.dark_red_color), PorterDuff.Mode.SRC_IN);
            holder.iv_veg.getBackground().setColorFilter(context.getResources().getColor(R.color.dark_red_color), PorterDuff.Mode.SRC_IN);
        }else if (maidLists.get(position).getCookingType().equalsIgnoreCase("Both")){
            holder.iv_veg.getDrawable().setColorFilter(context.getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
            holder.iv_veg.getBackground().setColorFilter(context.getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
        }else {
            holder.iv_veg.setVisibility(View.GONE);
        }

        if (maidLists.get(position).getIsFavourite().equalsIgnoreCase("true")){
            holder.iv_favourite.setLiked(true);
        }else {
            holder.iv_favourite.setLiked(false);
        }

        holder.iv_favourite.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {


                Call<Status> call = apiService.addFav(getFavJson(databaseHelper.getUserDetails().get(0).getUserId(), maidLists.get(position).getMadeId(), "True", catId).toString());
                call.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {

                        if (response.body().getStatus().equalsIgnoreCase("success")){
                            maidLists.get(position).setIsFavourite("true");
                        }
                    }
                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        holder.iv_favourite.setLiked(false);
                        showCustomToast(holder.itemView, context.getString(R.string.err_message_retrofit));
                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                maidLists.get(position).setIsFavourite("false");

                Call<Status> call = apiService.addFav(getFavJson(databaseHelper.getUserDetails().get(0).getUserId(), maidLists.get(position).getMadeId(), "False", catId).toString());
                call.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {

                        if (response.body().getStatus().equalsIgnoreCase("success")){
                            maidLists.get(position).setIsFavourite("false");
                        }

                    }
                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        holder.iv_favourite.setLiked(true);
                        showCustomToast(holder.itemView, context.getString(R.string.err_message_retrofit));

                    }
                });
            }
        });


        holder.btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("tel:" + maidLists.get(position).getPhoneNumber()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return maidLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public RoundedImageView iv_pic_maid;
        public ImageView  iv_veg, iv_non_veg, iv_verified;
        public LikeButton iv_favourite;
        public TextView tv_name, tv_religion, tv_rating, tv_total_ratings, tv_rs, tv_experience, tv_available_time ;
        Button btn_call;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_religion = (TextView) view.findViewById(R.id.tv_religion);
            tv_rating = (TextView) view.findViewById(R.id.tv_rating);
            tv_total_ratings = (TextView) view.findViewById(R.id.tv_total_ratings);
            tv_rs = (TextView) view.findViewById(R.id.tv_rs);
            tv_experience = (TextView) view.findViewById(R.id.tv_experience);
            tv_available_time = (TextView) view.findViewById(R.id.tv_available_time_text);
            iv_pic_maid = (RoundedImageView) view.findViewById(R.id.iv_pic_maid);
            iv_verified = (ImageView) view.findViewById(R.id.iv_verified);
            iv_favourite = (LikeButton) view.findViewById(R.id.iv_favourite);
            iv_non_veg = (ImageView) view.findViewById(R.id.iv_non_veg);
            iv_veg = (ImageView) view.findViewById(R.id.iv_veg);
            btn_call = (Button) view.findViewById(R.id.btn_call);
        }

        public void bind(final MaidList item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item, getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface OnItemClickListener {
        void onItemClick(MaidList item, int position);
    }


    protected JSONArray getFavJson(String userId, String maidId, String action, String categoryId) {
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

    protected void showCustomToast(View view, String message){
        Snackbar snackbar = Snackbar
                .make(view, message , Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.red_color));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(15);
        snackbar.show();
    }
}
