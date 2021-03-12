package com.technuoma.elittleplanet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appyvet.materialrangebar.RangeBar;
import com.technuoma.elittleplanet.searchPOJO.Datum;
import com.technuoma.elittleplanet.searchPOJO.searchBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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

public class Search extends Fragment {

    RecyclerView grid;
    ProgressBar progress;
    EditText query;
    List<Datum> list;
    private List<Datum> filteredList;
    private List<Datum> sortedList;
    SearchAdapter adapter;
    GridLayoutManager manager;
    MainActivity mainActivity;

    RelativeLayout sort, filter;

    private boolean isFilter = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search, container, false);

        mainActivity = (MainActivity) getActivity();

        list = new ArrayList<>();
        filteredList = new ArrayList<>();
        sortedList = new ArrayList<>();


        grid = view.findViewById(R.id.grid);
        progress = view.findViewById(R.id.progressBar5);
        query = view.findViewById(R.id.editText4);
        sort = view.findViewById(R.id.button11);
        filter = view.findViewById(R.id.button12);

        adapter = new SearchAdapter(mainActivity, list);
        manager = new GridLayoutManager(mainActivity, 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);


        query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 2) {


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

                    Call<searchBean> call = cr.search(s.toString(), SharePreferenceUtils.getInstance().getString("location"));
                    call.enqueue(new Callback<searchBean>() {
                        @Override
                        public void onResponse(Call<searchBean> call, Response<searchBean> response) {


                            if (response.body().getStatus().equals("1")) {
                                list = response.body().getData();
                                adapter.setData(list);
                            }

                            progress.setVisibility(View.GONE);


                        }

                        @Override
                        public void onFailure(Call<searchBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });


                } else {

                    adapter.setData(new ArrayList<Datum>());

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

    class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

        Context context;
        List<Datum> list = new ArrayList<>();

        public SearchAdapter(Context context, List<Datum> list) {
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
            View view = inflater.inflate(R.layout.search_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Datum item = list.get(position);


            holder.title.setText(item.getName());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentManager fm4 = mainActivity.getSupportFragmentManager();

                    FragmentTransaction ft4 = fm4.beginTransaction();
                    ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    SingleProduct frag14 = new SingleProduct();
                    Bundle b = new Bundle();
                    b.putString("id", item.getPid());
                    b.putString("title", item.getName());
                    frag14.setArguments(b);
                    ft4.replace(R.id.replace, frag14);
                    ft4.addToBackStack(null);
                    ft4.commit();

                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);


                title = itemView.findViewById(R.id.textView37);


            }
        }
    }

}
