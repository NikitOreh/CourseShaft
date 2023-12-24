package com.example.courseapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<ListItemClass> implements Filterable {
    private LayoutInflater inflater;
    private List<ListItemClass> listItem;
    private List<ListItemClass> filteredList;



    public CustomArrayAdapter(@NonNull Context context, int resource, List<ListItemClass> listItem, LayoutInflater inflater) {
        super(context, resource, listItem);
        this.inflater = inflater;
        this.listItem = listItem;
        this.filteredList = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        ListItemClass listItemMain = listItem.get(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_item_1, null, false);
            viewHolder = new ViewHolder();
            viewHolder.data1 = convertView.findViewById(R.id.tvData1);
            viewHolder.data2 = convertView.findViewById(R.id.tvData2);
            viewHolder.data3 = convertView.findViewById(R.id.tvData3);
            viewHolder.data4 = convertView.findViewById(R.id.tvData4);
            convertView.setTag(viewHolder);



        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.data1.setText(listItemMain.getData_1());
        viewHolder.data2.setText(listItemMain.getData_2());
        viewHolder.data3.setText(listItemMain.getData_3());
        viewHolder.data4.setText(listItemMain.getData_4());








        return convertView;
    }
    @NonNull
    @Override
    public android.widget.Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.d("Filtering", "Filtering started");
                FilterResults results = new FilterResults();
                List<ListItemClass> filtered = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    Log.d("Filtering", "No constraint, showing all items");

                    filtered.addAll(listItem);
                } else {
                    Log.d("Filtering", "Filtering with constraint: " + constraint.toString());

                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (ListItemClass item : listItem) {
                        // Ищем совпадения в каждом поле данных
                        if (item.getData_1().toLowerCase().contains(filterPattern) ||
                                item.getData_2().toLowerCase().contains(filterPattern) ||
                                item.getData_3().toLowerCase().contains(filterPattern) ||
                                item.getData_4().toLowerCase().contains(filterPattern)) {
                            filtered.add(item);
                        }
                    }
                }

                results.values = filtered;
                results.count = filtered.size();
                Log.d("Filtering", "Filtered count: " + results.count);
                return results;
            }


            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listItem.clear();
                if (results != null && results.count > 0) {
                    listItem.addAll((List<ListItemClass>) results.values);
                }
                notifyDataSetChanged();
            }


        };
    }

    private class ViewHolder{
        TextView data1;
        TextView data2;
        TextView data3;
        TextView data4;


    }
}