package com.technuoma.elittleplanet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.technuoma.elittleplanet.seingleProductPOJO.singleProductBean;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EditAddress extends AppCompatActivity {

    Toolbar toolbar;
    EditText name, house, pin;
    ProgressBar progress;
    Button submit;
    String id, address, housenumber, pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        id = getIntent().getStringExtra("id");
        address = getIntent().getStringExtra("name");
        housenumber = getIntent().getStringExtra("house");
        pincode = getIntent().getStringExtra("pin");

        toolbar = findViewById(R.id.toolbar4);
        name = findViewById(R.id.editText2);
        house = findViewById(R.id.editText3);
        pin = findViewById(R.id.button7);
        progress = findViewById(R.id.progressBar4);
        submit = findViewById(R.id.button6);

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Add Address");

        name.setText(address);
        house.setText(housenumber);
        pin.setText(pincode);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String n = name.getText().toString();
                String h = house.getText().toString();
                String p = pin.getText().toString();

                if (n.length() > 0) {
                    if (h.length() > 0) {
                        if (p.length() == 6) {

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

                            Call<singleProductBean> call = cr.editAddress(
                                    id,
                                    h,
                                    "",
                                    "",
                                    p,
                                    n
                            );

                            call.enqueue(new Callback<singleProductBean>() {
                                @Override
                                public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                    if (response.body().getStatus().equals("1")) {
                                        finish();
                                    }

                                    Toast.makeText(EditAddress.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                    progress.setVisibility(View.GONE);

                                }

                                @Override
                                public void onFailure(Call<singleProductBean> call, Throwable t) {
                                    progress.setVisibility(View.GONE);
                                }
                            });

                                /*addressPostPOJO body = new addressPostPOJO();
                                body.setUserId(String.valueOf(SharePreferenceUtils.getInstance().getInteger("id")));
                                body.setName(n);
                                body.setHouse(h);
                                body.setArea("");
                                body.setCity(c);
                                body.setPin(p);

                                Call<cartBean> call = cr.addAddress(body);
                                call.enqueue(new Callback<cartBean>() {
                                    @Override
                                    public void onResponse(Call<cartBean> call, Response<cartBean> response) {
                                        if (response.body().getStatus().equals("success")) {

                                            Toast.makeText(AddAddress.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                            finish();

                                        } else {
                                            Toast.makeText(AddAddress.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        progress.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFailure(Call<cartBean> call, Throwable t) {
                                        progress.setVisibility(View.GONE);
                                    }
                                });*/

                        } else {
                            Toast.makeText(EditAddress.this, "Please enter a valid PIN code", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditAddress.this, "Please enter a valid address", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditAddress.this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}