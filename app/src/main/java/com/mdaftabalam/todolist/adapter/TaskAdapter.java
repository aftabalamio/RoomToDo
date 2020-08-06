package com.mdaftabalam.todolist.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mdaftabalam.todolist.R;
import com.mdaftabalam.todolist.model.Note;

import java.util.List;

/**
 * Created by Aftab Alam on 06-08-2020.
 **/
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.BeanHolder> {

    private List<Note> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnNoteItemClick onNoteItemClick;

    public TaskAdapter(List<Note> list, Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.onNoteItemClick = (OnNoteItemClick) context;
    }

    @Override
    public BeanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.view_list, parent, false);
        return new BeanHolder(view);
    }

    @Override
    public void onBindViewHolder(BeanHolder holder, int position) {
        Log.e("bind", "onBindViewHolder: " + list.get(position));
        holder.textViewTitle.setText(list.get(position).getTitle());
        holder.textViewContent.setText(list.get(position).getContent());
        holder.textDate.setText(list.get(position).getDate().toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnNoteItemClick {
        void onNoteClick(int pos);
    }

    public class BeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewContent, textViewTitle, textDate;

        public BeanHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewContent = itemView.findViewById(R.id.item_text);
            textViewTitle = itemView.findViewById(R.id.tv_title);
            textDate = itemView.findViewById(R.id.tv_date);
        }

        @Override
        public void onClick(View view) {
            onNoteItemClick.onNoteClick(getAdapterPosition());
        }
    }
}
