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
import androidx.fragment.app.FragmentTransaction;
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
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.hsalf.smileyrating.SmileyRating;
import com.technuoma.elittleplanet.cartPOJO.cartBean;
import com.technuoma.elittleplanet.homePOJO.Banners;
import com.technuoma.elittleplanet.homePOJO.Best;
import com.technuoma.elittleplanet.homePOJO.Cat;
import com.technuoma.elittleplanet.homePOJO.Member;
import com.technuoma.elittleplanet.homePOJO.homeBean;
import com.technuoma.elittleplanet.ratingsPOJO.ratingsBean;
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


    Toolbar toolbar;
    DrawerLayout drawer;
    BottomNavigationView navigation;
    TextView count, rewards, login, terms, about, address, logout, cart, orders, refer, location, wishlist, contact, find;
    ImageView cart2, notification;
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    TextView email;

    ImageView facebook, twitter, instagram, youtube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        toolbar = findViewById(R.id.toolbar);
        navigation = findViewById(R.id.bottomNavigationView);
        wishlist = findViewById(R.id.wishlist);
        refer = findViewById(R.id.refer);
        location = findViewById(R.id.textView2);
        orders = findViewById(R.id.orders);
        count = findViewById(R.id.count);
        rewards = findViewById(R.id.rewards);
        cart2 = findViewById(R.id.imageView2);
        login = findViewById(R.id.textView3);
        terms = findViewById(R.id.terms);
        about = findViewById(R.id.about);
        address = findViewById(R.id.address);
        logout = findViewById(R.id.logout);
        cart = findViewById(R.id.cart);
        notification = findViewById(R.id.imageView10);
        find = findViewById(R.id.find);
        email = findViewById(R.id.textView8);

        contact = findViewById(R.id.contact);
        facebook = findViewById(R.id.imageView15);
        twitter = findViewById(R.id.imageView17);
        instagram = findViewById(R.id.imageView18);
        youtube = findViewById(R.id.imageView16);


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        final String uid = SharePreferenceUtils.getInstance().getString("userId");

        if (uid.length() > 0) {
            login.setText(SharePreferenceUtils.getInstance().getString("name"));
            email.setText(SharePreferenceUtils.getInstance().getString("email"));
            rewards.setText("LP Rewards - " + SharePreferenceUtils.getInstance().getString("rewards"));
            //rewards.setVisibility(View.VISIBLE);
            //getRew();
        } else {
            rewards.setVisibility(View.GONE);
        }


        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:

                        FragmentManager fm = getSupportFragmentManager();

                        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }

                        FragmentTransaction ft = fm.beginTransaction();
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        Home frag1 = new Home();
                        ft.replace(R.id.replace, frag1);
                        //ft.addToBackStack(null);
                        ft.commit();
                        //drawer.closeDrawer(GravityCompat.START);

                        break;
                    case R.id.action_search:


                        FragmentManager fm1 = getSupportFragmentManager();

                        for (int i = 0; i < fm1.getBackStackEntryCount(); ++i) {
                            fm1.popBackStack();
                        }

                        FragmentTransaction ft1 = fm1.beginTransaction();
                        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        Contact frag11 = new Contact();
                        ft1.replace(R.id.replace, frag11);
                        //ft.addToBackStack(null);
                        ft1.commit();
                        drawer.closeDrawer(GravityCompat.START);


                        // put your all data using put extra

                        //LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);


                        break;
                    case R.id.action_blog:
                        FragmentManager fm2 = getSupportFragmentManager();

                        for (int i = 0; i < fm2.getBackStackEntryCount(); ++i) {
                            fm2.popBackStack();
                        }

                        FragmentTransaction ft2 = fm2.beginTransaction();
                        ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        Search frag12 = new Search();
                        ft2.replace(R.id.replace, frag12);
                        //ft.addToBackStack(null);
                        ft2.commit();
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.action_cart:
                        FragmentManager fm3 = getSupportFragmentManager();

                        for (int i = 0; i < fm3.getBackStackEntryCount(); ++i) {
                            fm3.popBackStack();
                        }

                        FragmentTransaction ft3 = fm3.beginTransaction();
                        ft3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        Cart frag13 = new Cart();
                        ft3.replace(R.id.replace, frag13);
                        //ft.addToBackStack(null);
                        ft3.commit();
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.action_orders:
                        FragmentManager fm31 = getSupportFragmentManager();

                        for (int i = 0; i < fm31.getBackStackEntryCount(); ++i) {
                            fm31.popBackStack();
                        }

                        FragmentTransaction ft31 = fm31.beginTransaction();
                        ft31.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        Profile frag131 = new Profile();
                        ft31.replace(R.id.replace, frag131);
                        //ft.addToBackStack(null);
                        ft31.commit();
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
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

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Notifications.class);
                startActivity(intent);
            }
        });


        cart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uid.length() > 0) {
                    navigation.setSelectedItemId(R.id.action_cart);
                } else {
                    Toast.makeText(MainActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                }

                drawer.closeDrawer(GravityCompat.START);

            }
        });


        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigation.setSelectedItemId(R.id.action_search);

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
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    Wishlist frag1 = new Wishlist();
                    ft.replace(R.id.replace, frag1);
                    ft.addToBackStack(null);
                    ft.commit();
                    //drawer.closeDrawer(GravityCompat.START);
                } else {
                    Toast.makeText(MainActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                }

                drawer.closeDrawer(GravityCompat.START);

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

                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                LoginManager.getInstance().logOut();
                                SharePreferenceUtils.getInstance().deletePref();

                                Intent intent = new Intent(MainActivity.this, Spalsh.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        });

                /*SharePreferenceUtils.getInstance().deletePref();

                Intent intent = new Intent(MainActivity.this, Spalsh.class);
                startActivity(intent);
                finishAffinity();*/

            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uid.length() > 0) {
                    navigation.setSelectedItemId(R.id.action_cart);
                } else {
                    Toast.makeText(MainActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                }

                drawer.closeDrawer(GravityCompat.START);

            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm31 = getSupportFragmentManager();
                FragmentTransaction ft31 = fm31.beginTransaction();
                ft31.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                Orders frag131 = new Orders();
                ft31.replace(R.id.replace, frag131);
                ft31.addToBackStack(null);
                ft31.commit();
                drawer.closeDrawer(GravityCompat.START);


            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm31 = getSupportFragmentManager();
                FragmentTransaction ft31 = fm31.beginTransaction();
                ft31.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                Find frag131 = new Find();
                ft31.replace(R.id.replace, frag131);
                ft31.addToBackStack(null);
                ft31.commit();
                drawer.closeDrawer(GravityCompat.START);


            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/LittlePlanetGuntur/")));
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://twitter.com/Littleplanet_gn")));
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/littleplanet_gnt/")));
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/channel/UCFuPwPnWJ0fgalabPLG7luQ")));
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        navigation.setSelectedItemId(R.id.action_home);

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

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

            Call<cartBean> call2 = cr.getCart(
                    SharePreferenceUtils.getInstance().getString("userId"),
                    SharePreferenceUtils.getInstance().getString("location"),
                    SharePreferenceUtils.getInstance().getString("lat"),
                    SharePreferenceUtils.getInstance().getString("lng")
            );
            call2.enqueue(new Callback<cartBean>() {
                @Override
                public void onResponse(Call<cartBean> call, Response<cartBean> response) {

                    if (response.body().getData().size() > 0) {


                        count.setText(String.valueOf(response.body().getData().size()));


                    } else {

                        count.setText("0");

                    }


                }

                @Override
                public void onFailure(Call<cartBean> call, Throwable t) {

                }
            });

            //getRew();

        } else {
            count.setText("0");
        }
    }


    void getRew() {

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

        Call<String> call = cr.getRew(SharePreferenceUtils.getInstance().getString("userId"));

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                rewards.setText("LP Rewards - " + response.body());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        loadCart();

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

        Call<ratingsBean> call = cr.checkLoaderRating(
                SharePreferenceUtils.getInstance().getString("userId")
        );

        call.enqueue(new Callback<ratingsBean>() {
            @Override
            public void onResponse(Call<ratingsBean> call, final Response<ratingsBean> response) {

                if (response.body().getStatus().equals("1")) {
                    final Dialog dialog = new Dialog(MainActivity.this, R.style.MyDialogTheme);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.rating_dialog);
                    dialog.show();

                    TextView title = dialog.findViewById(R.id.textView143);
                    final SmileyRating rating = dialog.findViewById(R.id.textView142);
                    Button submit = dialog.findViewById(R.id.button18);
                    title.setText("Please rate Order #" + response.body().getMessage());

                    rating.setRating(5);

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            SmileyRating.Type smiley = rating.getSelectedSmiley();

                            // You can get the user rating too
                            // rating will between 1 to 5, but -1 is none selected
                            int rating2 = smiley.getRating();

                            Call<ratingsBean> call2 = cr.submitLoaderRating(
                                    response.body().getMessage(),
                                    String.valueOf(rating2)
                            );

                            call2.enqueue(new Callback<ratingsBean>() {
                                @Override
                                public void onResponse(Call<ratingsBean> call, Response<ratingsBean> response) {

                                    if (response.body().getStatus().equals("1")) {
                                        dialog.dismiss();
                                    }

                                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                                }

                                @Override
                                public void onFailure(Call<ratingsBean> call, Throwable t) {

                                }
                            });

                        }
                    });


                }


            }

            @Override
            public void onFailure(Call<ratingsBean> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
