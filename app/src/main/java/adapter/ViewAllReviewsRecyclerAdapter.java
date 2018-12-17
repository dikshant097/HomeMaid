package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rsi.homemaid.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import bean.MaidReviewList;
import helper.RoundedImageView;
import io.techery.properratingbar.ProperRatingBar;
import tools.HelperMethods;

/**
 * Created by deepak.sharma on 9/8/2017.
 */

public class ViewAllReviewsRecyclerAdapter extends RecyclerView.Adapter<ViewAllReviewsRecyclerAdapter.MyViewHolder>{


    private Context context;
    private List<MaidReviewList> maidReviewList;

    public ViewAllReviewsRecyclerAdapter(Context context, List<MaidReviewList> maidReviewList) {
        this.context = context;
        this.maidReviewList = maidReviewList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_all_review_row_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.view_divider.setVisibility(View.GONE);
        holder.tv_user_rating_name.setText(maidReviewList.get(position).getUserName());
        if (maidReviewList.get(position).getReview().equalsIgnoreCase(""))
            holder.tv_user_rating_review.setVisibility(View.GONE);
        holder.tv_user_rating_review.setText(maidReviewList.get(position).getReview());
        holder.upperRatingBar.setRating(maidReviewList.get(position).getRating());
        if (maidReviewList.get(position).getUserPhoto() != "")
        Picasso.with(context)
                .load(maidReviewList.get(position).getUserPhoto())
                .into(holder.iv_users_ratings);

        holder.tv_user_rating_date.setText("Reviewed on " + HelperMethods.getDate(maidReviewList.get(position).getDate(), "dd"));
    }

    @Override
    public int getItemCount() {
        return maidReviewList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public RoundedImageView iv_users_ratings;
        public TextView tv_user_rating_name, tv_user_rating_review, tv_user_rating_date;
        public ProperRatingBar upperRatingBar;

        public View view_divider;

        public MyViewHolder(View view) {
            super(view);
            iv_users_ratings = (RoundedImageView) view.findViewById(R.id.iv_users_ratings);
            tv_user_rating_name = (TextView) view.findViewById(R.id.tv_user_rating_name);
            tv_user_rating_review = (TextView) view.findViewById(R.id.tv_user_rating_review);
            tv_user_rating_date = (TextView) view.findViewById(R.id.tv_user_rating_date);
            upperRatingBar = (ProperRatingBar) view.findViewById(R.id.upperRatingBar);
            view_divider = view.findViewById(R.id.view_divider);

        }
    }

}
