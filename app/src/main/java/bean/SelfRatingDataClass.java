
package bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SelfRatingDataClass implements Parcelable
{

    private String status;
    private String rating;
    private String review;
    public final static Creator<SelfRatingDataClass> CREATOR = new Creator<SelfRatingDataClass>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SelfRatingDataClass createFromParcel(Parcel in) {
            SelfRatingDataClass instance = new SelfRatingDataClass();
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.rating = ((String) in.readValue((String.class.getClassLoader())));
            instance.review = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public SelfRatingDataClass[] newArray(int size) {
            return (new SelfRatingDataClass[size]);
        }

    }
    ;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(rating);
        dest.writeValue(review);
    }

    public int describeContents() {
        return  0;
    }

}
