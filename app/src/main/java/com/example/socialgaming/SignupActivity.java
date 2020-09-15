package com.example.socialgaming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private Button btn_signup;
    private EditText etfullname,etusername,etemail,etpassword;
    private TextView txt_already_acc;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btn_signup = (Button)findViewById(R.id.signup_btn);
        etfullname = (EditText)findViewById(R.id.ETfullname_signup);
        etusername = (EditText)findViewById(R.id.ETusername_signup);
        etpassword = (EditText)findViewById(R.id.ETpasswrd_signup);
        etemail = (EditText)findViewById(R.id.ETemail_signup);
        txt_already_acc = (TextView)findViewById(R.id.already_txt);
        txt_already_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this,SigninActivity.class);
                startActivity(intent);
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        final String fullname = etfullname.getText().toString();
        final String username = etusername.getText().toString();
        final String email = etemail.getText().toString();
        final String password = etpassword.getText().toString();

        if (TextUtils.isEmpty(fullname)){
            Toast.makeText(this, "enter full name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "enter full name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "enter full name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "enter full name", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog = new ProgressDialog(SignupActivity.this);
            progressDialog.setTitle("Creating account");
            progressDialog.setMessage("please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Saveuserinfo(fullname,username,email,password);
                            }
                            else{
                                String message = task.getException().toString();
                                Toast.makeText(SignupActivity.this, "Error"+message, Toast.LENGTH_SHORT).show();
                                Log.i("error","error"+message);
                                mAuth.signOut();
                                progressDialog.dismiss();
                            }
                        }
                    });

        }
    }

    private void Saveuserinfo(String fullname, String username, String email, String password) {
        String currentuserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> userMap = new HashMap<>();
        userMap.put("full name",fullname);
        userMap.put("user name",username);
        userMap.put("email",email);
        userMap.put("password",password);
        userMap.put("uId",currentuserId);

        usersRef.child(currentuserId).setValue(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           progressDialog.dismiss();
                           Toast.makeText(SignupActivity.this, "Account created Successfully", Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(SignupActivity.this,Home.class);
                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(intent);
                           finish();
                       }
                       else {
                           String message = task.getException().toString();
                           Toast.makeText(SignupActivity.this, "Error"+message, Toast.LENGTH_SHORT).show();
                           FirebaseAuth.getInstance().signOut();
                           progressDialog.dismiss();
                       }
                    }
                });
    }
}
