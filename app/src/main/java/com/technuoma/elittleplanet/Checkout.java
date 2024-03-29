package com.technuoma.elittleplanet;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.share.Share;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.razorpay.PaymentResultListener;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;
import com.technuoma.elittleplanet.addressPOJO.Datum;
import com.technuoma.elittleplanet.addressPOJO.addressBean;
import com.technuoma.elittleplanet.checkPromoPOJO.checkPromoBean;
import com.technuoma.elittleplanet.checkoutPOJO.checkoutBean;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
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

public class Checkout extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, PaymentResultListener {

    Toolbar toolbar;
    EditText name, address, area, city, pin, promo, phone;
    Button proceed, apply;
    ProgressBar progress;
    String amm, gtotal;
    Spinner slot, addr;
    String tslot = "";
    String paymode;
    RadioGroup group;
    String oid;
    TextView date, delivery;
    TextView amount, grand;
    String dd = "";
    List<String> ts;

    String pid;

    List<String> list;
    List<Datum> adlist;

    String isnew = "1";
    String del;
    float promo_amount = 0;
    int wallet_amount = 0;
    int wallet_value = 0;

    CheckBox wallettitle;
    TextView wallet;

    TextView coupon, coupontitle, change;
    private int AUTOCOMPLETE_REQUEST_CODE = 122;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.razorpay.Checkout.preload(getApplicationContext());

        setContentView(R.layout.activity_checkout);
        Places.initialize(this, getString(R.string.google_maps_key));
        MapUtility.apiKey = getResources().getString(R.string.google_maps_key);

        list = new ArrayList<>();
        ts = new ArrayList<>();
        adlist = new ArrayList<>();

        amm = getIntent().getStringExtra("amount");
        del = getIntent().getStringExtra("del");

        toolbar = findViewById(R.id.toolbar4);
        coupon = findViewById(R.id.textView512);
        coupontitle = findViewById(R.id.textView522);
        wallettitle = findViewById(R.id.wallettitle);
        wallet = findViewById(R.id.wallet);
        name = findViewById(R.id.editText2);
        address = findViewById(R.id.editText3);
        proceed = findViewById(R.id.button6);
        progress = findViewById(R.id.progressBar4);
        slot = findViewById(R.id.spinner);
        addr = findViewById(R.id.spinner2);
        group = findViewById(R.id.radioButton);
        area = findViewById(R.id.editText5);
        city = findViewById(R.id.editText6);
        pin = findViewById(R.id.editText7);
        date = findViewById(R.id.textView48);
        amount = findViewById(R.id.textView49);
        grand = findViewById(R.id.textView51);
        promo = findViewById(R.id.editText8);
        apply = findViewById(R.id.button9);
        delivery = findViewById(R.id.textView50);
        phone = findViewById(R.id.editText21);
        change = findViewById(R.id.textView107);


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
        toolbar.setTitle("Checkout");


        amount.setText("₹ " + amm);

        float gt = Float.parseFloat(amm) + Float.parseFloat(del);

        grand.setText("₹ " + gt);
        delivery.setText("₹ " + del);

        gtotal = String.valueOf(gt);

        address.setText(SharePreferenceUtils.getInstance().getString("deliveryLocation"));

        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


