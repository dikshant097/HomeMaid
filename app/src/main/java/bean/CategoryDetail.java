
package bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CategoryDetail implements Parcelable
{

    private String id;
    private String name;
    private String image;
    private String colorCode;
    public final static Creator<CategoryDetail> CREATOR = new Creator<CategoryDetail>() {


        @SuppressWarnings({
            "unchecked"
        })
        public CategoryDetail createFromParcel(Parcel in) {
            CategoryDetail instance = new CategoryDetail();
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.image = ((String) in.readValue((String.class.getClassLoader())));
            instance.colorCode = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public CategoryDetail[] newArray(int size) {
            return (new CategoryDetail[size]);
        }

    }
    ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(image);
        dest.writeValue(colorCode);
    }

    public int describeContents() {
        return  0;
    }

}
