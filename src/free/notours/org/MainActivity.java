/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package free.notours.org;

import android.app.ListActivity;
import android.content.Intent;

import android.support.v4.app.FragmentActivity;
import android.view.Menu;

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

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import freeHttp.notours.org.MenuContinent;
import free.notours.org.R;

/**
 * The main activity of the API library demo gallery.
 * <p>
 * The main layout lists the demonstrated features, with buttons to launch them.
 */
public final class MainActivity extends Activity {

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
        
		setContentView(R.layout.splash);
        
		//brightness
        // Make the screen full bright for this activity.
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = 0.5f;
		
        //Play Intro Sound
        //mp_intro.reset();
		//String my_path1="/sdcard/notours/system_sounds/welcome_to_notours.mp3";
        
     // Setting latitude and longitude in the TextView tv_location
        
        //noTours version
        /*
     		TextView tvLocation = (TextView) findViewById(R.id.noTours_version);	
           	tvLocation.setText("v2.0.3.Http");
           	tvLocation.setTypeface(null, Typeface.ITALIC);
           	tvLocation.bringToFront();
           	//tvLocation.setHeight(40);
           	tvLocation.setTextColor(Color.LTGRAY);
           	tvLocation.setTextSize(10);
        */
		
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
        
	        //check if noTours directory exists, if not create it!
	        File testDirectory = new File(Environment.getExternalStorageDirectory() + "/notours");
	        if(!testDirectory.exists()){
	        	File nfile=new File(Environment.getExternalStorageDirectory()+"/notours");
	        	nfile.mkdir();	        	
	        }
	        File testDirectory22 = new File("/storage/emulated/0/notours/");
	        if(!testDirectory22.exists()){
	        	File nfile2=new File("/storage/emulated/0/notours/");
	        	nfile2.mkdir();	        	
	        }
	        
	        //copy files from assests if necessary for having a demo!!
	        File testDirectory2 = new File(Environment.getExternalStorageDirectory() + "/notours/noToursDemo");
	        if(!testDirectory2.exists()){
	        	File nfile2=new File(Environment.getExternalStorageDirectory()+"/notours/noToursDemo");
	        	nfile2.mkdir();
	        	File nfile3=new File(Environment.getExternalStorageDirectory()+"/notours/noToursDemo/sound");
	        	nfile3.mkdir();
	        	copyDemoProject();
	        }
	        File testDirectory23 = new File("/storage/emulated/0/notours/noToursDemo");
	        if(!testDirectory23.exists()){
	        	File nfile2=new File("/storage/emulated/0/notours/noToursDemo");
	        	nfile2.mkdir();
	        	File nfile3=new File("/storage/emulated/0/notours/noToursDemo/sound");
	        	nfile3.mkdir();
	        	copyDemoProject2();
	        }
	        
	        //Re-scan directory for making it available to the users
	        //File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
	        File path11 = new File("/storage/emulated/0/notours/");
	        path11.mkdirs();

	        // fix
	        path11.setExecutable(true);
	        path11.setReadable(true);
	        path11.setWritable(true);

	        // initiate media scan and put the new things into the path array to
	        // make the scanner aware of the location and the files you want to see
	        MediaScannerConnection.scanFile(this, new String[] {path11.toString()}, null, null);
	        
	        
	        
	        //For a specific the project
	      //check if noTours directory exists, if not create it!
	        /*
	        File testDirectory3 = new File(Environment.getExternalStorageDirectory() + "/notours/carcassonne");
	        if(!testDirectory3.exists()){
	        	File nfile4=new File(Environment.getExternalStorageDirectory()+"/notours/carcassonne");
	        	nfile4.mkdir();
	        	File nfile5=new File(Environment.getExternalStorageDirectory()+"/notours/carcassonne/sound");
	        	nfile5.mkdir();
	        	File nfile6=new File(Environment.getExternalStorageDirectory()+"/notours/carcassonne/track");
	        	nfile6.mkdir();
	        	File nfile7=new File(Environment.getExternalStorageDirectory()+"/notours/carcassonne/image");
	        	nfile7.mkdir();
	        }  
	        */


        
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
                    //MenuContinent.c
                                  
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
    
    
    private void copyDemoProject() {
        AssetManager assetManager = getResources().getAssets();
        
        //now we copy soundscape.rss file
        String[] files2 = null;
        try {
            files2 = assetManager.list("rssDemoProject");
        } catch (Exception e) {
            Log.e("creation of RSS of Demo Project - ERROR", e.toString());
            e.printStackTrace();
        }
        for(int i=0; i<files2.length; i++) {
            InputStream in = null;
            OutputStream out = null;
            try {
              in = assetManager.open("rssDemoProject/" + files2[i]);
              out = new FileOutputStream(Environment.getExternalStorageDirectory()+"/notours/noToursDemo/" + files2[i]);
              copyFile(in, out);
              in.close();
              in = null;
              out.flush();
              out.close();
              out = null;
            } catch(Exception e) {
                Log.e("copy RSS of Demo - ERROR", e.toString());
                e.printStackTrace();
            }       
        }
              
        //copy sounds
        String[] files = null;
        try {
            files = assetManager.list("soundsDemoProject");
        } catch (Exception e) {
            Log.e("creation of Sounds of Demo Project - ERROR", e.toString());
            e.printStackTrace();
        }
        for(int i=0; i<files.length; i++) {
            InputStream in = null;
            OutputStream out = null;
            try {
              in = assetManager.open("soundsDemoProject/" + files[i]);
              out = new FileOutputStream(Environment.getExternalStorageDirectory()+"/notours/noToursDemo/sound/" + files[i]);
              copyFile(in, out);
              in.close();
              in = null;
              out.flush();
              out.close();
              out = null;
            } catch(Exception e) {
                Log.e("copy sounds of Demo - ERROR", e.toString());
                e.printStackTrace();
            }       
        }
        
       
        //end of for
    }
    
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
          out.write(buffer, 0, read);
        }
    }
    
    private void copyDemoProject2() {
        AssetManager assetManager = getResources().getAssets();
        
        //now we copy soundscape.rss file
        String[] files2 = null;
        try {
            files2 = assetManager.list("rssDemoProject");
        } catch (Exception e) {
            Log.e("creation of RSS of Demo Project - ERROR", e.toString());
            e.printStackTrace();
        }
        for(int i=0; i<files2.length; i++) {
            InputStream in = null;
            OutputStream out = null;
            try {
              in = assetManager.open("rssDemoProject/" + files2[i]);
              out = new FileOutputStream("/storage/emulated/0/notours/noToursDemo/" + files2[i]);
              copyFile(in, out);
              in.close();
              in = null;
              out.flush();
              out.close();
              out = null;
            } catch(Exception e) {
                Log.e("copy RSS of Demo - ERROR", e.toString());
                e.printStackTrace();
            }       
        }
              
        //copy sounds
        String[] files = null;
        try {
            files = assetManager.list("soundsDemoProject");
        } catch (Exception e) {
            Log.e("creation of Sounds of Demo Project - ERROR", e.toString());
            e.printStackTrace();
        }
        for(int i=0; i<files.length; i++) {
            InputStream in = null;
            OutputStream out = null;
            try {
              in = assetManager.open("soundsDemoProject/" + files[i]);
              out = new FileOutputStream("/storage/emulated/0/notours/noToursDemo/sound/" + files[i]);
              copyFile(in, out);
              in.close();
              in = null;
              out.flush();
              out.close();
              out = null;
            } catch(Exception e) {
                Log.e("copy sounds of Demo - ERROR", e.toString());
                e.printStackTrace();
            }       
        }
        
       
        //end of for
    }
    

    
}