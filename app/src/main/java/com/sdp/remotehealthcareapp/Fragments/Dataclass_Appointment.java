package com.sdp.remotehealthcareapp.Fragments;

import android.widget.ImageView;

public class Dataclass_Appointment {
    String Category;
    String photo_cat;

    Dataclass_Appointment()
    {

    }
    Dataclass_Appointment( String Category, String photo_cat){
        this.Category=Category;
        this.photo_cat=photo_cat;

    }



    public String getCategory() {
        return Category;
    }



    public String getPhoto_cat() {
        return photo_cat;
    }


}
