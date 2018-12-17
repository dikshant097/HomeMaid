package com.rsi.homemaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import bean.Login;
import bean.Registration;
import bean.UserDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.AppSharedPreference;
import tools.HelperMethods;

/**
 * Created by deepak.sharma on 7/12/2017.
 */

public class SignUpActivity extends BaseLoginActivity {

    private ImageView iv_back;
    private Button btn_next;
    private EditText et_mobile_number, et_email_address, et_full_name;

    private String photo_url = "";
    private String loginType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        et_mobile_number = (EditText) findViewById(R.id.et_mobile_number);
        et_email_address = (EditText) findViewById(R.id.et_email_address);
        et_full_name = (EditText) findViewById(R.id.et_full_name);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        btn_next = (Button) findViewById(R.id.btn_next);

        if(getIntent().getExtras() != null){
            if (getIntent().getExtras().getString("LoginType") != null)
                loginType = getIntent().getExtras().getString("LoginType");
            if (getIntent().getExtras().getString("Mobile_Number") != null)
            et_mobile_number.setText(getIntent().getExtras().getString("Mobile_Number"));
            if (getIntent().getExtras().getString("Email") != null)
            et_email_address.setText(getIntent().getExtras().getString("Email"));
            if (getIntent().getExtras().getString("Full Name") != null)
            et_full_name.setText(getIntent().getExtras().getString("Full Name"));
            if (getIntent().getExtras().getString("Photo_url") != null){
                photo_url = getIntent().getExtras().getString("Photo_url");
            }else {
                photo_url = "";
            }
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!HelperMethods.isNetworkConnected(SignUpActivity.this)){
                    showCustomToast(et_mobile_number, "No internet Connection !");
                    return;
                }
            if (validateFields()){
                Call<Login> call = apiService.getRegistration(getRegistrationJson(et_mobile_number.getText().toString(), et_email_address.getText().toString(), et_full_name.getText().toString(), photo_url, "RG",loginType, HelperMethods.getDeviceId(SignUpActivity.this)).toString());
                mProgressDialog.show();
                call.enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {
                        if(mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        if (response.body().getStatus().equalsIgnoreCase("success")){
                            dbHelper.deleteUserDetails();
                            dbHelper.insertUserDetails(response.body().getUserDetails());
                            AppSharedPreference.getInstance(SignUpActivity.this).setIsLogin(true);
                            Intent mIntent = new Intent(SignUpActivity.this, HomeActivity.class);
                            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mIntent);
                            finish();
                        }else {
                            Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {
                        if(mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                            showCustomToast(et_email_address, getString(R.string.err_message_retrofit));
                    }
                });
            }
            }
        });
    }

    private boolean validateFields() {
        if (et_email_address.getText().toString().trim().isEmpty()) {
            Toast.makeText(SignUpActivity.this, R.string.err_msg_email, Toast.LENGTH_SHORT).show();
            requestFocus(et_mobile_number);
            return false;
        }else if (!isValidEmailId(et_email_address.getText().toString().trim())){
            Toast.makeText(SignUpActivity.this, R.string.err_msg_email_valid, Toast.LENGTH_SHORT).show();
            requestFocus(et_mobile_number);
            return false;
        }
        if (et_full_name.getText().toString().trim().isEmpty()){
            Toast.makeText(SignUpActivity.this, R.string.err_msg_full_name, Toast.LENGTH_SHORT).show();
            requestFocus(et_mobile_number);
            return false;
        }
        return true;
    }

    private boolean isValidEmailId(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    private JSONArray getRegistrationJson(String mobile, String email, String fullName, String profile_pic, String locality, String loginType, String deviceId) {
        JSONObject paramObject = new JSONObject();
        JSONArray jsonArray =new JSONArray();
        try {
            paramObject.put("mobile", mobile);
            paramObject.put("email", email);
            paramObject.put("fullName", fullName);
            paramObject.put("profilePic", profile_pic);
            paramObject.put("locality", locality);
            paramObject.put("loginType", loginType);
            paramObject.put("deviceId", deviceId);
            jsonArray.put(paramObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
