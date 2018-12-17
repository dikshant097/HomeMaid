package tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppSharedPreference {

	private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
	public static AppSharedPreference appSharedPreference;
	private SharedPreferences sharedPreferences;
	private Editor editor;

	private AppSharedPreference(Context context) {
		this.sharedPreferences = context.getSharedPreferences("User_detail",
				Context.MODE_PRIVATE);
		this.editor = sharedPreferences.edit();
	}
	public static AppSharedPreference getInstance(Context context) {
		if (appSharedPreference == null) {
			appSharedPreference = new AppSharedPreference(context);
		}
		return appSharedPreference;
	}

	public String getUserId() {
		return sharedPreferences.getString("user_id", "");
	}

	public void setUserId(String user_id) {
		editor.putString("user_id", user_id);
		editor.commit();
	}

	public String getFullName() {
		return sharedPreferences.getString("fullName", "");
	}

	public void setFullName(String fullName) {
		editor.putString("fullName", fullName);
		editor.commit();
	}

	public String getMobile() {
		return sharedPreferences.getString("mobile", "");
	}

	public void setMobile(String mobile) {
		editor.putString("mobile", mobile);
		editor.commit();
	}

	public String getEmail() {
		return sharedPreferences.getString("email", "");
	}

	public void setEmail(String email) {
		editor.putString("email", email);
		editor.commit();
	}

	public String getProfilePic() {
		return sharedPreferences.getString("profilePic", "");
	}

	public void setProfilePic(String email) {
		editor.putString("profilePic", email);
		editor.commit();
	}

	public String getLocality() {
		return sharedPreferences.getString("locality", "");
	}

	public void setLocality(String locality) {
		editor.putString("locality", locality);
		editor.commit();
	}

	public boolean getIsLogin() {

		return sharedPreferences.getBoolean("is_login", false);
	}

	public void setIsLogin(boolean b) {

		editor.putBoolean("is_login", b);
		editor.commit();
	}

	public void setFirstTimeLaunch(boolean isFirstTime) {
		editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
		editor.commit();
	}

	public boolean isFirstTimeLaunch() {
		return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
	}

	public void clearPreference() {
		editor.clear();
	}
}
