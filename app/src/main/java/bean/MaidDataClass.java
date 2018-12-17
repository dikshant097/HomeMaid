
package bean;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MaidDataClass implements Parcelable
{

    private String status;
    private List<MaidList> maidList = null;
    public final static Creator<MaidDataClass> CREATOR = new Creator<MaidDataClass>() {


        @SuppressWarnings({
            "unchecked"
        })
        public MaidDataClass createFromParcel(Parcel in) {
            MaidDataClass instance = new MaidDataClass();
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.maidList, (MaidList.class.getClassLoader()));
            return instance;
        }

        public MaidDataClass[] newArray(int size) {
            return (new MaidDataClass[size]);
        }

    }
    ;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MaidList> getMaidList() {
        return maidList;
    }

    public void setMaidList(List<MaidList> maidList) {
        this.maidList = maidList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeList(maidList);
    }

    public int describeContents() {
        return  0;
    }

}
