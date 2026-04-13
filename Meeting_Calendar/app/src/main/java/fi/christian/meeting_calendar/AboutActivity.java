package fi.christian.meeting_calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    private TextView aboutTextView;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        aboutTextView = findViewById(R.id.aboutTextView);
        backButton = findViewById(R.id.backButton);

        // Luetaan teksti assets-kansiosta FileManagerin avulla
        String aboutContent = FileManager.readFromAssets(this, "about.txt");
        aboutTextView.setText(aboutContent);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Suljetaan aktiviteetti ja palataan takaisin
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Sovelletaan teema myös tähän näkymään
        ThemeManager.applyTheme(this, findViewById(R.id.aboutLayout));
    }
}
