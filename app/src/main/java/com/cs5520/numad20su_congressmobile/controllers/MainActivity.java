package com.cs5520.numad20su_congressmobile.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cs5520.numad20su_congressmobile.R;
import com.cs5520.numad20su_congressmobile.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// TODO Use anonymous sign-in
// TODO Use Cloud Storage for Firebase to upload user photo
// TODO Follow/Unfollow bills/members/committees, present in MyFeed, update in Settings

// TODO For each tab, grab content for both House and Senate
// TODO Respond to clicks of actions in action bar
// TODO Put in a working search bar
// TODO Cancel requests onSwipe for the ViewPager so as not to hold up other tabs
// TODO     See "Cancel a request" at https://developer.android.com/training/volley/simple
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    ActivityMainBinding binding;
    private ImageView targetImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        targetImage = (ImageView)findViewById(R.id.profile_picture);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Click listeners
        binding.buttonSignIn.setOnClickListener(this);


        // Create the adapter that will return a fragment for each section
        FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            private final Fragment[] mFragments = new Fragment[]{
                    new MyFeedFragment(),
                    new BillsFragment(),
                    new CommitteesFragment(),
                    new MembersFragment()
            };
            private final String[] mFragmentNames = new String[]{
                    getString(R.string.heading_my_feed),
                    getString(R.string.heading_bills),
                    getString(R.string.heading_committees),
                    getString(R.string.heading_members),
            };

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };

        // Set up the ViewPager with the sections adapter.
        binding.container.setAdapter(mPagerAdapter);
        binding.tabs.setupWithViewPager(binding.container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.manage_profile:
                //Toast.makeText(this, "braff", Toast.LENGTH_LONG).show();
                getImage(this.getCurrentFocus());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
        //Toast.makeText(this, "braff", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

//    public void getImage(View arg0) {
//        // TODO Auto-generated method stub
////        Intent intent = new Intent(Intent.ACTION_PICK,
////                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////        startActivityForResult(intent, 0);
//        Toast.makeText(this, "braff", Toast.LENGTH_LONG).show();
//
//
//    }


    @Override
    public void onClick(View view) {
        // TODO Add handler for profile image click
        int i = view.getId();
        if (i == R.id.buttonSignIn) {
            signInAnonymously();
        }
    }

    private void signInAnonymously() {
        // Sign in anonymously. Authentication is required to read or write from Firebase Storage.
        mAuth.signInAnonymously()
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "signInAnonymously:SUCCESS");
                        updateUI(authResult.getUser());
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        // TODO Update username and display it
        // TODO Add error checking for (empty) username
        // TODO Update registrationToken and authToken in database
        // Signed in or Signed out
        if (user != null) {
            binding.layoutSignin.setVisibility(View.GONE);
            binding.layoutMain.setVisibility(View.VISIBLE);
        } else {
            binding.layoutSignin.setVisibility(View.VISIBLE);
            binding.layoutMain.setVisibility(View.GONE);
        }
    }
}
