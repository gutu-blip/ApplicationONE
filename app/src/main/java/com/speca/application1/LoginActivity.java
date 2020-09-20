package com.speca.application1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.speca.application1.R;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmailtext;
    private EditText loginPasswordtext;
    private Button loginbtn;
    private Button loginregisterbtn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        progressBar =findViewById(R.id.login_progressbar);

        loginEmailtext = findViewById(R.id.login_email);
        loginPasswordtext = findViewById(R.id.login_password);
        loginbtn =findViewById(R.id.login_button);
        loginregisterbtn =findViewById(R.id.login_register_button);

        loginbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String loginEmail = loginEmailtext.getText().toString();
                String loginpass =loginPasswordtext.getText().toString();

                if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginpass)){
    progressBar.setVisibility(View.VISIBLE);

    mAuth.signInWithEmailAndPassword(loginEmail,loginpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {

            if(task.isSuccessful()){
               sendToMain();

            }else{
               String error = task.getException().getMessage();
                progressBar.setVisibility(View.INVISIBLE);
                      Toast.makeText(getApplicationContext(),error ,Toast.LENGTH_LONG).show();
            }

        }
    });
                }
                else{
                    // String error = task.getException().getMessage();
                    Toast.makeText(getApplicationContext(),"Fill both fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginregisterbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentuser !=null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();


        }
    }

    private void sendToMain(){
        Intent intent = new Intent(LoginActivity.this, VideosActivity.class);
    startActivity(intent);
        finish();

}
}
