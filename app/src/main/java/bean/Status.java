
package bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Status implements Parcelable
{

    private String status;
    public final static Creator<Status> CREATOR = new Creator<Status>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Status createFromParcel(Parcel in) {
            Status instance = new Status();
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Status[] newArray(int size) {
            return (new Status[size]);
        }

    }
    ;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
    }

    public int describeContents() {
        return  0;
    }

}