        Call<addressBean> call = cr.getAddress(SharePreferenceUtils.getInstance().getString("userId"));
        call.enqueue(new Callback<addressBean>() {
            @Override
            public void onResponse(Call<addressBean> call, Response<addressBean> response) {

                list.clear();
                adlist.clear();

                list.add("New Address");

                if (response.body().getData().size() > 0) {

                    adlist.addAll(response.body().getData());

                    for (int i = 0; i < response.body().getData().size(); i++) {
                        list.add(response.body().getData().get(i).getName());
                    }


                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Checkout.this,
                        android.R.layout.simple_list_item_1, list);

                addr.setAdapter(adapter);


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<addressBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

        addr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {


                    isnew = "0";
                    Datum item = adlist.get(position - 1);
                    name.setText(item.getName());
                    address.setText(item.getHouse());
                    area.setText(item.getArea());
                    city.setText(item.getCity());
                    pin.setText(item.getPin());


                } else {
                    isnew = "1";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        slot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!ts.get(position).equals("No time slot available for today")) {
                    tslot = ts.get(position);
                } else {
                    tslot = "";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Checkout.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );


                GregorianCalendar g1 = new GregorianCalendar();
                g1.add(Calendar.DATE, 1);
                GregorianCalendar gc = new GregorianCalendar();
                gc.add(Calendar.DAY_OF_MONTH, 30);
                List<Calendar> dayslist = new LinkedList<Calendar>();
                Calendar[] daysArray;
                Calendar cAux = Calendar.getInstance();
                while (cAux.getTimeInMillis() <= gc.getTimeInMillis()) {
                    if (cAux.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                        Calendar c = Calendar.getInstance();
                        c.setTimeInMillis(cAux.getTimeInMillis());
                        dayslist.add(c);
                    }
                    cAux.setTimeInMillis(cAux.getTimeInMillis() + (24 * 60 * 60 * 1000));
                }
                daysArray = new Calendar[dayslist.size()];
                for (int i = 0; i < daysArray.length; i++) {
                    daysArray[i] = dayslist.get(i);
                }
                dpd.setSelectableDays(daysArray);


// If you're calling this from a support Fragment
                dpd.show(getSupportFragmentManager(), "Datepickerdialog");



                /*final Dialog dialog = new Dialog(Checkout.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.date_dialog);
                dialog.show();


                final DatePicker picker = dialog.findViewById(R.id.date);
                Button ok = dialog.findViewById(R.id.ok);

                long now = System.currentTimeMillis() - 1000;
                picker.setMaxDate(now);






                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int year = picker.getYear();
                        int month = picker.getMonth();
                        int day = picker.getDayOfMonth();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = format.format(calendar.getTime());

                        dialog.dismiss();

                        date.setText("Date - " + strDate + " (click to change)");

                        dd = strDate;

                    }
                });*/

            }
        });


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pc = promo.getText().toString();

