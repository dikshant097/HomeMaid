
package bean;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class AllUsersRatingDataClass implements Parcelable
{

    private String status;
    private List<MaidReviewList> maidReviewList = null;
    public final static Creator<AllUsersRatingDataClass> CREATOR = new Creator<AllUsersRatingDataClass>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AllUsersRatingDataClass createFromParcel(Parcel in) {
            AllUsersRatingDataClass instance = new AllUsersRatingDataClass();
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.maidReviewList, (MaidReviewList.class.getClassLoader()));
            return instance;
        }

        public AllUsersRatingDataClass[] newArray(int size) {
            return (new AllUsersRatingDataClass[size]);
        }

    }
    ;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MaidReviewList> getMaidReviewList() {
        return maidReviewList;
    }

    public void setMaidReviewList(List<MaidReviewList> maidReviewList) {
        this.maidReviewList = maidReviewList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeList(maidReviewList);
    }

    public int describeContents() {
        return  0;
    }

}
