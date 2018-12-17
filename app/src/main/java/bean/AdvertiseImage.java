
package bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class AdvertiseImage implements Parcelable
{

    private String image;
    public final static Creator<AdvertiseImage> CREATOR = new Creator<AdvertiseImage>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AdvertiseImage createFromParcel(Parcel in) {
            AdvertiseImage instance = new AdvertiseImage();
            instance.image = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public AdvertiseImage[] newArray(int size) {
            return (new AdvertiseImage[size]);
        }

    }
    ;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(image);
    }

    public int describeContents() {
        return  0;
    }

}
