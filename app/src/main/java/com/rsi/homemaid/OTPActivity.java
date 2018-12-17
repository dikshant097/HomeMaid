package com.rsi.homemaid;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.goodiebag.pinview.Pinview;

import java.util.Random;

import bean.Login;
import bean.OTPDataClass;
import helper.Constants;
import retrofit.ApiClient;
import retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.AppSharedPreference;
import tools.HelperMethods;

/**
 * Created by deepak.sharma on 8/22/2017.
 */

public class OTPActivity extends BaseLoginActivity {

    private Pinview pin_view;
    private TextView tv_timer, tv_mobile;
    private Dialog mOverlayDialog ;
    private String mobile_number;
    private ApiInterface otp_apiService;
    private String otp_random_number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        otp_apiService = ApiClient.getOTPClient().create(ApiInterface.class);
        otp_random_number = String.format("%04d", new Random().nextInt(10000));
        tv_timer = (TextView) findViewById(R.id.tv_timer);
        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        tv_timer.setEnabled(false);

        if (getIntent().getExtras().getString("Mobile_Number") != null)
            mobile_number = getIntent().getExtras().getString("Mobile_Number");

        tv_mobile.setText("+91 "+mobile_number);
        sendOTP(mobile_number, "Your OTP is :" + " " + otp_random_number + "\n \nUse the following One Time Password to verify your identity on HomeMaid. Please do not disclose to anyone.", otp_random_number);



        mOverlayDialog  = new Dialog(OTPActivity.this, android.R.style.Theme_Panel);

        pin_view= (Pinview) findViewById(R.id.pin_view);
        pin_view.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(final Pinview pinview, boolean fromUser) {
                HelperMethods.hideKeyboardFrom(OTPActivity.this, pin_view);
                if (!HelperMethods.isNetworkConnected(OTPActivity.this)){
                    showCustomToast(pin_view, "No internet Connection !");
                    return;
                }
                verifyOTP(mobile_number, pinview.getValue());
            }
        });

        tv_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_timer.setEnabled(false);
                sendOTP(mobile_number, "Your OTP is :" + " " + otp_random_number + "\n \nUse the following One Time Password to verify your identity on HomeMaid. Please do not disclose to anyone.", otp_random_number);
            }
        });
    }

     private void validateLogin(){
         final Intent mIntent = new Intent(OTPActivity.this, SignUpActivity.class);
         mOverlayDialog.setCancelable(false);
         mOverlayDialog.show();
         Call<Login> call = apiService.loginValidation(getLoginJson(mobile_number, "NA","NA", "Mobile").toString());
         mProgressDialog.show();
         call.enqueue(new Callback<Login>() {
             @Override
             public void onResponse(Call<Login> call, Response<Login> response) {

                 if (mProgressDialog.isShowing())
                     mProgressDialog.dismiss();
                 mOverlayDialog.cancel();
                 if (response.body().getStatus().equalsIgnoreCase("success")){
                     setUserDetails(response);
                     dbHelper.deleteUserDetails();
                     dbHelper.insertUserDetails(response.body().getUserDetails());
                     AppSharedPreference.getInstance(OTPActivity.this).setIsLogin(true);
                     startActivity(new Intent(OTPActivity.this, HomeActivity.class));
                     LoginActivity.fa.finish();
                     finish();
                 }else if(response.body().getStatus().equalsIgnoreCase("failed")){
                     mIntent.putExtra("Mobile_Number", mobile_number);
                     mIntent.putExtra("LoginType", "Mobile");
                     startActivity(mIntent);
                 }
             }
             @Override
             public void onFailure(Call<Login> call, Throwable t) {
                 if (mProgressDialog.isShowing())
                     mProgressDialog.dismiss();
                 showCustomToast(pin_view, getString(R.string.err_message_retrofit));
                 mOverlayDialog.cancel();
             }
         });

     }

    private void sendOTP(String mobile_number, String message, String otp){
        mProgressDialog.show();
        Call<OTPDataClass> call = otp_apiService.OTPSend(Constants.AUTH_KEY, mobile_number, message , "HomeMaid", otp );
        call.enqueue(new Callback<OTPDataClass>() {
            @Override
            public void onResponse(Call<OTPDataClass> call, Response<OTPDataClass> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                    reverseTimer(120, tv_timer);
            }

            @Override
            public void onFailure(Call<OTPDataClass> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                    showCustomToast(pin_view, getString(R.string.err_message_retrofit));
            }
        });

    }

    private void verifyOTP(String mobile_number, String otp){

        mProgressDialog.show();
        Call<OTPDataClass> call = otp_apiService.OTPVerify(Constants.AUTH_KEY, mobile_number, otp );
        call.enqueue(new Callback<OTPDataClass>() {
            @Override
            public void onResponse(Call<OTPDataClass> call, Response<OTPDataClass> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                    if (response.body().getMessage().equalsIgnoreCase("otp_verified")){
                        validateLogin();
                    }else
                        showCustomToast(pin_view , "OTP mismatch");
            }

            @Override
            public void onFailure(Call<OTPDataClass> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                showCustomToast(pin_view, getString(R.string.err_message_retrofit));
            }
        });


    }

    public void reverseTimer(int Seconds, final TextView tv_timer) {

        new CountDownTimer(Seconds * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);

                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);
                tv_timer.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                tv_timer.setText("Resend OTP?");
                tv_timer.setEnabled(true);

            }
        }.start();
    }
}
