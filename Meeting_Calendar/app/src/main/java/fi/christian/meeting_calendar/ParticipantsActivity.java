package fi.christian.meeting_calendar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
    private Bitmap currentBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);

        initializeViews();
        setupImagePicker();
        setupListeners();
        loadAllParticipants();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ThemeManager.applyTheme(this, findViewById(R.id.participantsLayout));
    }

    private void loadAllParticipants() {
        participantsList.clear();
        participantsList.addAll(MeetingManager.getAllParticipants());
        
        ArrayList<Participant> temps = MeetingManager.getTempParticipants();
        for (Participant p : participantsList) {
            p.setSelected(false);
            for (Participant t : temps) {
                if (p.getName().equals(t.getName())) {
                    p.setSelected(true);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void initializeViews() {
        participantNameEditText = findViewById(R.id.participantNameEditText);
        addParticipantButton = findViewById(R.id.addParticipantButton);
        selectImageButton = findViewById(R.id.selectImageButton);
        selectedParticipantImageView = findViewById(R.id.selectedParticipantImageView);
        doneButton = findViewById(R.id.doneButton);
        backButton = findViewById(R.id.backButton);
        participantsRecyclerView = findViewById(R.id.participantsRecyclerView);

        InputHandler.setupTextWatcher(participantNameEditText, participantNameEditText.getBackground());

        adapter = new ParticipantAdapter(participantsList, true);
        participantsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        participantsRecyclerView.setAdapter(adapter);
    }

    private void setupImagePicker() {
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Uri uri = (result.getResultCode() == RESULT_OK && result.getData() != null) ? result.getData().getData() : null;
                    if (uri != null) {
                        try (InputStream is = getContentResolver().openInputStream(uri)) {
                            currentBitmap = BitmapFactory.decodeStream(is);
                            selectedParticipantImageView.setImageBitmap(currentBitmap);
                            selectedParticipantImageView.setVisibility(View.VISIBLE);
                        } catch (Exception ignored) {}
                    }
                });

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            launcher.launch(intent);
        });
    }

    private void setupListeners() {
        addParticipantButton.setOnClickListener(v -> addParticipant());
        doneButton.setOnClickListener(v -> returnResult());
        backButton.setOnClickListener(v -> finish());
    }

    private void addParticipant() {
        if (!InputHandler.validateInputIsEmpty(participantNameEditText)) return;

        Participant p = new Participant(participantNameEditText.getText().toString().trim(), currentBitmap);
        p.setSelected(true);
        participantsList.add(0, p);
        adapter.notifyItemInserted(0);
        participantsRecyclerView.scrollToPosition(0);

        participantNameEditText.setText("");
        selectedParticipantImageView.setVisibility(View.GONE);
        currentBitmap = null;
    }

    private void returnResult() {
        ArrayList<Participant> selected = new ArrayList<>();
        for (Participant p : participantsList) if (p.isSelected()) selected.add(p);
        MeetingManager.setTempParticipants(selected);
        setResult(RESULT_OK);
        finish();
    }
    

}
