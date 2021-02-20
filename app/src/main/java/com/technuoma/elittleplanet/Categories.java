package com.technuoma.elittleplanet;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.technuoma.elittleplanet.homePOJO.Cat;
import com.technuoma.elittleplanet.homePOJO.homeBean;

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

public class Categories extends Fragment {

    RecyclerView grid;
    ProgressBar progress;

    String id;

    List<Cat> list3;
    CategoryAdapter categoryAdapter;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_layout, container, false);
        mainActivity = (MainActivity) getActivity();
        list3 = new ArrayList<>();

        grid = view.findViewById(R.id.grid);
        progress = view.findViewById(R.id.progress);

        categoryAdapter = new CategoryAdapter(mainActivity, list3);
        GridLayoutManager manager = new GridLayoutManager(mainActivity, 3);
        grid.setAdapter(categoryAdapter);
        grid.setLayoutManager(manager);

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

        Call<homeBean> call = cr.getHome(SharePreferenceUtils.getInstance().getString("lat"), SharePreferenceUtils.getInstance().getString("lng"));
        call.enqueue(new Callback<homeBean>() {
            @Override
            public void onResponse(Call<homeBean> call, Response<homeBean> response) {


                if (response.body().getStatus().equals("1")) {

                    categoryAdapter.setData(response.body().getCat());

                }

                progress.setVisibility(View.GONE);

                Log.d("asdasd", response.body().getMessage());

            }

            @Override
            public void onFailure(Call<homeBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Log.d("asdasd", t.toString());
            }
        });

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

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.category_list_model5, parent, false);
            return new ViewHolder(view);
        }

        public int getSpans(int pos) {
            return Integer.parseInt(list.get(pos).getSpace());
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            final Cat item = list.get(position);


            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);


            holder.title.setText(item.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentManager fm4 = mainActivity.getSupportFragmentManager();

                    FragmentTransaction ft4 = fm4.beginTransaction();
                    ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    SubCat frag14 = new SubCat();
                    Bundle b = new Bundle();
                    b.putString("id", item.getId());
                    b.putString("title", item.getName());
                    b.putString("image", item.getImage());
                    frag14.setArguments(b);
                    ft4.replace(R.id.replace, frag14);
                    ft4.addToBackStack(null);
                    //ft.addToBackStack(null);
                    ft4.commit();

                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.imageView6);
                title = itemView.findViewById(R.id.textView9);
            }
        }
    }

}
