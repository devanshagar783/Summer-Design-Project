package com.sdp.remotehealthcareapp.Fragments;

public class Dataclass_Doctors {
    String Name;
    String Category;
    String photoURL;
    String Speaks;
    String Experience;
    String Time;
    Dataclass_Doctors()
    {

    }
    Dataclass_Doctors(String Name, String Category, String photoURL, String Speaks, String Experience){
        this.Name= Name;
        this.Category=Category;
        this.photoURL= photoURL;
        this.Experience= Experience;
        this.Speaks= Speaks;
    }

    public String getName() {
        return Name;
    }

    public String getCategory() {
        return Category;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getTime() {
        return Time;
    }

    public String getSpeaks() {
        return Speaks;
    }

    public String getExperience() {
        return Experience;
    }
}
