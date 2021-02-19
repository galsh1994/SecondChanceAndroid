package com.example.secondchance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


public class itemDetailsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_item_details, container, false);

        String item_id_arg = itemDetailsFragmentArgs.fromBundle(getArguments()).getItemId();
        String item_name_arg = itemDetailsFragmentArgs.fromBundle(getArguments()).getItemName();
        TextView item_name_text = view.findViewById(R.id.item_name);
        item_name_text.setText(item_name_arg);
        Log.d("TAG","item id is"+item_id_arg);

        return view;
    }
}