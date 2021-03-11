package com.technuoma.elittleplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.technuoma.elittleplanet.ratingsPOJO.Datum;
import com.technuoma.elittleplanet.ratingsPOJO.ratingsBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Ratings extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView grid;
    ProgressBar progress;
    String id;
    List<Datum> list;
    CategoryAdapter adapter;
    ImageView nodata;
    FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        list = new ArrayList<>();

        id = getIntent().getStringExtra("id");

        toolbar = findViewById(R.id.toolbar8);
        grid = findViewById(R.id.grid);
        progress = findViewById(R.id.progressBar9);
        nodata = findViewById(R.id.imageView5);
        add = findViewById(R.id.floatingActionButton2);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Ratings and Reviews");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        adapter = new CategoryAdapter(this, list);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                Call<ratingsBean> call = cr.checkPurchase(id, SharePreferenceUtils.getInstance().getString("userId"));
                call.enqueue(new Callback<ratingsBean>() {
                    @Override
                    public void onResponse(Call<ratingsBean> call, Response<ratingsBean> response) {

                        if (response.body().getStatus().equals("1")) {
                            Dialog dialog = new Dialog(Ratings.this, R.style.MyDialogTheme);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.add_rating_dialog);
                            dialog.show();

                            RatingBar rating = dialog.findViewById(R.id.ratingBar2);
                            EditText review = dialog.findViewById(R.id.editTextTextPersonName);
                            Button submit = dialog.findViewById(R.id.button3);
                            ProgressBar bar = dialog.findViewById(R.id.progressBar10);

                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    float rat = rating.getRating();
                                    String rev = review.getText().toString();
                                    if (rat > 0) {
                                        if (rev.length() > 0) {

                                            bar.setVisibility(View.VISIBLE);

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

                                            Call<ratingsBean> call = cr.addRating(id, String.valueOf(rat), rev, SharePreferenceUtils.getInstance().getString("userId"));

                                            call.enqueue(new Callback<ratingsBean>() {
                                                @Override
                                                public void onResponse(Call<ratingsBean> call, Response<ratingsBean> response) {
                                                    if (response.body().getStatus().equals("1")) {
                                                        Toast.makeText(Ratings.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                        onResume();
                                                    } else {
                                                        Toast.makeText(Ratings.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                    bar.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onFailure(Call<ratingsBean> call, Throwable t) {
                                                    bar.setVisibility(View.GONE);
                                                }
                                            });

                                        } else {
                                            Toast.makeText(Ratings.this, "Please add a review", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(Ratings.this, "Please add a rating", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        } else {
                            Dialog dialog = new Dialog(Ratings.this, R.style.MyDialogTheme);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.not_purchsed_dialog);
                            dialog.show();
                            TextView text = dialog.findViewById(R.id.textView78);
                            text.setText(response.body().getMessage());
                        }

                        progress.setVisibility(View.GONE);


                    }

                    @Override
                    public void onFailure(Call<ratingsBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

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

        Call<ratingsBean> call = cr.getRating(id);
        call.enqueue(new Callback<ratingsBean>() {
            @Override
            public void onResponse(Call<ratingsBean> call, Response<ratingsBean> response) {


                if (response.body().getData().size() > 0) {

                    nodata.setVisibility(View.GONE);
                    adapter.setData(response.body().getData());

                } else {
                    nodata.setVisibility(View.VISIBLE);
                    adapter.setData(response.body().getData());

                }

                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<ratingsBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }

    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

        Context context;
        List<Datum> list = new ArrayList<>();

        public CategoryAdapter(Context context, List<Datum> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Datum> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.rating_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Datum item = list.get(position);

            holder.name.setText(item.getUserId());
            holder.date.setText(item.getCreated());
            holder.review.setText(item.getReview());

            float rat = Float.parseFloat(item.getRating());
            holder.rating.setRating(rat);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView name, review, date;
            RatingBar rating;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.textView75);
                review = itemView.findViewById(R.id.textView76);
                date = itemView.findViewById(R.id.textView77);
                rating = itemView.findViewById(R.id.ratingBar);


            }
        }
    }

}