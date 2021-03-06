package com.yourcompany;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.tapit.adview.*;
import com.tapit.adview.AdView;
import com.tapit.adview.AdViewCore.OnAdDownload;
import com.tapit.adview.AdViewCore.OnInterstitialAdDownload;
import com.tapit.adview.track.InstallTracker;

import java.util.HashMap;
import java.util.Map;

public class TapItTestActivity extends Activity {

    public final static String BANNER_ZONE_ID = "7979";
    public final static String VIDEO_ZONE_ID = "7981";
    public final static String MED_RECT_ZONE_ID = "7982";
    public final static String INTRS_ZONE_ID = "7983";
    public final static String ADPROMPT_ZONE_ID = "7984";

    private Button loadButton;
    private Button showButton;

    private AdInterstitialView interstitialAd;
    private AdView bannerAd;
    private AdPrompt adPrompt;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        InstallTracker tracker = InstallTracker.getInstance();
        tracker.reportInstall(this, "offer_txt");

        setupBannerAd();

        setupButtons();

        TextView txt = (TextView)findViewById(R.id.tapitText);
        txt.setText(Utils.getDeviceId(this));
    }

    private void setupBannerAd(){
        bannerAd = (AdView)findViewById(R.id.bannerAd);
        bannerAd.setBackgroundColor(0);

        // the remainder of this method is optional...

//        Map<String, String> params = new HashMap<String, String>();
//        params.put("mode", "test");
//        bannerAd.setCustomParameters(params);

        // if you're interested in being updated with banner ad lifecycle events, register a listener
        bannerAd.setOnAdDownload(new OnAdDownload() {
            @Override
            public void begin(AdViewCore adView){
                // Called just before an ad request is made
                Log.d("TapItTest", "Requesting banner ad");
                Toast.makeText(getApplicationContext(), "Requesting banner ad", Toast.LENGTH_SHORT).show();
                bannerAd.setBackgroundColor(0);
            }

            @Override
            public void end(AdViewCore adView){
                // Called after an ad is successfully loaded... show ad
                Log.d("TapItTest", "Banner ad successfully loaded");
                Toast.makeText(getApplicationContext(), "Banner ad successfully loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(AdViewCore adView, String error){
                // Called when bannerAd fails to load an ad... hide ad
                Toast.makeText(getApplicationContext(), "Failed to load banner: " + error, Toast.LENGTH_LONG).show();
                Log.d("TapItTest", "Banner ad failed to load: " + error);
            }

            @Override
            public void clicked(AdViewCore adView){
                Log.d("TapItTest", "Ad clicked");
                Toast.makeText(getApplicationContext(), "Ad clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void willPresentFullScreen(AdViewCore adView){
                Log.d("TapItTest", "willPresentFullScreen");
                Toast.makeText(getApplicationContext(), "willPresentFullScreen", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void didPresentFullScreen(AdViewCore adView){
                Log.d("TapItTest", "didPresentFullScreen");
                Toast.makeText(getApplicationContext(), "didPresentFullScreen", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void willDismissFullScreen(AdViewCore adView){
                Log.d("TapItTest", "willDismissFullScreen");
                Toast.makeText(getApplicationContext(), "willDismissFullScreen", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void willLeaveApplication(AdViewCore adView){
                Log.d("TapItTest", "Leaving Application!");
                Toast.makeText(getApplicationContext(), "Leaving Application!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * AdPrompt init code with anonymous listener
     */
    public void initAdPrompt() {
        adPrompt = new AdPrompt(this, ADPROMPT_ZONE_ID);

        // the remainder of this method is optional...

        // send optional params to the AdPrompt
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("mode", "test");
//        adPrompt.setCustomParameters(params);

        // if you're interested in being updated with AdPrompt lifecycle events, register a listener
        adPrompt.setListener(new AdPrompt.AdPromptCallbackListener() {

            @Override
            public void adPromptError(AdPrompt adPrompt, String error){
                Log.d("TapItTest", "AdPrompt failed to load: " + error);
                Toast.makeText(getApplicationContext(), "AdPrompt failed to load: " + error, Toast.LENGTH_LONG).show();
                TapItTestActivity.this.adPrompt = null;
            }

            @Override
            public void adPromptLoaded(AdPrompt adPrompt){
                Log.d("TapItTest", "AdPrompt loaded");
                Toast.makeText(getApplicationContext(), "AdPrompt loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void adPromptDisplayed(AdPrompt adPrompt){
                Log.d("TapItTest", "AdPrompt has been shown");
            }

            @Override
            public void adPromptClosed(AdPrompt adPrompt, boolean didAccept){
                Log.d("TapItTest", "AdPrompt was closed using the " + (didAccept ? "CallToAction" : "Decline") + " button");
                TapItTestActivity.this.adPrompt = null;
            }
        });
    }

    /**
     * Pre-load the AdPrompt... We'll show it later
     */
    public void preloadAdPrompt() {
        Log.d("TapItTest", "Loading AdPrompt");
        initAdPrompt();
        adPrompt.load();
    }

    /**
     * Show the AdPrompt.  If it hasn't been pre-loaded, init before showing
     */
    public void fireAdPrompt() {
        Log.d("TapItTest", "showing AdPrompt");
        if(adPrompt == null) {
            // AdPrompt wasn't pre-loaded.  We'll instantiate and show at the same time...
            initAdPrompt();
        }
        adPrompt.showAdPrompt();
    }


    /**
     * Load up an interstitial with listener example
     */
    public void preloadInterstitial() {
        interstitialAd = new AdInterstitialView(this, INTRS_ZONE_ID);

        // Optionally specify custom params
        // un-comment to enable test mode
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("mode", "test");
//        interstitialAd.setCustomParameters(params);

        // Optionally register a listener to get ad lifecycle notifications.
        interstitialAd.setOnInterstitialAdDownload(new OnInterstitialAdDownload() {
            @Override
            public void willLoad(AdViewCore adView){
                // interstitial is about to load
                Log.d("TapItTest", "WillLoad");
                Toast.makeText(getApplicationContext(), "WillLoad", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void ready(AdViewCore adView){
                // interstitial is loaded and ready for display
                Log.d("TapItTest", "ready!");
                showButton.setEnabled(true);
                Toast.makeText(getApplicationContext(), "ready!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void willOpen(AdViewCore adView){
                // interstitial is about to cover the screen. minimize your app footprint
                Log.d("TapItTest", "WillOpen");
                Toast.makeText(getApplicationContext(), "WillOpen", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void didClose(AdViewCore adView){
                // interstitial is no longer covering the screen
                Log.d("TapItTest", "didClose");
                Toast.makeText(getApplicationContext(), "didClose", Toast.LENGTH_SHORT).show();
                destroyInterstitial();

                loadButton.setEnabled(true);
                showButton.setEnabled(false);
            }

            @Override
            public void error(AdViewCore adView, String error){
                Log.d("TapItTest", "Failed to load interstitial: " + error);
                Toast.makeText(getApplicationContext(), "Failed to load interstitial: " + error, Toast.LENGTH_LONG).show();

                showButton.setEnabled(false);
                loadButton.setEnabled(true);
            }

            @Override
            public void clicked(AdViewCore adView){
                Log.d("TapItTest", "Ad clicked");
                Toast.makeText(getApplicationContext(), "Ad clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void willLeaveApplication(AdViewCore adView){
                Log.d("TapItTest", "Leaving Application!");
                Toast.makeText(getApplicationContext(), "Leaving Application!", Toast.LENGTH_SHORT).show();
            }
        });

        // fire off the ad request.
        interstitialAd.load();
    }


    /**
     * Wire up the buttons...
     */
    public void setupButtons() {
        loadButton = (Button)findViewById(R.id.loadInterstitialButton);
        loadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View button){
                preloadInterstitial();
                loadButton.setEnabled(false);
            }
        });
        showButton = (Button)findViewById(R.id.showInterstitialButton);
        showButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View button) {
                interstitialAd.showInterstitial();
            }
        });
        showButton.setEnabled(false);

        final TapItTestActivity me = this;
        findViewById(R.id.loadAdPromptButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                me.preloadAdPrompt();
            }
        });

        findViewById(R.id.showAdPromptButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                me.fireAdPrompt();
            }
        });

//        // Uncomment to enable AdMob mediation example,
//        // as well as the AdMobActivity activity block in AndroidManifest.xml
//        // Download the AdMob SDK from https://developers.google.com/mobile-ads-sdk/download
//
//        Button adMobButton = (Button)findViewById(R.id.loadAdMobButton);
//        adMobButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TapItTestActivity.this, AdMobActivity.class);
//                startActivity(intent);
//            }
//        });
//        adMobButton.setEnabled(true);
    }


    private void destroyInterstitial() {
        if(interstitialAd != null) {
            interstitialAd.destroy();
            interstitialAd = null;
        }
    }

    @Override
    protected void onDestroy() {
        if(bannerAd != null) {
            // Shutdown ad internals
            bannerAd.destroy();
        }
        destroyInterstitial();
        super.onDestroy();
    }
}