package fi.christian.assignment_7;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {

    private ArrayList<Meeting> meetingList;
    private final OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, Meeting meeting);
    }

    public MeetingAdapter(ArrayList<Meeting> meetingList, OnItemClickListener clickListener) {
        this.meetingList = meetingList;
        this.clickListener = clickListener;
    }

    public void updateList(ArrayList<Meeting> newList) {
        this.meetingList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meeting_item, parent, false);
        return new MeetingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder meetingViewHolder, int position) {
        Meeting meeting = meetingList.get(position);
        meetingViewHolder.meetingDetailsTextView.setText(meeting.toString());

        meetingViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = meetingViewHolder.getBindingAdapterPosition();
                if (clickListener != null && currentPosition != RecyclerView.NO_POSITION) {
                    clickListener.onItemClick(currentPosition, meetingList.get(currentPosition));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return meetingList.size();
    }

    public static class MeetingViewHolder extends RecyclerView.ViewHolder {
        TextView meetingDetailsTextView;

        public MeetingViewHolder(@NonNull View itemView) {
            super(itemView);
            meetingDetailsTextView = itemView.findViewById(R.id.meeting_details_TextView);
        }
    }
}
