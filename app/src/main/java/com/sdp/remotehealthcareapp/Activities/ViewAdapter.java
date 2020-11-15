package com.sdp.remotehealthcareapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.sdp.remotehealthcareapp.R;

public class ViewAdapter extends PagerAdapter {
    private Context context;
    private MainActivity cont;
    private Integer[] images={R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.four };
    private int[] tagtext={R.string.one, R.string.two,R.string.three, R.string.four };
    ViewAdapter(Context context)
    {
        this.context=context;
    }
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view== object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        if (position == 4) {

            
            view = layoutInflater.inflate(R.layout.intro_login, null);
            view.findViewById(R.id.tagbutton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, PhoneAuthActivity.class));
                }
            });


        } else {
            view = layoutInflater.inflate(R.layout.item, null);
            ImageView imageView = view.findViewById(R.id.tagphoto);
            imageView.setImageResource(images[position]);
            TextView text = view.findViewById(R.id.tagline);
            text.setText(tagtext[position]);
        }
            ViewPager viewPager = (ViewPager) container;
            viewPager.addView(view, 0);
            return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager=(ViewPager) container;
        View view= (View) object;
        viewPager.removeView(view);
    }

}
