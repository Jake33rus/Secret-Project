package com.example.jake.university.exams;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jake.university.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class ArrearsAdapter extends ArrayAdapter<ExamItem> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<ExamItem> itemList;

    public ArrearsAdapter(@NonNull Context context, int resource, ArrayList<ExamItem> items) {
        super(context, resource, items);
        this.itemList = items;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ExamItem item = itemList.get(position);

        viewHolder.disciplineTV.setText(item.getDiscipline());
        viewHolder.semestrTV.setText(item.getSemestr());
        viewHolder.typeTV.setText(item.getType());
        return convertView;
    }

    private class ViewHolder {
        final TextView disciplineTV, semestrTV, typeTV;
        ViewHolder(View view){
            disciplineTV = (TextView) view.findViewById(R.id.disciplineArrears);
            semestrTV = (TextView) view.findViewById(R.id.semestrArrears);
            typeTV = (TextView) view.findViewById(R.id.TypeArrears);
        }
    }
}
