package com.technuoma.elittleplanet;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.technuoma.elittleplanet.cartPOJO.cartBean;
import com.technuoma.elittleplanet.homePOJO.Banners;
import com.technuoma.elittleplanet.homePOJO.Best;
import com.technuoma.elittleplanet.homePOJO.Cat;
import com.technuoma.elittleplanet.homePOJO.Member;
import com.technuoma.elittleplanet.homePOJO.homeBean;
import com.technuoma.elittleplanet.seingleProductPOJO.singleProductBean;
import com.nostra13.universalimageloader.BuildConfig;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.santalu.autoviewpager.AutoViewPager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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

public class MainActivity extends AppCompatActivity implements ResultCallback<LocationSettingsResult> {

    private static final String TAG = "MainActivity";
    Toolbar toolbar;
    DrawerLayout drawer;
    AutoViewPager pager;
    ProgressBar progress;
    CircleIndicator indicator;
    RecyclerView categories, recent, loved, safe, essentails, top, banner, cattop;
    BestAdapter adapter2, adapter3;
    BestAdapter adapter4;
    BestAdapter adapter5;
    BestAdapter adapter7;
    CategoryAdapter adapter6;
    List<Best> list;
    List<Best> list1;
    List<Best> list2;
    List<Cat> list3;
    List<Banners> list4;
    TextView count, rewards, login, addresstext, terms, about, address, logout, cart, orders, refer, location, wishlist;
    ImageButton cart1;
    EditText search;
    OfferAdapter adapter;

    ImageView banner1, banner2, banner3, banner4, banner5, banner6;

    private FusedLocationProviderClient fusedLocationClient;

    String lat = "", lng = "";

    LocationSettingsRequest.Builder builder;
    LocationRequest locationRequest;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        toolbar = findViewById(R.id.toolbar);
        banner1 = findViewById(R.id.banner1);
        banner2 = findViewById(R.id.banner2);
        banner3 = findViewById(R.id.banner3);
        banner4 = findViewById(R.id.banner4);
        banner5 = findViewById(R.id.banner5);
        banner6 = findViewById(R.id.banner6);

        addresstext = findViewById(R.id.textView8);
        cattop = findViewById(R.id.cattop);
        wishlist = findViewById(R.id.wishlist);
        refer = findViewById(R.id.refer);
        location = findViewById(R.id.location);
        orders = findViewById(R.id.orders);
        indicator = findViewById(R.id.indicator);
        banner = findViewById(R.id.banner);
        pager = findViewById(R.id.viewPager);
        progress = findViewById(R.id.progress);
        // indicator = findViewById(R.id.indicator);
        categories = findViewById(R.id.categories);
        loved = findViewById(R.id.loved);
        recent = findViewById(R.id.recent);
        count = findViewById(R.id.count);
        rewards = findViewById(R.id.rewards);
        safe = findViewById(R.id.safe);
        essentails = findViewById(R.id.essentials);
        top = findViewById(R.id.top);
        cart1 = findViewById(R.id.imageButton3);
        login = findViewById(R.id.textView3);
        terms = findViewById(R.id.terms);
        about = findViewById(R.id.about);
        address = findViewById(R.id.address);
        logout = findViewById(R.id.logout);
        cart = findViewById(R.id.cart);
        search = findViewById(R.id.search);

        list = new ArrayList<>();
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        list4 = new ArrayList<>();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        adapter = new OfferAdapter(this, list4);
        adapter2 = new BestAdapter(this, list);
        adapter3 = new BestAdapter(this, list);
        adapter4 = new BestAdapter(this, list1);
        adapter5 = new BestAdapter(this, list2);
        adapter6 = new CategoryAdapter(this, list3);
        adapter7 = new BestAdapter(this, list2);

        LinearLayoutManager manager1 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager manager2 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager manager3 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager manager4 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager manager6 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager manager5 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager manager7 = new GridLayoutManager(this, 1);

        recent.setAdapter(adapter2);
        recent.setLayoutManager(manager1);

        loved.setAdapter(adapter3);
        loved.setLayoutManager(manager2);

        essentails.setAdapter(adapter4);
        essentails.setLayoutManager(manager3);

        cattop.setAdapter(adapter6);
        cattop.setLayoutManager(manager5);

