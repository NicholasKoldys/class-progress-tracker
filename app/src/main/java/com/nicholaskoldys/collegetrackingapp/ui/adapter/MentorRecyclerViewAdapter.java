package com.nicholaskoldys.collegetrackingapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nicholaskoldys.collegetrackingapp.R;
import com.nicholaskoldys.collegetrackingapp.model.Mentor;

import java.util.List;

public class MentorRecyclerViewAdapter extends RecyclerView.Adapter<MentorRecyclerViewAdapter.ViewHolder> {

    private List<Mentor> mentorDisplayData;
    private LayoutInflater layoutInflater;
    private MentorItemClickListener mentorItemClickListener;

    public MentorRecyclerViewAdapter(Context context, MentorItemClickListener mentorItemClickListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.mentorItemClickListener = mentorItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mentorIndividualView = layoutInflater.inflate(R.layout.list_individualitem_mentor, parent, false);

        return new ViewHolder(mentorIndividualView, mentorItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mentorDisplayData.get(position));
    }

    @Override
    public int getItemCount() {
        if(mentorDisplayData != null) {
            return mentorDisplayData.size();
        } else {
            return 0;
        }
    }

    public void setMentorDisplayData(List<Mentor> mentorDisplayData) {
        this.mentorDisplayData = mentorDisplayData;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mentorNameTextView;
        private TextView mentorEmailTextView;
        private TextView mentorPhoneTextView;
        private MentorItemClickListener mentorItemClickListener;

        ViewHolder(@NonNull View itemView, MentorItemClickListener mentorItemClickListener) {
            super(itemView);

            mentorNameTextView = itemView.findViewById(R.id.mentorNameTextView);
            mentorEmailTextView = itemView.findViewById(R.id.mentorEmailTextView);
            mentorPhoneTextView = itemView.findViewById(R.id.mentorPhoneTextView);
            this.mentorItemClickListener = mentorItemClickListener;
            itemView.setOnClickListener(this);
        }

        void bindData(Mentor mentor) {
            mentorNameTextView.setText(mentor.getTitle());
            mentorEmailTextView.setText(mentor.getEmail());
            mentorPhoneTextView.setText(mentor.getPhone());
        }

        @Override
        public void onClick(View v) {
            mentorItemClickListener.onMentorItemClick(mentorDisplayData.get(getAdapterPosition()));
        }
    }

    public interface MentorItemClickListener {
        void onMentorItemClick(Mentor mentor);
    }
}
