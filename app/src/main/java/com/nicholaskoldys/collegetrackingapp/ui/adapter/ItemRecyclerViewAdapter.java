package com.nicholaskoldys.collegetrackingapp.ui.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nicholaskoldys.collegetrackingapp.R;
import com.nicholaskoldys.collegetrackingapp.model.CollegeBase;

import java.util.List;

public class ItemRecyclerViewAdapter<T extends CollegeBase> extends BaseItemRecyclerViewAdapter<T> {

    private List<T> displayData;
    private LayoutInflater layoutInflater;
    private ListItemClickListener listItemClickListener;
    private int selectedPosition = -1;

    private GradientDrawable selectedBorder;
    private GradientDrawable originalBackground;


    public ItemRecyclerViewAdapter(Context context, ListItemClickListener listItemClickListener) {

        layoutInflater = LayoutInflater.from(context);
        this.listItemClickListener = listItemClickListener;

        selectedBorder = new GradientDrawable();
        selectedBorder.setStroke(2, 0xFFD81B60);
        selectedBorder.setCornerRadius(5);

        originalBackground = new GradientDrawable();
        originalBackground.setColor(0x42000000);
    }

    @NonNull
    @Override
    public BaseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View individualItemView = layoutInflater
                .inflate(R.layout.list_individualitem, parent, false);

        return new ItemViewHolder(individualItemView, listItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseItemRecyclerViewAdapter.BaseItemViewHolder holder, int position) {

        if(displayData != null) {
            holder.bindData(displayData.get(position));
            if(position == selectedPosition) {
                holder.itemView.setBackground(selectedBorder);
            } else {
                holder.itemView.setBackground(originalBackground);
            }
        } else {
            holder.bindData(null);
        }
    }

    @Override
    public int getItemCount() {

        if(displayData != null) {
            return displayData.size();
        } else {
            return 0;
        }
    }

    @Override
    public void setDisplayData(List<T> items) {
        displayData = items;
    }

    class ItemViewHolder extends BaseItemViewHolder implements View.OnClickListener {

        TextView listIndividualItemTextView;
        ListItemClickListener listItemClicklistener;

        ItemViewHolder(@NonNull View itemView, ListItemClickListener listItemClickListener) {
            super(itemView);
            listIndividualItemTextView = itemView.findViewById(R.id.listIndividualItemTextView);
            this.listItemClicklistener = listItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        void bindData(T item) {

            if(item != null) {
                listIndividualItemTextView.setText(item.toString());
            } else {
                listIndividualItemTextView.setText("EMPTY");
            }
        }

        @Override
        public void onClick(View v) {

            int pos = getAdapterPosition();
            listItemClicklistener.onListItemClick(displayData.get(pos));
            selectedPosition = pos;
            notifyItemChanged(pos);
        }
    }
}
