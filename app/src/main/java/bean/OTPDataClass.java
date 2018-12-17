
package bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class OTPDataClass implements Parcelable
{

    private String message;
    private String type;
    public final static Creator<OTPDataClass> CREATOR = new Creator<OTPDataClass>() {


        @SuppressWarnings({
            "unchecked"
        })
        public OTPDataClass createFromParcel(Parcel in) {
            OTPDataClass instance = new OTPDataClass();
            instance.message = ((String) in.readValue((String.class.getClassLoader())));
            instance.type = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public OTPDataClass[] newArray(int size) {
            return (new OTPDataClass[size]);
        }

    }
    ;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(message);
        dest.writeValue(type);
    }

    public int describeContents() {
        return  0;
    }

}
