package com.technuoma.elittleplanet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Register extends AppCompatActivity {

    ImageButton back;
    EditText name, email, address, pin, password, phone;
    Button login;
    ProgressBar progress;
    CheckBox check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        back = findViewById(R.id.imageButton);
        name = findViewById(R.id.editTextTextPersonName2);
        email = findViewById(R.id.editText);
        address = findViewById(R.id.editTextTextPostalAddress);
        pin = findViewById(R.id.editTextNumber);
        password = findViewById(R.id.editTextTextPassword);
        login = findViewById(R.id.button);
        progress = findViewById(R.id.progressBar);
        phone = findViewById(R.id.editText11);
        check = findViewById(R.id.check);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        SpannableString myString = new SpannableString("I agree to Privacy Policy and Terms and Conditions");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NotNull View textView) {

                Intent intent = new Intent(Register.this, Web.class);
                intent.putExtra("title", "Privacy Policy");
                intent.putExtra("url", "https://technuoma.com/elittleplanet/privacy.php");
                startActivity(intent);

            }
        };

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NotNull View textView) {

                Intent intent = new Intent(Register.this, Web.class);
                intent.putExtra("title", "Terms & Conditions");
                intent.putExtra("url", "https://technuoma.com/elittleplanet/terms.php");
                startActivity(intent);

            }
        };


        //For Click
        myString.setSpan(clickableSpan, 11, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        myString.setSpan(clickableSpan2, 30, 50, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //For UnderLine
        myString.setSpan(new UnderlineSpan(), 11, 25, 0);
        myString.setSpan(new UnderlineSpan(), 30, 50, 0);

        //For Bold
        myString.setSpan(new StyleSpan(Typeface.BOLD), 11, 25, 0);
        myString.setSpan(new StyleSpan(Typeface.BOLD), 30, 50, 0);

        //Finally you can set to textView.

        check.setText(myString);
        check.setMovementMethod(LinkMovementMethod.getInstance());


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String f = name.getText().toString();
                String e = email.getText().toString();
                String a = address.getText().toString();
                String p = pin.getText().toString();
                String pa = password.getText().toString();
                String ph = phone.getText().toString();

                if (f.length() > 0) {
                    if (e.length() > 0) {
                        if (ph.length() == 10) {
                            if (a.length() > 0) {
                                if (p.length() == 6) {
                                    if (pa.length() > 0) {
                                        if (check.isChecked()) {
                                            progress.setVisibility(View.VISIBLE);

                                            Bean b = (Bean) getApplicationContext();

                                            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                                            logging.level(HttpLoggingInterceptor.Level.HEADERS);
                                            logging.level(HttpLoggingInterceptor.Level.BODY);

                                            OkHttpClient client = new OkHttpClient.Builder().writeTimeout(1000, TimeUnit.SECONDS).readTimeout(1000, TimeUnit.SECONDS).connectTimeout(1000, TimeUnit.SECONDS).addInterceptor(logging).build();

                                            Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl(b.baseurl)
                                                    .client(client)
                                                    .addConverterFactory(ScalarsConverterFactory.create())
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();

                                            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                                            Call<loginBean> call = cr.register(
                                                    e,
                                                    pa,
                                                    SharePreferenceUtils.getInstance().getString("token"),
                                                    ph,
                                                    f,
                                                    a,
                                                    p,
                                                    SharePreferenceUtils.getInstance().getString("referrer")
                                            );

                                            call.enqueue(new Callback<loginBean>() {
                                                @Override
                                                public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                                                    if (response.body().getStatus().equals("1")) {
                                                        //SharePreferenceUtils.getInstance().saveString("userId" , response.body().getUserId());
                                                        SharePreferenceUtils.getInstance().saveString("phone", response.body().getPhone());
                                                        Toast.makeText(Register.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(Register.this, OTP.class);
                                                        intent.putExtra("userid", response.body().getUserId());
                                                        startActivity(intent);
                                                        finishAffinity();

                                                    } else {
                                                        Toast.makeText(Register.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                    progress.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onFailure(Call<loginBean> call, Throwable t) {
                                                    progress.setVisibility(View.GONE);
                                                }
                                            });
                                        } else {
                                            Toast.makeText(Register.this, "Please agree to our Privacy Policy and Terms and Conditions", Toast.LENGTH_SHORT).show();
                                        }


                                    } else {
                                        Toast.makeText(Register.this, "Invalid password", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Register.this, "Invalid PIN", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Register.this, "Invalid address", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Register.this, "Invalid phone", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Register.this, "Invalid email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Register.this, "Invalid name", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}