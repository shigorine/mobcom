package com.example.ffff.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ffff.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ScanActivity extends AppCompatActivity {

    Button btn_scan;
    EditText amountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v -> scanCode());

        amountEditText = findViewById(R.id.amountEditText);
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan QR Code");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            String scannedEmail = result.getContents();
            String enteredAmount = amountEditText.getText().toString();

            if (enteredAmount.isEmpty()) {
                showAlertDialog("Error", "Please enter an amount");
            } else {
                int amount = Integer.parseInt(enteredAmount);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference usersRef = database.getReference("users");

                String senderEmail = scannedEmail;

                usersRef.orderByChild("email").equalTo(senderEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                String senderUserId = userSnapshot.getKey();

                                int senderBalance = userSnapshot.child("money").getValue(Integer.class);

                                if (senderBalance >= amount) {
                                    senderBalance -= amount;
                                    userSnapshot.getRef().child("money").setValue(senderBalance);

                                    usersRef.orderByChild("email").equalTo(scannedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot recipientData) {
                                            if (recipientData.exists()) {
                                                for (DataSnapshot recipientSnapshot : recipientData.getChildren()) {
                                                    int recipientBalance = recipientSnapshot.child("money").getValue(Integer.class);
                                                    recipientBalance += amount;
                                                    recipientSnapshot.getRef().child("money").setValue(recipientBalance);
                                                }
                                                showAlertDialog("Success", "Money transferred successfully");
                                            } else {
                                                showAlertDialog("Error", "Recipient not found");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            showAlertDialog("Error", "Database error: " + databaseError.getMessage());
                                        }
                                    });
                                } else {
                                    showAlertDialog("Error", "Insufficient funds for the transfer");
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        showAlertDialog("Error", "Database error: " + databaseError.getMessage());
                    }
                });
            }
        }
    });

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }
}
