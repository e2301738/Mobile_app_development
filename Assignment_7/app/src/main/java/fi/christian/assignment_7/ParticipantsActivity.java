package fi.christian.assignment_7;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ParticipantsActivity extends AppCompatActivity {

    private EditText participantNameEditText;
    private Button addParticipantButton, doneButton;
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

    private void initializeViews() {
        participantNameEditText = findViewById(R.id.participantNameEditText);
        addParticipantButton = findViewById(R.id.addParticipantButton);
        doneButton = findViewById(R.id.doneButton);
        participantsListView = findViewById(R.id.participantsListView);

        originalBackground = participantNameEditText.getBackground();
        InputHandler.setupTextWatcher(participantNameEditText, originalBackground);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, participantsList);
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
