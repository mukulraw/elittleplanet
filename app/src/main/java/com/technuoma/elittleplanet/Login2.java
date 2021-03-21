package com.technuoma.elittleplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Login2 extends AppCompatActivity {

    private static final int RC_SIGN_IN = 12;
    private static final String TAG = "Login";
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager mCallbackManager;
    Button signInButton, facebook;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager loginButton = LoginManager.getInstance();
        //loginButton.logInWithReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });


        signInButton = findViewById(R.id.button10);
        facebook = findViewById(R.id.button14);
        progress = findViewById(R.id.progressBar11);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Initialize Facebook Login button

                loginButton.logInWithReadPermissions(Login2.this, Arrays.asList("email", "public_profile"));
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, user.getDisplayName());
                            Log.d(TAG, user.getEmail());

                            String email = user.getEmail();
                            String password = user.getUid();
                            String name = user.getDisplayName();
                            String image = user.getPhotoUrl().toString();

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

                            Call<loginBean> call = cr.login(email, password, name, SharePreferenceUtils.getInstance().getString("token"));

                            call.enqueue(new Callback<loginBean>() {
                                @Override
                                public void onResponse(@NotNull Call<loginBean> call, @NotNull Response<loginBean> response) {

                                    assert response.body() != null;
                                    if (response.body().getStatus().equals("1")) {

                                        SharePreferenceUtils.getInstance().saveString("userId", response.body().getUserId());
                                        SharePreferenceUtils.getInstance().saveString("phone", response.body().getPhone());
                                        SharePreferenceUtils.getInstance().saveString("email", response.body().getEmail());
                                        SharePreferenceUtils.getInstance().saveString("name", response.body().getName());
                                        SharePreferenceUtils.getInstance().saveString("image", image);
                                        Toast.makeText(Login2.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(Login2.this, MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();

                                        /*DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                                        ImageLoader loader = ImageLoader.getInstance();
                                        loader.displayImage(SharePreferenceUtils.getInstance().getString("image"), profile, options);*/

                                    } else {
                                        Toast.makeText(Login2.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    progress.setVisibility(View.GONE);

                                }

                                @Override
                                public void onFailure(@NotNull Call<loginBean> call, @NotNull Throwable t) {
                                    progress.setVisibility(View.GONE);
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, user.getDisplayName());
                            Log.d(TAG, user.getEmail());

                            String email = user.getEmail();
                            String password = user.getUid();
                            String name = user.getDisplayName();
                            String image = user.getPhotoUrl().toString();

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

                            Call<loginBean> call = cr.login(email, password, name, SharePreferenceUtils.getInstance().getString("token"));

                            call.enqueue(new Callback<loginBean>() {
                                @Override
                                public void onResponse(@NotNull Call<loginBean> call, @NotNull Response<loginBean> response) {

                                    assert response.body() != null;
                                    if (response.body().getStatus().equals("1")) {

                                        SharePreferenceUtils.getInstance().saveString("userId", response.body().getUserId());
                                        SharePreferenceUtils.getInstance().saveString("phone", response.body().getPhone());
                                        SharePreferenceUtils.getInstance().saveString("email", response.body().getEmail());
                                        SharePreferenceUtils.getInstance().saveString("name", response.body().getName());
                                        SharePreferenceUtils.getInstance().saveString("image", image);
                                        Toast.makeText(Login2.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(Login2.this, MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();

                                        /*DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                                        ImageLoader loader = ImageLoader.getInstance();
                                        loader.displayImage(SharePreferenceUtils.getInstance().getString("image"), profile, options);*/

                                    } else {
                                        Toast.makeText(Login2.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    progress.setVisibility(View.GONE);

                                }

                                @Override
                                public void onFailure(@NotNull Call<loginBean> call, @NotNull Throwable t) {
                                    progress.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login2.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

}