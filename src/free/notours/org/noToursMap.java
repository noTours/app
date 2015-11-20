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



import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.Process;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import free.notours.org.R;


public class noToursMap extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks,
com.google.android.gms.location.LocationListener,
GooglePlayServicesClient.OnConnectionFailedListener, SensorEventListener {
	
	//VARIABLES
	String nickname;
	String soundwalk;
	
	//Shared Preferences
	//boolean stickyValue=false;
	boolean networkedValue=false;
	
	//POWER WAKELOCK
	PowerManager.WakeLock wl;
	
	//XML PARSER VARIABLES
	SitesList sitesList = null;
    int numberOfSoundscapes=0;
    int numberOfSndCircles=0;
    SoundCircle sndCircles[];
    Soundscape soundscp[];
    String projectID;
	boolean sticky = false;
	boolean DEMO_VERSION=false;
	private final String MY_DEBUG_TAG = "KikeeeeeeeParserrrrr";
	
	//MAPS
	/*
	GoogleMap googleMap;
	LocationManager locationManager;
	Criteria criteria;
	OnLocationChangedListener myLocationListener = null;
	final int RQS_GooglePlayServices = 1;
	LatLng latLng;
	*/
	
	//CIRCLE OVERLAYS
	CircleOptions circleOptions;
	Circle circleAux;
	float zoomLevel=7;
	float bearingLevel=0;
	float tiltLevel=25;
	
	private GoogleMap myMap;            // map reference
	private LocationClient myLocationClient;
	private static final LocationRequest REQUEST = LocationRequest.create()
	    .setInterval(1)         // 5 seconds
	    .setFastestInterval(1)    // 16ms = 60fps
	    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	boolean GPSEnabled=true;
	boolean firstGPSfix=true;
	
	//AUDIO THREAD
	int MAXIMUM_NUMBER_CIRCLES = 150;
	double lat=0.0f;
	double lng=0.0f;
	boolean connected=false;
	
	//Sequencer for narratives
	Sequencer seq;
	boolean sequenceStartMoment;
	boolean changeOfLevel;
	boolean GUIchanged;
	
	//OVERLAYS	
    public static final double RADIUS_OF_EARTH_METERS = 6371009;
    //Original coordinates for calculating the shift of location (if sticky==true)
  	float latOriginal=0.0f;
  	float lngOriginal=0.0f;
    
    //COMPASS
    private SensorManager mSensorManager;
    private Sensor mSensor;
    float orientationX;
    
    //CAMERA
    
    //HTTP
	//hour and date
	int seconds, minutes, hour, day, month, year;
	Calendar c;
	String actualDate;
	String macNum;
	int trigger=0;
	
	//to control the OSChttp
	boolean networked=false;
	
	boolean firstHttp=true;
	boolean inTrigger=false;
    
	//To store a reference to the cirles of the map
	private HashMap<String, Circle> mapStuff = new HashMap<String, Circle>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//window properties, called before layout is loaded
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		//WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//set Content View
		setContentView(R.layout.notoursmap);
		
		//Process.setPriority(Thread.MAX_PRIORITY);
		Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_DISPLAY);
		
		//get shared preferences
		SharedPreferences settings = getSharedPreferences("http", MODE_PRIVATE);
		nickname = settings.getString("nickname", "noTours");
		//stickyValue = settings.getBoolean("stickyValue", false);
		networkedValue = settings.getBoolean("networkedValue", false);
		
		//get phone ID
		TelephonyManager tManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		String uid = tManager.getDeviceId();
		macNum=uid;
		
		//screen timeout disabled
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
              
       //media volume
        AudioManager manager = (AudioManager) this.getSystemService(noToursMap.AUDIO_SERVICE);  
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, 99, 0 /*flags*/);       
       
		//receive parameter of soundwalk (Project we are going to load) from previous activity
		//disabled until integration in noTours
        Bundle bundle = this.getIntent().getExtras();
        soundwalk = bundle.getString("soundwalk");
        Log.v("[noTours]: Loading soundwalk - ", soundwalk);
        
        //Create Sequencer for controlling levels and milestones
        seq=new Sequencer();
        
        
        //For Http
        //Get calendar/hour
        c = Calendar.getInstance(); 
        
        //period for sending http
        final int splashTime=1000;
       
        /*
        try {
    		MapsInitializer.initialize(this);
    		} catch (GooglePlayServicesNotAvailableException e) {
    		e.printStackTrace();
    		}
        */
        
		///////////////////////////////////////////////////////////
        ///*************** XML PARSING CODE ***********************
        ///////////////////////////////////////////////////////////
              
        try {
                   
                   try {
           			
           			/** Handling XML */
           			SAXParserFactory spf2 = SAXParserFactory.newInstance();
           			SAXParser sp2 = spf2.newSAXParser();
           			XMLReader xr2 = sp2.getXMLReader();

           			/** Example of using URL to parse XML Tags (not used) */
           			/*
           			URL sourceUrl = new URL(
           					"http://www.ultranoise.es/extra/example7_new.xml");
           			*/
           			
           			//read RSS file from SDcard or external memory
           			FileInputStream in;
           			
           			in= new FileInputStream("/storage/emulated/0/notours/" + soundwalk + "/soundscape.rss");
           			//in= new FileInputStream(Environment.getExternalStorageDirectory()+"/notours/" + soundwalk + "/soundscape.rss");
           			       			
           			/** Create handler to handle XML Tags ( extends DefaultHandler ) */
           			MyXMLHandler myXMLHandler = new MyXMLHandler();
           			xr2.setContentHandler(myXMLHandler);
           			
           			//For loading from an online server
           			//xr2.parse(new InputSource(sourceUrl.openStream()));
           			
           			//For loading from SDcard or external memory
           			xr2.parse(new InputSource(in));
           			
           		} catch (Exception e) {
           			System.out.println("XML Pasing Exception = " + e);
           		}

           		/** Get result from MyXMLHandler SitlesList Object */
           		sitesList = MyXMLHandler.sitesList;
           		/* Here Parsing has finished. All is about extracting */
           		
           		
           		////////////////////////////////////////////////////////
           		//EXTRACTION OF THE INFORMATION AND LOADING OF THE VALUES
           		////////////////////////////////////////////////////////
           		numberOfSndCircles=sitesList.getPointCoordinates().size();
           		numberOfSoundscapes=sitesList.getScpCoordinates().size();
           		Log.v("number of circles", String.valueOf(numberOfSndCircles));
          		                
          		//DEFINITION OF CIRCLES AND SOUNDSCAPES CONTAINER SIZES
          		sndCircles=new SoundCircle[sitesList.getPointCoordinates().size()];  //maximum capacity of Circles                
                soundscp=new Soundscape[sitesList.getScpCoordinates().size()];           
                
                //Get project ID for Http
                String tempLink=sitesList.getLink().get(0);                 
                String aux1=tempLink.substring(39);
                String[] aux = aux1.split("/");
                projectID=aux[0];
                  
          		//get stickyness (if the project can move its origin to user location)
                Log.v("noTourssssssssssss sTRIIIIIICY cooore",sitesList.getSticky().get(0));
                if(sitesList.getSticky().get(0).equalsIgnoreCase("0")){
                  sticky=false;
                  //Log.v("noTourssssssssssss sTRIIIIIICY cooore","FALSE");
                }else {
                  sticky=true;
                  //Log.v("noTourssssssssssss sTRIIIIIICY cooore","TRUE");
                }
                  
                //but we kill the RSS preference if the user has chosen sticky in the GUI
                /*
                if(stickyValue){
                  	sticky=true;
                } else {
                  sticky=false;
                }
                */
                  
                //Initializing Circles and Soundscapes
                Log.v("noTourssssssssssss siiiiiiiiiiize circles",String.valueOf(sitesList.getPointCoordinates().size()));
                for(int i=0;i<sitesList.getPointCoordinates().size();i++) {
                  	sndCircles[i]=new SoundCircle();
                  	//Log.v("sndCircle created", String.valueOf(i));
                }
                for(int i=0;i<sitesList.getScpCoordinates().size();i++) {
                  	soundscp[i]=new Soundscape();            	 
                }
                  	
                //Setting values for Circles (Soundpoints)
                String[] splittedPointCoordinates;
                  
                for(int i=0;i<sitesList.getPointCoordinates().size();i++) {
                  
                	Log.v("circle #", String.valueOf(i));
                	
                  //coordinates of the circle
                  splittedPointCoordinates=sitesList.getPointCoordinates().get(i).split(" ");
                  		
                  if(splittedPointCoordinates.length==3){ 
                  
                  		sndCircles[i].setLat(Double.parseDouble(splittedPointCoordinates[0]));
                  		Log.v("circle lat", String.valueOf(Double.parseDouble(splittedPointCoordinates[0])));
                  		sndCircles[i].setLng(Double.parseDouble(splittedPointCoordinates[1]));
                  		Log.v("circle lng", String.valueOf(Double.parseDouble(splittedPointCoordinates[1])));
                  		sndCircles[i].setRad(Float.parseFloat(splittedPointCoordinates[2]));
                  		Log.v("circle rad", String.valueOf(Double.parseDouble(splittedPointCoordinates[2])));
                  } else {
                	  	sndCircles[i].setLat(0.0);
                		Log.v("circle lat", String.valueOf(Double.parseDouble(splittedPointCoordinates[0])));
                		sndCircles[i].setLng(0.0);
                		Log.v("circle lng", String.valueOf(Double.parseDouble(splittedPointCoordinates[1])));
                		sndCircles[i].setRad(0.0f);
                		Log.v("circle rad", String.valueOf(Double.parseDouble(splittedPointCoordinates[2])));
                  }
                    	//set Level
                    	sndCircles[i].setLevel(Integer.parseInt(sitesList.getPointLevel().get(i)));
                    	//Log.w("NIVELLLLLLLLLLLLLLLLLL",sitesList.getPointLevel().get(i));
                    	
                    	
                    	//Set Milestone (in case)
                    	if(sitesList.getPointMilestone().get(i)=="0"){
                    		sndCircles[i].setMilestone(-1);
                    	} else {
                    		sndCircles[i].setMilestone(Integer.parseInt(sitesList.getPointMilestone().get(i)));
                    	}
                    	Log.w("MILESTONNNNNEEEE",sitesList.getPointMilestone().get(i));
                    	
                    	//sound file of the circle
                    	//(in former times): //sndCircles[i].setFilee(sitesList.getPointFile().get(i));
                    	if(sitesList.getTypeFile().get(i)=="file") {
                    		sndCircles[i].setFilee(sitesList.getPointFile().get(i));
                    		sndCircles[i].setIsFolder(false);
                    	} else {
                    		sndCircles[i].setFilee(sitesList.getPointFolder().get(i));
                    		sndCircles[i].setIsFolder(true);
                    	}
                    	             	
                    	//Attributes
                    	Log.v("Atrttrtr", sitesList.getPointAttributes().get(i));
                    	if(sitesList.getPointAttributes().get(i).contains("speaker")) {
                    		sndCircles[i].setSpeaker(1.0f); 
                    	}
                    	else {
                    		sndCircles[i].setSpeaker(0.0f);
                    	}
                    	
                    	if(sitesList.getPointAttributes().get(i).contains("trigger")) {
                    		sndCircles[i].setIsTrigger(1); 
                    	}
                    	else {
                    		sndCircles[i].setIsTrigger(0);
                    	}
                    	
                    	if(sitesList.getPointAttributes().get(i).contains("fadein")) {
                    		sndCircles[i].setFadeIn(true); 
                    	}
                    	else {
                    		sndCircles[i].setFadeIn(false); 
                    	}
                    	
                    	if(sitesList.getPointAttributes().get(i).contains("fadeout")) {
                    		sndCircles[i].setFadeOut(true);
                    	}
                    	else {
                    		sndCircles[i].setFadeOut(false);
                    	}
                    	
                    	if(sitesList.getPointAttributes().get(i).contains("loop")) {
                    		sndCircles[i].setLoop(1);
                    	}
                    	else {
                    		sndCircles[i].setLoop(0);
                    	}
                    	
                    	if(sitesList.getPointAttributes().get(i).contains("vibrate")) {
                    		sndCircles[i].setVibrate(true); 
                    	}
                    	else {
                    		sndCircles[i].setVibrate(false);
                    	}
                    	
                    	if(sitesList.getPointAttributes().get(i).contains("playout")) {
                    		sndCircles[i].setNonStop(true); 
                    	}
                    	else {
                    		sndCircles[i].setNonStop(false);
                    	}
                    	
                    	if(sitesList.getPointAttributes().get(i).contains("pauseout")) {
                    		sndCircles[i].setPauseOut(true); 
                    	}
                    	else {
                    		sndCircles[i].setPauseOut(false);
                    	}
                    	
                    	if(sitesList.getPointAttributes().get(i).contains("stopout")) {
                    		sndCircles[i].setPauseOut(false); 
                    	}
                    	else {
                    		sndCircles[i].setPauseOut(true);
                    	}
                    	
                    	if(sitesList.getPointAttributes().get(i).contains("moveable")) {
                    		sndCircles[i].setMoveable(true); 
                    		sndCircles[i].setTrackFile(sitesList.getTrack().get(i));
                    		sndCircles[i].parseTrackFile(soundwalk);
                    	}
                    	else {
                    		sndCircles[i].setMoveable(false);
                    	}
                    	
                    	//other possible attributes not implemented yet
                    	if(sitesList.getPointAttributes().get(i).contains("microphone")) {
                    		//do nothing in this software version
                    	}
                    	else {
                    		//do nothing in this software version
                    	}
                    	if(sitesList.getPointAttributes().get(i).contains("inverse")) {
                    		//do nothing in this software version
                    	}
                    	else {
                    		//do nothing in this software version
                    	}
                    	
                    	if(sitesList.getPointAttributes().get(i).contains("nonstop")) {
                    	//do nothing in this software version
                    	}
                    	else {
                    	//do nothing in this software version
                    	}
                    //}
                  }    	
                    	
                //Setting values for Soundscapes 
                  String[] splittedScpCoordinates;
                  String[] splittedScpAngles1;
                  String[] splittedScpAngles2;
                  String[] splittedScpAngles3;
                  String[] splittedScpAngles4;
                  
                  for(int i=0;i<sitesList.getScpCoordinates().size();i++) {
                  	
                  	//coordinates
                  	splittedScpCoordinates=sitesList.getScpCoordinates().get(i).split(" ");
                  	soundscp[i].setLat(Double.parseDouble(splittedScpCoordinates[0]));
                  	soundscp[i].setLng(Double.parseDouble(splittedScpCoordinates[1]));
                  	soundscp[i].setRad(Float.parseFloat(splittedScpCoordinates[2]));
                    	
                    	//We see if we have any attribute
                
                    	if(sitesList.getScpAttributes().get(i).contains("speaker")) {
                    		//do nothing in this software version 
                    	}
                    	else {
                    		//do nothing in this software version
                    	}
                    	if(sitesList.getScpAttributes().get(i).contains("microphone")) {
                    		//do nothing in this software version
                    	}
                    	else {
                    		//do nothing in this software version
                    	}
                    	if(sitesList.getScpAttributes().get(i).contains("inverse")) {
                    		//do nothing in this software version
                    	}
                    	else {
                    		//do nothing in this software version
                    	}
                    	if(sitesList.getScpAttributes().get(i).contains("loop")) {
                    		//do nothing in this software version
                    	}
                    	else {
                    		//do nothing in this software version
                    	}
                    	if(sitesList.getScpAttributes().get(i).contains("nonstop")) {
                    		//do nothing in this software version
                    	}
                    	else {
                    		//do nothing in this software version
                    	}
                    	
                    	//files
                    	soundscp[i].setFilee(sitesList.getScpFile().get(4*i),sitesList.getScpFile().get(4*i+1),sitesList.getScpFile().get(4*i+2),sitesList.getScpFile().get(4*i+3));
                    	
                    	//set Angles
                    	splittedScpAngles1=sitesList.getScpAngle().get(4*i).split(" ");
                    	splittedScpAngles2=sitesList.getScpAngle().get(4*i+1).split(" ");
                    	splittedScpAngles3=sitesList.getScpAngle().get(4*i+2).split(" ");
                    	splittedScpAngles4=sitesList.getScpAngle().get(4*i+3).split(" ");
                    	
                    	soundscp[i].setAngles(Float.parseFloat(splittedScpAngles1[0]),Float.parseFloat(splittedScpAngles1[1]),Float.parseFloat(splittedScpAngles2[0]),Float.parseFloat(splittedScpAngles2[1]),Float.parseFloat(splittedScpAngles3[0]),Float.parseFloat(splittedScpAngles3[1]),Float.parseFloat(splittedScpAngles4[0]),Float.parseFloat(splittedScpAngles4[1]));
                    	
                  }
          		///
          		//END OF SOUND CIRCLES AND SOUNDSCAPES SETUP
          		///
          		 		
                   
              } catch (Exception e) {
                   /* Display any Error to the GUI. */
               //    tv.setText("Error: " + e.getMessage());
                   Log.e(MY_DEBUG_TAG, "ErrorParsingXML", e);
              }
             
      		///****************************** END XML *****************
        
        
		////////////////////////////
		//GOOGLE MAPS SETTINGS
		////////////////////////////
        
        	//this is all to do in onCreate for location
        	getMapReference();
        	
        	//zoom and camera at the beginning
        	zoomLevel=17;
        	bearingLevel=0;
        	tiltLevel=25;
        	
        	
        	changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(lat, lng))
        			.zoom(zoomLevel)
        			.bearing(bearingLevel)
        	        .tilt(tiltLevel)
        	        .build()
        				), new GoogleMap.CancelableCallback() {
        	    @Override
        	    public void onFinish() {
        	        // Your code here to do something after the Map is rendered
        	    }

        	    @Override
        	    public void onCancel() {
        	        // Your code here to do something after the Map rendering is cancelled
        	    }
        	});
        	
        	
        	
        	/////////////////////////////////////////
			// thread for sending the http $_post
	        /////////////////////////////////////////
        if(networked){
			Thread splashTread = new Thread() {
	            @Override
	            public void run() {
	             
	            	
	             int waited=0;
	             
             
	             while(true){
	            	
	                try {
	                    waited = 0;
	                    while(connected && (waited < splashTime)) {
	                        sleep(100);
	                        if(connected) {
	                            waited += 100;
	                        }
	                    }
	                } catch(InterruptedException e) {
	                    // do nothing
	                } finally {
	                    //send http
	                	//get hour
	                	if(connected) {
	                		/*
	                		seconds = c.get(Calendar.SECOND);
	        	        	minutes = c.get(Calendar.MINUTE);
	        	        	hour = c.get(Calendar.HOUR);
	        	        	day = c.get(Calendar.DATE);
	        	        	month = c.get(Calendar.MONTH);
	        	        	year = c.get(Calendar.YEAR);
	        	        
	        	        	actualDate= Integer.toString(year) + "-" + Integer.toString(day) + "-" + Integer.toString(month)+ " " + Integer.toString(hour) + ":" + Integer.toString(minutes) + ":" + Integer.toString(seconds);	        	        		
	                		*/
	                		actualDate="0";
	                		
	        				//send Http post
	        				if(networked) {
	        					//send Http postStart
	        					if(firstHttp){
	        						postDataStart(macNum);
	        						//Log.w("noTours http", "this is firstHTTP");
	        						firstHttp=false;
	        					}
	        					
	        					//no lo mandamos ahora todo el rato, solo cuando llega al totem
	        					if(inTrigger){
	        						postData(Double.toString(lat), Double.toString(lng) , actualDate, macNum);
	        						//Log.w("noTourshttp","location packet sent");
	        					
	        						postDataTotem(Integer.toString(seq.getLevel()), Integer.toString(trigger) , actualDate, macNum);
	        					}
	        					//Log.v("packkket", "**********************************************************************************************");
	        				}          		
	                	}
	                }
	             }
	            }
	        };
	        splashTread.start();
        }
        	
	        
			
			/////////////////////////////////////////////
	        //LOCATION SERVICE
	        /////////////////////////////////////////////

		
        	//**************************************************
      		//Start Thread of Audio Playback of Circles
      		//**************************************************
      		Thread audios = new Thread() {
      			
      			//definitions
      			
      			String my_path2= "/storage/emulated/0/notours/" + soundwalk;
      			//String my_path2=Environment.getExternalStorageDirectory() + "/notours/"+ soundwalk;
      			
      			Location bLocation = new Location("acircle");
      			Location aLocation = new Location("whereIam");
      			
      			float distance;
      			float dist[]=new float[MAXIMUM_NUMBER_CIRCLES];
      			double lat_aux=0;
      	        double lng_aux=0;	
      	        boolean infadeout=false;
      	        int actualLevel=0;
      	        
      			public void run(){

      				//enable vibrating
          			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);    
          			
      				while(true) {				
      										
      						if(connected) {						 	
      							//set location based on actual values
      				            aLocation.setLatitude(lat);  
      				            aLocation.setLongitude(lng); 

      				            //check distances to all circles           
      				            lat_aux=0;
      				            lng_aux=0;
      				            distance=10000000; //just for having a big maximum to compare at the beginning
      				                 				            
      				            //Log.v("noTours:::", Integer.toString(numberOfSndCircles)); 	
      				    		for(int jj=0;jj<numberOfSndCircles;jj++){   				    							    							    			
      				    			//each sounds coordinates    				    			
      				    	        lat_aux=sndCircles[jj].getLat();
      				    	        //Log.v("noTours:::", Double.toString(lat_aux)); 
      				    	        lng_aux=sndCircles[jj].getLng();
      				    	            	
      				    	        bLocation.setLatitude(lat_aux);        
      				    		    bLocation.setLongitude(lng_aux);
      				    	        
      				    		    //DISTANCES TO EVERY CIRCLE
      				    		    dist[jj]=aLocation.distanceTo(bLocation);
      				    		       	
      				    	            	if (dist[jj]<=distance) distance=dist[jj]; //for a TextView
      				    	            	
      				    	            	if(dist[jj]<sndCircles[jj].getRad()) { //if we are inside the circle
      				    	            		 	
      				    	            		//check if it is a milestone and then increment level of the game
  							    	            if((sndCircles[jj].getMilestone()>0) && sndCircles[jj].getLevel()==seq.getLevel()) {
  							    	            		seq.setLevel(sndCircles[jj].getMilestone());
  							    	            		changeOfLevel=true; //flag for drawing the overlays
  							    	            }
  							    	            
  							    	            //check if we are inside of a trigger and update trigger variable and trigger post http
  							    	            if(sndCircles[jj].isTrigger()==1){
  							    	            	trigger=1;
  							    	            	inTrigger=true;  //for http post
  							    	            	
  							    	            } else {
  							    	            	trigger=0;
  							    	            	inTrigger=false; //for http post
  							    	            }
      				    	            		    				    	            		
      				    	            	   //check if the circle belongs to this level or is just level 0 (ambient sounds always are played)
      						    	           actualLevel=seq.getLevel();
      						    	           //actualLevel=0;
      						    	           
      						    	           if((sndCircles[jj].getLevel()== seq.getLevel()) || (sndCircles[jj].getLevel()==0) ) { 
      				    	            		
      				    	            		 if(sndCircles[jj].released==true) { //at the beginning of everything					    	            		 
      				    	            			
      				    	            			 
      				    	            			 
      				    	            			//init and load sound
      				    	            			sndCircles[jj].initSound();
      				    	            			
      				    	            			//check if it is an set of audios (ontology) or a simple file
      				    	            	        if(sndCircles[jj].getIsFolder()){
      				    	            	        	//Log.v("[noTours] - ontology?", "yes");			    	            	                   	            
      				    	            	            sndCircles[jj].loadSoundSet(my_path2.concat(sndCircles[jj].getFilee())+"/");
      				    	            	            //Log.v("he cargadoooooooo",my_path2.concat(sndCircles[jj].getFilee())+"/" );
      				    	            	        }
      				    	            	        else {
      				    	            	        	//Log.v("[noTours] - ontology?", "no");
      				    	            	        	//Log.v("path no Onto",my_path2.concat(sndCircles[jj].getFilee()));
      				    	            	        	sndCircles[jj].loadSound(my_path2.concat(sndCircles[jj].getFilee()));
      				    	            	        }
          							    	            		
      				    	            			//play that sound
      				    	            			sndCircles[jj].playSound();	
      				    	            			
      				    	            			// 1. Vibrate for some milliseconds  
      				    	            			if(sndCircles[jj].getVibrate()){ 
      					    	   					 	v.vibrate(400); 
      				    	            			} 				    	            							    	            						    	            		
      				    	            		}
      				    	            		else{ //--> release == false --> it is playing - after stop but before release
      				    	            			
      				    	            			if(sndCircles[jj].isPlaying==false) { //if after a stop()
      					    	            			//we are a circle in non Loop status after a mp.stop(), wait until going out
      				    	            				
      					    	            		}
      					    	            		else{ //if still playing
      					    	            			if(sndCircles[jj].getSpeaker()==1.0f){
      					    	            				if(sndCircles[jj].released==false){
      					    	            					sndCircles[jj].speakerVolume(dist[jj]);	
      					    	            				}
      					    	            			}				    	            			
      					    	            		}
      				    	            		}
      						    	          }
      				    	            	} //if we are out of the circle
      				    	            	else { //if we go out of the circle try stop the sound (we dont pause, we stop and seek at the new entrance)
      				    	            		   // in case of playOut we try to stop, but the Circle class will manage it.
      				    	            		if(sndCircles[jj].released==false){ //it is still sounding or after a stop()
      				    	            		
      				    	            			if(sndCircles[jj].isPlaying==true) { //still playing					    	            			
      				    	            				sndCircles[jj].stopSound();	//stopSound does Stop and Release			    	            								    	            			
      				    	            			} else { //after stop, ONLY waiting for release
      				    	            				sndCircles[jj].releaseSound();
      				    	            			}
      				    	            		}
      				    	            	}
      				    					
      				    	            }  //end del for
      				    		
      				    		
      				    		for(int jj=0;jj<numberOfSoundscapes;jj++){  
      				    	           
      				    			
      				    			//each sounds coordinates
      				    	        lat_aux=soundscp[jj].getLat();
      				    	        lng_aux=soundscp[jj].getLng();
      				    	            	
      				    	        bLocation.setLatitude(lat_aux);        
      				    		    bLocation.setLongitude(lng_aux);
      				    	            	
      				    		    dist[jj]=aLocation.distanceTo(bLocation);
      				    	            	
      				    	            	if (dist[jj]<=distance) distance=dist[jj]; //for TextView
      				    	            	
      				    	            	if(dist[jj]<soundscp[jj].getRad()) { //if we are in the circle
      				    	            		
      				    	            		if(soundscp[jj].isPlaying==false) { //only if it is not playing
      				    	            			
      				    	            			//init and load sound
      				    	            			soundscp[jj].initSound();
      				    	            			soundscp[jj].loadSound(my_path2);
      				    	            			
      				    	            			//play that sound
      				    	            			soundscp[jj].playSound();
      				    	            			//Log.v("Playing sounnnnnnnnnd", "now");
      				    	            			
      				    	            			
      				    	   					 	// 1. Vibrate for some milliseconds   
      				    	   					 	v.vibrate(400); 
      				    	   					 	
      				    	            		}else{
      				    	            			//update volume
      				    	            			soundscp[jj].updateAngle(orientationX);
      				    	            		}
      				    
      				    	            	}
      				    	            	else { //if we go out of the circle pause the sound
      				    	            		if(soundscp[jj].isPlaying==true) { //only if it is not playing
      				    	            			
      				    	            			soundscp[jj].stopSound();
      				    	            			    				    	            			
      				    	            		}
      				    	            	}
      				    					
      				    	            }  //end del for
      				    		
    				    	            //display minimum distance to any Circle in the TextView
      				    	            //outlong.setText("Distance to closest circle (meters): " +  distance);
      							
     				    			
      						}
      						else { 
      							//if it is not connected, this thread is executed very fast doing nothing
      							//what it will consume all CPU, then we will wait a bit until connection
      							//if not, many phones will crash
      							try {
      							    Thread.sleep(100);
      							} catch (InterruptedException e) {
      							    // TODO Auto-generated catch block
      							    e.printStackTrace();
      							}
      							
      						}
   				
      				}
      			}
      		};
      		audios.start();
      		//Audio Player Control Thread END..................................	
        
      	
      	//COMPASS INIT	
      	mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
      	mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION); 
      	
     // Setting THE LOGO OF NOTOURS
		TextView tvLocation = (TextView) findViewById(R.id.tv_location);	
      	tvLocation.setText("noTours - waiting for location");
      	tvLocation.bringToFront();
      	//tvLocation.setHeight(40);
      	tvLocation.setTextColor(Color.BLACK);
      	tvLocation.setTextSize(20);
      
	} //END OF ONCREATE
	
	////////////////////
	//COMPASS CALLBACKS
	////////////////////
	@Override
	  public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    // Do something here if sensor accuracy changes.
	    // You must implement this callback in your code.
	  }
	@Override
	  public void onSensorChanged(SensorEvent event) {
		orientationX = event.values[0];
	    //float pitch_angle = event.values[1];
	    //float roll_angle = event.values[2];
	    // Do something with these orientation angles.
	  }

	
	//////////////////////////////
	//key control for kiosk mode
	//////////////////////////////
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	/*
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		//do what you want
    		//startActivity(new Intent("com.noTours.escoitar.noTours.noToursCore"));
    		return true;
    	}
    	*/
    	if (keyCode == KeyEvent. KEYCODE_ENDCALL) {
    		//do what you want
    		//startActivity(new Intent("com.noTours.escoitar.noTours.noToursCore"));
    		return true;
    	}
    	if (keyCode == KeyEvent. KEYCODE_SEARCH) {
    		//do what you want
    		//startActivity(new Intent("com.noTours.escoitar.noTours.noToursCore"));
    		return true;
    	}
    	if (keyCode == KeyEvent. KEYCODE_POWER) {
    		//do what you want
    		//startActivity(new Intent("com.noTours.escoitar.noTours.noToursCore"));
    		return true;
    	}
    	if (keyCode == KeyEvent. KEYCODE_HOME) {
    		//do what you want		
    		return true;
    	}
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		//do what you want		
    		return true;
    	}
    	if (keyCode == KeyEvent.KEYCODE_CAMERA) {
    		//do what you want		
    		return true;
    	}
    	if (keyCode == KeyEvent. KEYCODE_CALL) {
    		//do what you want
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    ////////////////////////
    //launch GPS settings
    ////////////////////////
    private void launchGPSOptions() {
        //Method for displaying an alert if the GPS is not enabled
        //it brings the user to settings if necessary
            
        	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        	alertDialog.setTitle("Your GPS is not enabled");
        	alertDialog.setMessage("Please enable it before starting noTours");
        	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
        	   public void onClick(DialogInterface dialog, int which) {
        	      // here you can add functions
        		   startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
        		    
        	   }
        	});
        	alertDialog.setIcon(R.drawable.icon);
        	alertDialog.show();
        } 
   
    
    ////////////////////
    //SYSTEM CALLBACKS
    ////////////////////
    @Override
    protected void onResume() {   
    	super.onResume();
    	getMapReference();
    	wakeUpLocationClient();
    	myLocationClient.connect();    
    }

    @Override
    protected void onPause() {
      
      super.onPause();
      if(myLocationClient != null){
          myLocationClient.disconnect();
      }
      mSensorManager.unregisterListener(this);
      
      	seconds = c.get(Calendar.SECOND);
  		minutes = c.get(Calendar.MINUTE);
  		hour = c.get(Calendar.HOUR);
  		day = c.get(Calendar.DATE);
  		month = c.get(Calendar.MONTH);
  		year = c.get(Calendar.YEAR);
  
  		actualDate= Integer.toString(year) + "-" + Integer.toString(day) + "-" + Integer.toString(month)+ " " + Integer.toString(hour) + ":" + Integer.toString(minutes) + ":" + Integer.toString(seconds);
  		//Log.v("noTourshttpppppppppppppppppp","2");
		
  		//send Http post
		if(networked){
			postDataEnd(Double.toString(lat), Double.toString(lng) , actualDate, macNum);
			Log.v("noTourshttp","end packet sent");
		}
      
      
      System.exit(1);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
        if(myLocationClient != null){
            myLocationClient.disconnect();
        }
        mSensorManager.unregisterListener(this);
        
        seconds = c.get(Calendar.SECOND);
    	minutes = c.get(Calendar.MINUTE);
    	hour = c.get(Calendar.HOUR);
    	day = c.get(Calendar.DATE);
    	month = c.get(Calendar.MONTH);
    	year = c.get(Calendar.YEAR);
    
    	actualDate= Integer.toString(year) + "-" + Integer.toString(day) + "-" + Integer.toString(month)+ " " + Integer.toString(hour) + ":" + Integer.toString(minutes) + ":" + Integer.toString(seconds);
    
		//send Http post
		if(networked){
			postDataEnd(Double.toString(lat), Double.toString(lng) , actualDate, macNum);
			Log.v("noTourshttp","end packet sent");
		}
        
        
        System.exit(1);
	  
    }
    
    @Override
    protected void onDestroy() {
      mSensorManager.unregisterListener(this);
      
      	seconds = c.get(Calendar.SECOND);
  		minutes = c.get(Calendar.MINUTE);
  		hour = c.get(Calendar.HOUR);
  		day = c.get(Calendar.DATE);
  		month = c.get(Calendar.MONTH);
  		year = c.get(Calendar.YEAR);
  
  		actualDate= Integer.toString(year) + "-" + Integer.toString(day) + "-" + Integer.toString(month)+ " " + Integer.toString(hour) + ":" + Integer.toString(minutes) + ":" + Integer.toString(seconds);
  
		//send Http post
		if(networked){
			postDataEnd(Double.toString(lat), Double.toString(lng) , actualDate, macNum);
			Log.v("noTourshttp","end packet sent");
		}
      
      
      System.exit(1);
    }


