package fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rsi.homemaid.MaidDetailsActivity;
import com.rsi.homemaid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import adapter.MaidListRecyclerAdapter;
import bean.MaidDataClass;
import bean.MaidList;
import bean.MaidReviewDataClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Ravi on 29/07/15.
 */
public class FavoritesFragment extends BaseFragment {

    public static final int REQUEST_CODE = 1;

    private RecyclerView recyclerView;
    private MaidListRecyclerAdapter maidListRecyclerAdapter;
    private List<MaidList> maidLists;
    private int list_position;


    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        progress_bar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        empty_view = (TextView) rootView.findViewById(R.id.empty_view);
        Call<MaidDataClass> call = apiService.getFavMaidsList(getFavMaidsJson(dbHelper.getUserDetails().get(0).getUserId()).toString());
        progress_bar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<MaidDataClass>() {
            @Override
            public void onResponse(Call<MaidDataClass> call, Response<MaidDataClass> response) {
                progress_bar.setVisibility(View.GONE);

                if (response.body() == null) return;
                if (response.body().getStatus().equalsIgnoreCase("success")){

                    if (response.body().getMaidList() == null){
                        recyclerView.setVisibility(View.GONE);
                        empty_view.setVisibility(View.VISIBLE);
                        return;
                    }
                    maidLists = response.body().getMaidList();

                    if (maidLists.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        empty_view.setVisibility(View.VISIBLE);
                    }
                    else {
                        recyclerView.setVisibility(View.VISIBLE);
                        empty_view.setVisibility(View.GONE);
                    }
                    maidListRecyclerAdapter = new MaidListRecyclerAdapter(getActivity(), response.body().getMaidList(), new MaidListRecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MaidList item, int position) {
                            list_position = position;

                            Intent mIntent = new Intent(getActivity(), MaidDetailsActivity.class);
                            mIntent.putExtra("Maid", item);
                            startActivityForResult(mIntent,REQUEST_CODE);
                        }
                    }, "");
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    protected JSONArray getFavMaidsJson(String userId) {
        JSONObject paramObject = new JSONObject();
        JSONArray jsonArray =new JSONArray();
        try {
            paramObject.put("userId", userId);
            jsonArray.put(paramObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
