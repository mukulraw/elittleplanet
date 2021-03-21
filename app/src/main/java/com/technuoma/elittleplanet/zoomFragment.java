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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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

public class zoomFragment extends Fragment {
    ViewPager zoompager;
    CircleIndicator zoomindicator;
    MainActivity mainActivity;
    List<String> blist = new ArrayList<>();
    int pos;


    void setData(List<String> blist, int pos) {
        this.blist = blist;
        this.pos = pos;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zoom2, container, false);
        mainActivity = (MainActivity) getActivity();

        zoompager = view.findViewById(R.id.pager);
        zoomindicator = view.findViewById(R.id.indicator);

        ZoomAdapter adapter = new ZoomAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, blist);
        zoompager.setAdapter(adapter);
        zoomindicator.setViewPager(zoompager);

        zoompager.setCurrentItem(pos);

        return view;

    }


    public class ZoomAdapter extends FragmentStatePagerAdapter {

        List<String> blist = new ArrayList<>();

        public ZoomAdapter(@NonNull FragmentManager fm, int behavior, List<String> blist) {
            super(fm, behavior);
            this.blist = blist;
        }

        /*public BannerAdapter(FragmentManager fm, List<Banners> blist) {
            super(fm);
            this.blist = blist;
        }*/

        @Override
        public Fragment getItem(int position) {
            zoompage frag = new zoompage();
            frag.setData(blist.get(position));
            return frag;
        }

        @Override
        public int getCount() {
            return blist.size();
            //return 1;
        }
    }


    public static class zoompage extends Fragment {

        String url, tit, cid = "", image2;

        ZoomageView image;

        void setData(String url) {
            this.url = url;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.banner_layout3, container, false);

            image = view.findViewById(R.id.imageView3);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(url, image, options);

            return view;
        }
    }

}