                if (pc.length() > 0) {

                    apply.setEnabled(false);
                    apply.setClickable(false);

                    promo.setEnabled(false);
                    promo.setClickable(false);

                    progress.setVisibility(View.VISIBLE);

                    Bean b = (Bean) getApplicationContext();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                    Call<checkPromoBean> call = cr.checkPromo(pc, SharePreferenceUtils.getInstance().getString("userId"), amm);

                    call.enqueue(new Callback<checkPromoBean>() {
                        @Override
                        public void onResponse(Call<checkPromoBean> call, Response<checkPromoBean> response) {

                            if (response.body().getStatus().equals("1")) {

                                float amt = Float.parseFloat(amm);
                                float dis = Float.parseFloat(response.body().getData().getDiscount());
                                promo_amount = dis;

                                float na = amt - dis;

                                coupon.setText("₹ " + dis);
                                coupon.setVisibility(View.VISIBLE);
                                coupontitle.setVisibility(View.VISIBLE);
                                //amm = String.valueOf(na);

                                amount.setText("₹ " + amm);

                                float gt = Float.parseFloat(amm) + Float.parseFloat(del) - wallet_amount - promo_amount;

                                grand.setText("₹ " + gt);

                                gtotal = String.valueOf(gt);

                                pid = response.body().getData().getPid();

                                Toast.makeText(Checkout.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(Checkout.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                apply.setEnabled(true);
                                apply.setClickable(true);
                                promo_amount = 0;
                                promo.setEnabled(true);
                                promo.setClickable(true);
                                coupon.setVisibility(View.GONE);
                                coupontitle.setVisibility(View.GONE);
                            }

                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<checkPromoBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                            apply.setEnabled(true);
                            apply.setClickable(true);

                            promo.setEnabled(true);
                            promo.setClickable(true);
                        }
                    });

                } else {
                    Toast.makeText(Checkout.this, "Invalid PROMO code", Toast.LENGTH_SHORT).show();
                }

            }
        });

        wallettitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wallet_amount = wallet_value;
                    wallet.setText("₹ " + wallet_amount);
                    float gt = Float.parseFloat(amm) + Float.parseFloat(del) - wallet_amount - promo_amount;

                    grand.setText("₹ " + gt);

                    gtotal = String.valueOf(gt);
                } else {
                    wallet_amount = 0;
                    wallet.setText("₹ " + wallet_amount);
                    float gt = Float.parseFloat(amm) + Float.parseFloat(del) - wallet_amount - promo_amount;

                    grand.setText("₹ " + gt);

                    gtotal = String.valueOf(gt);
                }
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String n = name.getText().toString();
                String ph = phone.getText().toString();
                String a = address.getText().toString();
                String ar = area.getText().toString();
                String c = city.getText().toString();
                String p = pin.getText().toString();

                if (n.length() > 0) {
                    if (ph.length() == 10) {
                        if (a.length() > 0) {
                            if (p.length() > 0) {

                                int iidd = group.getCheckedRadioButtonId();

                                if (iidd > -1) {

                                    if (dd.length() > 0) {

                                        if (tslot.length() > 0) {
                                            RadioButton cb = group.findViewById(iidd);

                                            paymode = cb.getText().toString();


                                            oid = String.valueOf(System.currentTimeMillis());

                                            if (paymode.equals("Cash on Delivery")) {
                                                progress.setVisibility(View.VISIBLE);

                                                Bean b = (Bean) getApplicationContext();

                                                String adr = a + ", " + p;

                                                Log.d("addd", adr);

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

                                                Call<checkoutBean> call = cr.buyVouchers(
                                                        SharePreferenceUtils.getInstance().getString("userId"),
                                                        SharePreferenceUtils.getInstance().getString("lat"),
                                                        SharePreferenceUtils.getInstance().getString("lng"),
                                                        gtotal,
                                                        oid,
                                                        del,
                                                        String.valueOf(wallet_amount),
                                                        n,
                                                        ph,
                                                        adr,
                                                        paymode,
                                                        tslot,
                                                        dd,
                                                        pid,
                                                        String.valueOf(promo_amount),
                                                        a,
                                                        ar,
                                                        c,
                                                        p,
                                                        isnew
                                                );
                                                call.enqueue(new Callback<checkoutBean>() {
                                                    @Override
                                                    public void onResponse(Call<checkoutBean> call, Response<checkoutBean> response) {

                                                        Toast.makeText(Checkout.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                                        progress.setVisibility(View.GONE);

                                                        Dialog dialog = new Dialog(Checkout.this, R.style.DialogCustomTheme);
                                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                        dialog.setCancelable(true);
                                                        dialog.setContentView(R.layout.success_popup);
                                                        dialog.show();

                                                        TextView oi = dialog.findViewById(R.id.textView57);
                                                        TextView au = dialog.findViewById(R.id.textView58);

                                                        oi.setText(oid);
                                                        au.setText("₹ " + gtotal);

                                                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                            @Override
                                                            public void onCancel(DialogInterface dialog) {

                                                                dialog.dismiss();
                                                                finish();

                                                            }
                                                        });

                                                    }

                                                    @Override
                                                    public void onFailure(Call<checkoutBean> call, Throwable t) {
                                                        progress.setVisibility(View.GONE);
                                                    }
                                                });
                                            } else {

                                                progress.setVisibility(View.VISIBLE);

                                                Bean b = (Bean) getApplicationContext();

                                                String adr = a + ", " + ar + ", " + c + ", " + p;

                                                Log.d("addd", adr);

                                                Retrofit retrofit = new Retrofit.Builder()
                                                        .baseUrl(b.baseurl)
                                                        .addConverterFactory(ScalarsConverterFactory.create())
                                                        .addConverterFactory(GsonConverterFactory.create())
                                                        .build();

                                                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                                                Call<payBean> call1 = cr.getOrderId(String.valueOf(Float.parseFloat(gtotal) * 100), oid);
                                                //Call<payBean> call1 = cr.getOrderId(String.valueOf(1 * 100), oid);

                                                call1.enqueue(new Callback<payBean>() {
                                                    @Override
                                                    public void onResponse(Call<payBean> call, Response<payBean> response) {

                                                        com.razorpay.Checkout checkout = new com.razorpay.Checkout();
                                                        checkout.setKeyID("rzp_live_OkvAX9qLSg7u74");
                                                        checkout.setImage(R.drawable.back);

                                                        try {
                                                            JSONObject options = new JSONObject();

                                                            options.put("name", "little planet");
                                                            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                                                            options.put("order_id", response.body().getId());//from response of step 3.
                                                            options.put("theme.color", "#3399cc");
                                                            options.put("currency", "INR");
                                                            options.put("amount", String.valueOf(response.body().getAmount() * 100));//pass amount in currency subunits
                                                            //options.put("prefill.email", "gaurav.kumar@example.com");
                                                            options.put("prefill.contact", SharePreferenceUtils.getInstance().getString("phone"));
                                                            checkout.open(Checkout.this, options);
                                                        } catch (Exception e) {
                                                            Log.e("TAG", "Error in starting Razorpay Checkout", e);
                                                        }

                                                        progress.setVisibility(View.GONE);

                                                    }

                                                    @Override
                                                    public void onFailure(Call<payBean> call, Throwable t) {
                                                        progress.setVisibility(View.GONE);
                                                    }
                                                });

                                                    /*Intent intent = new Intent(Checkout.this, WebViewActivity.class);
                                                    intent.putExtra(AvenuesParams.ACCESS_CODE, "AVOL70EE77BF91LOFB");
                                                    intent.putExtra(AvenuesParams.MERCHANT_ID, "133862");
                                                    intent.putExtra(AvenuesParams.ORDER_ID, oid);
                                                    intent.putExtra(AvenuesParams.CURRENCY, "INR");
                                                    intent.putExtra(AvenuesParams.AMOUNT, String.valueOf(gtotal));
                                                    //intent.putExtra(AvenuesParams.AMOUNT, "1");
                                                    intent.putExtra("pid", SharePreferenceUtils.getInstance().getString("userid"));

                                                    intent.putExtra(AvenuesParams.REDIRECT_URL, "https://mrtecks.com/grocery/api/pay/ccavResponseHandler.php");
                                                    intent.putExtra(AvenuesParams.CANCEL_URL, "https://mrtecks.com/grocery/api/pay/ccavResponseHandler.php");
                                                    intent.putExtra(AvenuesParams.RSA_KEY_URL, "https://mrtecks.com/grocery/api/pay/GetRSA.php");

                                                    startActivityForResult(intent, 12);*/


                                            }
                                        } else {
                                            Toast.makeText(Checkout.this, "Please select a Delivery Time Slot", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(Checkout.this, "Please select a Delivery Date", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(Checkout.this, "Please select a Payment Mode", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(Checkout.this, "Please select a valid PIN Code", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Checkout.this, "Please enter a valid address", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Checkout.this, "Please enter a valid phone", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Checkout.this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
                }

            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Checkout.this, LocationPickerActivity.class);
                intent.putExtra(MapUtility.LATITUDE, Double.parseDouble(SharePreferenceUtils.getInstance().getString("lat")));
                intent.putExtra(MapUtility.LONGITUDE, Double.parseDouble(SharePreferenceUtils.getInstance().getString("lng")));
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getRew();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                try {
                    if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {

                        double lat = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                        double lng = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);

                        SharePreferenceUtils.getInstance().saveString("lat", String.valueOf(lat));
                        SharePreferenceUtils.getInstance().saveString("lng", String.valueOf(lng));

                        Log.d("asdaddasd", data.getStringExtra(MapUtility.ADDRESS));

                        String srcAddress = data.getStringExtra(MapUtility.ADDRESS);
                        address.setText(srcAddress);

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(Checkout.this, status.toString(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {

            progress.setVisibility(View.VISIBLE);

            Bean b = (Bean) getApplicationContext();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.baseurl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

            String n = name.getText().toString();
            String ph = phone.getText().toString();
            String a = address.getText().toString();
            String ar = area.getText().toString();
            String c = city.getText().toString();
            String p = pin.getText().toString();

            String adr = a + ", " + ar + ", " + c + ", " + p;

            Log.d("addd", adr);


            Call<checkoutBean> call = cr.buyVouchers(
                    SharePreferenceUtils.getInstance().getString("userId"),
                    SharePreferenceUtils.getInstance().getString("lat"),
                    SharePreferenceUtils.getInstance().getString("lng"),
                    gtotal,
                    oid,
                    del,
                    String.valueOf(wallet_amount),
                    n,
                    ph,
                    adr,
                    "online",
                    tslot,
                    dd,
                    pid,
                    String.valueOf(promo_amount),
                    a,
                    ar,
                    c,
                    p,
                    isnew
            );
            call.enqueue(new Callback<checkoutBean>() {
                @Override
                public void onResponse(Call<checkoutBean> call, Response<checkoutBean> response) {

                    Toast.makeText(Checkout.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    progress.setVisibility(View.GONE);


                    Dialog dialog = new Dialog(Checkout.this, R.style.DialogCustomTheme);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.success_popup);
                    dialog.show();


                    TextView oi = dialog.findViewById(R.id.textView57);
                    TextView au = dialog.findViewById(R.id.textView58);

                    oi.setText(oid);
                    au.setText("₹ " + gtotal);


                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                            dialog.dismiss();
                            finish();

                        }
                    });


                }

                @Override
                public void onFailure(Call<checkoutBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });

        }

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = format.format(calendar.getTime());


        date.setText(strDate + " (click to change)");

        dd = strDate;


        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        Log.d("current date", formattedDate);

        if (dd.equals(formattedDate)) {

            String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

            Log.d("today", currentTime);

            String time1 = "10:00";
            String time2 = "15:00";
            String time3 = "14:00";
            String time4 = "16:00";
            String time5 = "18:00";
            String time6 = "19:30";

            Date date1 = null;
            Date date2 = null;
            Date date3 = null;
            Date date4 = null;
            Date date5 = null;
            Date date6 = null;
            Date cd = null;

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

            try {
                cd = dateFormat.parse(currentTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            try {
                date1 = dateFormat.parse(time1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                date2 = dateFormat.parse(time2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                date3 = dateFormat.parse(time3);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                date4 = dateFormat.parse(time4);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                date5 = dateFormat.parse(time5);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                date6 = dateFormat.parse(time6);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ts.clear();
            tslot = "";


            if (date1.compareTo(cd) > 0) {
                ts.add("10:00 AM - 03:00 PM");
            }
            if (date2.compareTo(cd) > 0) {
                ts.add("03:00 PM - 08:00 PM");
            }

           /* if (date2.compareTo(cd) > 0)
            {
                ts.add("11:30 - 1:30");
            }

            if (date3.compareTo(cd) > 0)
            {
                ts.add("2:00 - 4:00");
            }
            if (date4.compareTo(cd) > 0)
            {
                ts.add("4:00 - 6:00");
            }
            if (date5.compareTo(cd) > 0)
            {
                ts.add("6:00 - 7:30");
            }
            if (date6.compareTo(cd) > 0)
            {
                ts.add("7:30 - 9:00");
            }*/
            else {
                ts.add("No time slot available for today");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, ts);

            slot.setAdapter(adapter);


        } else {
            Log.d("today", "false");
            ts.clear();
            tslot = "";


            ts.add("10:00 AM - 03:00 PM");
            ts.add("03:00 PM - 08:00 PM");
            //ts.add("11:30 - 1:30");
            //ts.add("2:00 - 4:00");
            //ts.add("4:00 - 6:00");
            //ts.add("6:00 - 7:30");
            //ts.add("7:30 - 9:00");


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, ts);

            slot.setAdapter(adapter);

        }

    }

    @Override
    public void onPaymentSuccess(String s) {

        Log.e("TAG", "Exception in onPaymentError" + s);

        try {
            //Toast.makeText(this, "Payment Successful: " + s, Toast.LENGTH_SHORT).show();

            progress.setVisibility(View.VISIBLE);

            Bean b = (Bean) getApplicationContext();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.baseurl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

            String n = name.getText().toString();
            String ph = phone.getText().toString();
            String a = address.getText().toString();
            String ar = area.getText().toString();
            String c = city.getText().toString();
            String p = pin.getText().toString();

            String adr = a + ", " + ar + ", " + c + ", " + p;

            Log.d("addd", adr);


            Call<checkoutBean> call = cr.buyVouchers(
                    SharePreferenceUtils.getInstance().getString("userId"),
                    SharePreferenceUtils.getInstance().getString("lat"),
                    SharePreferenceUtils.getInstance().getString("lng"),
                    gtotal,
                    s,
                    del,
                    String.valueOf(wallet_amount),
                    n,
                    ph,
                    adr,
                    "online",
                    tslot,
                    dd,
                    pid,
                    String.valueOf(promo_amount),
                    a,
                    ar,
                    c,
                    p,
                    isnew
            );
            call.enqueue(new Callback<checkoutBean>() {
                @Override
                public void onResponse(Call<checkoutBean> call, Response<checkoutBean> response) {

                    Toast.makeText(Checkout.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    progress.setVisibility(View.GONE);


                    Dialog dialog = new Dialog(Checkout.this, R.style.DialogCustomTheme);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.success_popup);
                    dialog.show();


                    TextView oi = dialog.findViewById(R.id.textView57);
                    TextView au = dialog.findViewById(R.id.textView58);

                    oi.setText(s);
                    au.setText("₹ " + gtotal);


                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                            dialog.dismiss();
                            finish();

                        }
                    });


                }

                @Override
                public void onFailure(Call<checkoutBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });

        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("TAG", "Exception in onPaymentError" + s);
        try {
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentError", e);
        }
    }

    void getRew() {

//        progress.setVisibility(View.VISIBLE);

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

                wallettitle.setText("Use Wallet - ₹ " + response.body());
                wallet_amount = Integer.parseInt(response.body());
                wallet_value = Integer.parseInt(response.body());
//
//                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                progress.setVisibility(View.GONE);
            }
        });

    }

}
