package com.example.zhang.objectbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TasksAdapter extends BaseAdapter {

    private List<Task> dataset;

    private static class NoteViewHolder {

        public TextView text;
        public TextView comment;

        public NoteViewHolder(View itemView) {
            text = (TextView) itemView.findViewById(R.id.textViewNoteText);
            comment = (TextView) itemView.findViewById(R.id.textViewNoteComment);
        }
    }

    public TasksAdapter() {
        this.dataset = new ArrayList<>();
    }

    public void setTasks(List<Task> tasks) {
        dataset = tasks;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoteViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_note, parent, false);
            holder = new NoteViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (NoteViewHolder) convertView.getTag();
        }

        Task task = getItem(position);
        holder.text.setText(task.getTitle());
        holder.comment.setText(task.getComment());

        return convertView;
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public Task getItem(int position) {
        return dataset.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
