package com.cs5520.numad20su_congressmobile.controllers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.cs5520.numad20su_congressmobile.R;
import com.cs5520.numad20su_congressmobile.content.models.Bill;

public class BillDetailsActivity extends AppCompatActivity {

    private Bill bill;
    private TextView billSponsor;
    private String urlText;

    private TextView passedHouse;
    private TextView passedSenate;
    private TextView passedPresident;
    private TextView passedLaw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        TextView billTitle = findViewById(R.id.bill_title);
        TextView billNumber = findViewById(R.id.bill_number);
        TextView billIntroduced = findViewById(R.id.bill_introduced);
        billSponsor = findViewById(R.id.bill_sponsor);
        TextView billCommittees = findViewById(R.id.bill_committees);

        passedHouse = findViewById(R.id.passed_house);
        passedSenate = findViewById(R.id.passed_senate);
        passedPresident = findViewById(R.id.passed_president);
        passedLaw = findViewById(R.id.became_law);

        Intent openDetailsIntent = getIntent();

        bill = openDetailsIntent.getParcelableExtra("bill");
        billNumber.setText(bill.number);
        billTitle.setText(bill.title);
        billIntroduced.setText(bill.introduced_date);
        sponsorSetup();
        billCommittees.setText(bill.committees);
        urlText = bill.congressdotgov_url + "/text";

        setUpStatusDiagram();
    }

    private void sponsorSetup() {
        String sponsorText = bill.sponsor_title + " " + bill.sponsor_name + " (" +
            bill.sponsor_party + ", " + bill.sponsor_state + ")";
        billSponsor.setText(sponsorText);

    }

    public void goToPDF(View v) {
        Uri uriUrl = Uri.parse(urlText);
        Intent launchFullText = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchFullText);
    }

    private void setUpStatusDiagram() {
        if (bill.house_passage != null) {
            passedHouse.setText(R.string.passed_house);
            passedHouse.append("\n" + bill.house_passage);
            passedHouse.setBackgroundResource(R.drawable.text_border_green);
        }
        if (bill.senate_passage != null) {
            passedSenate.setText(R.string.passed_senate);
            passedSenate.append("\n" + bill.senate_passage);
            passedSenate.setBackgroundResource(R.drawable.text_border_green);
        }
        if (bill.vetoed != null) {
            if (bill.enacted != null) {
                passedPresident.setText(R.string.vetoed_overridden);
                passedPresident.append("\n" + bill.vetoed);
                passedPresident.setBackgroundResource(R.drawable.text_border_yellow);
            } else {
                passedPresident.setText(R.string.vetoed);
                passedPresident.append("\n" + bill.vetoed);
                passedPresident.setBackgroundResource(R.drawable.text_border_red);
            }

        }
        if (bill.enacted != null) {
            passedLaw.append("\n" + bill.enacted);
            passedLaw.setBackgroundResource(R.drawable.text_border_green);
            if (bill.vetoed == null) {
                passedPresident.setText(R.string.approved_by_president);
                passedPresident.setBackgroundResource(R.drawable.text_border_green);
            }
        }
    }
}