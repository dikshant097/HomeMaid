package retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiClient {

    public static final String BASE_URL = "http://homemade.hostzi.com/homemade/";
    public static final String OTP_URL = "http://smstxt.riteshinfotech.com/api/";
    private static Retrofit retrofit = null;
    private static Retrofit otp_retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getOTPClient() {
        if (otp_retrofit == null) {
            otp_retrofit = new Retrofit.Builder()
                    .baseUrl(OTP_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return otp_retrofit;
    }
}
