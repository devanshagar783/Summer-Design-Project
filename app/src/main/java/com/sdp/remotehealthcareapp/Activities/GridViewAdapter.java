package com.sdp.remotehealthcareapp.Activities;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sdp.remotehealthcareapp.R;

import java.util.List;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {

    List<String> titles;
    LayoutInflater inflater;
    ConstraintLayout prev,curr;
    ConstraintLayout cons;
    public static String time;

    public GridViewAdapter()
    {

    }
    public GridViewAdapter(Context ctx, List<String> titles){
        this.titles = titles;
        this.inflater = LayoutInflater.from(ctx);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_time,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_time);
            //cons = itemView.findViewById(R.id.cons);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), titles.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    setTime(titles.get(getAdapterPosition()));
                    prev = curr;
                    curr =  (ConstraintLayout) itemView;
                    int color_normal = ContextCompat.getColor(itemView.getContext(), R.color.Black);
                    int color_selected = ContextCompat.getColor(itemView.getContext(), R.color.colorBlue);
                    if (prev != null)
                        prev.setBackgroundColor(color_normal);
                    curr.setBackgroundColor(color_selected);
                }
            });
        }
    }
        public String getTime()
        {
            return this.time;
        }
        public void setTime(String time)
        {
            this.time= time;
        }

}