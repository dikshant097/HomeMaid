
package bean;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class AdvertisementDataClass implements Parcelable
{

    private String status;
    private List<AdvertiseImage> advertiseImages = null;
    public final static Creator<AdvertisementDataClass> CREATOR = new Creator<AdvertisementDataClass>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AdvertisementDataClass createFromParcel(Parcel in) {
            AdvertisementDataClass instance = new AdvertisementDataClass();
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.advertiseImages, (bean.AdvertiseImage.class.getClassLoader()));
            return instance;
        }

        public AdvertisementDataClass[] newArray(int size) {
            return (new AdvertisementDataClass[size]);
        }

    }
    ;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AdvertiseImage> getAdvertiseImages() {
        return advertiseImages;
    }

    public void setAdvertiseImages(List<AdvertiseImage> advertiseImages) {
        this.advertiseImages = advertiseImages;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeList(advertiseImages);
    }

    public int describeContents() {
        return  0;
    }

}
