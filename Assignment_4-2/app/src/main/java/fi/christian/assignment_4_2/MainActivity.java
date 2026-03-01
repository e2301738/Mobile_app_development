package fi.christian.assignment_4_2;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LayoutParams viewLayoutParams = null;
    private ScrollView scrollView;
    private EditText idEditText, nameEditText, unitPriceEditText, amountEditText;
    private TextView summaryTextView, summaryHeader;
    private Button submitButton;
    private static ArrayList<Product> productList = new ArrayList<>();
    private StringBuilder summaryBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        viewLayoutParams.setMargins(40, 10, 40, 10);

        LayoutParams labelParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        labelParams.setMargins(40, 20, 40, 0);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(20, 20, 20, 20);

        TextView idLabel = new TextView(this);
        idLabel.setText(getString(R.string.label_id));
        idLabel.setLayoutParams(labelParams);
        linearLayout.addView(idLabel);

        idEditText = new EditText(this);
        idEditText.setHint(getString(R.string.id_hint));
        idEditText.setLayoutParams(viewLayoutParams);
        linearLayout.addView(idEditText);

        TextView nameLabel = new TextView(this);
        nameLabel.setText(getString(R.string.label_name));
        nameLabel.setLayoutParams(labelParams);
        linearLayout.addView(nameLabel);

        nameEditText = new EditText(this);
        nameEditText.setHint(getString(R.string.name_hint));
        nameEditText.setLayoutParams(viewLayoutParams);
        linearLayout.addView(nameEditText);

        TextView priceLabel = new TextView(this);
        priceLabel.setText(getString(R.string.label_price));
        priceLabel.setLayoutParams(labelParams);
        linearLayout.addView(priceLabel);

        unitPriceEditText = new EditText(this);
        unitPriceEditText.setHint(getString(R.string.price_hint));
        unitPriceEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        unitPriceEditText.setLayoutParams(viewLayoutParams);
        linearLayout.addView(unitPriceEditText);

        TextView amountLabel = new TextView(this);
        amountLabel.setText(getString(R.string.label_amount));
        amountLabel.setLayoutParams(labelParams);
        linearLayout.addView(amountLabel);

        amountEditText = new EditText(this);
        amountEditText.setHint(getString(R.string.amount_hint));
        amountEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        amountEditText.setLayoutParams(viewLayoutParams);
        linearLayout.addView(amountEditText);

        submitButton = new Button(this);
        submitButton.setText(R.string.button_submit);
        submitButton.setLayoutParams(viewLayoutParams);
        linearLayout.addView(submitButton);

        summaryHeader = new TextView(this);
        summaryHeader.setText(R.string.product_summary);
        summaryHeader.setTextSize(18);
        summaryHeader.setPadding(40, 0, 0, 0);
        linearLayout.addView(summaryHeader);

        scrollView = new ScrollView(this);
        summaryTextView = new TextView(this);
        summaryTextView.setPadding(40, 20, 40, 20);
        scrollView.addView(summaryTextView);
        linearLayout.addView(scrollView);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productSubmit();
            }
        });
        setContentView(linearLayout);
    }

    private void productSubmit() {
        if (idEditText.getText().toString().isEmpty() ||
                nameEditText.getText().toString().isEmpty() ||
                unitPriceEditText.getText().toString().isEmpty() ||
                amountEditText.getText().toString().isEmpty()) {

            Toast.makeText(this, R.string.fields_empty_toast, Toast.LENGTH_SHORT).show();
            return;
        }

        String id = idEditText.getText().toString();
        String name = nameEditText.getText().toString();
        double price = Double.parseDouble(unitPriceEditText.getText().toString());
        int amount = Integer.parseInt(amountEditText.getText().toString());

        Product product = new Product(id, name, price, amount);
        productList.add(product);

        updateSummary();

        idEditText.setText("");
        nameEditText.setText("");
        unitPriceEditText.setText("");
        amountEditText.setText("");
        idEditText.requestFocus();
    }

    private void updateSummary() {
        summaryBuilder = new StringBuilder();

        for (Product product : productList) {
            summaryBuilder.append(product.toString()).append("\n");
        }

        summaryTextView.setText(summaryBuilder.toString());
    }
}