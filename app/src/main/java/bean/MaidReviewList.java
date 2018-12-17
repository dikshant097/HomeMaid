
package bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MaidReviewList implements Parcelable
{

    private int rating;
    private String review;
    private long date;
    private String userName;
    private String userPhoto;
    public final static Creator<MaidReviewList> CREATOR = new Creator<MaidReviewList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public MaidReviewList createFromParcel(Parcel in) {
            MaidReviewList instance = new MaidReviewList();
            instance.rating = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.review = ((String) in.readValue((String.class.getClassLoader())));
            instance.date = ((Long) in.readValue((String.class.getClassLoader())));
            instance.userName = ((String) in.readValue((String.class.getClassLoader())));
            instance.userPhoto = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public MaidReviewList[] newArray(int size) {
            return (new MaidReviewList[size]);
        }

    }
    ;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(rating);
        dest.writeValue(review);
        dest.writeValue(date);
        dest.writeValue(userName);
        dest.writeValue(userPhoto);
    }

    public int describeContents() {
        return  0;
    }

}
