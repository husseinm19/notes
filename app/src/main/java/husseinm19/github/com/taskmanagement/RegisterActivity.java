package husseinm19.github.com.taskmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Hussein on 24-04-2020
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private Button btnRegister;

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        mDialog= new ProgressDialog(this);

        email=(EditText) findViewById(R.id.text_emailR);
        pass=(EditText) findViewById(R.id.edit_text_passwordR);

        btnRegister=(Button) findViewById(R.id.button_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mEmail=email.getText().toString().trim();
                String mPass=pass.getText().toString().trim();

                if (TextUtils.isEmpty(mEmail)&&(TextUtils.isEmpty(mPass))){
                    email.setError("Required Filed");
                    pass.setError("Required Filed");
                    return;

                }
                if (TextUtils.isEmpty(mEmail)){
                    email.setError("Required Filed");
                    return;

                }

                if (TextUtils.isEmpty(mPass)){
                    pass.setError("Required Filed");
                    return;

                }
                mDialog.setMessage("Processing...");
                mDialog.show();

                mAuth.createUserWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Register", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                            mDialog.dismiss();
                        }else {
                            Toast.makeText(RegisterActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
                });



            }
        });



    }

    public void SignIn(View view) {

        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
