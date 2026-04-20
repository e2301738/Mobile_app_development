package fi.christian.meeting_calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        
        meetingViewHolder.participantImagesContainer.removeAllViews();
        
        if (meeting.getParticipants() != null) {
            LayoutInflater inflater = LayoutInflater.from(meetingViewHolder.itemView.getContext());
            for (Participant participant : meeting.getParticipants()) {
                View participantView = inflater.inflate(R.layout.participant_display_item, meetingViewHolder.participantImagesContainer, false);
                ImageView imageView = participantView.findViewById(R.id.participantImageView);
                TextView nameView = participantView.findViewById(R.id.participantNameTextView);

                nameView.setText(participant.getName());
                if (participant.getImage() != null) {
                    imageView.setImageBitmap(participant.getImage());
                } else {
                    imageView.setImageResource(android.R.drawable.ic_menu_report_image);
                }
                
                int fontColor = ThemeManager.getFontColor(meetingViewHolder.itemView.getContext());
                nameView.setTextColor(fontColor);

                meetingViewHolder.participantImagesContainer.addView(participantView);
            }
        }
        
        ThemeManager.applyTheme(meetingViewHolder.itemView.getContext(), meetingViewHolder.itemView);

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
        LinearLayout participantImagesContainer;

        public MeetingViewHolder(@NonNull View itemView) {
            super(itemView);
            meetingDetailsTextView = itemView.findViewById(R.id.meeting_details_TextView);
            participantImagesContainer = itemView.findViewById(R.id.participantImagesContainer);
        }
    }
}
