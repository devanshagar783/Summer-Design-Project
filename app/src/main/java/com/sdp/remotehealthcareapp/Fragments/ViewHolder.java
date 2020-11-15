package com.sdp.remotehealthcareapp.Fragments;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sdp.remotehealthcareapp.R;
import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView cat_name;
    public ImageView cat_image;

    public TextView doc_name;
    public TextView doc_speaks;
    public ImageView doc_image;
    public TextView doc_cat;
    public TextView doc_exp;
    public TextView time;

    public ViewHolder(View itemView) {
        super(itemView);
        cat_image= itemView.findViewById(R.id.cat_Image);
        cat_name=itemView.findViewById(R.id.cat_Name);
        doc_name=itemView.findViewById(R.id.doc_Name);
        doc_exp=itemView.findViewById(R.id.doc_Exp);
        doc_image=itemView.findViewById(R.id.doc_Image);
        doc_speaks=itemView.findViewById(R.id.doc_Speaks);
        time=itemView.findViewById(R.id.text_time);

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
}