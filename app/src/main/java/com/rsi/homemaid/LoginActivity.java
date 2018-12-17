package com.rsi.homemaid;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import bean.Login;
import database.DatabaseHelper;
import retrofit.ApiClient;
import retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.AppSharedPreference;
import tools.HelperMethods;

/**
 * Created by deepak.sharma on 7/12/2017.
 */

public class LoginActivity extends BaseLoginActivity {

    private EditText et_mobile_number;
    private Button btn_login, fb_button, btn_google;
    public static Activity fa;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        fa = this;
        et_mobile_number = (EditText) findViewById(R.id.et_mobile_number);
        btn_login = (Button) findViewById(R.id.btn_login);
        fb_button = (Button) findViewById(R.id.fb_button);
        btn_google = (Button) findViewById(R.id.btn_google);


        btn_login.setBackgroundColor(getResources().getColor(R.color.black));
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!HelperMethods.isNetworkConnected(LoginActivity.this)){
                    showCustomToast(et_mobile_number, "No internet Connection !");
                    return;
                }
                if (validateMobileNumber()){
                    validateOTP();
                }
            }
        });

        fb_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("user_photos", "email", "public_profile"));
            }
        });

        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>()
                {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        getFacebookUserDetails(loginResult);
                    }

                    @Override
                    public void onCancel()
                    {
                        Log.e("cancel", "OnCancel");
                    }
                    @Override
                    public void onError(FacebookException exception)
                    {
                        Log.e("Error", exception.toString());
                    }
                });
    }


    private boolean validateMobileNumber() {
        if (et_mobile_number.getText().toString().trim().isEmpty()) {
            Toast.makeText(LoginActivity.this, R.string.err_msg_mobile_number, Toast.LENGTH_SHORT).show();
            requestFocus(et_mobile_number);
            return false;
        } else if (et_mobile_number.getText().toString().length() < 10){
            Toast.makeText(LoginActivity.this, R.string.err_msg_mobile_number_wrong, Toast.LENGTH_SHORT).show();
            requestFocus(et_mobile_number);
            return false;
        }
        return true;
    }


    public boolean validateOTP(){
        Intent mIntent = new Intent(this, OTPActivity.class);
        mIntent.putExtra("Mobile_Number", et_mobile_number.getText().toString());
        startActivity(mIntent);

        return true;
    }

}