        safe.setAdapter(adapter4);
        safe.setLayoutManager(manager4);

        top.setAdapter(adapter7);
        top.setLayoutManager(manager6);

        banner.setAdapter(adapter);
        banner.setLayoutManager(manager7);

        final String uid = SharePreferenceUtils.getInstance().getString("userId");

        if (uid.length() > 0) {
            login.setText(SharePreferenceUtils.getInstance().getString("name"));
            addresstext.setText(SharePreferenceUtils.getInstance().getString("address"));
            rewards.setText("eCashback - " + SharePreferenceUtils.getInstance().getString("rewards"));
            //rewards.setVisibility(View.VISIBLE);
            getRew();
        } else {
            rewards.setVisibility(View.GONE);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (uid.length() == 0) {
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }


            }
        });

        cart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uid.length() > 0) {
                    Intent intent = new Intent(MainActivity.this, Cart.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                }

                drawer.closeDrawer(GravityCompat.START);

            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Web.class);
                intent.putExtra("title", "Terms & Conditions");
                intent.putExtra("url", "https://technuoma.com/elittleplanet/terms.php");
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);

            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uid.length() > 0) {
                    Intent intent = new Intent(MainActivity.this, Wishlist.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                }

                drawer.closeDrawer(GravityCompat.START);

            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Search.class);
                startActivity(intent);

            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Web.class);
                intent.putExtra("title", "FAQs");
                intent.putExtra("url", "https://technuoma.com/elittleplanet/faq.php");
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);

            }
        });


        refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShareCompat.IntentBuilder.from(MainActivity.this)
                        .setType("text/plain")
                        .setChooserTitle("Chooser title")
                        .setText("http://play.google.com/store/apps/details?id=" + getPackageName() + "&referrer=" + SharePreferenceUtils.getInstance().getString("userId"))
                        .startChooser();

                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(
                        "http://play.google.com/store/apps/details?id=" + getPackageName() + "&referrer=" + SharePreferenceUtils.getInstance().getString("userId")));
                intent.setPackage("com.android.vending");
                startActivity(intent);

                Log.d("adasd", "http://play.google.com/store/apps/details?id=" + getPackageName() + "&referrer=" + SharePreferenceUtils.getInstance().getString("userId"));
                */
                drawer.closeDrawer(GravityCompat.START);
                /*ShareCompat.IntentBuilder.from(MainActivity.this)
                        .setType("text/plain")
                        .setChooserTitle("Chooser title")
                        .setText("http://play.google.com/store/apps/details?id=" + getPackageName() + "&referrer=" + SharePreferenceUtils.getInstance().getString("userId"))
                        .startChooser();*/

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (uid.length() == 0) {
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }


            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uid.length() > 0) {
                    Intent intent = new Intent(MainActivity.this, Address.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                }

                drawer.closeDrawer(GravityCompat.START);

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharePreferenceUtils.getInstance().deletePref();

                Intent intent = new Intent(MainActivity.this, Spalsh.class);
                startActivity(intent);
                finishAffinity();

            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uid.length() > 0) {
                    Intent intent = new Intent(MainActivity.this, Cart.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                }

                drawer.closeDrawer(GravityCompat.START);

            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uid.length() > 0) {
                    Intent intent = new Intent(MainActivity.this, Orders.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                }

                drawer.closeDrawer(GravityCompat.START);

            }
        });

        loaddata();

        createLocationRequest();

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

    }

    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

        Context context;
        List<Cat> list = new ArrayList<>();

        public CategoryAdapter(Context context, List<Cat> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Cat> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        String getSpace(int position) {
            return list.get(position).getSpace();
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.category_list_model3, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Cat item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);

            //holder.tag.setText(item.getTag());
            holder.title.setText(item.getName());
            //holder.desc.setText(item.getDescription());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, SubCat.class);
                    intent.putExtra("id", item.getId());
                    intent.putExtra("title", item.getName());
                    intent.putExtra("image", item.getImage());
                    context.startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView tag, title, desc;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView5);
                //tag = itemView.findViewById(R.id.textView17);
                title = itemView.findViewById(R.id.textView18);
                //desc = itemView.findViewById(R.id.textView19);


            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        loadCart();

    }

    void loaddata() {
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

        Call<homeBean> call = cr.getHome(SharePreferenceUtils.getInstance().getString("lat"), SharePreferenceUtils.getInstance().getString("lng"));
        call.enqueue(new Callback<homeBean>() {
            @Override
            public void onResponse(Call<homeBean> call, Response<homeBean> response) {


                if (response.body().getStatus().equals("1")) {


                    BannerAdapter adapter1 = new BannerAdapter(getSupportFragmentManager(), response.body().getPbanner());
                    pager.setAdapter(adapter1);
                    indicator.setViewPager(pager);

                    adapter2.setData(response.body().getBest());
                    adapter3.setData(response.body().getToday());
                    adapter4.setData(response.body().getBest());
                    adapter5.setData(response.body().getToday());
                    adapter7.setData(response.body().getToday());
                    adapter6.setData(response.body().getCat());

                    Log.d("ssiizzee", String.valueOf(response.body().getObanner().size()));


                    try {
                        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                        ImageLoader loader = ImageLoader.getInstance();
                        String url = response.body().getObanner().get(0).getImage();
                        loader.displayImage(url, banner1, options);

                        String cid = response.body().getObanner().get(0).getCid();
                        String tit = response.body().getObanner().get(0).getCname();
                        String image = response.body().getObanner().get(0).getCatimage();

                        banner1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (cid != null) {
                                    Intent intent = new Intent(MainActivity.this, SubCat.class);
                                    intent.putExtra("id", cid);
                                    intent.putExtra("title", tit);
                                    intent.putExtra("image", image);
                                    startActivity(intent);
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                        ImageLoader loader = ImageLoader.getInstance();
                        String url = response.body().getObanner().get(1).getImage();
                        loader.displayImage(url, banner2, options);

                        String cid = response.body().getObanner().get(1).getCid();
                        String tit = response.body().getObanner().get(1).getCname();
                        String image = response.body().getObanner().get(1).getCatimage();

                        banner2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (cid != null) {
                                    Intent intent = new Intent(MainActivity.this, SubCat.class);
                                    intent.putExtra("id", cid);
                                    intent.putExtra("title", tit);
                                    intent.putExtra("image", image);
                                    startActivity(intent);
                                }
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                        ImageLoader loader = ImageLoader.getInstance();
                        String url = response.body().getObanner().get(2).getImage();
                        loader.displayImage(url, banner3, options);

                        String cid = response.body().getObanner().get(2).getCid();
                        String tit = response.body().getObanner().get(2).getCname();
                        String image = response.body().getObanner().get(2).getCatimage();

                        banner3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (cid != null) {
                                    Intent intent = new Intent(MainActivity.this, SubCat.class);
                                    intent.putExtra("id", cid);
                                    intent.putExtra("title", tit);
                                    intent.putExtra("image", image);
                                    startActivity(intent);
                                }
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                        ImageLoader loader = ImageLoader.getInstance();
                        String url = response.body().getObanner().get(3).getImage();
                        loader.displayImage(url, banner4, options);

                        String cid = response.body().getObanner().get(3).getCid();
                        String tit = response.body().getObanner().get(3).getCname();
                        String image = response.body().getObanner().get(3).getCatimage();

                        banner4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (cid != null) {
                                    Intent intent = new Intent(MainActivity.this, SubCat.class);
                                    intent.putExtra("id", cid);
                                    intent.putExtra("title", tit);
                                    intent.putExtra("image", image);
                                    startActivity(intent);
                                }
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                        ImageLoader loader = ImageLoader.getInstance();
                        String url = response.body().getObanner().get(4).getImage();
                        loader.displayImage(url, banner5, options);

                        String cid = response.body().getObanner().get(4).getCid();
                        String tit = response.body().getObanner().get(4).getCname();
                        String image = response.body().getObanner().get(4).getCatimage();

                        banner5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (cid != null) {
                                    Intent intent = new Intent(MainActivity.this, SubCat.class);
                                    intent.putExtra("id", cid);
                                    intent.putExtra("title", tit);
                                    intent.putExtra("image", image);
                                    startActivity(intent);
                                }
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                        ImageLoader loader = ImageLoader.getInstance();
                        String url = response.body().getObanner().get(5).getImage();
                        loader.displayImage(url, banner6, options);

                        String cid = response.body().getObanner().get(5).getCid();
                        String tit = response.body().getObanner().get(5).getCname();
                        String image = response.body().getObanner().get(5).getCatimage();

                        banner6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (cid != null) {
                                    Intent intent = new Intent(MainActivity.this, SubCat.class);
                                    intent.putExtra("id", cid);
                                    intent.putExtra("title", tit);
                                    intent.putExtra("image", image);
                                    startActivity(intent);
                                }
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (response.body().getObanner().size() > 6) {
                        List<Banners> ll = response.body().getObanner();
                        ll.remove(0);
                        ll.remove(0);
                        ll.remove(0);
                        ll.remove(0);
                        ll.remove(0);
                        ll.remove(0);
                        adapter.setData(ll);
                    }

                    SharePreferenceUtils.getInstance().saveString("location", response.body().getLocation());


                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    List<android.location.Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(Double.parseDouble(SharePreferenceUtils.getInstance().getString("lat")), Double.parseDouble(SharePreferenceUtils.getInstance().getString("lng")), 1);
                        SharePreferenceUtils.getInstance().saveString("deliveryLocation", addresses.get(0).getAddressLine(0));
                        location.setText(addresses.get(0).getAddressLine(0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    Log.d("address", addresses.toString());




                    //location.setText(response.body().getCity());

                }

                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<homeBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

        loadCart();
    }

    class BannerAdapter extends FragmentStatePagerAdapter {

        List<Banners> blist = new ArrayList<>();

        public BannerAdapter(FragmentManager fm, List<Banners> blist) {
            super(fm);
            this.blist = blist;
        }

        @Override
        public Fragment getItem(int position) {
            page frag = new page();
            frag.setData(blist.get(position).getImage(), blist.get(position).getCname(), blist.get(position).getCid(), blist.get(position).getCatimage());
            return frag;
        }

        @Override
        public int getCount() {
            return blist.size();
        }
    }


    public static class page extends Fragment {

        String url, tit, cid = "", image2;

        ImageView image;

        void setData(String url, String tit, String cid, String image2) {
            this.url = url;
            this.tit = tit;
            this.cid = cid;
            this.image2 = image2;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.banner_layout, container, false);

            image = view.findViewById(R.id.imageView3);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(url, image, options);


            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (cid != null) {
                        Intent intent = new Intent(getContext(), SubCat.class);
                        intent.putExtra("id", cid);
                        intent.putExtra("title", tit);
                        intent.putExtra("image", image2);
                        startActivity(intent);
                    }


                }
            });


            return view;
        }
    }

    class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {

        Context context;
        List<Banners> list = new ArrayList<>();

        public OfferAdapter(Context context, List<Banners> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Banners> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.best_list_model1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Banners item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (item.getCid() != null) {
                        Intent intent = new Intent(context, SubCat.class);
                        intent.putExtra("id", item.getCid());
                        intent.putExtra("title", item.getCname());
                        intent.putExtra("image", item.getCatimage());
                        startActivity(intent);
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

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView4);


            }
        }
    }

    class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

        Context context;
        List<Member> list = new ArrayList<>();

        public MemberAdapter(Context context, List<Member> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Member> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.member_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Member item = list.get(position);


            holder.duration.setText(item.getDuration());
            holder.price.setText("\u20B9 " + item.getPrice());


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView duration, price, discount;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                duration = itemView.findViewById(R.id.textView13);
                price = itemView.findViewById(R.id.textView15);
                discount = itemView.findViewById(R.id.textView14);


            }
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
            View view = inflater.inflate(R.layout.best_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Best item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);

            float dis = Float.parseFloat(item.getDiscount());

            final String nv1;

            if (item.getStock().equals("In stock")) {
                holder.add.setEnabled(true);
            } else {
                holder.add.setEnabled(false);
            }

            holder.stock.setText(item.getStock());

            if (dis > 0) {

                float pri = Float.parseFloat(item.getPrice());
                float dv = (dis / 100) * pri;

                float nv = pri - dv;

                nv1 = String.valueOf(nv);

                holder.discount.setVisibility(View.VISIBLE);
                holder.discount.setText(item.getDiscount() + "% OFF");
                holder.price.setText(Html.fromHtml("<font color=\"#FF6000\"><b>\u20B9 " + String.valueOf(nv) + " </b></font><strike>\u20B9 " + item.getPrice() + "</strike>"));
            } else {

                nv1 = item.getPrice();
                holder.discount.setVisibility(View.GONE);
                holder.price.setText(Html.fromHtml("<font color=\"#FF6000\"><b>\u20B9 " + String.valueOf(item.getPrice()) + " </b></font>"));
            }


            holder.title.setText(item.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, SingleProduct.class);
                    intent.putExtra("id", item.getId());
                    intent.putExtra("title", item.getName());
                    context.startActivity(intent);

                }
            });

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String uid = SharePreferenceUtils.getInstance().getString("userId");

                    if (uid.length() > 0) {

                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.add_cart_dialog);
                        dialog.show();

                        final StepperTouch stepperTouch = dialog.findViewById(R.id.stepperTouch);
                        Button add = dialog.findViewById(R.id.button8);
                        final ProgressBar progressBar = dialog.findViewById(R.id.progressBar2);
                        TextView colortitle = dialog.findViewById(R.id.textView5);
                        Spinner color = dialog.findViewById(R.id.color);

                        if (item.getUnit().length() > 0) {
                            color.setVisibility(View.VISIBLE);
                            colortitle.setVisibility(View.VISIBLE);

                            String[] dd = item.getUnit().split(",");

                            List<String> clist = Arrays.asList(dd);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                                    android.R.layout.simple_list_item_1, clist);

                            color.setAdapter(adapter);

                        } else {
                            color.setVisibility(View.GONE);
                            colortitle.setVisibility(View.GONE);
                        }

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
                                Log.d("pid", item.getId());
                                Log.d("quantity", String.valueOf(stepperTouch.getCount()));
                                Log.d("price", nv1);

                                int versionCode = BuildConfig.VERSION_CODE;
                                String versionName = BuildConfig.VERSION_NAME;

                                String cl = String.valueOf(color.getSelectedItem());

                                Call<singleProductBean> call = cr.addCart(SharePreferenceUtils.getInstance().getString("userId"), item.getId(), String.valueOf(stepperTouch.getCount()), nv1, versionName, cl);

                                call.enqueue(new Callback<singleProductBean>() {
                                    @Override
                                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                        if (response.body().getStatus().equals("1")) {
                                            //loadCart();
                                            dialog.dismiss();
                                            loadCart();
                                        }

                                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

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
            TextView price, title, discount, stock;
            Button add;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView4);
                price = itemView.findViewById(R.id.textView11);
                title = itemView.findViewById(R.id.textView12);
                discount = itemView.findViewById(R.id.textView10);
                add = itemView.findViewById(R.id.button5);
                stock = itemView.findViewById(R.id.textView63);


            }
        }
    }

    void loadCart() {
        String uid = SharePreferenceUtils.getInstance().getString("userId");

        if (uid.length() > 0) {
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

            Call<cartBean> call2 = cr.getCart(SharePreferenceUtils.getInstance().getString("userId"));
            call2.enqueue(new Callback<cartBean>() {
                @Override
                public void onResponse(Call<cartBean> call, Response<cartBean> response) {

                    if (response.body().getData().size() > 0) {


                        count.setText(String.valueOf(response.body().getData().size()));


                    } else {

                        count.setText("0");

                    }

                    progress.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<cartBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });

            getRew();

        } else {
            count.setText("0");
        }
    }


    void getRew() {

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

        Call<String> call = cr.getRew(SharePreferenceUtils.getInstance().getString("user_id"));

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                rewards.setText("eCashback - " + response.body());

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }


    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getLocation();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(this, "Location is required for this app", Toast.LENGTH_LONG).show();
                        finishAffinity();
                        break;
                }
                break;
        }
    }

    void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //TODO: UI updates.
                        lat = String.valueOf(location.getLatitude());
                        lng = String.valueOf(location.getLongitude());

                        SharePreferenceUtils.getInstance().saveString("lat", lat);
                        SharePreferenceUtils.getInstance().saveString("lng", lng);

                        Log.d("lat123", lat);
                        Log.d("lat123", lng);

                        LocationServices.getFusedLocationProviderClient(MainActivity.this).removeLocationUpdates(this);

                        loaddata();

                    }
                }
            }
        };

        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, mLocationCallback, null);

    }

}
