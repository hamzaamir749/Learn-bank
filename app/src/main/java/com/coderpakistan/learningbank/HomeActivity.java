package com.coderpakistan.learningbank;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coderpakistan.learningbank.Game.GameSessionActivity;
import com.coderpakistan.learningbank.HelperClasses.Session;
import com.coderpakistan.learningbank.HelperClasses.SharedHelper;
import com.coderpakistan.learningbank.HelperClasses.URLHelper;
import com.coderpakistan.learningbank.HelperClasses.UserSessionManager;
import com.coderpakistan.learningbank.Quiz.QuizHomeActivity;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ImageView btnNav;
    LinearLayout playGame, localVocabulary, Quizes, pronunciation, support;
    UserSessionManager userSessionManager;
    Session session;
    NavigationView navigationView;
    SharedHelper sharedHelper;
    View view;
    CircleImageView headerImage;
    TextView headerName;
    Context context=HomeActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        navigationView.setNavigationItemSelectedListener(this);


        localVocabulary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,learnVocabularyActivity.class));
              /*  startActivity(new Intent(HomeActivity.this,CheckActivity.class));*/
            }
        });


        playGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, GameSessionActivity.class));
            }
        });
        Quizes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, QuizHomeActivity.class));
            }
        });


        pronunciation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, PronunciationActivity.class));
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,SupportActivity.class));
            }
        });
        btnNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UpdateProfileActivity.class));
            }
        });

    }

    private void initViews() {
        btnNav = findViewById(R.id.nav);
        localVocabulary = findViewById(R.id.local_vocabulary);
        Quizes = findViewById(R.id.quiz);
        playGame = findViewById(R.id.playgame);
        pronunciation = findViewById(R.id.pronunciation);
        support = findViewById(R.id.support);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        view = navigationView.getHeaderView(0);
        headerImage = view.findViewById(R.id.header_Image);
        headerName = view.findViewById(R.id.header_Name);
        userSessionManager = new UserSessionManager(HomeActivity.this);
        session = userSessionManager.getSessionDetails();
        sharedHelper = new SharedHelper(HomeActivity.this);
        setHeader();
    }

    private void setHeader() {
        Picasso.get().load(URLHelper.BASE_IMAGE + "profile/" + session.getImage()).into(headerImage);
        headerName.setText(session.getName());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
        } else if (id == R.id.nav_logout) {
            LogOut();
        } else if (id == R.id.nav_rate_us) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
        } else if (id == R.id.nav_share) {
            ShareLink();
        } else if (id == R.id.nav_about_us) {
            SampleTextActivities("this is about us.","About Us");
        } else if (id == R.id.nav_terms_condition) {
            SampleTextActivities("this is terms and conditions.","Terms and Conditions");
        } else if(id==R.id.nav_easy_writing){
            OpenWhatsApp();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void SampleTextActivities(String text,String texttext) {
        Intent intent = new Intent(getApplicationContext(), SampleTextActivity.class);
        intent.putExtra("text", text);
        intent.putExtra("tooltext",texttext);
        startActivity(intent);
    }

    void ShareLink() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    void OpenWhatsApp() {
        String contact = "+974 7077 6724"; // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void LogOut() {
        userSessionManager.setLoggedIn(false);
        userSessionManager.clearSessionData();
        sharedHelper.clearStoreInfoData();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finishAffinity();
    }

}
