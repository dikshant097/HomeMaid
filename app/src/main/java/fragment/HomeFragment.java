package fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.rsi.homemaid.HomeActivity;
import com.rsi.homemaid.LoginActivity;
import com.rsi.homemaid.MaidListActivity;
import com.rsi.homemaid.R;

import java.util.HashMap;
import java.util.List;

import adapter.GridViewHomeAdapter;
import bean.AdvertiseImage;
import bean.AdvertisementDataClass;
import bean.CategoryDataClass;
import bean.CategoryDetail;
import bean.Login;
import in.srain.cube.views.GridViewWithHeaderAndFooter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.AppSharedPreference;
import tools.HelperMethods;


public class HomeFragment extends BaseFragment {

    private SliderLayout mDemoSlider;
    private GridViewWithHeaderAndFooter grid;
    private GridViewHomeAdapter gridViewHomeAdapter;
    private List<CategoryDetail> categoryDetails;
    private List<AdvertiseImage> advertiseImages;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        progress_bar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        mDemoSlider = (SliderLayout) rootView.findViewById(R.id.slider);
        grid = (GridViewWithHeaderAndFooter) rootView.findViewById(R.id.grid);
        grid.setVerticalScrollBarEnabled(false);

        Call<CategoryDataClass> call = apiService.getCategories();
        progress_bar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<CategoryDataClass>() {
            @Override
            public void onResponse(Call<CategoryDataClass> call, Response<CategoryDataClass> response) {
                progress_bar.setVisibility(View.GONE);

                if (response.body() == null) return;
                if (response.body().getStatus().equals("success")){
                    categoryDetails = response.body().getCategoryDetails();
                    gridViewHomeAdapter = new GridViewHomeAdapter(getActivity(), response.body().getCategoryDetails());
                    grid.setAdapter(gridViewHomeAdapter);
                }

            }
            @Override
            public void onFailure(Call<CategoryDataClass> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);

                    showCustomToast(grid, getResources().getString(R.string.err_message_retrofit));
            }
        });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent mIntent = new Intent(getActivity(), MaidListActivity.class);
                mIntent.putExtra("CatId", categoryDetails.get(position).getId());
                mIntent.putExtra("Title", categoryDetails.get(position).getName());
                startActivity(mIntent);
            }
        });

        addAdvertisementBanner();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }


    private void addAdvertisementBanner(){

        Call<AdvertisementDataClass> call = apiService.getAdvertisementURLs();
        call.enqueue(new Callback<AdvertisementDataClass>() {
            @Override
            public void onResponse(Call<AdvertisementDataClass> call, Response<AdvertisementDataClass> response) {

                if (response.body() == null) return;
                if (response.body().getStatus().equals("success")){
                    advertiseImages = response.body().getAdvertiseImages();

                    for(AdvertiseImage image : advertiseImages){
                        DefaultSliderView textSliderView = new DefaultSliderView (getActivity());
                        // initialize a SliderLayout
                        textSliderView
                                .description("")
                                .image(image.getImage())
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(HomeFragment.this);

                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("extra",image.getImage());

                        mDemoSlider.addSlider(textSliderView);
                    }
                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
                    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                    mDemoSlider.setDuration(2000);
                    mDemoSlider.addOnPageChangeListener(HomeFragment.this);
                }

            }
            @Override
            public void onFailure(Call<AdvertisementDataClass> call, Throwable t) {
                showCustomToast(grid, getResources().getString(R.string.err_message_retrofit));
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        mDemoSlider.startAutoCycle();

    }
}
