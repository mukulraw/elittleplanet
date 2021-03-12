package com.technuoma.elittleplanet;

import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.technuoma.elittleplanet.filtersPOJO.O;
import com.technuoma.elittleplanet.filtersPOJO.filtersBean;

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
    Spinner brand, ram, internal_storage, network, os, camera, battery;
    ProgressBar progress;
    Button proceed;
    MainActivity mainActivity;
    List<String> brandlist;
    List<String> ramlist, ramidlist;
    List<String> internallist, internalidlist;
    List<String> networklist, networkidlist;
    List<String> oslist, osidlist;
    List<String> cameralist, cameraidlist;
    List<String> batterylist, batteryidlist;

    String brr = "", raa = "", inn = "", nee = "", oss = "", caa = "", baa = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find, container, false);
        mainActivity = (MainActivity) getActivity();

        brandlist = new ArrayList<>();
        ramlist = new ArrayList<>();
        ramidlist = new ArrayList<>();
        internallist = new ArrayList<>();
        internalidlist = new ArrayList<>();
        networklist = new ArrayList<>();
        networkidlist = new ArrayList<>();
        oslist = new ArrayList<>();
        osidlist = new ArrayList<>();
        cameralist = new ArrayList<>();
        cameraidlist = new ArrayList<>();
        batterylist = new ArrayList<>();
        batteryidlist = new ArrayList<>();

        min = view.findViewById(R.id.editTextNumber2);
        max = view.findViewById(R.id.editTextNumber3);
        brand = view.findViewById(R.id.brand);
        ram = view.findViewById(R.id.ram);
        internal_storage = view.findViewById(R.id.internal_storage);
        network = view.findViewById(R.id.network);
        os = view.findViewById(R.id.os);
        camera = view.findViewById(R.id.camera);
        battery = view.findViewById(R.id.battery);
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
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        brandlist.add(response.body().getData().get(i).getBrand());
                    }

                    ArrayAdapter<String> brandadapter = new ArrayAdapter<>(mainActivity,
                            android.R.layout.simple_list_item_1, brandlist);
                    brand.setAdapter(brandadapter);
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<brandBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

        brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brr = brandlist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Call<filtersBean> call1 = cr.getFilters();
        call1.enqueue(new Callback<filtersBean>() {
            @Override
            public void onResponse(Call<filtersBean> call, Response<filtersBean> response) {

                if (response.body().getStatus().equals("1")) {
                    for (int i = 0; i < response.body().getRam().size(); i++) {
                        ramlist.add(response.body().getRam().get(i).getValue());
                        ramidlist.add(response.body().getRam().get(i).getId());
                    }

                    ArrayAdapter<String> ramAdapter = new ArrayAdapter<>(mainActivity,
                            android.R.layout.simple_list_item_1, ramlist);
                    ram.setAdapter(ramAdapter);


                    for (int i = 0; i < response.body().getInternalStorage().size(); i++) {
                        internallist.add(response.body().getInternalStorage().get(i).getValue());
                        internalidlist.add(response.body().getInternalStorage().get(i).getId());
                    }

                    ArrayAdapter<String> internalAdapter = new ArrayAdapter<>(mainActivity,
                            android.R.layout.simple_list_item_1, internallist);
                    internal_storage.setAdapter(internalAdapter);

                    for (int i = 0; i < response.body().getNetwork().size(); i++) {
                        networklist.add(response.body().getNetwork().get(i).getValue());
                        networkidlist.add(response.body().getNetwork().get(i).getId());
                    }

                    ArrayAdapter<String> networkAdapter = new ArrayAdapter<>(mainActivity,
                            android.R.layout.simple_list_item_1, networklist);
                    network.setAdapter(networkAdapter);

                    for (int i = 0; i < response.body().getOs().size(); i++) {
                        oslist.add(response.body().getOs().get(i).getValue());
                        osidlist.add(response.body().getOs().get(i).getId());
                    }

                    ArrayAdapter<String> osAdapter = new ArrayAdapter<>(mainActivity,
                            android.R.layout.simple_list_item_1, oslist);
                    os.setAdapter(osAdapter);

                    for (int i = 0; i < response.body().getCamera().size(); i++) {
                        cameralist.add(response.body().getCamera().get(i).getValue());
                        cameraidlist.add(response.body().getCamera().get(i).getId());
                    }

                    ArrayAdapter<String> cameraAdapter = new ArrayAdapter<>(mainActivity,
                            android.R.layout.simple_list_item_1, cameralist);
                    camera.setAdapter(cameraAdapter);

                    for (int i = 0; i < response.body().getBattery().size(); i++) {
                        batterylist.add(response.body().getBattery().get(i).getValue());
                        batteryidlist.add(response.body().getBattery().get(i).getId());
                    }

                    ArrayAdapter<String> batteryAdapter = new ArrayAdapter<>(mainActivity,
                            android.R.layout.simple_list_item_1, batterylist);
                    battery.setAdapter(batteryAdapter);

                }

            }

            @Override
            public void onFailure(Call<filtersBean> call, Throwable t) {

            }
        });

        ram.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                raa = ramidlist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        internal_storage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inn = internalidlist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        network.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nee = networkidlist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        os.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                oss = osidlist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        camera.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                caa = cameraidlist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        battery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                baa = batteryidlist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mi = min.getText().toString();
                String ma = max.getText().toString();

                if (mi.length() > 0) {
                    if (ma.length() > 0) {

                            FragmentManager fm31 = mainActivity.getSupportFragmentManager();
                            FragmentTransaction ft31 = fm31.beginTransaction();
                            ft31.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            result frag131 = new result();
                            Bundle b = new Bundle();
                            b.putString("min", mi);
                            b.putString("max", ma);
                            b.putString("brand", brr);
                            b.putString("ram", raa);
                            b.putString("internal_storage", inn);
                            b.putString("network", nee);
                            b.putString("os", oss);
                            b.putString("camera", caa);
                            b.putString("battery", baa);
                            frag131.setArguments(b);
                            ft31.replace(R.id.replace, frag131);
                            ft31.addToBackStack(null);
                            ft31.commit();

                    } else {
                        Toast.makeText(mainActivity, "Please enter a max. price", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mainActivity, "Please enter a min. price", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }


}
