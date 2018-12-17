
package bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MaidCostList implements Parcelable
{

    private String categoryId;
    private String cost;
    private String categoryName;
    public final static Creator<MaidCostList> CREATOR = new Creator<MaidCostList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public MaidCostList createFromParcel(Parcel in) {
            MaidCostList instance = new MaidCostList();
            instance.categoryId = ((String) in.readValue((String.class.getClassLoader())));
            instance.cost = ((String) in.readValue((String.class.getClassLoader())));
            instance.categoryName = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public MaidCostList[] newArray(int size) {
            return (new MaidCostList[size]);
        }

    }
    ;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(categoryId);
        dest.writeValue(cost);
        dest.writeValue(categoryName);
    }

    public int describeContents() {
        return  0;
    }

}
