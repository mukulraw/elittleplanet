package com.technuoma.elittleplanet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appyvet.materialrangebar.RangeBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.technuoma.elittleplanet.productsPOJO.Datum;
import com.technuoma.elittleplanet.productsPOJO.productsBean;
import com.technuoma.elittleplanet.seingleProductPOJO.singleProductBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

public class productList extends Fragment {

    RecyclerView grid;
    ProgressBar progress;

    String id;

    List<Datum> list;
    private List<Datum> filteredList;
    private List<Datum> sortedList;
    BestAdapter adapter;
    MainActivity mainActivity;

    RelativeLayout sort, filter;

    private boolean isFilter = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_layout, container, false);
        mainActivity = (MainActivity) getActivity();

        list = new ArrayList<>();
        filteredList = new ArrayList<>();
        sortedList = new ArrayList<>();

        id = getArguments().getString("id");

        grid = view.findViewById(R.id.grid);
        progress = view.findViewById(R.id.progress);
        sort = view.findViewById(R.id.button11);
        filter = view.findViewById(R.id.button12);

        adapter = new BestAdapter(mainActivity, list);
        GridLayoutManager manager = new GridLayoutManager(mainActivity, 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        sort.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(mainActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.sort_dialog_layout);
            dialog.setCancelable(true);
            dialog.show();

            final RadioGroup group = dialog.findViewById(R.id.group);
            TextView res = dialog.findViewById(R.id.reset);
            final TextView sor = dialog.findViewById(R.id.sort);

            res.setOnClickListener(v14 -> {

                if (isFilter) {

                    adapter.setData(filteredList);


                } else {

                    adapter.setData(list);


                }

                dialog.dismiss();

            });

            sor.setOnClickListener(v13 -> {

                int iidd = group.getCheckedRadioButtonId();

                if (iidd > -1) {

                    Log.d("sort", "1");

                    if (iidd == R.id.l2h) {

                        sortedList.clear();

                        Log.d("sort", "2");

                        if (isFilter) {
                            Log.d("sort", "3");
                            List<Datum> fl = new ArrayList<>();
                            fl = filteredList;
                            sortedList.addAll(fl);
                        } else {
                            Log.d("sort", "4");
                            List<Datum> fl = new ArrayList<>();
                            fl = list;
                            sortedList.addAll(fl);
                        }


                        Collections.sort(sortedList, (o1, o2) -> {
                            Log.d("sort", "5");
                            if (o2.getPrice() != null && o1.getPrice() != null) {
                                return Float.valueOf(o1.getPrice()).compareTo(Float.valueOf(o2.getPrice()));
                            } else if (o2.getPrice() == null && o1.getPrice() != null) {
                                return Float.valueOf(o1.getPrice()).compareTo(Float.valueOf(o2.getPrice()));
                            } else if (o2.getPrice() != null && o1.getPrice() == null) {
                                return Float.valueOf(o1.getPrice()).compareTo(Float.valueOf(o2.getPrice()));
                            } else {
                                return Float.valueOf(o1.getPrice()).compareTo(Float.valueOf(o2.getPrice()));
                            }
                        });


                        Log.d("sort", "6");
                        adapter.setData(sortedList);


                        dialog.dismiss();

                    } else if (iidd == R.id.h2l) {
                        Log.d("sort", "7");
                        sortedList.clear();

                        if (isFilter) {
                            Log.d("sort", "8");
                            sortedList.addAll(filteredList);
                        } else {
                            Log.d("sort", "9");
                            sortedList.addAll(list);
                        }


                        Collections.sort(sortedList, (o2, o1) -> {
                            Log.d("sort", "10");
                            if (o2.getPrice() != null && o1.getPrice() != null) {
                                return Float.valueOf(o1.getPrice()).compareTo(Float.valueOf(o2.getPrice()));
                            } else if (o2.getPrice() == null && o1.getPrice() != null) {
                                return Float.valueOf(o1.getPrice()).compareTo(Float.valueOf(o2.getPrice()));
                            } else if (o2.getPrice() != null && o1.getPrice() == null) {
                                return Float.valueOf(o1.getPrice()).compareTo(Float.valueOf(o2.getPrice()));
                            } else {
                                return Float.valueOf(o1.getPrice()).compareTo(Float.valueOf(o2.getPrice()));
                            }
                        });

                        Log.d("sort", "12");
                        adapter.setData(sortedList);


                        dialog.dismiss();

                    }

                } else {
                    Log.d("sort", "13");
                    Toast.makeText(mainActivity, "Please select a Sorting type", Toast.LENGTH_SHORT).show();
                }

            });

        });

        filter.setOnClickListener(v -> {

            final Dialog dialog1 = new Dialog(mainActivity);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setContentView(R.layout.price_filter_dialog);
            dialog1.setCancelable(true);
            dialog1.show();


            final TextView prii = dialog1.findViewById(R.id.prii);
            RangeBar range = dialog1.findViewById(R.id.range);
            RecyclerView fgrid = dialog1.findViewById(R.id.grid);
            GridLayoutManager fmanager = new GridLayoutManager(mainActivity, 1);
            TextView fil = dialog1.findViewById(R.id.filter);
            TextView res = dialog1.findViewById(R.id.reset);

            final String[] min = {"1"};
            final String[] max = {"5000"};

            range.setOnRangeBarChangeListener((rangeBar, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue) -> {

                min[0] = leftPinValue;
                max[0] = rightPinValue;

                prii.setText("Price: " + leftPinValue + " - " + rightPinValue);

            });


            List<String> flist = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                flist.add(list.get(i).getBrand());
            }

            Log.d("flist", TextUtils.join(",", flist));

            HashSet<String> hashSet = new HashSet<>();
            hashSet.addAll(flist);
            flist.clear();
            flist.addAll(hashSet);


            final FilterAdapter fadapter = new FilterAdapter(mainActivity, flist);
            fgrid.setAdapter(fadapter);
            fgrid.setLayoutManager(fmanager);


            res.setOnClickListener(v12 -> {


                adapter.setData(list);


                isFilter = false;
                dialog1.dismiss();

            });


            fil.setOnClickListener(v1 -> {

                List<String> fl = fadapter.getChecked();

                Log.d("flist", TextUtils.join(",", fl));

                filteredList.clear();

                for (int i = 0; i < list.size(); i++) {

                    for (int j = 0; j < fl.size(); j++) {

                        if (fl.get(j).equals(list.get(i).getBrand()) && Float.parseFloat(list.get(i).getPrice()) >= Float.parseFloat(min[0]) && Float.parseFloat(list.get(i).getPrice()) <= Float.parseFloat(max[0])) {
                            filteredList.add(list.get(i));
                        }


                    }

                }


                adapter.setData(filteredList);


                isFilter = true;
                dialog1.dismiss();

            });


        });


        return view;
    }

    class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

        final Context context;

        List<String> flist;

        final List<String> checked = new ArrayList<>();

        FilterAdapter(Context context, List<String> flist) {
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

            final String item = flist.get(i);

            viewHolder.check.setText(item);

            viewHolder.check.setChecked(true);

            checked.add(item);

            viewHolder.check.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {

                    checked.add(item);

                } else {

                    checked.remove(item);

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

    @Override
    public void onResume() {
        super.onResume();

        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getActivity().getApplicationContext();

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

        Call<productsBean> call = cr.getProducts(id, SharePreferenceUtils.getInstance().getString("location"));
        call.enqueue(new Callback<productsBean>() {
            @Override
            public void onResponse(Call<productsBean> call, Response<productsBean> response) {


                if (response.body().getStatus().equals("1")) {
                    list = response.body().getData();
                    adapter.setData(list);
                }

                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<productsBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });
    }

    class BestAdapter extends RecyclerView.Adapter<BestAdapter.ViewHolder> {

        Context context;
        List<Datum> list = new ArrayList<>();

        public BestAdapter(Context context, List<Datum> list) {
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
            View view = inflater.inflate(R.layout.best_list_model2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Datum item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);


            if (item.getStock().equals("In stock")) {
                holder.add.setEnabled(true);
            } else {
                holder.add.setEnabled(false);
            }

            holder.stock.setText(item.getStock());


            float dis = Float.parseFloat(item.getDiscount());
            String nv1 = null;


            if (dis > 0) {

                float pri = Float.parseFloat(item.getPrice());
                float dv = (dis / 100) * pri;

                float nv = pri - dv;

                nv1 = String.valueOf(nv);

                holder.discount.setVisibility(View.VISIBLE);
                holder.discount.setText(item.getDiscount() + "% OFF");
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

            final String finalNv = nv1;
            String finalNv1 = nv1;
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
                                Log.d("quantity", String.valueOf(stepperTouch.getCount()));
                                Log.d("price", finalNv1);

                                int versionCode = BuildConfig.VERSION_CODE;
                                String versionName = BuildConfig.VERSION_NAME;

                                String cl = String.valueOf(color.getSelectedItem());
                                String sz = String.valueOf(size.getSelectedItem());

                                Call<singleProductBean> call = cr.addCart(SharePreferenceUtils.getInstance().getString("userId"), item.getId(), String.valueOf(stepperTouch.getCount()), finalNv1, versionName, sz, cl);

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
            TextView price, title, discount, stock;
            Button add;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView4);
                price = itemView.findViewById(R.id.textView11);
                title = itemView.findViewById(R.id.textView12);
                discount = itemView.findViewById(R.id.textView10);
                add = itemView.findViewById(R.id.button5);
                stock = itemView.findViewById(R.id.textView64);

            }
        }
    }

}
