package com.example.ffff.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import com.example.ffff.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GenerateQRCodeActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String userEmail = user.getEmail();

            if (userEmail != null) {
                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap qrCode = barcodeEncoder.encodeBitmap(userEmail, BarcodeFormat.QR_CODE, 400, 400);

                    Log.d("YourTag", "QR Code generated successfully");

                    ImageView qrCodeImageView = findViewById(R.id.qrCodeImageView);
                    qrCodeImageView.setImageBitmap(qrCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
