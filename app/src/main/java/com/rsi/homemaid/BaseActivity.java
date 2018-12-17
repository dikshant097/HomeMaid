package com.rsi.homemaid;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import database.DatabaseHelper;
import retrofit.ApiClient;
import retrofit.ApiInterface;

/**
 * Created by deepak.sharma on 7/12/2017.
 */

public class BaseActivity extends AppCompatActivity {

    protected ApiInterface apiService;
    protected DatabaseHelper dbHelper;
    protected ProgressBar progress_bar;
    protected TextView empty_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        dbHelper = DatabaseHelper.getInstance(this);
    }

    void showMessage(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.alert_dark_frame)
                .show();
    }

    protected void showCustomToast(View view, String message){
        Snackbar snackbar = Snackbar
                .make(view, message , Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.red_color));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(15);
        snackbar.show();
    }
}
