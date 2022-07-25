package com.test.SimplePFC;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Random;

public class AdInterstitial {

    private static InterstitialAd mInterstitialAd = null;
    private static final String adUnitId = "ca-app-pub-3940256099942544/1033173712";// adUnitIdをセットする
    private static final int interval = 1;//広告の表示頻度を設定する

    //広告ロード
    public static void loadAd(Context context, AdRequest adRequest){
        InterstitialAd.load(context,adUnitId, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("TAG", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("TAG", loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }
    //広告表示
    public static void show(Activity activity){
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d("TAG", "Ad was clicked.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d("TAG", "Ad dismissed fullscreen content.");
                mInterstitialAd = null;
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e("TAG", "Ad failed to show fullscreen content.");
                mInterstitialAd = null;
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d("TAG", "Ad recorded an impression.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d("TAG", "Ad showed fullscreen content.");
            }
        });

        Random r = new Random();
        int randInt = r.nextInt(interval);


        if(mInterstitialAd != null && randInt == 0 ){
            mInterstitialAd.show(activity);
        }

    }

}
