package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.rsi.homemaid.R;

import java.util.List;

import bean.CategoryDetail;
import tools.AppSharedPreference;

/**
 * Created by deepak.sharma on 7/18/2017.
 */

public class GridViewHomeAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<CategoryDetail> categoryDetails;
    private DisplayImageOptions options;

    public GridViewHomeAdapter(Context context, List<CategoryDetail> categoryDetails) {
        this.context = context;
        this.categoryDetails = categoryDetails;
        inflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder_thumb)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

    }

    @Override
    public int getCount() {
        return categoryDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_grid_view, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_category);
            holder.tv_category = (TextView) convertView.findViewById(R.id.tv_category);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress);

            holder.tv_category.getBackground().setAlpha(70);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_category.setText(categoryDetails.get(position).getName());
        ImageLoader.getInstance()
                .displayImage(categoryDetails.get(position).getImage(), holder.imageView, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        holder.progressBar.setProgress(0);
                        holder.progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        holder.progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        holder.progressBar.setVisibility(View.GONE);
                        if (loadedImage != null)
                            //holder.rl_category.setBackgroundDrawable( new BitmapDrawable(context.getResources(), loadedImage));
                        holder.imageView.setImageBitmap(loadedImage);
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        holder.progressBar.setProgress(Math.round(100.0f * current / total));
                    }
                });

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
        TextView tv_category;
    }
}
