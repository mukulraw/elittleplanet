package com.technuoma.elittleplanet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import zendesk.chat.Chat;
import zendesk.chat.ChatConfiguration;
import zendesk.chat.ChatEngine;
import zendesk.chat.ChatProvidersConfiguration;
import zendesk.chat.VisitorInfo;
import zendesk.messaging.MessagingActivity;

public class Contact extends Fragment {

    TextView whatsapp;
    Toolbar toolbar;
    Button chat;
    MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_contact, container, false);
        mainActivity = (MainActivity) getActivity();
        whatsapp = view.findViewById(R.id.textView7);
        toolbar = view.findViewById(R.id.toolbar5);
        chat = view.findViewById(R.id.button15);

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                String.format("https://api.whatsapp.com/send?phone=%s&text=%s", whatsapp.getText().toString(), "")
                        )
                );

                startActivity(intent);

            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatConfiguration chatConfiguration = ChatConfiguration.builder()
                        .withAgentAvailabilityEnabled(false)
                        .build();

                VisitorInfo visitorInfo = VisitorInfo.builder()
                        .withName(SharePreferenceUtils.getInstance().getString("name"))
                        .withEmail(SharePreferenceUtils.getInstance().getString("email"))
                        .withPhoneNumber(SharePreferenceUtils.getInstance().getString("phone")) // numeric string
                        .build();

                ChatProvidersConfiguration chatProvidersConfiguration = ChatProvidersConfiguration.builder()
                        .withVisitorInfo(visitorInfo)
                        .withDepartment("eLittle Planet")
                        .build();

                Chat.INSTANCE.setChatProvidersConfiguration(chatProvidersConfiguration);

                MessagingActivity.builder()
                        .withEngines(ChatEngine.engine())
                        .show(mainActivity, chatConfiguration);
            }
        });

        return view;
    }

}