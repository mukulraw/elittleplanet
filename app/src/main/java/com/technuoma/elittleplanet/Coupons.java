package com.technuoma.elittleplanet;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.technuoma.elittleplanet.couponsPOJO.Datum;
import com.technuoma.elittleplanet.couponsPOJO.couponsBean;
import com.technuoma.elittleplanet.filtersPOJO.O;

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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Coupons extends Fragment {

    RecyclerView grid;
    ProgressBar progress;
    MainActivity mainActivity;
    GridLayoutManager manager;
    List<Datum> list;
    OrdersAdapter adapter;
    ImageView nodata;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coupons, container, false);
        mainActivity = (MainActivity) getActivity();

        list = new ArrayList<>();

        grid = view.findViewById(R.id.grid);
        progress = view.findViewById(R.id.progressBar12);
        nodata = view.findViewById(R.id.imageView5);

        adapter = new OrdersAdapter(list, mainActivity);

        manager = new GridLayoutManager(mainActivity, 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        getRew();


        return view;
    }

    void getRew() {

        Bean b = (Bean) mainActivity.getApplicationContext();

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

        Call<couponsBean> call = cr.getCoupons(SharePreferenceUtils.getInstance().getString("userId"));

        call.enqueue(new Callback<couponsBean>() {
            @Override
            public void onResponse(Call<couponsBean> call, Response<couponsBean> response) {
                if (response.body().getData().size() > 0) {
                    nodata.setVisibility(View.GONE);
                    adapter.setgrid(response.body().getData());
                } else {
                    nodata.setVisibility(View.VISIBLE);
                    adapter.setgrid(response.body().getData());
                    Toast.makeText(mainActivity, "No data found", Toast.LENGTH_SHORT).show();
                    //finish();
                }
            }

            @Override
            public void onFailure(Call<couponsBean> call, Throwable t) {

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

            View view = inflater.inflate(R.layout.coupon_list_item, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i1) {

            final Datum item = list.get(i1);

            //viewHolder.setIsRecyclable(false);

            viewHolder.code.setText(item.getCode());
            viewHolder.discount.setText("₹ " + item.getDiscount());
            viewHolder.min.setText("₹ " + item.getMinValue());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", item.getCode());
                    if (clipboard == null || clip == null) return;
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "Code copied successfully", Toast.LENGTH_SHORT).show();

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView code, discount, min;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                code = itemView.findViewById(R.id.textView96);
                discount = itemView.findViewById(R.id.textView105);
                min = itemView.findViewById(R.id.textView106);


            }
        }
    }

}