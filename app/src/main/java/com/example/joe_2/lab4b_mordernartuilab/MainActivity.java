package com.example.joe_2.lab4b_mordernartuilab;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int NUM_OF_VIEWS = 5;
    private static final int SEEK_BAR_MAX = 150;
    private final ArrayList<View> mViews = new ArrayList<View>();
    private final ArrayList<Integer> mViewOriginalColors = new ArrayList<Integer>();
    private SeekBar mSeekBar;
//    private Dialog mDialog;
//    private static Menu mMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init member values
        mViews.add(findViewById(R.id.view1));
        mViews.add(findViewById(R.id.view2));
        mViews.add(findViewById(R.id.view3));
        mViews.add(findViewById(R.id.view4));
        mViews.add(findViewById(R.id.view5));
        for (View view : mViews) {
            mViewOriginalColors.add(((ColorDrawable)view.getBackground()).getColor());
        }
        mSeekBar = (SeekBar)findViewById(R.id.seekBar);
        mSeekBar.setMax(SEEK_BAR_MAX);

        if (savedInstanceState != null) {
            ArrayList<Integer> currentColors =
                    savedInstanceState.getIntegerArrayList("viewCurrentColors");
            for (int i = 0; i < NUM_OF_VIEWS; i++) {
                mViews.get(i).setBackgroundColor(currentColors.get(i));
            }
            mSeekBar.setProgress(savedInstanceState.getInt("seekBarProgress"));
//            if (savedInstanceState.getBoolean("dialogOn")) {
//                //mDialog.show();
//                mMenu.
//                onOptionsItemSelected(mMenu.findItem(R.id.more_info));
//            }


        }

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (progress >= 0 && progress <= seekBar.getMax()) {
                        setViewColor(progress);
                        seekBar.setSecondaryProgress(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // store all current color into currentColors
        ArrayList<Integer> currentColors = new ArrayList<Integer>();
        for (View view : mViews) {
            currentColors.add(((ColorDrawable)view.getBackground()).getColor());

        }
        savedInstanceState.putIntegerArrayList("viewCurrentColors", currentColors);
        // store seekBar's progress value
        savedInstanceState.putInt("seekBarProgress", mSeekBar.getProgress());

//        if (mDialog.isShowing()) {
//            savedInstanceState.putBoolean("dialogOn", true);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        //mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.more_info:
                final Dialog mDialog = new Dialog(this);
                mDialog.setContentView(R.layout.dialog);
                Button visitBtn = (Button) mDialog.findViewById(R.id.visitButton);
                visitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String url = "https://www.moma.org";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        mDialog.dismiss();
                    }
                });
                Button cancelBtn = (Button)mDialog.findViewById(R.id.cancelButton);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
                mDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    // set the color of all non-white and non-grey views
    // according to progress value
    private void setViewColor(int progress) {
        for (int i=0; i<NUM_OF_VIEWS; i++) {
            int color = mViewOriginalColors.get(i);
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            // white, grey or black
            if (red == green && green == blue) {
                // black color
                if (red == 0)
                    break;
                continue;
            }
            int newColor = Color.rgb(Math.min(255, Color.red(color) + progress),
                    Math.min(255, Color.green(color) + progress), Color.blue(color));
            mViews.get(i).setBackgroundColor(newColor);
        }
    }
}
