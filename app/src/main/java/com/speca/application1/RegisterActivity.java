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

public class RegisterActivity extends AppCompatActivity {

    private EditText register_email_field;
    private EditText register_pass_field;
    private EditText register_confirm_pass_field;
    private Button reg_btn;
    private Button reg_login_btn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_email_field =findViewById(R.id.register_email);
        register_pass_field = findViewById(R.id.register_password);
        register_confirm_pass_field =findViewById(R.id.register_confirmpass);
        reg_btn =findViewById(R.id.register_btn);
        reg_login_btn =findViewById(R.id.register_login_btn);
        mAuth = FirebaseAuth.getInstance();
        progressBar =findViewById(R.id.register_progressbar);


        reg_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String email =register_email_field.getText().toString();
                String pass =register_pass_field.getText().toString();
                String confirm_pass =register_confirm_pass_field.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(confirm_pass)){

                    if(pass.equals(confirm_pass)){

                        progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           sendToMain();
                       }else{
                        String error =task.getException().getMessage();
                           Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
                       }

                       progressBar.setVisibility(View.INVISIBLE);

                        }
                    });
                    }else{
                        Toast.makeText(getApplicationContext(),"Password mismatch :",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "Fill all the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

    reg_login_btn.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentuser != null){
            sendToMain();
        }
    }

private void sendToMain(){
    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
    startActivity(intent);
    finish();

}
}
