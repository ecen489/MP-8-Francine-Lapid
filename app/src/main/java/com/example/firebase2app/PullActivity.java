package com.example.firebase2app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class PullActivity extends AppCompatActivity {

    private EditText mStudentIDField;
    private Button mQuery1Button;
    private Button mQuery2Button;
    private Button mPushActivityButton;
    private Button mSignOutButton;

    private FirebaseAuth mAuth;
    private FirebaseDatabase simpsons_database;
    private DatabaseReference simpsons_database_ref;
    private FirebaseUser user = null;

    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    ArrayList<String> course_names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull);

        // EditText
        mStudentIDField = (EditText) findViewById(R.id.studentIDfield);

        // Buttons
        mSignOutButton = (Button) findViewById(R.id.signOutButton);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {
                signOut();
                goBackToMainActivity();
            }
        });

        mPushActivityButton = (Button) findViewById(R.id.pushActivityButton);
        mPushActivityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {
                goToPushActivity();
            }
        });

        mQuery1Button = (Button) findViewById(R.id.query1button);
        mQuery1Button.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                query1function();
            }
        });

        mQuery2Button = (Button) findViewById(R.id.query2button);
        mQuery2Button.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                query2function();
            }
        });

        // Firebase Things
        mAuth = FirebaseAuth.getInstance();
        simpsons_database = FirebaseDatabase.getInstance();
        simpsons_database_ref = simpsons_database.getReference();

        // set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // adapter = new MyRecyclerViewAdapter(this, course_names);
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
    }

    public void goBackToMainActivity() {
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }

    public void goToPushActivity() {
        Intent intent = new Intent (this, PushActivity.class);
        startActivity(intent);
    }

    public void query1function() {
        course_names.clear();

        String studentID = mStudentIDField.getText().toString();
        if (studentID != null && !studentID.isEmpty()) {
            int studentIDint = Integer.parseInt(studentID);

            DatabaseReference gradeKey = simpsons_database_ref.child("simpsons/grades/");
            Query query = gradeKey.orderByChild("student_id").equalTo(studentIDint);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Grade grade = snapshot.getValue(Grade.class);
                            String temp = grade.getcourse_name() + ", " + grade.getgrade();
                            course_names.add(temp);
                        }
                    }
                    else {
                        Toast.makeText(PullActivity.this, "WRONG", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),"failed :(((" + databaseError,Toast.LENGTH_SHORT).show();
                }
            });
            adapter = new MyRecyclerViewAdapter(this, course_names);
            recyclerView.setAdapter(adapter);
        }
        else {
            Toast.makeText(PullActivity.this, "PLEASE INPUT SOMETHING", Toast.LENGTH_SHORT).show();
        }

        /*
        int studentIDint = Integer.parseInt(studentID);

        DatabaseReference gradeKey = simpsons_database_ref.child("simpsons/grades/");
        Query query = gradeKey.orderByChild("student_id").equalTo(studentIDint);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Grade grade = snapshot.getValue(Grade.class);
                        String temp = grade.getcourse_name() + ", " + grade.getgrade();
                        course_names.add(temp);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"failed :(((" + databaseError,Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new MyRecyclerViewAdapter(this, course_names);
        recyclerView.setAdapter(adapter);
        */

    }

    public void query2function() {
        course_names.clear();

        String studentID = mStudentIDField.getText().toString();
        if (studentID != null && !studentID.isEmpty()) {
            int studentIDint = Integer.parseInt(studentID);

            DatabaseReference gradeKey = simpsons_database_ref.child("simpsons/grades/");
            Query query = gradeKey.orderByChild("student_id").startAt(studentIDint);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Grade grade = snapshot.getValue(Grade.class);
                            String temp = grade.getstudent_id() + ", " + grade.getcourse_name() + ", " + grade.getgrade();
                            course_names.add(temp);
                        }
                    }
                    else {
                        Toast.makeText(PullActivity.this, "WRONG", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),"failed :(((" + databaseError,Toast.LENGTH_SHORT).show();
                }
            });
            adapter = new MyRecyclerViewAdapter(this, course_names);
            recyclerView.setAdapter(adapter);
        }
        else {
            Toast.makeText(PullActivity.this, "PLEASE INPUT SOMETHING", Toast.LENGTH_SHORT).show();
        }

        /*
        int studentIDint = Integer.parseInt(studentID);

        DatabaseReference gradeKey = simpsons_database_ref.child("simpsons/grades/");
        Query query = gradeKey.orderByChild("student_id").startAt(studentIDint);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Grade grade = snapshot.getValue(Grade.class);
                        String temp = grade.getstudent_id() + ", " + grade.getcourse_name() + ", " + grade.getgrade();
                        course_names.add(temp);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"failed :(((" + databaseError,Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new MyRecyclerViewAdapter(this, course_names);
        recyclerView.setAdapter(adapter);
        */

    }

    private void signOut() {
        mAuth.signOut();
    }


}
