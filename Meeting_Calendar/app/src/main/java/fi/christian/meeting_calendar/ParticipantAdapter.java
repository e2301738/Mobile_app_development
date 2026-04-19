package fi.christian.meeting_calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder> {

    private final ArrayList<Participant> participantList;
    private final boolean isEditMode;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Participant participant);
    }

    public ParticipantAdapter(ArrayList<Participant> participantList, boolean isEditMode) {
        this.participantList = participantList;
        this.isEditMode = isEditMode;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_display_item, parent, false);
        return new ParticipantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
        Participant participant = participantList.get(position);
        holder.nameTextView.setText(participant.getName());
        if (participant.getImage() != null) {
            holder.imageView.setImageBitmap(participant.getImage());
        } else {
            holder.imageView.setImageResource(android.R.drawable.ic_menu_report_image);
        }
        
        if (isEditMode) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(participant.isSelected());
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> participant.setSelected(isChecked));
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(participant);
                }
            });
        }

        // Apply theme colors
        int fontColor = ThemeManager.getFontColor(holder.itemView.getContext());
        holder.nameTextView.setTextColor(fontColor);
    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }

    public static class ParticipantViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        CheckBox checkBox;

        public ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.participantImageView);
            nameTextView = itemView.findViewById(R.id.participantNameTextView);
            checkBox = itemView.findViewById(R.id.participantCheckBox);
        }
    }
}