/**
*
* @param lat - latitude of the location to move the camera to
* @param lng - longitude of the location to move the camera to
*            Prepares a CameraUpdate object to be used with  callbacks
*/
private void gotoMyLocation(double lat, double lng) {
	//
	zoomLevel=myMap.getCameraPosition().zoom;
	bearingLevel=myMap.getCameraPosition().bearing;
	tiltLevel=myMap.getCameraPosition().tilt;
	
	changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(lat, lng))
		.zoom(zoomLevel)
		.bearing(bearingLevel) //0
        .tilt(tiltLevel)  //25
        .build()
			), new GoogleMap.CancelableCallback() {
    @Override
    public void onFinish() {
        // Your code here to do something after the Map is rendered
    }

    @Override
    public void onCancel() {
        // Your code here to do something after the Map rendering is cancelled
    }
});
}

/**
*      When we receive focus, we need to get back our LocationClient
*      Creates a new LocationClient object if there is none
*/
private void wakeUpLocationClient() {
if(myLocationClient == null){
    myLocationClient = new LocationClient(getApplicationContext(),
            this,       // Connection Callbacks
            this);      // OnConnectionFailedListener
}
}

/**
*      Get a map object reference if none exits and enable blue arrow icon on map
*/
private void getMapReference() {
if(myMap == null){
    myMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
        .getMap();
}
if(myMap != null){
    myMap.setMyLocationEnabled(true);
}
}

    

