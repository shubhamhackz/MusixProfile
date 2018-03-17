package com.hackz.userprofile;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cleveroad.audiovisualization.AudioVisualization;
import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.GLAudioVisualizationView;
import com.cleveroad.audiovisualization.SpeechRecognizerDbmHandler;
import com.cleveroad.audiovisualization.VisualizerDbmHandler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private AudioVisualization audioVisualization;
    private GLAudioVisualizationView mGLAudioVisualizationView;


    private TextView mTextViewFollower;
    private TextView mTextViewFollowing;

    private static final int PERMISSION= 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            int hasAudioPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            int hasInternetPermission = checkSelfPermission(Manifest.permission.INTERNET);

            List<String> permissions = new ArrayList<>();


            if (hasAudioPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }

            if (hasInternetPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.INTERNET);
            }

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]),PERMISSION);
            }
        }

        setContentView(R.layout.activity_main);


        audioVisualization= findViewById(R.id.visualizer_view);

        //setting up background visualization
        mGLAudioVisualizationView= findViewById(R.id.visualizer_view);
        audioVisualization= (AudioVisualization) mGLAudioVisualizationView;

        // set speech recognizer handler
        SpeechRecognizerDbmHandler speechRecHandler = DbmHandler.Factory.newSpeechRecognizerHandler(this);
        speechRecHandler.innerRecognitionListener(speechRecHandler);
        audioVisualization.linkTo(speechRecHandler);

        // set audio visualization handler. This will REPLACE previously set speech recognizer handler
        VisualizerDbmHandler vizualizerHandler = DbmHandler.Factory.newVisualizerHandler(this, 0);
        audioVisualization.linkTo(vizualizerHandler);

        mTextViewFollower= findViewById(R.id.no_of_followers);
        mTextViewFollowing= findViewById(R.id.no_of_following);

        startFollowerAnimation();
        startFollowingAnimation();

    }

    @Override
    protected void onResume() {
        super.onResume();
        audioVisualization.onResume();
    }

    @Override
    protected void onPause() {
        audioVisualization.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        audioVisualization.release();
        super.onDestroy();
    }

    private void startFollowerAnimation() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 5677);
        animator.setDuration(4000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mTextViewFollower.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.start();
    }

    private void startFollowingAnimation() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 677);
        animator.setDuration(4000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mTextViewFollowing.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.start();
    }

}
