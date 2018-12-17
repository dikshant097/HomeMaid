
package bean;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MaidList implements Parcelable
{

    private String madeId;
    private String name;
    private String phoneNumber;
    private String workLocation;
    private String religion;
    private String cookingType;
    private String experience;
    private String photo;
    private String verification;
    private String workTime;
    private String cost;
    private float rating;
    private String workStyle;
    private String localAddress;
    private List<MaidCostList> maidCostList = new ArrayList<>();
    private String workingFlat;
    private String isFavourite;
    private int maidRatingCount;
    private String age;
    private String maidVerificationImage;
    public final static Creator<MaidList> CREATOR = new Creator<MaidList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public MaidList createFromParcel(Parcel in) {
            MaidList instance = new MaidList();
            instance.madeId = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.phoneNumber = ((String) in.readValue((String.class.getClassLoader())));
            instance.workLocation = ((String) in.readValue((String.class.getClassLoader())));
            instance.religion = ((String) in.readValue((String.class.getClassLoader())));
            instance.cookingType = ((String) in.readValue((String.class.getClassLoader())));
            instance.experience = ((String) in.readValue((String.class.getClassLoader())));
            instance.photo = ((String) in.readValue((String.class.getClassLoader())));
            instance.verification = ((String) in.readValue((String.class.getClassLoader())));
            instance.workTime = ((String) in.readValue((String.class.getClassLoader())));
            instance.cost = ((String) in.readValue((String.class.getClassLoader())));
            instance.rating = ((Float) in.readValue((Float.class.getClassLoader())));
            instance.workStyle = ((String) in.readValue((String.class.getClassLoader())));
            instance.localAddress = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.maidCostList, (MaidCostList.class.getClassLoader()));
            instance.workingFlat = ((String) in.readValue((String.class.getClassLoader())));
            instance.isFavourite = ((String) in.readValue((String.class.getClassLoader())));
            instance.age = ((String) in.readValue((String.class.getClassLoader())));
            instance.maidRatingCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.maidVerificationImage = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public MaidList[] newArray(int size) {
            return (new MaidList[size]);
        }

    }
    ;

    public String getMadeId() {
        return madeId;
    }

    public void setMadeId(String madeId) {
        this.madeId = madeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getCookingType() {
        return cookingType;
    }

    public void setCookingType(String cookingType) {
        this.cookingType = cookingType;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getWorkStyle() {
        return workStyle;
    }

    public void setWorkStyle(String workStyle) {
        this.workStyle = workStyle;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public List<MaidCostList> getMaidCostList() {
        return maidCostList;
    }

    public void setMaidCostList(List<MaidCostList> maidCostList) {
        this.maidCostList = maidCostList;
    }

    public String getWorkingFlat() {
        return workingFlat;
    }

    public void setWorkingFlat(String workingFlat) {
        this.workingFlat = workingFlat;
    }

    public String getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        this.isFavourite = isFavourite;
    }

    public int getMaidRatingCount() {
        return maidRatingCount;
    }

    public void setMaidRatingCount(int maidRatingCount) {
        this.maidRatingCount = maidRatingCount;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMaidVerificationImage() {
        return maidVerificationImage;
    }

    public void setMaidVerificationImage(String maidVerificationImage) {
        this.maidVerificationImage = maidVerificationImage;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(madeId);
        dest.writeValue(name);
        dest.writeValue(phoneNumber);
        dest.writeValue(workLocation);
        dest.writeValue(religion);
        dest.writeValue(cookingType);
        dest.writeValue(experience);
        dest.writeValue(photo);
        dest.writeValue(verification);
        dest.writeValue(workTime);
        dest.writeValue(cost);
        dest.writeValue(rating);
        dest.writeValue(workStyle);
        dest.writeValue(localAddress);
        dest.writeList(maidCostList);
        dest.writeValue(workingFlat);
        dest.writeValue(isFavourite);
        dest.writeValue(age);
        dest.writeValue(maidRatingCount);
        dest.writeValue(maidVerificationImage);
    }

    public int describeContents() {
        return  0;
    }

}
