package com.example.liudingming.listenyourbrain.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.liudingming.listenyourbrain.*;
import com.example.liudingming.listenyourbrain.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class homePage extends Fragment {//主页面显示碎片
    private Button mode1;
    private Button mode2;
    public homePage() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view=inflater.inflate(R.layout.activity_mode3, container, false);
        mode1=(Button)view.findViewById(R.id.mode1);
        mode2=(Button)view.findViewById(R.id.mode2);
        mode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),mode1.class);
                startActivity(intent);
            }
        });
        mode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),mode2.class);
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }


}
