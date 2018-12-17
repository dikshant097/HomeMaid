package com.rsi.homemaid;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.login.LoginManager;

import fragment.AboutUsFragment;
import fragment.FragmentDrawer;
import fragment.FavoritesFragment;
import fragment.HomeFragment;
import fragment.HistoryFragment;
import tools.AppSharedPreference;

/**
 * Created by deepak.sharma on 7/12/2017.
 */

public class HomeActivity extends BaseActivity implements FragmentDrawer.FragmentDrawerListener{

    private static String TAG = HomeActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.findViewById(R.id.llContainer).setVisibility(View.VISIBLE);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        displayView(0);

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                mToolbar.findViewById(R.id.llContainer).setVisibility(View.VISIBLE);
                break;
            case 1:
                fragment = new FavoritesFragment();
                title = getString(R.string.title_favorite);
                mToolbar.findViewById(R.id.llContainer).setVisibility(View.GONE);
                break;
            case 2:
                fragment = new HistoryFragment();
                title = getString(R.string.title_history);
                mToolbar.findViewById(R.id.llContainer).setVisibility(View.GONE);
                break;
            case 3:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                       "Hey I strongly recommend this app, you will find this immensely useful to search maids/cooks in your society. Download it now : https://play.google.com/store/apps/details?id=com.rsi.homemaid");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case 4:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.rsi.homemaid"));
                startActivity(browserIntent);
                break;

            case 5:
                fragment = new AboutUsFragment();
                title = getString(R.string.title_about_us);
                mToolbar.findViewById(R.id.llContainer).setVisibility(View.GONE);
                break;
            case 6:
                AppSharedPreference.getInstance(HomeActivity.this).setIsLogin(false);
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                LoginManager.getInstance().logOut();
                finish();
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.black)));
    }
}
