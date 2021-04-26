package edu.csueb.codepath.fitness_tracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class workout_timer extends FragmentActivity {
    private String TAG = "workout_timer";
    private Chronometer chronometer;
    private boolean running;
    private boolean started;
    private Button start_stop;
    private Button reset;
    private Button finishWorkout;
    private long pauseOffset;
    private int currentTime;    //will be used to store the current time on the chronometer
    public List<String> workouts;
    private TextView workoutTitle;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_timer);

        chronometer = (Chronometer) findViewById(R.id.timer);

        start_stop = (Button) findViewById(R.id.btnStart_stop);
        reset = (Button) findViewById(R.id.btnReset);
        finishWorkout = (Button) findViewById(R.id.btnFinish);
        workoutTitle = (TextView) findViewById(R.id.tvWorkoutList);
        running = false;
        started = false;

        workouts = (List<String>) getIntent().getSerializableExtra("Workout");
        Log.e(TAG, Arrays.toString(workouts.toArray()));
        String workouttitle = Arrays.toString(workouts.toArray());
        workoutTitle.setText(workouttitle);

        start_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStopTimer(v);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer(v);
            }
        });

        finishWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishWorkout(v);
            }
        });

    }

    public void startStopTimer(View view){
        if(!running && !started){   //If workout has just started
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            running = true;
            start_stop.setText("Stop");
            start_stop.setBackgroundTintList(getResources().getColorStateList(R.color.pastelred));
            started = true;
        }else if(!running && started) { //if workout is getting unpaused
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
            start_stop.setText("Stop");
            start_stop.setBackgroundTintList(getResources().getColorStateList(R.color.pastelred));
        }else if(running){  //stops the timer
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            long elapsedMili = SystemClock.elapsedRealtime()
                    - chronometer.getBase();
            currentTime = Math.round(elapsedMili/1000);
            start_stop.setText("start");
            start_stop.setBackgroundTintList(getResources().getColorStateList(R.color.pastelgreen));
        }
    }


    public void resetTimer(View view){
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        //Toast.makeText(this, "Finished", Toast.LENGTH_SHORT).show();
    }

    public void finishWorkout(View view){
        //https://www.semicolonworld.com/question/46673/android-get-time-of-chronometer-widget

        //int seconds = Math.round(time/1000);
        int seconds;
        if(!running){
            seconds = currentTime;
        }else{
            //long time = SystemClock.elapsedRealtime() - pauseOffset;
            //Log.e("workout_timer", String.valueOf(time));
            long elapsedMillis = SystemClock.elapsedRealtime()
                    - chronometer.getBase();
            seconds = Math.round(elapsedMillis/1000);
            Log.e("workout_timer", String.valueOf(seconds));

        }

        Intent i = new Intent(this, workout_summary.class);
        i.putExtra("Workout2", (Serializable) workouts);
        i.putExtra("finalTime", seconds);
        startActivity(i);
        //Toast.makeText(this, "Elapsed seconds: " + seconds, Toast.LENGTH_SHORT).show();


    }
}
