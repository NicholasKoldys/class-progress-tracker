package com.nicholaskoldys.collegetrackingapp.ui.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nicholaskoldys.collegetrackingapp.R;
import com.nicholaskoldys.collegetrackingapp.model.Notes;

import java.util.List;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {

    private List<Notes> notesDisplayData;
    private LayoutInflater layoutInflater;
    private NotesListClickListener notesListClickListener;
    private int selectedPosition = -1;

    private GradientDrawable selectedBorder;
    private GradientDrawable originalBackground;


    public NotesRecyclerViewAdapter(Context context, NotesListClickListener notesListClickListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.notesListClickListener = notesListClickListener;

        selectedBorder = new GradientDrawable();
        selectedBorder.setStroke(2, 0xFFD81B60);
        selectedBorder.setColor(0xFFdedede);
        selectedBorder.setCornerRadius(5);

        originalBackground = new GradientDrawable();
        originalBackground.setColor(0xFFB1B1B1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View notesIndividualView = layoutInflater.inflate(R.layout.list_individualitem_notes, parent, false);

        return new ViewHolder(notesIndividualView, notesListClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesRecyclerViewAdapter.ViewHolder holder, int position) {

        if(notesDisplayData != null) {
            holder.bindData(notesDisplayData.get(position));
            if (position == selectedPosition) {
                holder.itemView.setBackground(selectedBorder);
            } else {
                holder.itemView.setBackground(originalBackground);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(notesDisplayData != null) {
            return notesDisplayData.size();
        } else {
            return 0;
        }
    }

    public void setNotesDisplayData(List<Notes> notesDisplayData) {
        this.notesDisplayData = notesDisplayData;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView notesTextView;
        private NotesListClickListener notesListClickListener;

        ViewHolder(@NonNull View itemView, NotesListClickListener notesListClickListener) {
            super(itemView);

            notesTextView = itemView.findViewById(R.id.notesTextView);
            this.notesListClickListener = notesListClickListener;
            itemView.setOnClickListener(this);
        }

        void bindData(Notes note) {
            notesTextView.setText(note.getContent());
        }

        @Override
        public void onClick(View v) {

            int pos = getAdapterPosition();
            notesListClickListener.onNotesListClick(notesDisplayData.get(getAdapterPosition()));
            selectedPosition = pos;
            notifyItemChanged(pos);
        }
    }

    public interface NotesListClickListener {
        void onNotesListClick(Notes note);
    }
}