/**
*
* @param bundle
*      LocationClient is connected
*/
@Override
public void onConnected(Bundle bundle) {
myLocationClient.requestLocationUpdates(
        REQUEST,
        this); // LocationListener
}

/**
*      LocationClient is disconnected
*/
@Override
public void onDisconnected() {

}

/**
*
* @param location - Location object with all the information about location
*                 Callback from LocationClient every time our location is changed
*/
@Override
public void onLocationChanged(Location location) {
	gotoMyLocation(location.getLatitude(), location.getLongitude());
	connected=true;

	// Getting latitude of the current location
	lat = location.getLatitude();

	// Getting longitude of the current location
	lng = location.getLongitude();	
	
	if(firstGPSfix){	
		
		//
		//PAINT THE CIRCLE OVERLAYS
    	//
    	// we need to wait until we get a first location cause we could work in "sticky mode"
    	//
    			
		//Get the location
		latOriginal=(float) lat;
		lngOriginal =(float) lng;
				
		//if sticky, update the circles coordinates
		if(sticky){
			//offset calculation
			double latOffset=latOriginal-sndCircles[0].getLat();
			double lngOffset=lngOriginal-sndCircles[0].getLng();
			
			for(int jj=0;jj<numberOfSndCircles;jj++) {		
    			
				sndCircles[jj].setLat(sndCircles[jj].getLat()+latOffset);
				sndCircles[jj].setLng(sndCircles[jj].getLng()+lngOffset);
				
			}
						
			for(int jj=0;jj<numberOfSoundscapes;jj++) {
				
				soundscp[jj].setLat(soundscp[jj].getLat()+latOffset);
				soundscp[jj].setLng(soundscp[jj].getLng()+lngOffset);
								
			}
		}  //end of sticky processs
		
		
		//Paint soundCircles overlays
        for(int jj=0;jj<numberOfSndCircles;jj++) {
        	
        	if(sndCircles[jj].getLevel() == seq.getLevel() || sndCircles[jj].getLevel() == 0 ){   //visible circles
        		
        		if(sndCircles[jj].getMilestone()>0 || sndCircles[jj].isTrigger()==1){     //if it is a milestone or a trigger
        			circleOptions = new CircleOptions()
        			.center(new LatLng(sndCircles[jj].getLat(), sndCircles[jj].getLng()))
        			.radius(sndCircles[jj].getRad())
        			.visible(true)
        			.fillColor(Color.argb(20, 255, 0, 0))
        			.strokeWidth(1);// In meters
        		
        			circleAux = myMap.addCircle(circleOptions);
        			mapStuff.put(String.valueOf(jj), circleAux);
        			
        		} else {
        		
        			if(sndCircles[jj].getSpeaker()==0){    //if they are not speakers (paint in blue) 		
        				circleOptions = new CircleOptions()
        				.center(new LatLng(sndCircles[jj].getLat(), sndCircles[jj].getLng()))
        				.radius(sndCircles[jj].getRad())
        				.visible(true)
        				.fillColor(Color.argb(20, 0, 0, 255))
        				.strokeWidth(1);// In meters
        		
        				circleAux = myMap.addCircle(circleOptions);
        				mapStuff.put(String.valueOf(jj), circleAux);
        			
        			} else {								//if they are speakers (paint in green) 
        				circleOptions = new CircleOptions()
        				.center(new LatLng(sndCircles[jj].getLat(), sndCircles[jj].getLng()))
        				.radius(sndCircles[jj].getRad())
        				.visible(true)
        				.fillColor(Color.argb(20, 0, 255, 0))
        				.strokeWidth(1);// In meters
        		
        				circleAux = myMap.addCircle(circleOptions);
        				mapStuff.put(String.valueOf(jj), circleAux);
        			    			
        			}
        		}
        		
		  
        	} else {   //non visible circles but we create them
        		
        		if(sndCircles[jj].getMilestone()>0 || sndCircles[jj].isTrigger()==1){
        			circleOptions = new CircleOptions()
        			.center(new LatLng(sndCircles[jj].getLat(), sndCircles[jj].getLng()))
        			.radius(sndCircles[jj].getRad())
        			.visible(false)
        			.fillColor(Color.argb(20, 255, 0, 0))
        			.strokeWidth(1);// In meters
        		
        			circleAux = myMap.addCircle(circleOptions);
        			mapStuff.put(String.valueOf(jj), circleAux);
        			
        		} else {
        		
        			if(sndCircles[jj].getSpeaker()==0){    //if they are not speakers (paint in blue) 		
        				circleOptions = new CircleOptions()
        				.center(new LatLng(sndCircles[jj].getLat(), sndCircles[jj].getLng()))
        				.radius(sndCircles[jj].getRad())
        				.visible(false)
        				.fillColor(Color.argb(20, 0, 0, 255))
        				.strokeWidth(1);// In meters
        		
        				circleAux = myMap.addCircle(circleOptions);
        				mapStuff.put(String.valueOf(jj), circleAux);
        			
        			} else {								//if they are speakers (paint in green) 
        				circleOptions = new CircleOptions()
        				.center(new LatLng(sndCircles[jj].getLat(), sndCircles[jj].getLng()))
        				.radius(sndCircles[jj].getRad())
        				.visible(false)
        				.fillColor(Color.argb(20, 0, 255, 0))
        				.strokeWidth(1);// In meters
        		
        				circleAux = myMap.addCircle(circleOptions);
        				mapStuff.put(String.valueOf(jj), circleAux);
        			    			
        			}
        		}
        	}
        	
		} //end of soundCirles painting
        
        
        //Paint Soundscapes Overlays
        for(int jj=0;jj<numberOfSoundscapes;jj++) {				
        	circleOptions = new CircleOptions()
		    .center(new LatLng(soundscp[jj].getLat(), soundscp[jj].getLng()))
		    .radius(soundscp[jj].getRad())
		    .fillColor(Color.argb(20, 255, 0, 0))
		    .strokeWidth(1);// In meters
        	circleAux = myMap.addCircle(circleOptions);							
		}
		
        
        TextView tvLocation2 = (TextView) findViewById(R.id.tv_location);	
      	tvLocation2.setText("noTours");
      	tvLocation2.bringToFront();
      	//tvLocation.setHeight(40);
      	tvLocation2.setTextColor(Color.BLACK);
      	tvLocation2.setTextSize(20);
        
        
      	firstGPSfix=false;
	}  //end of first Gps Fix
	
	if(changeOfLevel) {
		changeOfLevel=false; //flag for drawing the overlays
		
		for(int jj=0;jj<numberOfSndCircles;jj++) {
			
			circleAux = mapStuff.get(String.valueOf(jj));   //get reference to the circle
			
			if(sndCircles[jj].getLevel() == seq.getLevel() || sndCircles[jj].getLevel() == 0 || (sndCircles[jj].getLevel() == (seq.getLevel()-1) && sndCircles[jj].getMilestone()>0) ){   //visible circles
				
				circleAux.setVisible(true);
				
			} else {
				circleAux.setVisible(false);
			}
			
		}
		
		
	}

}

