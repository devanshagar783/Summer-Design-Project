package com.sdp.remotehealthcareapp.Fragments.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdp.remotehealthcareapp.R;

public class CustomHomeGrid extends BaseAdapter {
    private Context mContext;
    private final String[] categories;
    private final int[] Imageid;

    public CustomHomeGrid(Context c, String[] categories, int[] Imageid ) {
        mContext = c;
        this.Imageid = Imageid;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return categories.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid= new View(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = inflater.inflate(R.layout.grid_category_items, null);
            TextView textView = (TextView) grid.findViewById(R.id.category_name);
            ImageView imageView = (ImageView)grid.findViewById(R.id.category_image);
            textView.setText(categories[position]);
            imageView.setImageResource(Imageid[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
