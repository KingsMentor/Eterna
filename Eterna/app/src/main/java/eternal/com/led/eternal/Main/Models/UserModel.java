package eternal.com.led.eternal.Main.Models;

/**
 * Created by CrowdStar on 2/18/2015.
 */
public class UserModel {
    private String name;
    private String imageUrl;
    private String email;
    private String phone;

    public void setName(String name) {
        this.name = name;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
