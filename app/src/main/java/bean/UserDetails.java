
package bean;


import com.j256.ormlite.field.DatabaseField;

public class UserDetails {

    @DatabaseField
    private String fullName;
    @DatabaseField
    private String mobile;
    @DatabaseField
    private String email;
    @DatabaseField
    private String profilePic;
    @DatabaseField
    private String locality;
    @DatabaseField
    private String userId;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
