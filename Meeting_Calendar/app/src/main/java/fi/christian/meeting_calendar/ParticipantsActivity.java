package fi.christian.meeting_calendar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;

public class ParticipantsActivity extends AppCompatActivity {

    private EditText participantNameEditText;
    private Button addParticipantButton, doneButton, selectImageButton;
    private ImageButton backButton;
    private ImageView selectedParticipantImageView;
    private RecyclerView participantsRecyclerView;
    private ArrayList<Participant> participantsList = new ArrayList<>();
    private ParticipantAdapter adapter;
    private Drawable originalBackground;
    private Bitmap currentBitmap = null;

    private ActivityResultLauncher<Intent> selectImageActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);

        ArrayList<Participant> temps = MeetingManager.getTempParticipants();
        for (Participant p : temps) {
            p.setSelected(true);
            participantsList.add(p);
        }
        
        initializeViews();
        setupImagePicker();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ThemeManager.applyTheme(this, findViewById(R.id.participantsLayout));
    }

    private void initializeViews() {
        participantNameEditText = findViewById(R.id.participantNameEditText);
        addParticipantButton = findViewById(R.id.addParticipantButton);
        selectImageButton = findViewById(R.id.selectImageButton);
        selectedParticipantImageView = findViewById(R.id.selectedParticipantImageView);
        doneButton = findViewById(R.id.doneButton);
        backButton = findViewById(R.id.backButton);
        participantsRecyclerView = findViewById(R.id.participantsRecyclerView);

        originalBackground = participantNameEditText.getBackground();
        InputHandler.setupTextWatcher(participantNameEditText, originalBackground);

        adapter = new ParticipantAdapter(participantsList, true);
        participantsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        participantsRecyclerView.setAdapter(adapter);
    }

    private void setupImagePicker() {
        selectImageActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(uri);
                                currentBitmap = BitmapFactory.decodeStream(inputStream);
                                selectedParticipantImageView.setImageBitmap(currentBitmap);
                                selectedParticipantImageView.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void setupListeners() {
        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            selectImageActivityResultLauncher.launch(intent);
        });

        addParticipantButton.setOnClickListener(v -> addParticipant());

        doneButton.setOnClickListener(v -> returnResult());

        backButton.setOnClickListener(v -> finish());
    }

    private void addParticipant() {
        if (!InputHandler.validateInputIsEmpty(participantNameEditText)) {
            Toast.makeText(this, getString(R.string.toast_enter_name), Toast.LENGTH_SHORT).show();
            return;
        }

        String name = participantNameEditText.getText().toString().trim();
        Participant participant = new Participant(name, currentBitmap);
        participant.setSelected(true);
        participantsList.add(participant);
        adapter.notifyItemInserted(participantsList.size() - 1);
        participantsRecyclerView.scrollToPosition(participantsList.size() - 1);
        
        // Reset inputs
        participantNameEditText.setText("");
        selectedParticipantImageView.setVisibility(View.GONE);
        currentBitmap = null;
    }

    private void returnResult() {
        // Palautetaan vain ne, jotka on valittu checkboxilla
        ArrayList<Participant> selectedOnly = new ArrayList<>();
        for (Participant p : participantsList) {
            if (p.isSelected()) {
                selectedOnly.add(p);
            }
        }
        MeetingManager.setTempParticipants(selectedOnly);
        setResult(RESULT_OK);
        finish();
    }
}
