package com.technuoma.elittleplanet;

import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.technuoma.elittleplanet.brandPOJO.Datum;
import com.technuoma.elittleplanet.brandPOJO.brandBean;

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

public class Find extends Fragment {

    EditText min, max;
    RadioGroup grid;
    ProgressBar progress;
    Button proceed;
    MainActivity mainActivity;
    List<Datum> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find, container, false);
        mainActivity = (MainActivity) getActivity();

        list = new ArrayList<>();

        min = view.findViewById(R.id.editTextNumber2);
        max = view.findViewById(R.id.editTextNumber3);
        grid = view.findViewById(R.id.grid);
        progress = view.findViewById(R.id.progressBar8);
        proceed = view.findViewById(R.id.button4);

        progress.setVisibility(View.VISIBLE);

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

        Call<brandBean> call = cr.getBrands();

        call.enqueue(new Callback<brandBean>() {
            @Override
            public void onResponse(Call<brandBean> call, Response<brandBean> response) {

                if (response.body().getStatus().equals("1")) {

                    grid.removeAllViews();

                    for (int i = 0; i < response.body().getData().size(); i++) {
                        RadioButton btn = new RadioButton(mainActivity);
                        btn.setText(response.body().getData().get(i).getBrand());
                        grid.addView(btn);
                    }

                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<brandBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mi = min.getText().toString();
                String ma = max.getText().toString();

                if (mi.length() > 0)
                {
                    if (ma.length() > 0)
                    {
                        int iidd = grid.getCheckedRadioButtonId();
                        if (iidd > -1)
                        {
                            RadioButton btn = grid.findViewById(iidd);
                            String brand = btn.getText().toString();
                            Log.d("min", mi);
                            Log.d("max", ma);
                            Log.d("brand", brand);

                            FragmentManager fm31 = mainActivity.getSupportFragmentManager();
                            FragmentTransaction ft31 = fm31.beginTransaction();
                            ft31.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            result frag131 = new result();
                            Bundle b = new Bundle();
                            b.putString("min", mi);
                            b.putString("max", ma);
                            b.putString("brand", brand);
                            frag131.setArguments(b);
                            ft31.replace(R.id.replace, frag131);
                            ft31.addToBackStack(null);
                            ft31.commit();
                        }
                        else
                        {
                            Toast.makeText(mainActivity, "Please select a brand", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(mainActivity, "Please enter a max. price", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(mainActivity, "Please enter a min. price", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

        final Context context;

        List<Datum> flist;

        final List<String> checked = new ArrayList<>();

        FilterAdapter(Context context, List<Datum> flist) {
            this.context = context;
            this.flist = flist;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.filter_list_model, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

            viewHolder.setIsRecyclable(false);

            final Datum item = flist.get(i);

            viewHolder.check.setText(item.getBrand());

            viewHolder.check.setChecked(true);

            checked.add(item.getBrand());

            viewHolder.check.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {

                    checked.add(item.getBrand());

                } else {

                    checked.remove(item.getBrand());

                }

            });

        }

        List<String> getChecked() {
            return checked;
        }

        @Override
        public int getItemCount() {
            return flist.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            final CheckBox check;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                check = itemView.findViewById(R.id.check);

            }
        }
    }

}
