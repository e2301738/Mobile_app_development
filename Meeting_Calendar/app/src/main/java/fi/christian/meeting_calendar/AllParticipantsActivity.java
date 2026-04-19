package fi.christian.meeting_calendar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;

public class AllParticipantsActivity extends AppCompatActivity {

    private RecyclerView allParticipantsRecyclerView;
    private ParticipantAdapter adapter;
    private ArrayList<Participant> participantsList = new ArrayList<>();
    private DBAdapter dbAdapter;
    
    private ActivityResultLauncher<Intent> editImageLauncher;
    private ImageView dialogImageView;
    private Bitmap tempDialogBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_participants);

        dbAdapter = new DBAdapter(this);
        
        initializeViews();
        setupLaunchers();
        loadParticipants();
    }

    private void initializeViews() {
        allParticipantsRecyclerView = findViewById(R.id.allParticipantsRecyclerView);
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        allParticipantsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new ParticipantAdapter(participantsList, false); 
        adapter.setOnItemClickListener(this::showEditDialog);
        allParticipantsRecyclerView.setAdapter(adapter);
    }

    private void setupLaunchers() {
        editImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
                                tempDialogBitmap = BitmapFactory.decodeStream(inputStream);
                                if (dialogImageView != null) dialogImageView.setImageBitmap(tempDialogBitmap);
                            } catch (Exception e) {
                                Toast.makeText(this, R.string.error_in_loading_image, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void loadParticipants() {
        participantsList.clear();
        participantsList.addAll(dbAdapter.getAllParticipants());

        MeetingManager.setAllParticipants(new ArrayList<>(participantsList));
        MeetingManager.setMeetings(dbAdapter.getAllMeetings());
        
        adapter.notifyDataSetChanged();
        ThemeManager.applyTheme(this, findViewById(R.id.allParticipantsLayout));
    }

    private void showEditDialog(Participant participant) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_participant, null);
        builder.setView(dialogView);

        EditText nameEditText = dialogView.findViewById(R.id.editParticipantName);
        dialogImageView = dialogView.findViewById(R.id.editParticipantImage);
        Button changeImageButton = dialogView.findViewById(R.id.btnChangeImage);
        Button saveButton = dialogView.findViewById(R.id.btnSaveParticipant);
        Button deleteButton = dialogView.findViewById(R.id.btnDeleteParticipant);
        TextView titleTextView = dialogView.findViewById(R.id.dialogTitle);

        titleTextView.setText(R.string.update_participant);
        nameEditText.setText(participant.getName());
        tempDialogBitmap = participant.getImage();
        
        if (tempDialogBitmap != null) {
            dialogImageView.setImageBitmap(tempDialogBitmap);
        } else {
            dialogImageView.setImageResource(android.R.drawable.ic_menu_report_image);
        }

        AlertDialog dialog = builder.create();

        changeImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            editImageLauncher.launch(intent);
        });

        saveButton.setOnClickListener(v -> {
            String newName = nameEditText.getText().toString().trim();
            if (newName.isEmpty()) {
                Toast.makeText(this, R.string.toast_enter_name, Toast.LENGTH_SHORT).show();
                return;
            }
            participant.setName(newName);
            participant.setImage(tempDialogBitmap);
            if (dbAdapter.updateParticipant(participant)) {
                Toast.makeText(this, R.string.updated_successfully, Toast.LENGTH_SHORT).show();
                loadParticipants();
                dialog.dismiss();
            }
        });

        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirm_title)
                .setMessage(R.string.warning_data_delete)
                .setPositiveButton(R.string.delete_confirm_yes, (d, w) -> {
                    if (dbAdapter.deleteParticipant(participant.getId())) {
                        Toast.makeText(this, R.string.deleted_from_db, Toast.LENGTH_SHORT).show();
                        loadParticipants();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
        });

        dialog.show();
    }
}
