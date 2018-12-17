
package bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MaidReviewDataClass implements Parcelable
{

    private String status;
    private Float averageRating;
    private Integer maidRatingCount;
    public final static Creator<MaidReviewDataClass> CREATOR = new Creator<MaidReviewDataClass>() {


        @SuppressWarnings({
            "unchecked"
        })
        public MaidReviewDataClass createFromParcel(Parcel in) {
            MaidReviewDataClass instance = new MaidReviewDataClass();
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.averageRating = ((Float) in.readValue((Float.class.getClassLoader())));
            instance.maidRatingCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
            return instance;
        }

        public MaidReviewDataClass[] newArray(int size) {
            return (new MaidReviewDataClass[size]);
        }

    }
    ;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Float averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getMaidRatingCount() {
        return maidRatingCount;
    }

    public void setMaidRatingCount(Integer maidRatingCount) {
        this.maidRatingCount = maidRatingCount;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(averageRating);
        dest.writeValue(maidRatingCount);
    }

    public int describeContents() {
        return  0;
    }

}
