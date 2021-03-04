package com.technuoma.elittleplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.technuoma.elittleplanet.notiPOJO.Datum;
import com.technuoma.elittleplanet.notiPOJO.notiBeam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Notifications extends AppCompatActivity {

    private Toolbar toolbar;
    ProgressBar progress;
    String base;
    OrdersAdapter adapter;
    List<Datum> list;

    //CartAdapter adapter;

    GridLayoutManager manager;

    RecyclerView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        list = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar3);
        progress = findViewById(R.id.progressBar3);
        grid = findViewById(R.id.grid);
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
        toolbar.setTitle("Notifications");


        adapter = new OrdersAdapter(list, this);

        manager = new GridLayoutManager(this, 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);


    }

    @Override
    protected void onResume() {
        super.onResume();

        loadCart();

    }

    void loadCart() {
        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


        Call<notiBeam> call = cr.getNotification(SharePreferenceUtils.getInstance().getString("userId"));
        call.enqueue(new Callback<notiBeam>() {
            @Override
            public void onResponse(Call<notiBeam> call, Response<notiBeam> response) {

                if (response.body().getData().size() > 0) {


                    adapter.setgrid(response.body().getData());

                } else {
                    adapter.setgrid(response.body().getData());
                    Toast.makeText(Notifications.this, "No address found", Toast.LENGTH_SHORT).show();
                    finish();
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<notiBeam> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }

    class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

        List<Datum> list = new ArrayList<>();
        Context context;
        LayoutInflater inflater;

        OrdersAdapter(List<Datum> list, Context context) {
            this.context = context;
            this.list = list;
        }

        void setgrid(List<Datum> list) {

            this.list = list;
            notifyDataSetChanged();

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.noti_list_item, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i1) {

            final Datum item = list.get(i1);

            viewHolder.txn.setText(item.getText());
            viewHolder.date.setText(item.getCreated());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txn, date;

            ImageButton delete;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                txn = itemView.findViewById(R.id.textView27);
                date = itemView.findViewById(R.id.textView71);


            }
        }
    }

}