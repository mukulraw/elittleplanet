package com.technuoma.elittleplanet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.google.firebase.messaging.FirebaseMessaging;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

public class Spalsh extends AppCompatActivity implements InstallReferrerStateListener {

    private static final String TAG = "referrer";
    Timer t;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,


    };

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    InstallReferrerClient mReferrerClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);

        mReferrerClient = InstallReferrerClient.newBuilder(this).build();
        mReferrerClient.startConnection(this);

        if (hasPermissions(this, PERMISSIONS)) {
            startApp();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS);
        }

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {

            Log.d("permmm", "1");

            if (hasPermissions(this, PERMISSIONS)) {

                Log.d("permmm", "2");

                startApp();

            } else {
                if (
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                ) {

                    Log.d("permmm", "3");

                    Toast.makeText(getApplicationContext(), "Permissions are required for this app", Toast.LENGTH_SHORT).show();
                    finish();

                } else {

                    Log.d("permmm", "4");
                    Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                            .show();
                    finish();
                    //                            //proceed with logic by disabling the related features or quit the app.
                }
            }
        }

    }


    void startApp() {

        FirebaseMessaging.getInstance().subscribeToTopic("elittlplanet").addOnCompleteListener(task -> Log.d("task", task.toString()));

        t = new Timer();

        String id = SharePreferenceUtils.getInstance().getString("userId");

        t.schedule(new TimerTask() {
            @Override
            public void run() {

                if (id.length() > 0) {


                    Intent intent2 = getIntent();
                    Uri data = intent2.getData();
                    if (data != null) {
                        String pid = data.getQueryParameter("id");
                        String action = intent2.getAction();
                        Log.d("action", action);
                        Log.d("data", pid);

                        Intent intent = new Intent(Spalsh.this, MainActivity.class);
                        intent.putExtra("pid", pid);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(Spalsh.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }



                } else {
                    Intent intent = new Intent(Spalsh.this, Login2.class);
                    startActivity(intent);
                    finish();
                }


            }
        }, 1200);

    }

    @Override
    public void onInstallReferrerSetupFinished(int responseCode) {
        switch (responseCode) {
            case InstallReferrerClient.InstallReferrerResponse.OK:
                try {
                    Log.v(TAG, "InstallReferrer conneceted");
                    ReferrerDetails response = mReferrerClient.getInstallReferrer();

                    SharePreferenceUtils.getInstance().saveString("referrer", response.getInstallReferrer());

                    mReferrerClient.endConnection();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                Log.w(TAG, "InstallReferrer not supported");
                break;
            case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                Log.w(TAG, "Unable to connect to the service");
                break;
            default:
                Log.w(TAG, "responseCode not found.");
        }
    }

    @Override
    public void onInstallReferrerServiceDisconnected() {
        mReferrerClient.startConnection(this);
    }
}
