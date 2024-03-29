package com.cs5520.numad20su_congressmobile.controllers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.cs5520.numad20su_congressmobile.R;
import com.cs5520.numad20su_congressmobile.content.models.Committee;

public class CommitteeDetailsActivity extends AppCompatActivity {

    private Committee committee;
    private TextView chair;
    private TextView chairAddon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee_details);
        TextView title = findViewById(R.id.committee_title);
        TextView chamber = findViewById(R.id.committee_chamber);
        chair = findViewById(R.id.committee_chair);
        chairAddon = findViewById(R.id.chair_text);
        TextView website = findViewById(R.id.committee_url);

        Intent openDetailsIntent = getIntent();

        committee = openDetailsIntent.getParcelableExtra("committee");
        title.setText(committee.name);
        chamber.setText(committee.chamber);
        website.setText(committee.url);
        chairSetup();
    }

    private void chairSetup() {
        if (committee.chair != null && committee.chair_party != null
            && committee.chair_state != null) {
            String chairText = committee.chair + " (" +
                committee.chair_party + ", " + committee.chair_state + ")";
            chair.setText(chairText);
        } else {
            chairAddon.setText("");
        }
    }

    public void goToWebsite(View v) {
        try {
            Uri urlObj = Uri.parse(committee.url);
            Intent goToLink = new Intent(Intent.ACTION_VIEW, urlObj);
            this.startActivity(goToLink);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid Link.", Toast.LENGTH_LONG).show();
        }
    }

}