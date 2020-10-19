package com.sdp.remotehealthcareapp.Activities;

import com.google.firebase.database.Exclude;

public class Upload {
    private String imageName;
    private String imageUrl;
    private String mKey;

    public Upload() {

    }

    public Upload(String name, String url) {
        if (name.trim().equals(""))
            name = "No name";
        imageName = name;
        imageUrl = url;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Exclude
    public String getmKey() {
        return mKey;
    }

    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
