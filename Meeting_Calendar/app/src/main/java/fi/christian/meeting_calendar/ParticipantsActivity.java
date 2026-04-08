package fi.christian.meeting_calendar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ParticipantsActivity extends AppCompatActivity {

    private EditText participantNameEditText;
    private Button addParticipantButton, doneButton;
    private ImageButton backButton;
    private ListView participantsListView;
    private ArrayList<String> participantsList;
    private ArrayAdapter<String> adapter;
    private Drawable originalBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);

        participantsList = getIntent().getStringArrayListExtra("participants");
        if (participantsList == null) {
            participantsList = new ArrayList<>();
        }

        initializeViews();
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
        doneButton = findViewById(R.id.doneButton);
        backButton = findViewById(R.id.backButton);
        participantsListView = findViewById(R.id.participantsListView);

        originalBackground = participantNameEditText.getBackground();
        InputHandler.setupTextWatcher(participantNameEditText, originalBackground);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, participantsList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ThemeManager.applyTheme(getContext(), view);
                return view;
            }
        };

        participantsListView.setAdapter(adapter);
        participantsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        for (int i = 0; i < participantsList.size(); i++) {
            participantsListView.setItemChecked(i, true);
        }
    }

    private void setupListeners() {
        addParticipantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addParticipant();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addParticipant() {
        if (!InputHandler.validateInputIsEmpty(participantNameEditText)) {
            Toast.makeText(this, getString(R.string.toast_enter_name), Toast.LENGTH_SHORT).show();
            return;
        }

        String name = participantNameEditText.getText().toString().trim();
        participantsList.add(name);
        adapter.notifyDataSetChanged();
        participantsListView.setItemChecked(participantsList.size() - 1, true);
        participantNameEditText.setText("");
    }

    private void returnResult() {
        ArrayList<String> selectedParticipants = new ArrayList<>();
        for (int i = 0; i < participantsList.size(); i++) {
            if (participantsListView.isItemChecked(i)) {
                selectedParticipants.add(participantsList.get(i));
            }
        }

        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra("participants", selectedParticipants);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
