package com.nicholaskoldys.collegetrackingapp.ui.adapter;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nicholaskoldys.collegetrackingapp.model.CollegeBase;

import java.util.List;

public abstract class BaseItemRecyclerViewAdapter<T extends CollegeBase> extends RecyclerView.Adapter<BaseItemRecyclerViewAdapter.BaseItemViewHolder> {

    public abstract void setDisplayData(List<T> items);

    public interface ListItemClickListener<T extends CollegeBase> {

        void onListItemClick(T item);
    }

    abstract class BaseItemViewHolder extends RecyclerView.ViewHolder {

        public BaseItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bindData(T item);
    }
}