@Override
public void onConnectionFailed(ConnectionResult connectionResult) {
}


private void changeCamera(CameraUpdate update, GoogleMap.CancelableCallback callback) {
	myMap.moveCamera(update);
}    
    
    
///////////////////
//MENU
///////////////////
/*
@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_main, menu);
	return true;
}
*/

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case 0:
            // do whatever
        	/*
        	seq.incrementLevel();
        	changeOfLevel=true;
        	Log.w("noToursHttp", "Change of Level");
        	Log.w("noToursHttp", String.valueOf(seq.getLevel()));
        	*/
        	
            return true;
        default:
        	// do whatever
        	/*
        	seq.incrementLevel();
        	changeOfLevel=true;
        	Log.w("noToursHttp", "Change of Level");
        	Log.w("noToursHttp", String.valueOf(seq.getLevel()));
        	*/
            return super.onOptionsItemSelected(item);
    }
}



///////////////////////
// HTTP SEND FUNCTIONS
//////////////////////

public void postData(String lat, String lng, String time, String mac) {
    // Create a new HttpClient and Post Header
    
	HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost("http://www.editor.notours.org/exec/update_walker.php");

    try {
        // Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        
        nameValuePairs.add(new BasicNameValuePair("project_id", projectID));
        //Log.w("ID",projectID);
        nameValuePairs.add(new BasicNameValuePair("latitude", lat));
        nameValuePairs.add(new BasicNameValuePair("longitude", lng));
        nameValuePairs.add(new BasicNameValuePair("name", nickname));
        nameValuePairs.add(new BasicNameValuePair("mac", mac));
        //Log.w("mac",mac);
        //nameValuePairs.add(new BasicNameValuePair("time", time));
        
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        // Execute HTTP Post Request
        HttpResponse response = httpclient.execute(httppost);
        //Log.w("noTourshttpppppppppppppppppp","executed postData");
        
    } catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
    } catch (IOException e) {
        // TODO Auto-generated catch block
    }
    
}

