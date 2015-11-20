package free.notours.org;


import android.app.ListActivity;
import android.content.Intent;

import android.support.v4.app.FragmentActivity;

import android.view.View;

import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Locale;

import free.notours.org.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public final class Menu extends Activity {

    protected boolean _active = true;
    protected int _splashTime = 5000;
    
    MediaPlayer mp_intro;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      //window properties, called before layout is loaded
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		setContentView(R.layout.menu);
        
		//brightness
        // Make the screen full bright for this activity.
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = 0.5f;
		
        //Play Intro Sound
        //mp_intro.reset();
		//String my_path1="/sdcard/notours/system_sounds/welcome_to_notours.mp3";
        
     // Setting latitude and longitude in the TextView tv_location
        
     		TextView tvLocation = (TextView) findViewById(R.id.noTours_version);	
           	tvLocation.setText("v2.0.3.Http");
           	tvLocation.setTypeface(null, Typeface.ITALIC);
           	tvLocation.bringToFront();
           	//tvLocation.setHeight(40);
           	tvLocation.setTextColor(Color.LTGRAY);
           	tvLocation.setTextSize(10);
        
		
		mp_intro=MediaPlayer.create(this, R.raw.welcome);
		//mp_intro.setDataSource(my_path1);
		
		
		//mp_intro.setVolume(1.0f, 1.0f);
		
		//if(mp_intro.isPlaying()==false) {
			//Log.v("home sound","entrooo");
			mp_intro.start();
			//Log.v("home sound","start");
			 // i.e. react on the end of the music-file:
			mp_intro.setOnCompletionListener(new OnCompletionListener(){

                 // @Override
                 public void onCompletion(MediaPlayer arg0) {
                      // File has ended !!! Wink
                	 //Log.v("Intro Sounde"," he terminaaaaaaaaaaaaaaado");
                	 if(mp_intro.isPlaying()){
                		 mp_intro.stop();
                 	}
                	 mp_intro.reset();
                	 mp_intro.release();
                 }
            }); 
		//}
        //End of Play Intro Sound
        


        
        // thread for displaying the SplashScreen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while(_active && (waited < _splashTime)) {
                        sleep(100);
                        if(_active) {
                            waited += 100;
                        }
                    }
                } catch(InterruptedException e) {
                    // do nothing
                } finally {
                    finish();
                    //go to the menu
                    startActivity(new Intent("com.example.mapdemo.escoitar.Projects"));//"com.noTours.escoitar.noTours.Menu"));
                    //go to the projects menu
                    //startActivity(new Intent("com.noTours.escoitar.noTours.Projects"));
                                     
                    //stop();
                }
            }
        };
        splashTread.start();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //if (event.getAction() == MotionEvent.ACTION_DOWN) {
        //    _active = false;
        //}
        return true;
    }
    
    

	
}
