package com.example.basicact;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdManager {
    private static final String TAG = "AdsManager";
    //String Interstitialid ="ca-app-pub-3940256099942544/1033173712" ;
    //String bannerId ="ca-app-pub-3940256099942544/6300978111" ;


    private Context context;
    private AdView bannerAdView;
    private InterstitialAd mInterstitialAd;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Ads");


    public AdManager(Context context) {
        this.context = context;

    }


    public void loadBannerAd(ViewGroup adContainer) {
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.exists()) {
                    String bannerId = dataSnapshot.child("Banner_ads").getValue(String.class);
                    Log.d(TAG, "Value is B: " + bannerId);
                    bannerAdView = new AdView(context);
                    bannerAdView.setAdSize(AdSize.BANNER);

                    bannerAdView.setAdUnitId(bannerId); // Replace with your banner ad unit ID

                    adContainer.addView(bannerAdView);

                    bannerAdView.setAdListener(new AdListener() {
                        @Override
                        public void onAdClicked() {
                            // Code to be executed when the user clicks on an ad.
                        }

                        @Override
                        public void onAdClosed() {
                            // Code to be executed when the user is about to return
                            // to the app after tapping on an ad.
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Code to be executed when an ad request fails.
                        }

                        @Override
                        public void onAdImpression() {
                            // Code to be executed when an impression is recorded
                            // for an ad.
                        }

                        @Override
                        public void onAdLoaded() {
                            // Code to be executed when an ad finishes loading.
                        }

                        @Override
                        public void onAdOpened() {
                            // Code to be executed when an ad opens an overlay that
                            // covers the screen.
                        }
                    });


                    AdRequest adRequest = new AdRequest.Builder().build();
                    bannerAdView.loadAd(adRequest);

                } else {
                    Log.d(TAG, "Ids are not found" );
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void loadInterstitialAd() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.exists()) {
                    String Interstitialid = dataSnapshot.child("Int").getValue(String.class);
                    Log.d(TAG, "Value is C: " + Interstitialid);
                    AdRequest adRequest = new AdRequest.Builder().build();
                    InterstitialAd.load(context,Interstitialid, adRequest,
                            new InterstitialAdLoadCallback() {
                                @Override
                                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                    // The mInterstitialAd reference will be null until
                                    // an ad is loaded.
                                    mInterstitialAd = interstitialAd;
                                    Log.i(TAG, "onAdLoaded");
                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    // Handle the error
                                    Log.d(TAG, loadAdError.toString());
                                    mInterstitialAd = null;
                                }
                            });

                } else {
                    Log.d(TAG, "Ids are not found" );
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    public void showInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show((Activity) context);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }
}