public void postDataTotem(String l, String t, String time, String mac) {
    // Create a new HttpClient and Post Header
    
	HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost("http://www.editor.notours.org/exec/update_trigger.php");

    try {
        // Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        
        nameValuePairs.add(new BasicNameValuePair("project_id", projectID));
        //Log.w("ID",projectID);
        nameValuePairs.add(new BasicNameValuePair("level", l));
        nameValuePairs.add(new BasicNameValuePair("trigger", t));
        nameValuePairs.add(new BasicNameValuePair("name", nickname));
        nameValuePairs.add(new BasicNameValuePair("mac", mac));
        //Log.w("mac",mac);
        //nameValuePairs.add(new BasicNameValuePair("time", time));
        
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        // Execute HTTP Post Request
        HttpResponse response = httpclient.execute(httppost);
        //Log.w("noTourshttpppppppppppppppppp","executed postData");
        
    } catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
    } catch (IOException e) {
        // TODO Auto-generated catch block
    }
    
}

public void postDataEnd(String lat, String lng, String time, String mac) {
    // Create a new HttpClient and Post Header
    
	HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost("http://www.editor.notours.org/exec/store_walker.php");
    //Log.v("noTourshttpppppppppppppppppp","3");

    try {
    	//Log.v("noTourshttpppppppppppppppppp","4");
        // Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        
        nameValuePairs.add(new BasicNameValuePair("project_id", projectID));
        nameValuePairs.add(new BasicNameValuePair("latitude", lat));
        nameValuePairs.add(new BasicNameValuePair("longitude", lng));
        nameValuePairs.add(new BasicNameValuePair("name", nickname));
        nameValuePairs.add(new BasicNameValuePair("mac", mac));
        nameValuePairs.add(new BasicNameValuePair("store", "true"));
        nameValuePairs.add(new BasicNameValuePair("time", time));
        
        //Log.v("noTourshttpppppppppppppppppp","5");
        
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        
        //Log.v("noTourshttpppppppppppppppppp","6");
        // Execute HTTP Post Request
        HttpResponse response = httpclient.execute(httppost);
        Log.w("noTourshttpppppppppppppppppp","post Data end");
        
    } catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
    } catch (IOException e) {
        // TODO Auto-generated catch block
    }
    
}

public void postDataStart(String mac) {
    // Create a new HttpClient and Post Header
    
	HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost("http://www.editor.notours.org/exec/start_walker.php");
    //Log.v("noTourshttpppppppppppppppppp","3");

    try {
    	//Log.v("noTourshttpppppppppppppppppp","4");
        // Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        
        nameValuePairs.add(new BasicNameValuePair("project_id", projectID));
        nameValuePairs.add(new BasicNameValuePair("name", nickname));
        nameValuePairs.add(new BasicNameValuePair("mac", mac));
        nameValuePairs.add(new BasicNameValuePair("start", "true"));
        
        
        //Log.v("noTourshttpppppppppppppppppp","5");
        
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        
        //Log.v("noTourshttpppppppppppppppppp","6");
        // Execute HTTP Post Request
        HttpResponse response = httpclient.execute(httppost);
        Log.w("noTourshttpppppppppppppppppp","executed postData start");
        
    } catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
    } catch (IOException e) {
        // TODO Auto-generated catch block
    }
    
}





}

