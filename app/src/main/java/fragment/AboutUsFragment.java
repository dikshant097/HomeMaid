package fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.rsi.homemaid.R;

/**
 * Created by deepak.sharma on 8/24/2017.
 */

public class AboutUsFragment extends BaseFragment {

    private WebView web_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);
        web_view = (WebView) rootView.findViewById(R.id.web_view);
        web_view.loadUrl("http://homemade.hostzi.com/homemade/aboutus.php");

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
}
