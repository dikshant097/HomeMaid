
package bean;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CategoryDataClass implements Parcelable
{

    private String status;
    private List<CategoryDetail> categoryDetails = null;
    public final static Creator<CategoryDataClass> CREATOR = new Creator<CategoryDataClass>() {


        @SuppressWarnings({
            "unchecked"
        })
        public CategoryDataClass createFromParcel(Parcel in) {
            CategoryDataClass instance = new CategoryDataClass();
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.categoryDetails, (CategoryDetail.class.getClassLoader()));
            return instance;
        }

        public CategoryDataClass[] newArray(int size) {
            return (new CategoryDataClass[size]);
        }

    }
    ;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CategoryDetail> getCategoryDetails() {
        return categoryDetails;
    }

    public void setCategoryDetails(List<CategoryDetail> categoryDetails) {
        this.categoryDetails = categoryDetails;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeList(categoryDetails);
    }

    public int describeContents() {
        return  0;
    }

}
