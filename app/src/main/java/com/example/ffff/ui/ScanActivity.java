package com.example.ffff.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v -> scanCode());
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

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference usersRef = database.getReference("users");

            usersRef.orderByChild("email").equalTo(scannedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                            String recipientId = userSnapshot.getKey();
                            String recipientName = userSnapshot.child("name").getValue(String.class);
                            String recipientMoney = userSnapshot.child("money").getValue(String.class);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                            builder.setTitle("Success");
                            builder.setMessage("Money transferred to " + recipientName);
                            builder.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
                            builder.show();
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                        builder.setTitle("Error");
                        builder.setMessage("User not found");
                        builder.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
                        builder.show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Database error: " + databaseError.getMessage());
                    builder.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
                    builder.show();
                }
            });
        }
    });
}
