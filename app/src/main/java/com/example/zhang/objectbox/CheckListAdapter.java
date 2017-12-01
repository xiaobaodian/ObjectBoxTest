package com.example.zhang.objectbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CheckListAdapter extends BaseAdapter {

    private List<CheckItem> dataset;

    private static class NoteViewHolder {

        public TextView text;
        public NoteViewHolder(View itemView) {
            text = (TextView) itemView.findViewById(R.id.textViewTitle);
        }
    }

    public CheckListAdapter() {
        this.dataset = new ArrayList<>();
    }

    public void setCheckList(List<CheckItem> checkList) {
        dataset = checkList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoteViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.check_item, parent, false);
            holder = new NoteViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (NoteViewHolder) convertView.getTag();
        }

        CheckItem item = getItem(position);
        holder.text.setText(item.getTitle());

        return convertView;
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public CheckItem getItem(int position) {
        return dataset.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
