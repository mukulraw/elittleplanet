package com.technuoma.elittleplanet;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.technuoma.elittleplanet.seingleProductPOJO.Data;
import com.technuoma.elittleplanet.seingleProductPOJO.singleProductBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.relex.circleindicator.CircleIndicator;
import nl.dionsegijn.steppertouch.StepperTouch;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SingleProduct extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager image;
    TextView discount, title, price;
    Button add;
    TextView brand, unit, seller;
    TextView description, key_features, packaging, life, disclaimer, stock;
    ProgressBar progress;

    String id, name;

    String pid, nv1;

    CircleIndicator indicator;
    TabLayout tabs;

    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("title");

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        indicator = findViewById(R.id.indicator);
        toolbar = findViewById(R.id.toolbar);
        tabs = findViewById(R.id.tabLayout);
        image = findViewById(R.id.image);
        discount = findViewById(R.id.discount);
        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        add = findViewById(R.id.add);
        brand = findViewById(R.id.brand);
        unit = findViewById(R.id.unit);
        seller = findViewById(R.id.seller);
        description = findViewById(R.id.description);
        key_features = findViewById(R.id.key_features);
        packaging = findViewById(R.id.packaging);
        life = findViewById(R.id.life);
        disclaimer = findViewById(R.id.disclaimer);
        progress = findViewById(R.id.progress);
        stock = findViewById(R.id.stock);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(name);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (pid.length() > 0) {
                    String uid = SharePreferenceUtils.getInstance().getString("userId");

                    if (uid.length() > 0) {

                        final Dialog dialog = new Dialog(SingleProduct.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.add_cart_dialog);
                        dialog.show();

                        final StepperTouch stepperTouch = dialog.findViewById(R.id.stepperTouch);
                        Button add = dialog.findViewById(R.id.button8);
                        final ProgressBar progressBar = dialog.findViewById(R.id.progressBar2);


                        stepperTouch.setMinValue(1);
                        stepperTouch.setMaxValue(99);
                        stepperTouch.setSideTapEnabled(true);
                        stepperTouch.setCount(1);

                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                progressBar.setVisibility(View.VISIBLE);

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

                                Log.d("userid", SharePreferenceUtils.getInstance().getString("userid"));
                                Log.d("pid", pid);
                                Log.d("quantity", String.valueOf(stepperTouch.getCount()));
                                Log.d("price", nv1);

                                int versionCode = BuildConfig.VERSION_CODE;
                                String versionName = BuildConfig.VERSION_NAME;

                                Call<singleProductBean> call = cr.addCart(SharePreferenceUtils.getInstance().getString("userId"), pid, String.valueOf(stepperTouch.getCount()), nv1, versionName);

                                call.enqueue(new Callback<singleProductBean>() {
                                    @Override
                                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                        if (response.body().getStatus().equals("1")) {
                                            //loadCart();
                                            dialog.dismiss();
                                        }

                                        Toast.makeText(SingleProduct.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                        progressBar.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onFailure(Call<singleProductBean> call, Throwable t) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });


                            }
                        });

                    } else {
                        Toast.makeText(SingleProduct.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SingleProduct.this, Login.class);
                        startActivity(intent);

                    }
                }


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
        Log.d("iidd", id);
        Call<singleProductBean> call = cr.getProductById(id);
        call.enqueue(new Callback<singleProductBean>() {
            @Override
            public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {


                if (response.body().getStatus().equals("1")) {
                    Data item = response.body().getData();

                    pid = item.getId();

                    PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, item.getImage());
                    image.setAdapter(adapter);
                    indicator.setViewPager(image);

                    for (int i = 0; i < item.getImage().size(); i++) {
                        tabs.addTab(tabs.newTab().setCustomView(getCustomView(inflater, item.getImage().get(i))));
                    }
                    tabs.setupWithViewPager(image);
                    for (int i = 0; i < item.getImage().size(); i++) {
                        tabs.getTabAt(i).setCustomView(getCustomView(inflater, item.getImage().get(i)));
                    }

                    float dis = Float.parseFloat(item.getDiscount());

                    if (dis > 0) {

                        float pri = Float.parseFloat(item.getPrice());
                        float dv = (dis / 100) * pri;

                        float nv = pri - dv;

                        nv1 = String.valueOf(nv);

                        discount.setVisibility(View.VISIBLE);
                        discount.setText(item.getDiscount() + "% OFF");
                        price.setText(Html.fromHtml("<font color=\"#000000\"><b>\u20B9 " + String.valueOf(nv) + " </b></font><strike>\u20B9 " + item.getPrice() + "</strike>"));
                    } else {

                        nv1 = item.getPrice();
                        discount.setVisibility(View.GONE);
                        price.setText(Html.fromHtml("<font color=\"#000000\"><b>\u20B9 " + String.valueOf(item.getPrice()) + " </b></font>"));
                    }


                    title.setText(item.getName());

                    brand.setText(item.getBrand());
                    unit.setText(item.getUnit());
                    seller.setText(item.getSeller());

                    description.setText(item.getDescription());
                    key_features.setText(item.getKeyFeatures());
                    packaging.setText(item.getPackagingType());
                    life.setText(item.getShelfLife());
                    disclaimer.setText(item.getDisclaimer());


                    if (item.getStock().equals("In stock")) {
                        add.setEnabled(true);
                    } else {
                        add.setEnabled(false);
                    }

                    stock.setText(item.getStock());


                }

                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<singleProductBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        List<String> list = new ArrayList<>();

        public PagerAdapter(@NonNull FragmentManager fm, int behavior, List<String> list) {
            super(fm, behavior);
            this.list = list;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            page frag = new page();
            Bundle b = new Bundle();
            b.putString("image", list.get(position));
            frag.setArguments(b);
            return frag;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    public static class page extends Fragment {

        String url;
        ImageView image;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.page3, container, false);

            url = getArguments().getString("image");

            image = view.findViewById(R.id.image);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(url, image, options);

            return view;
        }
    }

    private View getCustomView(LayoutInflater inflater, String url) {
        View view = getLayoutInflater().inflate(R.layout.tabs_layout, null);

        ImageView image = view.findViewById(R.id.imageView);

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(url, image, options);

        return view;
    }

}
