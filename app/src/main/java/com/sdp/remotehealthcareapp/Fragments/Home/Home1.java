package com.sdp.remotehealthcareapp.Fragments.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.sdp.remotehealthcareapp.R;

public class Home1 extends Fragment {

    View v;
    String[] CategoryNames = {"Cough", "Backpain", "Fever", "Hairfall","Headache","Acidity", "Stomach ache"};
    int[] CategoryImages = {R.drawable.cough, R.drawable.backpain, R.drawable.fever, R.mipmap.hairfall, R.drawable.headache,
                            R.drawable.acidity,R.drawable.stomachpain};

    public Home1() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_home1, container, false);
        GridView gridView;


        //finding listview
        gridView = v.findViewById(R.id.gridview);

        CustomHomeGrid customGrid = new CustomHomeGrid(getActivity(), CategoryNames, CategoryImages);
        gridView.setAdapter(customGrid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), CategoryNames[position], Toast.LENGTH_SHORT).show();
                /*getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                        new Selected_Category_Fragment(CategoryNames[position])).addToBackStack("Selected Fragment").commit();
                //startActivity(new Intent(getActivity(), Selected_Category_Fragment.class));*/
            }

        });
        return v;
    }

}