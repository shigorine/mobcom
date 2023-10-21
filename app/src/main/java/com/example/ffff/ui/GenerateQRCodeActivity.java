package com.example.ffff.ui;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ffff.R;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GenerateQRCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);

        // Get recipient account details (e.g., account number)
        String recipientAccount = "recipient_account_number";

        // Create a QR Code
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap qrCode = barcodeEncoder.encodeBitmap(recipientAccount, BarcodeFormat.QR_CODE, 400, 400);
            // Display the QR code or save it as an image
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
