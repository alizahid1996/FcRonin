package com.example.fcronin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fcronin.R;

public class ContactViewerActivity extends AppCompatActivity {
    private ImageView contactImage;
    private TextView contactName;
    private RecyclerView contactPhones, contactEmails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_viewer);
    }
}