package com.technuoma.elittleplanet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Profile extends Fragment {

    MainActivity mainActivity;
    TextView terms, about, address, logout, cart, orders, refer, location, wishlist, contact, wallet, coupons;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);
        mainActivity = (MainActivity) getActivity();

        wishlist = view.findViewById(R.id.wishlist);
        refer = view.findViewById(R.id.refer);
        location = view.findViewById(R.id.textView2);
        orders = view.findViewById(R.id.orders);
        terms = view.findViewById(R.id.terms);
        about = view.findViewById(R.id.about);
        address = view.findViewById(R.id.address);
        logout = view.findViewById(R.id.logout);
        cart = view.findViewById(R.id.cart);
        contact = view.findViewById(R.id.contact);
        wallet = view.findViewById(R.id.wallet);
        coupons = view.findViewById(R.id.coupons);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.navigation.setSelectedItemId(R.id.action_search);

            }
        });


        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mainActivity, Web.class);
                intent.putExtra("title", "Terms & Conditions");
                intent.putExtra("url", "https://technuoma.com/elittleplanet/terms.php");
                startActivity(intent);

            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = mainActivity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                Wishlist frag1 = new Wishlist();
                ft.replace(R.id.replace, frag1);
                ft.addToBackStack(null);
                ft.commit();


            }
        });
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = mainActivity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                Wallet frag1 = new Wallet();
                ft.replace(R.id.replace, frag1);
                ft.addToBackStack(null);
                ft.commit();


            }
        });

        coupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = mainActivity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                Coupons frag1 = new Coupons();
                ft.replace(R.id.replace, frag1);
                ft.addToBackStack(null);
                ft.commit();


            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mainActivity, Web.class);
                intent.putExtra("title", "FAQs");
                intent.putExtra("url", "https://technuoma.com/elittleplanet/faq.php");
                startActivity(intent);

            }
        });


        refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShareCompat.IntentBuilder.from(mainActivity)
                        .setType("text/plain")
                        .setChooserTitle("Chooser title")
                        .setText("http://play.google.com/store/apps/details?id=" + mainActivity.getPackageName() + "&referrer=" + SharePreferenceUtils.getInstance().getString("userId"))
                        .startChooser();


            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, Address.class);
                startActivity(intent);

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharePreferenceUtils.getInstance().deletePref();

                Intent intent = new Intent(mainActivity, Spalsh.class);
                startActivity(intent);
                mainActivity.finishAffinity();

            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mainActivity.navigation.setSelectedItemId(R.id.action_cart);

            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm31 = mainActivity.getSupportFragmentManager();
                FragmentTransaction ft31 = fm31.beginTransaction();
                ft31.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                Orders frag131 = new Orders();
                ft31.replace(R.id.replace, frag131);
                ft31.addToBackStack(null);
                ft31.commit();
            }
        });

        return view;
    }
}
