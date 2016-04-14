package com.icluradio.icluradio;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton playButton;
    private ImageButton stopButton;
    private ImageButton pauseButton;
    private ImageButton stepForwardButton;

    MediaPlayer mediaPlayer;
    final String RADIO_URL = "http://icluradio.callutheran.edu:8000/listen2"; // iCLU Radio URL
    boolean isPaused = false;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO UI Controls
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
        progressDialog.show();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(RADIO_URL);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void play() {
        playButton.setEnabled(false);
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
        stepForwardButton.setEnabled(true);

        if(isPaused) {
            mediaPlayer.start();
            isPaused = false;
        }
        else {
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
        pauseButton.setEnabled(false);
        stepForwardButton.setEnabled(true);
    }
    private void stop() {
        mediaPlayer.stop();
        mediaPlayer.release();
        initializePlayer();

        playButton.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        stepForwardButton.setEnabled(false);
    }

    private void stepForward() {
        isPaused = false;
        mediaPlayer.stop();
        initializePlayer();
        play();

        stepForwardButton.setEnabled(false);
        pauseButton.setEnabled(true);
    }
}
