package com.example.healthcareapp.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Fragments.Fragment_baiviet1;
import com.example.healthcareapp.Model.IngredientData;
import com.example.healthcareapp.R;

import java.util.ArrayList;
import java.util.Arrays;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private Context context;
    private ArrayList<IngredientData> items;
    public IngredientAdapter(Context activity, ArrayList<IngredientData> items)
    {
        this.items = items;
        this.context = activity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(view);
    }
    ArrayList<String> dataList = new ArrayList<>(Arrays.asList("g","kg","ml","l","cup","tsp","tbsp","piece","pieces"));
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(!items.get(position).unit.equals("")){
        String desiredItem = items.get(position).unit;
        int desiredPosition = dataList.indexOf(desiredItem);
        holder.spinner.setSelection(desiredPosition);
        }
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position1, long id) {
                String selectedItem = adapterView.getItemAtPosition(position1).toString();
                items.get(position).unit = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
//                if(items.get(position).unit.equals("")) {
//                    items.get(position).unit = "g";
//                    System.out.println("1");
//                }
//                String desiredItem = items.get(position).unit;
//                System.out.println("i "+desiredItem);
//                int desiredPosition = dataList.indexOf(desiredItem);
//                System.out.println("p "+ desiredPosition);
//                holder.spinner.setSelection(desiredPosition);
            }
        });
        holder.name.setText(items.get(position).name);
        holder.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                items.get(position).name = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.wty.setText(items.get(position).wty);
        holder.wty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                items.get(position).wty = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_baiviet1.listIdata.remove(items.get(position));
                IngredientAdapter adapter = new IngredientAdapter(context, Fragment_baiviet1.listIdata);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                Fragment_baiviet1.listI.setLayoutManager(linearLayoutManager);
                Fragment_baiviet1.listI.setItemAnimator(new DefaultItemAnimator());
                Fragment_baiviet1.listI.setAdapter(adapter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Spinner spinner;
        EditText name, wty;
        ImageButton remove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            spinner = itemView.findViewById(R.id.spinner);
            name = itemView.findViewById(R.id.nameI);
            wty = itemView.findViewById(R.id.wty);
            remove = itemView.findViewById(R.id.removeI);
        }
    }

//    @Override
//    public int getCount() {
//        return items.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return items;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//
//        LayoutInflater inflater = activity.getLayoutInflater();
//        view = inflater.inflate(R.layout.ingredient_item,null);
//        Spinner spinner = view.findViewById(R.id.spinner);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                String selectedItem = adapterView.getItemAtPosition(position).toString();
//                // Do something with the selected item
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                // Handle the case when nothing is selected
//            }
//        });
//        return view;
//    }
}
