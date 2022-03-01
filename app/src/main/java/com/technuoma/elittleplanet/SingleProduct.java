package com.technuoma.elittleplanet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jsibbold.zoomage.ZoomageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.technuoma.elittleplanet.homePOJO.Best;
import com.technuoma.elittleplanet.homePOJO.homeBean;
import com.technuoma.elittleplanet.seingleProductPOJO.Data;
import com.technuoma.elittleplanet.seingleProductPOJO.Related;
import com.technuoma.elittleplanet.seingleProductPOJO.Size;
import com.technuoma.elittleplanet.seingleProductPOJO.singleProductBean;

import java.util.ArrayList;
import java.util.Arrays;
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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SingleProduct extends Fragment {

    Toolbar toolbar;
    ViewPager image;
    TextView discount, title, price;
    Button add;
    TextView brand, unit, seller;
    TextView description, key_features, packaging, life, disclaimer, stock;
    TextView descriptiontitle, key_featurestitle, packagingtitle, lifetitle;
    ProgressBar progress;

    TextView relatedtitle, save;

    String id, name;

    String pid, nv1;

    CircleIndicator indicator;

    RecyclerView recent, loved;
    List<Related> list;
    List<Best> list2;
    BestAdapter adapter2;
    RelatedAdapter adapter3;

    ImageButton wishlist, share;

    ImageButton cart1;
    TextView count;

    static MainActivity mainActivity;

    RecyclerView variants, colors;

    String siz = "", col = "";

    TextView sizetitle, colorstitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_single_product, container, false);
        mainActivity = (MainActivity) getActivity();

        list = new ArrayList<>();
        list2 = new ArrayList<>();

        id = getArguments().getString("id");
        name = getArguments().getString("title");

        sizetitle = view.findViewById(R.id.sizetitle);
        colorstitle = view.findViewById(R.id.colorstitle);
        variants = view.findViewById(R.id.variants);
        colors = view.findViewById(R.id.colors);
        loved = view.findViewById(R.id.loved);
        relatedtitle = view.findViewById(R.id.relatedtitle);
        save = view.findViewById(R.id.save);
        count = view.findViewById(R.id.count);
        cart1 = view.findViewById(R.id.imageButton3);
        wishlist = view.findViewById(R.id.wishlist);
        recent = view.findViewById(R.id.recent);
        toolbar = view.findViewById(R.id.toolbar);
        descriptiontitle = view.findViewById(R.id.descriptiontitle);
        key_featurestitle = view.findViewById(R.id.key_featurestitle);
        packagingtitle = view.findViewById(R.id.packagingtitle);
        lifetitle = view.findViewById(R.id.lifetitle);
        indicator = view.findViewById(R.id.indicator);
        image = view.findViewById(R.id.image);
        discount = view.findViewById(R.id.discount);
        title = view.findViewById(R.id.title);
        price = view.findViewById(R.id.price);
        add = view.findViewById(R.id.add);
        brand = view.findViewById(R.id.brand);
        unit = view.findViewById(R.id.unit);
        seller = view.findViewById(R.id.seller);
        description = view.findViewById(R.id.description);
        key_features = view.findViewById(R.id.key_features);
        packaging = view.findViewById(R.id.packaging);
        life = view.findViewById(R.id.life);
        disclaimer = view.findViewById(R.id.disclaimer);
        progress = view.findViewById(R.id.progress);
        stock = view.findViewById(R.id.stock);
        share = view.findViewById(R.id.share);

        adapter2 = new BestAdapter(mainActivity, list2);
        adapter3 = new RelatedAdapter(mainActivity, list);
        LinearLayoutManager manager1 = new LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager manager2 = new LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false);

        recent.setAdapter(adapter2);
        recent.setLayoutManager(manager1);

        loved.setAdapter(adapter3);
        loved.setLayoutManager(manager2);


        descriptiontitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (description.getVisibility() == View.GONE) {
                    description.setVisibility(View.VISIBLE);
                } else {
                    description.setVisibility(View.GONE);
                }
            }
        });

        key_featurestitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (key_features.getVisibility() == View.GONE) {
                    key_features.setVisibility(View.VISIBLE);
                } else {
                    key_features.setVisibility(View.GONE);
                }
            }
        });


        packagingtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (packaging.getVisibility() == View.GONE) {
                    packaging.setVisibility(View.VISIBLE);
                } else {
                    packaging.setVisibility(View.GONE);
                }
            }
        });


        lifetitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mainActivity, Ratings.class);
                intent.putExtra("id", id);
                mainActivity.startActivity(intent);

            }
        });


        cart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //navigation.setSelectedItemId(R.id.action_cart);


            }
        });


        return view;

    }


    @Override
    public void onResume() {
        super.onResume();

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

        Call<singleProductBean> call = cr.getProductById(id, SharePreferenceUtils.getInstance().getString("userId"));
        call.enqueue(new Callback<singleProductBean>() {
            @Override
            public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {


                if (response.body().getStatus().equals("1")) {
                    Data item = response.body().getData();

                    pid = item.getId();

                    BannerAdapter adapter = new BannerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, item.getImage());
                    image.setAdapter(adapter);
                    indicator.setViewPager(image);

                    if (item.getRelated().size() > 0) {
                        adapter3.setData(item.getRelated());
                        loved.setVisibility(View.VISIBLE);
                        relatedtitle.setVisibility(View.VISIBLE);
                    } else {
                        adapter3.setData(item.getRelated());
                        loved.setVisibility(View.GONE);
                        relatedtitle.setVisibility(View.GONE);
                    }

                    share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ShareCompat.IntentBuilder.from(mainActivity)
                                    .setType("text/plain")
                                    .setChooserTitle(item.getName())
                                    .setText("https://elittleplanet.com/product.php?id=" + id)
                                    .startChooser();

                            /*Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT,
                                    item.getName() + " : https://elittleplanet.com/product.php?id=" + id);
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);*/

                        }
                    });


                    /*String clr = item.getColor();
                    String[] arr = clr.split(",");

                    ColorAdapter adapter1 = new ColorAdapter(mainActivity, Arrays.asList(arr));
                    LinearLayoutManager manager = new LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false);
                    colors.setAdapter(adapter1);
                    colors.setLayoutManager(manager);

                    adapter1.selectPos(0);*/

                    if (item.getColor().length() > 0) {

                        colors.setVisibility(View.VISIBLE);
                        colorstitle.setVisibility(View.VISIBLE);

                        List<sizeBean> lis = new ArrayList<>();

                        sizeBean bean1 = new sizeBean();
                        bean1.setId(pid);
                        bean1.setSize(item.getColor());
                        bean1.setType("current");
                        bean1.setName(item.getName());
                        lis.add(bean1);

                        for (int i = 0; i < response.body().getData().getAdditionalColor().size(); i++) {
                            sizeBean bean2 = new sizeBean();
                            bean2.setId(response.body().getData().getAdditionalColor().get(i).getId());
                            bean2.setSize(response.body().getData().getAdditionalColor().get(i).getColor());
                            bean2.setName(response.body().getData().getAdditionalColor().get(i).getName());
                            bean2.setType("new");
                            lis.add(bean2);
                        }

                        ColorAdapter adapter2 = new ColorAdapter(mainActivity, lis);
                        LinearLayoutManager manage2 = new LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false);
                        colors.setAdapter(adapter2);
                        colors.setLayoutManager(manage2);

                        adapter2.selectPos(0);

                    } else {
                        colors.setVisibility(View.GONE);
                        colorstitle.setVisibility(View.GONE);
                    }


                    if (item.getSize().length() > 0) {

                        variants.setVisibility(View.VISIBLE);
                        sizetitle.setVisibility(View.VISIBLE);
                        colors.setVisibility(View.VISIBLE);
                        colorstitle.setVisibility(View.VISIBLE);

                        List<sizeBean> lis = new ArrayList<>();

                        sizeBean bean1 = new sizeBean();
                        bean1.setId(pid);
                        bean1.setSize(item.getSize());
                        bean1.setType("current");
                        bean1.setName(item.getName());
                        lis.add(bean1);

                        for (int i = 0; i < response.body().getData().getAdditionalSize().size(); i++) {
                            sizeBean bean2 = new sizeBean();
                            bean2.setId(response.body().getData().getAdditionalSize().get(i).getId());
                            bean2.setSize(response.body().getData().getAdditionalSize().get(i).getSize());
                            bean2.setName(response.body().getData().getAdditionalSize().get(i).getName());
                            bean2.setType("new");
                            lis.add(bean2);
                        }

                        SizeAdapter adapter2 = new SizeAdapter(mainActivity, lis);
                        LinearLayoutManager manage2 = new LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false);
                        variants.setAdapter(adapter2);
                        variants.setLayoutManager(manage2);

                        adapter2.selectPos(0);

                    } else {
                        variants.setVisibility(View.GONE);
                        sizetitle.setVisibility(View.GONE);
                        colors.setVisibility(View.GONE);
                        colorstitle.setVisibility(View.GONE);
                    }

                    float pri = Float.parseFloat(item.getPrice());
                    float dv1 = Float.parseFloat(item.getDiscount());
                    float dv = pri - dv1;
                    float dis = (dv / pri) * 100;
                    String nv1 = null;


                    if (dis > 0) {

                        //float dv = (dis / 100) * pri;

                        float nv = pri - dv;

                        nv1 = String.valueOf(nv);

                        discount.setVisibility(View.VISIBLE);
                        discount.setText(Math.round(dis) + "% OFF");
                        price.setText(Html.fromHtml("<font color=\"#000000\"><b>\u20B9 " + String.valueOf(nv) + " </b></font><strike>\u20B9 " + item.getPrice() + "</strike>"));
                        save.setText(Html.fromHtml("You save - <b>\u20B9" + dv + "</b>"));
                        save.setVisibility(View.VISIBLE);
                    } else {
                        nv1 = item.getPrice();
                        discount.setVisibility(View.GONE);
                        price.setText(Html.fromHtml("<font color=\"#000000\"><b>\u20B9 " + String.valueOf(item.getPrice()) + " </b></font>"));
                        save.setVisibility(View.GONE);
                    }

                    /*float dis = Float.parseFloat(item.getDiscount());

                    if (dis > 0) {

                        float pri = Float.parseFloat(item.getPrice());
                        float dv = (dis / 100) * pri;

                        float nv = pri - dv;

                        nv1 = String.valueOf(nv);

                        discount.setVisibility(View.VISIBLE);
                        discount.setText(item.getDiscount() + "% OFF");
                        price.setText(Html.fromHtml("Selling Price:  <font color=\"#000000\"><b>\u20B9" + String.valueOf(nv) + " </b></font><strike>\u20B9" + item.getPrice() + "</strike>"));

                        save.setText(Html.fromHtml("You save - <b>\u20B9" + dv + "</b>"));
                        save.setVisibility(View.VISIBLE);

                    } else {

                        nv1 = item.getPrice();
                        discount.setVisibility(View.GONE);
                        price.setText(Html.fromHtml("Selling Price:  <font color=\"#000000\"><b>\u20B9" + String.valueOf(item.getPrice()) + " </b></font>"));
                        save.setVisibility(View.GONE);
                    }*/

                    if (item.getWishlist().equals("1")) {
                        wishlist.setBackground(mainActivity.getDrawable(R.drawable.ic_heart1));

                        wishlist.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

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


                                int versionCode = com.nostra13.universalimageloader.BuildConfig.VERSION_CODE;
                                String versionName = com.nostra13.universalimageloader.BuildConfig.VERSION_NAME;

                                Call<singleProductBean> call = cr.removeWishlist(SharePreferenceUtils.getInstance().getString("userId"), item.getId());

                                call.enqueue(new Callback<singleProductBean>() {
                                    @Override
                                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                        if (response.body().getStatus().equals("1")) {
                                            onResume();
                                        }

                                        Toast.makeText(mainActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                        progress.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onFailure(Call<singleProductBean> call, Throwable t) {
                                        progress.setVisibility(View.GONE);
                                    }
                                });

                            }
                        });

                    } else {
                        wishlist.setBackground(mainActivity.getDrawable(R.drawable.ic_heart));

                        wishlist.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

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


                                int versionCode = com.nostra13.universalimageloader.BuildConfig.VERSION_CODE;
                                String versionName = com.nostra13.universalimageloader.BuildConfig.VERSION_NAME;

                                Call<singleProductBean> call = cr.addWishlist(SharePreferenceUtils.getInstance().getString("userId"), item.getId());

                                call.enqueue(new Callback<singleProductBean>() {
                                    @Override
                                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                        if (response.body().getStatus().equals("1")) {
                                            onResume();
                                        }

                                        Toast.makeText(mainActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                        progress.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onFailure(Call<singleProductBean> call, Throwable t) {
                                        progress.setVisibility(View.GONE);
                                    }
                                });

                            }
                        });


                    }


                    title.setText(item.getName());

                    brand.setText(item.getBrand());
                    //unit.setText(item.getSize());
                    seller.setText(item.getSeller());

                    description.setText(Html.fromHtml(item.getDescription()));
                    key_features.setText(Html.fromHtml(item.getKeyFeatures()));
                    packaging.setText(Html.fromHtml(item.getPackagingType()));
                    life.setText(Html.fromHtml(item.getShelfLife()));
                    disclaimer.setText(Html.fromHtml(item.getDisclaimer()));


                    if (item.getStock().equals("In stock")) {
                        add.setEnabled(true);
                    } else {
                        add.setEnabled(false);
                    }

                    stock.setText(item.getStock());

                    String finalNv = nv1;
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            if (pid.length() > 0) {
                                String uid = SharePreferenceUtils.getInstance().getString("userId");

                                if (uid.length() > 0) {

                                    final Dialog dialog = new Dialog(mainActivity, R.style.MyDialogTheme);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(true);
                                    dialog.setContentView(R.layout.add_cart_dialog);
                                    dialog.show();

                                    final StepperTouch stepperTouch = dialog.findViewById(R.id.stepperTouch);
                                    Button add = dialog.findViewById(R.id.button8);
                                    final ProgressBar progressBar = dialog.findViewById(R.id.progressBar2);
                                    TextView sizetitle = dialog.findViewById(R.id.textView5);
                                    TextView colortitle = dialog.findViewById(R.id.textView67);
                                    Spinner size = dialog.findViewById(R.id.size);
                                    Spinner color = dialog.findViewById(R.id.color);

                                    color.setVisibility(View.GONE);
                                    size.setVisibility(View.GONE);
                                    colortitle.setVisibility(View.GONE);
                                    sizetitle.setVisibility(View.GONE);

                                    /*if (item.getSize().size() > 0) {
                                        color.setVisibility(View.VISIBLE);
                                        size.setVisibility(View.VISIBLE);
                                        sizetitle.setVisibility(View.VISIBLE);
                                        colortitle.setVisibility(View.VISIBLE);

                                        *//*List<String> ll = new ArrayList<>();
                                        for (int i = 0; i < item.getSize().size(); i++) {
                                            ll.add(item.getSize().get(i).getSize());
                                        }

                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity,
                                                android.R.layout.simple_list_item_1, ll);

                                        size.setAdapter(adapter);

                                        size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                String clr = item.getSize().get(position).getColor();
                                                List<String> list = new ArrayList<>(Arrays.asList(clr.split(",")));
                                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity,
                                                        android.R.layout.simple_list_item_1, list);

                                                color.setAdapter(adapter);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });*//*

                                    } else {
                                        color.setVisibility(View.GONE);
                                        size.setVisibility(View.GONE);
                                        colortitle.setVisibility(View.GONE);
                                        sizetitle.setVisibility(View.GONE);
                                    }*/

                                    stepperTouch.setMinValue(1);
                                    stepperTouch.setMaxValue(99);
                                    stepperTouch.setSideTapEnabled(true);
                                    stepperTouch.setCount(1);

                                    add.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            progressBar.setVisibility(View.VISIBLE);

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

                                            Log.d("userid", SharePreferenceUtils.getInstance().getString("userid"));
                                            Log.d("pid", pid);
                                            Log.d("quantity", String.valueOf(stepperTouch.getCount()));
                                            Log.d("price", finalNv);

                                            int versionCode = BuildConfig.VERSION_CODE;
                                            String versionName = BuildConfig.VERSION_NAME;

                                            /*String cl = String.valueOf(color.getSelectedItem());
                                            String sz = String.valueOf(size.getSelectedItem());*/

                                            Call<singleProductBean> call = cr.addCart(SharePreferenceUtils.getInstance().getString("userId"), pid, String.valueOf(stepperTouch.getCount()), finalNv, versionName, siz, col);

                                            call.enqueue(new Callback<singleProductBean>() {
                                                @Override
                                                public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                                    if (response.body().getStatus().equals("1")) {
                                                        mainActivity.loadCart();
                                                        dialog.dismiss();
                                                    }

                                                    Toast.makeText(mainActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();

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
                                    Toast.makeText(mainActivity, "Please login to continue", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(mainActivity, Login.class);
                                    startActivity(intent);

                                }
                            }


                        }
                    });

                }

                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<singleProductBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

        Call<homeBean> call2 = cr.getHome(SharePreferenceUtils.getInstance().getString("lat"), SharePreferenceUtils.getInstance().getString("lng"));
        call2.enqueue(new Callback<homeBean>() {
            @Override
            public void onResponse(Call<homeBean> call, Response<homeBean> response) {

                if (response.body().getStatus().equals("1")) {


                    adapter2.setData(response.body().getToday());

                }
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<homeBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }

    class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.ViewHolder> {
        Context context;
        List<sizeBean> list = new ArrayList<>();
        int pos = -1;

        public SizeAdapter(Context context, List<sizeBean> list) {
            this.context = context;
            this.list = list;
        }

        public void selectPos(int pos) {
            this.pos = pos;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.tags, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.setIsRecyclable(false);
            sizeBean item = list.get(position);

            holder.tag.setText(item.getSize());

            if (position == pos) {

                siz = item.getSize();

                holder.tag.setBackground(colors.getResources().getDrawable(R.drawable.accent_back_round2));
                holder.tag.setTextColor(Color.BLACK);


            } else {
                holder.tag.setBackground(colors.getResources().getDrawable(R.drawable.dotted_back_stroke));
                holder.tag.setTextColor(Color.LTGRAY);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getType().equals("new")) {
                        FragmentManager fm4 = mainActivity.getSupportFragmentManager();

                        FragmentTransaction ft4 = fm4.beginTransaction();
                        ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        SingleProduct frag14 = new SingleProduct();
                        Bundle b = new Bundle();
                        b.putString("id", item.getId());
                        b.putString("title", item.getName());
                        frag14.setArguments(b);
                        ft4.replace(R.id.replace, frag14);
                        ft4.addToBackStack(null);
                        ft4.commit();
                    } else {
                        selectPos(position);
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tag;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tag = itemView.findViewById(R.id.tag);

            }
        }
    }

    class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
        Context context;
        List<sizeBean> list = new ArrayList<>();
        int pos = -1;

        public ColorAdapter(Context context, List<sizeBean> list) {
            this.context = context;
            this.list = list;
        }

        public void selectPos(int pos) {
            this.pos = pos;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.tags, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.setIsRecyclable(false);
            sizeBean item = list.get(position);

            holder.tag.setText(item.getSize());

            if (position == pos) {

                col = item.getSize();

                holder.tag.setBackground(colors.getResources().getDrawable(R.drawable.accent_back_round2));
                holder.tag.setTextColor(Color.BLACK);


            } else {
                holder.tag.setBackground(colors.getResources().getDrawable(R.drawable.dotted_back_stroke));
                holder.tag.setTextColor(Color.LTGRAY);
            }

            /*if (position == pos) {
                col = item;

                holder.tag.setBackground(colors.getResources().getDrawable(R.drawable.accent_back_round2));
                holder.tag.setTextColor(Color.BLACK);
            } else {
                holder.tag.setBackground(colors.getResources().getDrawable(R.drawable.dotted_back_stroke));
                holder.tag.setTextColor(Color.LTGRAY);
            }*/

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getType().equals("new")) {
                        FragmentManager fm4 = mainActivity.getSupportFragmentManager();

                        FragmentTransaction ft4 = fm4.beginTransaction();
                        ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        SingleProduct frag14 = new SingleProduct();
                        Bundle b = new Bundle();
                        b.putString("id", item.getId());
                        b.putString("title", item.getName());
                        frag14.setArguments(b);
                        ft4.replace(R.id.replace, frag14);
                        ft4.addToBackStack(null);
                        ft4.commit();
                    } else {
                        selectPos(position);
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tag;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tag = itemView.findViewById(R.id.tag);

            }
        }
    }

    class BannerAdapter extends FragmentStatePagerAdapter {

        List<String> blist = new ArrayList<>();

        public BannerAdapter(@NonNull FragmentManager fm, int behavior, List<String> blist) {
            super(fm, behavior);
            this.blist = blist;
        }

        /*public BannerAdapter(FragmentManager fm, List<Banners> blist) {
            super(fm);
            this.blist = blist;
        }*/

        @Override
        public Fragment getItem(int position) {
            page frag = new page();
            frag.setData(blist.get(position), blist, position);
            return frag;
        }

        @Override
        public int getCount() {
            return blist.size();
            //return 1;
        }
    }


    public static class page extends Fragment {

        String url, tit, cid = "", image2;
        int pos;
        ImageView image;
        List<String> blist = new ArrayList<>();

        void setData(String url, List<String> blist, int pos) {
            this.url = url;
            this.blist = blist;
            this.pos = pos;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.banner_layout2, container, false);

            image = view.findViewById(R.id.imageView3);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(url, image, options);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm4 = mainActivity.getSupportFragmentManager();

                    FragmentTransaction ft4 = fm4.beginTransaction();
                    ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    zoomFragment frag14 = new zoomFragment();
                    frag14.setData(blist, pos);
                    ft4.replace(R.id.replace, frag14);
                    ft4.addToBackStack(null);
                    ft4.commit();
                }
            });

            return view;
        }
    }


    class BestAdapter extends RecyclerView.Adapter<BestAdapter.ViewHolder> {

        Context context;
        List<Best> list = new ArrayList<>();

        public BestAdapter(Context context, List<Best> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Best> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.best_list_model3, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.setIsRecyclable(false);

            final Best item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);

            if (item.getStock().equals("In stock")) {
                holder.add.setEnabled(true);
            } else {
                holder.add.setEnabled(false);
            }

            holder.stock.setText(item.getStock());
            //holder.size.setText(item.getSize());

            float pri = Float.parseFloat(item.getPrice());
            float dv1 = Float.parseFloat(item.getDiscount());
            float dv = pri - dv1;
            float dis = (dv / pri) * 100;
            String nv1 = null;


            if (dis > 0) {

                //float dv = (dis / 100) * pri;

                float nv = pri - dv;

                nv1 = String.valueOf(nv);

                holder.discount.setVisibility(View.VISIBLE);
                holder.discount.setText(Math.round(dis) + "% OFF");
                holder.price.setText(Html.fromHtml("<font color=\"#000000\"><b>\u20B9 " + String.valueOf(nv) + " </b></font><strike>\u20B9 " + item.getPrice() + "</strike>"));
            } else {
                nv1 = item.getPrice();
                holder.discount.setVisibility(View.GONE);
                holder.price.setText(Html.fromHtml("<font color=\"#000000\"><b>\u20B9 " + String.valueOf(item.getPrice()) + " </b></font>"));
            }

            /*if (dis > 0) {

                float pri = Float.parseFloat(item.getPrice());
                float dv = (dis / 100) * pri;

                float nv = pri - dv;

                nv1 = String.valueOf(nv);

                holder.discount.setVisibility(View.VISIBLE);
                holder.discount.setText(item.getDiscount() + "% OFF");
                holder.price.setText(Html.fromHtml("\u20B9 " + String.valueOf(nv)));
                holder.newamount.setText(Html.fromHtml("<strike>\u20B9 " + item.getPrice() + "</strike>"));
                holder.newamount.setVisibility(View.VISIBLE);
            } else {

                nv1 = item.getPrice();
                holder.discount.setVisibility(View.GONE);
                holder.price.setText("\u20B9 " + item.getPrice());
                holder.newamount.setVisibility(View.GONE);
            }*/


            holder.title.setText(item.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentManager fm4 = mainActivity.getSupportFragmentManager();

                    FragmentTransaction ft4 = fm4.beginTransaction();
                    ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    SingleProduct frag14 = new SingleProduct();
                    Bundle b = new Bundle();
                    b.putString("id", item.getId());
                    b.putString("title", item.getName());
                    frag14.setArguments(b);
                    ft4.replace(R.id.replace, frag14);
                    ft4.addToBackStack(null);
                    ft4.commit();

                }
            });

            String finalNv = nv1;
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String uid = SharePreferenceUtils.getInstance().getString("userId");

                    if (uid.length() > 0) {

                        final Dialog dialog = new Dialog(mainActivity, R.style.MyDialogTheme);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.add_cart_dialog);
                        dialog.show();

                        final StepperTouch stepperTouch = dialog.findViewById(R.id.stepperTouch);
                        Button add = dialog.findViewById(R.id.button8);
                        final ProgressBar progressBar = dialog.findViewById(R.id.progressBar2);
                        TextView sizetitle = dialog.findViewById(R.id.textView5);
                        TextView colortitle = dialog.findViewById(R.id.textView67);
                        Spinner size = dialog.findViewById(R.id.size);
                        Spinner color = dialog.findViewById(R.id.color);

                        if (item.getSize().size() > 0) {
                            color.setVisibility(View.VISIBLE);
                            size.setVisibility(View.VISIBLE);
                            sizetitle.setVisibility(View.VISIBLE);
                            colortitle.setVisibility(View.VISIBLE);

                            List<String> ll = new ArrayList<>();
                            for (int i = 0; i < item.getSize().size(); i++) {
                                ll.add(item.getSize().get(i).getSize());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity,
                                    android.R.layout.simple_list_item_1, ll);

                            size.setAdapter(adapter);

                            size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String clr = item.getSize().get(position).getColor();
                                    List<String> list = new ArrayList<>(Arrays.asList(clr.split(",")));
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity,
                                            android.R.layout.simple_list_item_1, list);

                                    color.setAdapter(adapter);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } else {
                            color.setVisibility(View.GONE);
                            size.setVisibility(View.GONE);
                            colortitle.setVisibility(View.GONE);
                            sizetitle.setVisibility(View.GONE);
                        }

                        stepperTouch.setMinValue(1);
                        stepperTouch.setMaxValue(99);
                        stepperTouch.setSideTapEnabled(true);
                        stepperTouch.setCount(1);

                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                progressBar.setVisibility(View.VISIBLE);

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

                                Log.d("userid", SharePreferenceUtils.getInstance().getString("userid"));
                                Log.d("pid", pid);
                                Log.d("quantity", String.valueOf(stepperTouch.getCount()));
                                Log.d("price", finalNv);

                                int versionCode = BuildConfig.VERSION_CODE;
                                String versionName = BuildConfig.VERSION_NAME;

                                String cl = String.valueOf(color.getSelectedItem());
                                String sz = String.valueOf(size.getSelectedItem());

                                Call<singleProductBean> call = cr.addCart(SharePreferenceUtils.getInstance().getString("userId"), pid, String.valueOf(stepperTouch.getCount()), finalNv, versionName, sz, cl);

                                call.enqueue(new Callback<singleProductBean>() {
                                    @Override
                                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                        if (response.body().getStatus().equals("1")) {
                                            mainActivity.loadCart();
                                            dialog.dismiss();
                                        }

                                        Toast.makeText(mainActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(context, "Please login to continue", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, Login.class);
                        context.startActivity(intent);

                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView price, title, discount, stock, newamount, size;
            Button add;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView4);
                price = itemView.findViewById(R.id.textView11);
                title = itemView.findViewById(R.id.textView12);
                discount = itemView.findViewById(R.id.textView10);
                add = itemView.findViewById(R.id.button5);
                stock = itemView.findViewById(R.id.textView63);
                newamount = itemView.findViewById(R.id.textView6);
                size = itemView.findViewById(R.id.textView7);


            }
        }
    }

    class RelatedAdapter extends RecyclerView.Adapter<RelatedAdapter.ViewHolder> {

        Context context;
        List<Related> list = new ArrayList<>();

        public RelatedAdapter(Context context, List<Related> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Related> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.best_list_model3, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.setIsRecyclable(false);

            final Related item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);


            if (item.getStock().equals("In stock")) {
                holder.add.setEnabled(true);
            } else {
                holder.add.setEnabled(false);
            }

            holder.stock.setText(item.getStock());
            //holder.size.setText(item.getSize());

            float pri = Float.parseFloat(item.getPrice());
            float dv1 = Float.parseFloat(item.getDiscount());
            float dv = pri - dv1;
            float dis = (dv / pri) * 100;
            String nv1 = null;


            if (dis > 0) {

                //float dv = (dis / 100) * pri;

                float nv = pri - dv;

                nv1 = String.valueOf(nv);

                holder.discount.setVisibility(View.VISIBLE);
                holder.discount.setText(Math.round(dis) + "% OFF");
                holder.price.setText(Html.fromHtml("<font color=\"#000000\"><b>\u20B9 " + String.valueOf(nv) + " </b></font><strike>\u20B9 " + item.getPrice() + "</strike>"));
            } else {
                nv1 = item.getPrice();
                holder.discount.setVisibility(View.GONE);
                holder.price.setText(Html.fromHtml("<font color=\"#000000\"><b>\u20B9 " + String.valueOf(item.getPrice()) + " </b></font>"));
            }


            holder.title.setText(item.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentManager fm4 = mainActivity.getSupportFragmentManager();

                    FragmentTransaction ft4 = fm4.beginTransaction();
                    ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    SingleProduct frag14 = new SingleProduct();
                    Bundle b = new Bundle();
                    b.putString("id", item.getId());
                    b.putString("title", item.getName());
                    frag14.setArguments(b);
                    ft4.replace(R.id.replace, frag14);
                    ft4.addToBackStack(null);
                    ft4.commit();

                }
            });

            String finalNv = nv1;
            /*holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String uid = SharePreferenceUtils.getInstance().getString("userId");

                    if (uid.length() > 0) {

                        final Dialog dialog = new Dialog(mainActivity, R.style.MyDialogTheme);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.add_cart_dialog);
                        dialog.show();

                        final StepperTouch stepperTouch = dialog.findViewById(R.id.stepperTouch);
                        Button add = dialog.findViewById(R.id.button8);
                        final ProgressBar progressBar = dialog.findViewById(R.id.progressBar2);
                        TextView sizetitle = dialog.findViewById(R.id.textView5);
                        TextView colortitle = dialog.findViewById(R.id.textView67);
                        Spinner size = dialog.findViewById(R.id.size);
                        Spinner color = dialog.findViewById(R.id.color);

                        if (item.getSize().size() > 0) {
                            color.setVisibility(View.VISIBLE);
                            size.setVisibility(View.VISIBLE);
                            sizetitle.setVisibility(View.VISIBLE);
                            colortitle.setVisibility(View.VISIBLE);

                            List<String> ll = new ArrayList<>();
                            for (int i = 0; i < item.getSize().size(); i++) {
                                ll.add(item.getSize().get(i).getSize());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity,
                                    android.R.layout.simple_list_item_1, ll);

                            size.setAdapter(adapter);

                            size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String clr = item.getSize().get(position).getColor();
                                    List<String> list = new ArrayList<>(Arrays.asList(clr.split(",")));
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity,
                                            android.R.layout.simple_list_item_1, list);

                                    color.setAdapter(adapter);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } else {
                            color.setVisibility(View.GONE);
                            size.setVisibility(View.GONE);
                            colortitle.setVisibility(View.GONE);
                            sizetitle.setVisibility(View.GONE);
                        }

                        stepperTouch.setMinValue(1);
                        stepperTouch.setMaxValue(99);
                        stepperTouch.setSideTapEnabled(true);
                        stepperTouch.setCount(1);

                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                progressBar.setVisibility(View.VISIBLE);

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

                                Log.d("userid", SharePreferenceUtils.getInstance().getString("userid"));
                                Log.d("pid", pid);
                                Log.d("quantity", String.valueOf(stepperTouch.getCount()));
                                Log.d("price", finalNv);

                                int versionCode = BuildConfig.VERSION_CODE;
                                String versionName = BuildConfig.VERSION_NAME;

                                String cl = String.valueOf(color.getSelectedItem());
                                String sz = String.valueOf(size.getSelectedItem());

                                Call<singleProductBean> call = cr.addCart(SharePreferenceUtils.getInstance().getString("userId"), pid, String.valueOf(stepperTouch.getCount()), finalNv, versionName, sz, cl);

                                call.enqueue(new Callback<singleProductBean>() {
                                    @Override
                                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                        if (response.body().getStatus().equals("1")) {
                                            mainActivity.loadCart();
                                            dialog.dismiss();
                                        }

                                        Toast.makeText(mainActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(context, "Please login to continue", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, Login.class);
                        context.startActivity(intent);

                    }

                }
            });*/

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView price, title, discount, stock, newamount, size;
            Button add;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView4);
                price = itemView.findViewById(R.id.textView11);
                title = itemView.findViewById(R.id.textView12);
                discount = itemView.findViewById(R.id.textView10);
                add = itemView.findViewById(R.id.button5);
                stock = itemView.findViewById(R.id.textView63);
                newamount = itemView.findViewById(R.id.textView6);
                size = itemView.findViewById(R.id.textView7);


            }
        }
    }

}
