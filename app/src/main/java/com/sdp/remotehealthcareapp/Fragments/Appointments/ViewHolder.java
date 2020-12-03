package com.sdp.remotehealthcareapp.Fragments.Appointments;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sdp.remotehealthcareapp.R;
import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView cat_name,doc_name,doc_speaks,doc_cat,doc_exp,time,App_Time,App_Date,App_Dr,App_Clinic,App_Pat;
    public ImageView cat_image,doc_image,App_Photo;


    public ViewHolder(View itemView) {
        super(itemView);
        cat_image= itemView.findViewById(R.id.cat_Image);
        cat_name=itemView.findViewById(R.id.cat_Name);
        doc_name=itemView.findViewById(R.id.doc_Name);
        doc_exp=itemView.findViewById(R.id.doc_Exp);
        doc_image=itemView.findViewById(R.id.doc_Image);
        doc_speaks=itemView.findViewById(R.id.doc_Speaks);
        time=itemView.findViewById(R.id.text_time);
        App_Time= itemView.findViewById(R.id.confirm_booked_time);
        //App_Date=itemView.findViewById(R.id.confirm_booked_date);
        App_Dr= itemView.findViewById(R.id.confirm_booked_name);
        App_Clinic=itemView.findViewById(R.id.confirm_booked_clinic);
        App_Photo=itemView.findViewById(R.id.confirm_booked_image);
        App_Pat=itemView.findViewById(R.id.confirm_booked_patient_name);

    }
    public TextView gettime() { return time;}
    public TextView getCat_name() {
        return cat_name;
    }

    public void setCat_name(String string) {
       cat_name.setText(string);
    }

    public void settime(String str)
    {
        time.setText(str);
    }
    public ImageView getCat_image() {
        return cat_image;
    }

    public void setCat_image(String url) {
        Picasso.get().load(url).error(R.drawable.ic_launcher_background).into(cat_image);
    }



    public TextView getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String string) {
       doc_name.setText(string);
    }

    public TextView getDoc_speaks() {
        return doc_speaks;
    }

    public void setDoc_speaks(String string) {
        doc_speaks.setText("Speaks:-"+string);
    }

    public ImageView getDoc_image() {
        return doc_image;
    }

    public void setDoc_image(String url) {
        Picasso.get().load(url).error(R.drawable.ic_launcher_background).into(doc_image);
    }

    public TextView getDoc_cat() {
        return doc_cat;
    }

    public void setDoc_cat(String string) {
        this.doc_cat = doc_cat;
    }

    public TextView getDoc_exp() {
        return doc_exp;
    }

    public void setDoc_exp(String string) {
        doc_exp.setText(string+" Years");
    }
    public TextView getApp_Time() {
        return App_Time;
    }

    public void setApp_Time(String app_Time) {
        App_Time.setText(app_Time);
    }

    public TextView getApp_Date() {
        return App_Date;
    }

    public void setApp_Date(String app_Date) {
        App_Date.setText(app_Date);
    }

    public TextView getApp_Dr() {
        return App_Dr;
    }

    public void setApp_Dr(String app_Dr) {
        App_Dr.setText(app_Dr);
    }

    public TextView getApp_Clinic() {
        return App_Clinic;
    }

    public void setApp_Clinic(String app_Clinic) {
        App_Clinic.setText(app_Clinic);
    }

    public ImageView getApp_Photo() {
        return App_Photo;
    }

    public void setApp_Photo(String app_Photo) {
        Picasso.get().load(app_Photo).error(R.drawable.ic_launcher_background).into(App_Photo);
    }
    public void setApp_Pat(String app_pat) {
        App_Pat.setText(app_pat);
    }

    public TextView getApp_Pat() {
        return App_Pat;
    }
}