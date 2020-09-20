package com.speca.application1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class FullscreenActivity extends AppCompatActivity {

    private SimpleExoPlayer player;
    private PlayerView playerView;
    TextView textView;
    boolean fullscreen =false;
    ImageView fullscreenButton;
    private String url,title,desc;
    private boolean playwhenready = false;
    private int currentWindow = 0;
    private long playbackposition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

      ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        playerView = findViewById(R.id.exoplayer_fullscreen);
        textView = findViewById(R.id.tv_fullscreen);
       // textView = findViewById(R.id.desc);

        fullscreenButton = playerView.findViewById(R.id.exoplayer_fullscreen_icon);

        Intent intent = getIntent();
        url = intent.getExtras().getString("ur");
        title = intent.getExtras().getString("nam");
      //  desc =intent.getExtras().getString("des");

        textView.setText(title);
   //     textView.setText(desc);

        fullscreenButton.setOnClickListener(new View.OnClickListener(){

        @SuppressLint("SourceLockedOrientationActivity")
        @Override
        public void onClick(View view) {

            if(fullscreen){
                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(FullscreenActivity.this,R.drawable.noun_maximise_3389464));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            if(getSupportActionBar() != null){
                getSupportActionBar().show();

            }

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                LinearLayout.LayoutParams params =(LinearLayout.LayoutParams)playerView.getLayoutParams();
            params.width =params.MATCH_PARENT;
            params.height =(int)(200 * getApplicationContext().getResources().getDisplayMetrics().density);
            playerView.setLayoutParams(params);
            fullscreen =false;

            }else{
                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(FullscreenActivity.this,R.drawable.noun_exit_fullscreen));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            if(getSupportActionBar() != null){
                getSupportActionBar().hide();
            }

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            LinearLayout.LayoutParams params =(LinearLayout.LayoutParams)playerView.getLayoutParams();
            params.width =params.MATCH_PARENT;
            params.height= params.MATCH_PARENT;
            playerView.setLayoutParams(params);
            fullscreen=true;
            }
        }
    });
    }

    private MediaSource buildMediaSource(Uri uri) {

        DataSource.Factory datasourcefactory = new DefaultHttpDataSourceFactory("videos");
        return new ProgressiveMediaSource.Factory(datasourcefactory)
                .createMediaSource(uri);

    }

    private void initalizerplayer() {

        player = ExoPlayerFactory.newSimpleInstance(this);
        playerView.setPlayer(player);
        Uri uri = Uri.parse(url);
        MediaSource mediaSource = buildMediaSource(uri);
        player.setPlayWhenReady(playwhenready);
        player.seekTo(currentWindow, playbackposition);
        player.prepare(mediaSource, false, false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Util.SDK_INT >= 30) {
            initalizerplayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Util.SDK_INT >= 30 || player == null) {
            //initalizerplayer();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Util.SDK_INT > 30) {
            releaseplayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (Util.SDK_INT >= 30) {
            releaseplayer();
        }
    }

    private void releaseplayer() {
        if (player != null) {
            playwhenready = player.getPlayWhenReady();
            playbackposition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player = null;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    player.stop();
    releaseplayer();

    final Intent intent = new Intent();
    setResult(RESULT_OK,intent);
    finish();

    }
}