package com.icluradio.icluradio;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.devbrackets.android.exomedia.EMAudioPlayer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton playButton;
    private ImageButton stopButton;
    private ImageButton pauseButton;
    private ImageButton stepForwardButton;

    EMAudioPlayer mediaPlayer;
    final String RADIO_URL = "http://icluradio.callutheran.edu:8000/listen2"; // iCLU Radio URL
    boolean isPaused = false;
    boolean isStopped = false;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton        = (ImageButton) findViewById(R.id.playButton);
        playButton.setOnClickListener(this);

        stopButton        = (ImageButton) findViewById(R.id.stopButton);
        stopButton.setEnabled(false);
        stopButton.setOnClickListener(this);

        pauseButton       = (ImageButton) findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(this);

        stepForwardButton = (ImageButton) findViewById(R.id.stepForwardButton);
        stepForwardButton.setOnClickListener(this);

        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Buffering...");
        progressDialog.setCancelable(false);
        initializePlayer();
        play();
    }

    @Override
    public void onClick(View v) {
        if(v == playButton) {
            play();
        }
        else if(v == pauseButton) {
            pause();
        }
        else if(v == stopButton) {
            stop();
        }
        else if(v == stepForwardButton) {
            stepForward();
        }
    }

    private void initializePlayer() {
        mediaPlayer = new EMAudioPlayer(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(this, Uri.parse(RADIO_URL));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void play() {
        playButton.setEnabled(false);
        playButton.setImageResource(R.drawable.play_icon_purple);
        pauseButton.setEnabled(true);
        pauseButton.setImageResource(R.drawable.pause_icon);
        stopButton.setEnabled(true);
        stopButton.setImageResource(R.drawable.stop_icon);
        stepForwardButton.setEnabled(true);
        stepForwardButton.setImageResource(R.drawable.stepforward_icon);
        if(isStopped) {
            isStopped = false;
            initializePlayer();
        }
        if(isPaused) {
            mediaPlayer.start();
            isPaused = false;
        }
        else {
            progressDialog.show();
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    progressDialog.dismiss();
                    mediaPlayer.start();
                }
            });// might take long! (for buffering, etc)
        }
    }

    private void pause() {
        mediaPlayer.pause();
        isPaused = true;
        playButton.setEnabled(true);
        playButton.setImageResource(R.drawable.play_icon);
        pauseButton.setEnabled(false);
        pauseButton.setImageResource(R.drawable.pause_icon_purple);
        stepForwardButton.setEnabled(true);
        stepForwardButton.setImageResource(R.drawable.stepforward_icon);
    }
    private void stop() {
        mediaPlayer.stopPlayback();
        mediaPlayer.release();
        isStopped = true;

        playButton.setEnabled(true);
        playButton.setImageResource(R.drawable.play_icon);
        pauseButton.setEnabled(false);
        pauseButton.setImageResource(R.drawable.pause_icon_gray);
        stopButton.setEnabled(false);
        stopButton.setImageResource(R.drawable.stop_icon_purple);
        stepForwardButton.setEnabled(false);
        stepForwardButton.setImageResource(R.drawable.stepforward_icon_gray);
    }

    private void stepForward() {
        stepForwardButton.setImageResource(R.drawable.stepforward_icon_purple);
        isPaused = false;
        mediaPlayer.stopPlayback();
        initializePlayer();
        play();

        stepForwardButton.setEnabled(false);
        pauseButton.setEnabled(true);
    }
}
