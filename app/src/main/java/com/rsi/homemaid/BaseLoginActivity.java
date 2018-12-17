package com.rsi.homemaid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bean.Login;
import bean.Registration;
import database.DatabaseHelper;
import retrofit.ApiClient;
import retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.AppSharedPreference;

/**
 * Created by deepak.sharma on 7/12/2017.
 */

public class BaseLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    protected ApiInterface apiService;
    protected CallbackManager callbackManager;

    protected static final int RC_SIGN_IN = 007;
    protected GoogleApiClient mGoogleApiClient;
    protected static final String TAG = BaseLoginActivity.class.getSimpleName();

    protected ProgressDialog mProgressDialog;
    protected DatabaseHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);

        dbHelper = DatabaseHelper.getInstance(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
      /*  OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }*/
    }

    protected void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }



    protected void getFacebookUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {

                        String email = null, name = null, photo_url = null;
                        try {
                            email = json_object.get("email").toString();
                            name = json_object.get("name").toString();

                            json_object = json_object.getJSONObject("picture");
                            json_object= json_object.getJSONObject("data");
                            photo_url = json_object.get("url").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Call<Login> call = apiService.loginValidation(getLoginJson("NA", email, photo_url, "FB").toString());
                        final String finalEmail = email;
                        final String finalName = name;
                        final String finalPhoto_url = photo_url;
                        call.enqueue(new Callback<Login>() {
                            @Override
                            public void onResponse(Call<Login> call, Response<Login> response) {
                                if (response.body().getStatus().equalsIgnoreCase("success")){
                                    setUserDetails(response);
                                    dbHelper.deleteUserDetails();
                                    dbHelper.insertUserDetails(response.body().getUserDetails());
                                    AppSharedPreference.getInstance(BaseLoginActivity.this).setIsLogin(true);
                                    startActivity(new Intent(BaseLoginActivity.this, HomeActivity.class));
                                    finish();
                                }else if (response.body().getStatus().equalsIgnoreCase("failed")){
                                    Intent mIntent = new Intent(BaseLoginActivity.this, SignUpActivity.class);
                                    mIntent.putExtra("Email", finalEmail);
                                    mIntent.putExtra("Full Name", finalName);
                                    mIntent.putExtra("Photo_url", finalPhoto_url);
                                    mIntent.putExtra("LoginType", "FB");
                                    startActivity(mIntent);
                                }
                            }

                            @Override
                            public void onFailure(Call<Login> call, Throwable t) {
                                Toast.makeText(BaseLoginActivity.this,"Failure", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
    }

    String personPhotoUrl = "";
    public void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            final String personName = acct.getDisplayName();
            final String email = acct.getEmail();
            if (acct.getPhotoUrl() != null)
                personPhotoUrl = acct.getPhotoUrl().toString();

            Call<Login> call = apiService.loginValidation(getLoginJson("NA", email, personPhotoUrl, "Google").toString());

            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    if (response.body().getStatus().equalsIgnoreCase("success")){
                        setUserDetails(response);
                        dbHelper.deleteUserDetails();
                        dbHelper.insertUserDetails(response.body().getUserDetails());
                        AppSharedPreference.getInstance(BaseLoginActivity.this).setIsLogin(true);
                        startActivity(new Intent(BaseLoginActivity.this, HomeActivity.class));
                        finish();
                    }else if (response.body().getStatus().equalsIgnoreCase("failed")){
                        Intent mIntent = new Intent(BaseLoginActivity.this, SignUpActivity.class);
                        mIntent.putExtra("Email", email);
                        mIntent.putExtra("Full Name", personName);
                        mIntent.putExtra("Photo_url", personPhotoUrl);
                        mIntent.putExtra("LoginType", "Google");
                        startActivity(mIntent);
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    Toast.makeText(BaseLoginActivity.this,"Failure", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    protected JSONArray getLoginJson(String mobile, String email, String profilePic, String loginType) {
        JSONObject paramObject = new JSONObject();
        JSONArray jsonArray =new JSONArray();
        try {
            paramObject.put("mobile", mobile);
            paramObject.put("email", email);
            paramObject.put("profilePic", profilePic);
            paramObject.put("loginType", loginType);
            jsonArray.put(paramObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    protected void setUserDetails(Response<Login> response){

        AppSharedPreference.getInstance(BaseLoginActivity.this).setUserId(response.body().getUserDetails().getUserId());
        AppSharedPreference.getInstance(BaseLoginActivity.this).setFullName(response.body().getUserDetails().getFullName());
        AppSharedPreference.getInstance(BaseLoginActivity.this).setMobile(response.body().getUserDetails().getMobile());
        AppSharedPreference.getInstance(BaseLoginActivity.this).setEmail(response.body().getUserDetails().getEmail());
        AppSharedPreference.getInstance(BaseLoginActivity.this).setProfilePic(response.body().getUserDetails().getProfilePic());
        AppSharedPreference.getInstance(BaseLoginActivity.this).setLocality(response.body().getUserDetails().getLocality());
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
