package com.example.tahminci1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterAct extends AppCompatActivity {
    ImageButton btn_reg_login,btn_reg_reg;
    EditText et_regemail,et_regpassword,et_isim,et_soyisim;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btn_reg_login=findViewById(R.id.im_btn_reg_log);
        btn_reg_reg=findViewById(R.id.im_btn_reg_reg);
        et_regemail=findViewById(R.id.et_mail_reg);
        et_isim=findViewById(R.id.et_name_reg);
        et_soyisim=findViewById(R.id.et_surname_rg);
        et_regpassword=findViewById(R.id.et_pass_reg);
        mAuth= FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();

        btn_reg_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), LoginAct.class);
                startActivity(intent);
                finish();

            }
        });
        btn_reg_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password,isim,soyisim;
                email=et_regemail.getText().toString();
                password=et_regpassword.getText().toString();
                isim=et_isim.getText().toString();
                soyisim=et_soyisim.getText().toString();
                if (TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterAct.this,"Email ve Sifre bos birakilamaz",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(RegisterAct.this,"Email yanlis formatta girildi",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(RegisterAct.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Map<String, Object> user=new HashMap<>();
                                    user.put("name",isim);
                                    user.put("lastname",soyisim);
                                    firebaseFirestore.collection("users").document(mAuth.getUid()).set(user);
                                    Toast.makeText(RegisterAct.this,"Hesap Olusturuldu",Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    Toast.makeText(RegisterAct.this,"Hesap Olusturulamadi",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            }
        });


    }
}