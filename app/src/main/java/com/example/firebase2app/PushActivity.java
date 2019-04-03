package com.example.firebase2app;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PushActivity extends AppCompatActivity {

    private RadioGroup mStudentGroup;
    private RadioButton mStudent;
    private EditText mCourseIDField;
    private EditText mCourseNameField;
    private EditText mGradeField;
    private Button mPushButton;
    private Button mGoBackToPullButton;

    private FirebaseAuth mAuth;
    private FirebaseDatabase simpsons_database;
    private DatabaseReference simpsons_database_ref;
    private FirebaseUser user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        // Radio Button stuff
        mStudentGroup = (RadioGroup) findViewById(R.id.radioGroup);

        // Edit Text Stuff
        mCourseIDField = (EditText) findViewById(R.id.courseIDfield);
        mCourseNameField = (EditText) findViewById(R.id.courseNameField);
        mGradeField = (EditText) findViewById(R.id.gradeField);

        // Buttons
        mPushButton = (Button) findViewById(R.id.pushButton);
        mPushButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {
                pushGradeToDatabase();
                // goBackToPullActivity();
            }
        });
        mGoBackToPullButton = (Button) findViewById(R.id.goToPullButton);
        mGoBackToPullButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {
                goBackToPullActivity();
            }
        });

        // Firebase Things
        mAuth = FirebaseAuth.getInstance();
        simpsons_database = FirebaseDatabase.getInstance();
        simpsons_database_ref = simpsons_database.getReference();

    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
    }

    public void goBackToPullActivity() {
        Intent intent = new Intent (this, PullActivity.class);
        startActivity(intent);
    }

    public void pushGradeToDatabase() {
        // get student from radio button

        int selectedID = mStudentGroup.getCheckedRadioButtonId();
        int student_id = 0;
        if (selectedID == -1) {
            Toast.makeText(PushActivity.this, "please check one radio button", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            mStudent = (RadioButton) findViewById(selectedID);
            String student_name = (String) mStudent.getText();
            student_id = getStudentID(student_name); // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        }
        /*
        mStudent = (RadioButton) findViewById(selectedID);
        String student_name = (String) mStudent.getText();
        int student_id = getStudentID(student_name);
        // Toast.makeText(PushActivity.this, Integer.toString(student_id), Toast.LENGTH_SHORT).show();
        */

        // get course_id
        int courseIDint = 0;
        String courseID = mCourseIDField.getText().toString();
        if (courseID != null && !courseID.isEmpty()) {
            courseIDint = Integer.parseInt(courseID); // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            // Toast.makeText(PushActivity.this, Integer.toString(courseIDint), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(PushActivity.this, "COURSE ID REQUIRED", Toast.LENGTH_SHORT).show();
            return;
        }

        // get course name
        String courseName = mCourseNameField.getText().toString();
        if (courseName != null && !courseName.isEmpty()) {
            // Toast.makeText(PushActivity.this, courseName, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(PushActivity.this, "COURSE NAME REQUIRED", Toast.LENGTH_SHORT).show();
            return;
        }

        // get grade
        String grade = mGradeField.getText().toString();
        if (grade != null && !grade.isEmpty()) {
            // Toast.makeText(PushActivity.this, grade, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(PushActivity.this, "GRADE REQUIRED", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference gradeKey = simpsons_database_ref.child("simpsons/grades/");
        DatabaseReference newGrade = gradeKey.push();
        newGrade.child("course_id").setValue(courseIDint);
        newGrade.child("course_name").setValue(courseName);
        newGrade.child("grade").setValue(grade);
        newGrade.child("student_id").setValue(student_id);
        Toast.makeText(PushActivity.this, "PUSHED!", Toast.LENGTH_SHORT).show();
    }

    public int getStudentID (String name) {
        int student_id = 123;
        switch (name) {
            case "Bart":
                student_id = 123;
                break;
            case "Ralph":
                student_id = 404;
                break;
            case "Milhouse":
                student_id = 456;
                break;
            case "Lisa":
                student_id = 888;
                break;
        }
        return student_id;
    }

}
