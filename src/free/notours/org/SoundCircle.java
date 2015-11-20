package free.notours.org;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Environment;
import android.util.Log;
import free.notours.org.R;

//class for storing Circles information got from the RSS
    public class SoundCircle {
    	
    	String filee;
    	double lat;
    	double lng;    	
    	float radius;
    	//MediaPlayer mp = new MediaPlayer();// = new MediaPlayer();
    	MediaPlayer mp;
    	
    	//boolean fade=false;
    	public float volume;
    	
    	//IN_FADE, IN_STOP son dos cerrojos para no poder
    	//acceder al objeto varias veces sin haberse creado
    	//o destruido
    	//El GPS hace que se entre y salga del circulo
    	//antes de llegar al release del MediaPlayer y lo casca
    	//al hacer un nuevo init.
    	boolean in_fade;
    	boolean in_stop;
    	boolean enableFadein;
    	boolean enableFadeOut;
    	
    	//los siguientes flags evitan que ocurra lo mismo
    	boolean loop;
    	boolean isPlaying;
    	boolean released=true;
    	
    	boolean in_speaker;
    	int ramp;
    	float distance;
    	float speaker;
    	float lastDistance;
    	
    	int level;
    	int milestone;
    	int trigger;
    	
    	boolean isFolder;
    	boolean vibrate;
    	boolean pauseOut;
    	int pausePosition;
    	boolean nonStop;
    	boolean nonStopCompleted;
    	
    	private ArrayList<String> item = null;
    	public String[] projectsArray=new String[50];
    	
        // all possible internal states
        private static final int STATE_ERROR              = -1;
        private static final int STATE_IDLE               = 0;
        private static final int STATE_PREPARING          = 1;
        private static final int STATE_PREPARED           = 2;
        private static final int STATE_PLAYING            = 3;
        private static final int STATE_PAUSED             = 4;
        private static final int STATE_PLAYBACK_COMPLETED = 5;
        private static final int STATE_SUSPEND            = 6;
        private static final int STATE_RESUME             = 7;
        private static final int STATE_SUSPEND_UNSUPPORTED = 8;
    	
     // mCurrentState is a VideoView object's current state.
        // mTargetState is the state that a method caller intends to reach.
        // For instance, regardless the VideoView object's current state,
        // calling pause() intends to bring the object to a target state
        // of STATE_PAUSED.
        public int mCurrentState = STATE_IDLE;
        private int mTargetState  = STATE_IDLE;
        
        private MediaPlayer.OnPreparedListener mOnPreparedListener;
        private OnErrorListener mOnErrorListener;
        private boolean     mCanPause;
        private boolean     mCanSeekBack;
        private boolean     mCanSeekForward;
        
        //for circles with movement
        public  boolean moveable;
        public String[] track;
        public String trackFile;
        
        /** Create Object For SiteList Class */
    	CoordinatesTrackList sitesList = null;
    	
        
    	public SoundCircle() {	
    	// TODO Auto-generated constructor stub
    		//mymediaPlayer.reset();
    		in_fade=false;
    		in_stop=false;
    		in_speaker=false;
    		lastDistance=0.0f;
    		isPlaying=false;
    		released=true;
    		volume=1.0f;
    		milestone=0;
    		enableFadein=true;
    		enableFadeOut=true;
    		vibrate=true;
    		pauseOut=false;
    		pausePosition=0;
    		nonStop=false;
    		mCurrentState = STATE_IDLE;
            mTargetState  = STATE_IDLE;
            nonStopCompleted=false;
            moveable=false;
            trigger=0;
		}
    	
    	//methods for mediaplayer
    	public void initSound(){
    		mp=new MediaPlayer(); //esto es nuevo (12/01/2011)
    		
    		//set error and prepared status listener
    		//mp.setOnPreparedListener(mPreparedListener);
    		mp.setOnErrorListener(mErrorListener);
    		
    		if (in_fade==false){  			
    			volume=1.0f;
    		}
    	}
    	
    	
    	
        private MediaPlayer.OnErrorListener mErrorListener =
                new MediaPlayer.OnErrorListener() {
                public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
                    mCurrentState = STATE_ERROR;
                    mTargetState = STATE_ERROR;
                    Log.d("[noTours]", "Error: " + framework_err + "," + impl_err);
                    
                    //if (mMediaController != null) {
                    //    mMediaController.hide();
                    //}

                    /* If an error handler has been supplied, use it and finish. */
                    if (mOnErrorListener != null) {
                        if (mOnErrorListener.onError(mp, framework_err, impl_err)) {
                            mp.reset();
                            released=true;
                            //mp.release();
                        	return true;
                        }
                    }
                    return true;
                }
            };
    	
            /**
             * Register a callback to be invoked when the media file
             * is loaded and ready to go.
             *
             * @param l The callback that will be run
             */
            public void setOnPreparedListener(MediaPlayer.OnPreparedListener l)
            {
                mOnPreparedListener = l;
            }
            
            /**
             * Register a callback to be invoked when an error occurs
             * during playback or setup.  If no listener is specified,
             * or if the listener returned false, VideoView will inform
             * the user of any errors.
             *
             * @param l The callback that will be run
             */
            public void setOnErrorListener(OnErrorListener l)
            {
                mOnErrorListener = l;
            }
    	
    	
    	public void loadSound(String path) {
    		
    	  if (in_fade==false){
    		try {
    			this.mp.setDataSource(path);
    			//released=false;
    			
    		} catch (IllegalArgumentException e) {
    			// TODO Auto-generated catch block
    			this.mp.reset();
    			mCurrentState = STATE_ERROR;
                mTargetState  = STATE_ERROR;
    			e.printStackTrace();
    		} catch (IllegalStateException e) {
    			// TODO Auto-generated catch block
    			this.mp.reset();
    			mCurrentState = STATE_ERROR;
                mTargetState  = STATE_ERROR;
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			this.mp.reset();
    			mCurrentState = STATE_ERROR;
                mTargetState  = STATE_ERROR;
    			e.printStackTrace();
    		}
    		}
    	}
    	
    	public void loadSoundSet(String path) {
    		//released=false;
    		if (in_fade==false){
    			
    		//explore the folder
    	    item = new ArrayList<String>();
    	         
    	    File f = new File(path);
    	    File[] files = f.listFiles();
    	         	        
    	    for(int i=0; i < files.length; i++) {
    	       File file = files[i];
    	       //path.add(file.getPath());
    	       if(!file.isDirectory()) {
    	       item.add(file.getName());
    	       projectsArray[i]=file.getName();
    	       //Log.v("item", projectsArray[i]);
    	       }
    	    }
    	    
    	    Random r = new Random();
    	    int i1=r.nextInt(files.length);
    		String finalPath=path+projectsArray[i1];
    		
    		Log.v("[noTours]- path folder: ", finalPath);
    			
    		try {
    			this.mp.setDataSource(finalPath);
    			
    		} catch (IllegalArgumentException e) {
    			// TODO Auto-generated catch block
    			this.mp.reset();
    			mCurrentState = STATE_ERROR;
                mTargetState  = STATE_ERROR;
    			e.printStackTrace();
    		} catch (IllegalStateException e) {
    			// TODO Auto-generated catch block
    			this.mp.reset();
    			mCurrentState = STATE_ERROR;
                mTargetState  = STATE_ERROR;
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			this.mp.reset();
    			mCurrentState = STATE_ERROR;
                mTargetState  = STATE_ERROR;
    			e.printStackTrace();
    		}
    		}
    	}
    	
    	public void playSound() {
    	 
    		
    		//Log.v("[noTours]", "playing sound!");
    		if(this.mp.isPlaying()==false) {
    			isPlaying=false;
    			if (in_fade==false){
    			try {		
    				this.mp.prepare();
    				mCurrentState = STATE_PREPARING;
    			} catch (IllegalStateException e) {
    				// TODO Auto-generated catch block
    				mCurrentState = STATE_ERROR;
    	            mTargetState = STATE_ERROR;
    	            mErrorListener.onError(mp, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
    				this.mp.reset();
    				e.printStackTrace();
    				return;
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				mCurrentState = STATE_ERROR;
    	            mTargetState = STATE_ERROR;
    	            mErrorListener.onError(mp, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
    				this.mp.reset();
    				e.printStackTrace();
    				return;
    			}
    			
    			//set loop attribute 
    			if(loop){
    				this.mp.setLooping(true);
    			}
    			else{
    				this.mp.setLooping(false);
    			}
    			
    			//set Completion Listener for nonStop (playOut)
    			if(nonStop){
    				mp.setOnCompletionListener(new OnCompletionListener() {
    	                public void onCompletion(MediaPlayer mp) {
    	                	nonStopCompleted=true;
    	                }
    	            });
    			}
    			
    			//mute before playing
    			this.mp.setVolume(0.0f, 0.0f);
    			
    			//set pause settings
    			if(pauseOut){ //take the position for recovering it next entrance
    				try{
    					/*
    					mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
        	                public void onSeekComplete(MediaPlayer mp) {
        	                   Log.d("[noTours]", "listener SeekTo created!" );
        	                   mp.start();          // <------------------ start video on seek completed
        	                   //THIS TWO FLAGS ARE VERY IMPORTANT IN ORDER TO CONTROL THREADS ENTRANCE (sync)
        	       				isPlaying=true;
        	       				released=false;
        	                    mp.setOnSeekCompleteListener(null);
        	                }
        	            });
    					*/
    					mp.seekTo(pausePosition); //we don�t pause, we stop, release and seek to at the new entry
    					mp.start();
    					isPlaying=true;
	       				released=false;
    					
    				} catch (IllegalStateException e) {
        				// TODO Auto-generated catch block
        				mCurrentState = STATE_ERROR;
        	            mTargetState = STATE_ERROR;
        	            mErrorListener.onError(mp, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        				this.mp.release();
        				e.printStackTrace();
        				return;
        			}
				}else {  //not a pauseOut Sound
    	    		this.mp.start(); //sound playing!!!
    	    		//THIS TWO FLAGS ARE VERY IMPORTANT IN ORDER TO CONTROL THREADS ENTRANCE (sync)
        			isPlaying=true;
        			released=false;
    	    	}
   			
    			
    			
    			this.mp.setVolume(0.0f, 0.0f);
    			
    			
    			//NON-STOP CONTROL (opcion en la que el sonido se sigue reproduciendo aunque se salga del circulo)
    			if(!loop){ //ESTO NO ES LOOP, es un NON-STOP!!!!!!!!!!!!
    				/*
    				mp.setOnCompletionListener(new OnCompletionListener(){

                        // @Override
                        public void onCompletion(MediaPlayer arg0) {
                             // File has ended !!! Wink
                        	if(mp.isPlaying()){
                        		mp.stop();                       		
                        	}
                        	
                        	mp.stop();
                        	isPlaying=false;
                        		                                              	
                        }
                   });
                   */ 
    			}
    			
    			    			    			
    			
    			//and in here I create a thread for controlling fadein volume
    			//it controls the volume when entering a circle, smoothing the
    			//audio from 0.0 to 1.0 in 40 steps of 100 ms.
    			if(!enableFadein) {
    				mp.setVolume(1.0f,1.0f);
    			}
    			if(speaker==0.0f && enableFadein){ //there is not speaker effect
	            		            
    			Thread fadein = new Thread() {
    				public void run() {
    					
    					double volumeValue=0.0f;
    					in_fade=true;
    					if(true){ //aqui habia otra cosa, dejo el if
    						for(int i=0;i<41;i++) {
    							try {
    								volumeValue=(float) (Math.pow(10.0f,((float)i/40.0)*3.0))/1000.0;
    								mp.setVolume((float)volumeValue,(float) volumeValue);
    								volume=(float)volumeValue;
    								
    								//Log.v("fadeiiiiiiiiiiiiiiiiiiinnnnnnnnnnn", Double.toString(volumeValue));
    								//Log.v("fadeeeeee valueeeeee", Double.toString((Math.pow(10.0f,((float)i/40.0)*3.0))/1000.0));
    								//Log.v("iteracion", Integer.toString(i));
    								
    								//setValor(sndCircles[fade_circle].volume);
    								//handlerAudios.sendEmptyMessage(0);
    								sleep(100);
								
    							} catch (InterruptedException e) {
    								// TODO Auto-generated catch block
    								e.printStackTrace();
    							}
    						}
    						
    						in_fade=false;
    					}
    				}
    			};
    			fadein.start();
    			//end of modification
	            } 
    			}	 
    		}
    	 
    	}//end of playSound()
    	
    	//The method stop sound develops the fade out thread.
    	//It makes it similar to fade in but releases the MediaPlayer
    	//only at the end of the fade out
    	public void stopSound() {
    	  if(mCurrentState != STATE_ERROR ){
    		  
    		if(!nonStop){	
    		 
    			if(speaker==0.0f && enableFadeOut){ //if there is not speaker effect
    			 if(released==false){
    				if(in_stop==false){
            
    					Thread fadeout = new Thread() {
            	
    						public void run() {
    							in_stop=true;

    							double volumeValue=1.0f;;
						
    							for(int i=81;i>1;i--) {
							
    								try {
    									//volumeValue=(float) (Math.pow(10.0f,((float)i/40.0)*3.0))/1000.0;
								
								
    									volumeValue=(float) (Math.pow(10.0f,((float)i/80.0f)*3.0f))/1000.0f;
    									if(released==false){
    										mp.setVolume((float)volumeValue,(float) volumeValue);
    									}
    									volume=(float)volumeValue;
    									
    									/*
    									Log.v("fadeooooooooooooout", Double.toString(volumeValue));
    									Log.v("fadeeeeee oooooouuuut valllluuuue", Double.toString((Math.pow(10.0f,((float)i/80.0)*3.0))/1000.0));
    									Log.v("iteracion", Integer.toString(i));
										*/
    									
    									sleep(100);
						
    								} catch (InterruptedException e) {
    									// TODO Auto-generated catch block
    									e.printStackTrace();
    								}
    							}
    							
    							
    							//if(mp.isPlaying()){
    							if(released==false){
    								if(pauseOut){ //take the position for recovering it next entrance
    									pausePosition=mp.getCurrentPosition();
    								}
    								if(mCurrentState != STATE_ERROR && released==false ){
    									try {
    										if(mp != null){
    											mp.stop();
    											isPlaying=false;
    										}
    									}catch (IllegalStateException e){
    										// TODO Auto-generated catch block
    				        				mCurrentState = STATE_ERROR;
    				        	            mTargetState = STATE_ERROR;
    				        	            mErrorListener.onError(mp, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
    				        				//mp.reset();
    				        				mp.release();
    				        				released=true;
    				        				e.printStackTrace();
    				        				return;
    									}
    								}
    							}
    							
    							
    							if(released==false){
    								mp.release();
    								released=true;
    								mCurrentState = STATE_IDLE;
    					            mTargetState  = STATE_IDLE;
    							}
    							
    							in_fade=false;
    							in_stop=false;
    							//Log.v("Release sound", "releeeeeassssssse sooouunnnnd");
					
						
    						}
    					};
    					fadeout.start();  			
    				}
    			 }
    			}  			
    			else{ //if speaker effect, don�t apply fade out
    				if(pauseOut){ //take the position for recovering it next entrance
						pausePosition=mp.getCurrentPosition();
					}
    				mp.stop();
					isPlaying=false;
					
    				if(in_speaker==false){   					
    					//(12/01/2011) hacemos release en vez de reset 
    					//mp.reset();
    					if(released==false){
    						mp.release();
    						released=true;
    						mCurrentState = STATE_IDLE;
    			            mTargetState  = STATE_IDLE;
    					}
    				}
    			} // end of speaker or not
    		} else { //we are in a nonStop (playOut case)
    			if(nonStopCompleted){
    				//stop it
    				mp.stop();
					isPlaying=false;
					
    				if(in_speaker==false){   					
    					//(12/01/2011) hacemos release en vez de reset 
    					//mp.reset();
    					if(released==false){
    						mp.release();
    						released=true;
    						mCurrentState = STATE_IDLE;
    			            mTargetState  = STATE_IDLE;
    			            nonStopCompleted=false;
    					}
    				}
    			}
    			else{
    				//do nothing, just wait until a completion listener says nonStopCompleted=true  				
    			}
    	  	}
    	  }
    	}
    	
    	public void speakerVolume(float d){ //speaker effect (volume depends on distance to the center)
    	 if(mCurrentState != STATE_ERROR ){
    	  //in_fade=trueeee; 
    	  distance=d;
    	  in_speaker=false;
    		
    	  if(released==false){
    		
    		if(this.mp.isPlaying()) {
    			isPlaying=true;
    			if(in_speaker==false && in_fade==false && in_stop==false){
    				
    				Thread speaker = new Thread() {
    	            	
    					public void run() {
    						in_speaker=true;
    						//mp.setVolume((float)Math.pow(2.0f,(1.0f-(distance/radius))*3)/8.0f,(float)Math.pow(2.0f,(1.0f-(distance/radius))*3)/8.0f);
    	    				//Log.v("volume",Float.toString((float)Math.pow(10.0f,(1.0f-(distance/radius))*3)/1000.0f));
    	    				
    						//Log.v("distancia",Float.toString(distance));
    						//Log.v("radio", Float.toString(radius));
    						
    						//probamos una interpolacion entre valor actual y anterior
    						float increment=0.0f;
    						float aux_volume=0.0f;
    						increment=(distance-lastDistance)/10.0f; //positive or negative
    						
    						//Log.v("lllllllllegoooo akkki", "hereeeeeeeeeeee2222");
    						//Log.v("radius",Float.toString(radius));
    						//Log.v("increment",Float.toString(increment));
    						
    						for(int i=0;i<10;i++){
    							try{
    								aux_volume=(float)Math.pow(2.0f,(1.0f-((lastDistance+i*increment)/radius))*4)/16.0f;
    								if(in_fade==false && in_stop==false){
    									if(released==false){
    										mp.setVolume(aux_volume,aux_volume);
    									}
    									try {
    										sleep(20);
    									} catch (InterruptedException e) {
    										// TODO Auto-generated catch block
    										e.printStackTrace();						
    									}
    								}
    								//Log.v("lllllllllegoooo akkki", "hereeeeeeeeeeee");
    								//Log.v("speaker",Float.toString(aux_volume));
    							}
    							catch(IllegalStateException e) {
									// TODO Auto-generated catch block
    								//Log.v("excepcionnn", "*********hereeeeeeeeeeee");
									e.printStackTrace();
									
								}
    						}
    						lastDistance=distance;
    	    				in_speaker=false;   	    				  	    				
    					}
    				};
    				speaker.start();  				
    			}
    		}
    	  }		
    	 }	
    	}
    	
    	public void pauseSound() {
    		if(this.mp.isPlaying()) this.mp.pause();
    	}
    	
    	public void releaseSound(){
    		 if(mCurrentState != STATE_ERROR ){
    			if(released==false){
    				//this.mp.reset();
    				this.mp.release();
    				released=true;
    				mCurrentState = STATE_IDLE;
    				mTargetState  = STATE_IDLE;
    		 }
    		}
    	}
    	
    	public void setVolume(float leftVolume, float rightVolume){
    		//volume=leftVolume;
    		this.mp.setVolume(leftVolume, rightVolume);
    	}
    	
    	
    	//methods for storing and getting the info when reading from XML
    	public String getFilee() {
    		return this.filee;
    	}  	
    	public void setFilee(String inp) {
    		this.filee=inp;
    	}   	
    	public double getLat() {
    		return this.lat;
    	}
    	public void setLat(double latitude) {
    		this.lat=latitude;
    	}
    	public double getLng() {
    		return this.lng;
    	}
    	public void setLng(double longitude) {
    		this.lng=longitude;
    	}
    	public float getRad() {
    		return this.radius;
    	}
    	public void setRad(float rad) {
    		this.radius=rad;
    		this.lastDistance=rad;
    	}
    	public float getSpeaker() {
    		return this.speaker;
    	}
    	public void setSpeaker(float spk) {
    		this.speaker=spk;   		
    	}
    	public void setLoop(int lp){
    		if(lp==1) {
    			loop=true;
    		} 
    		else{
    			loop=false;
    		}
    	}
    	public boolean getLoop(){
    		return loop;
    	}
    	public void setLevel(int lvl){
    		this.level=lvl;
    	}
    	public int getLevel(){
    		return this.level;
    	}
    	public void setMilestone(int m){
    		this.milestone=m;
    	}
    	public int getMilestone(){
    		return this.milestone;
    	}
    	public void setIsFolder(boolean f) {
    		this.isFolder=f;
    	}
    	public boolean getIsFolder(){
    		return this.isFolder;
    	}
    	public void setFadeIn(boolean fade){
    		this.enableFadein=fade;
    	}
    	public boolean getFadeIn(){
    		return this.enableFadein;
    	}
    	public void setFadeOut(boolean fade){
    		this.enableFadeOut=fade;
    	}
    	public boolean getFadeOut(){
    		return this.enableFadeOut;
    	}
    	public void setVibrate(boolean vib){
    		this.vibrate=vib;
    	}
    	public boolean getVibrate(){
    		return this.vibrate;
    	}
    	public void setPauseOut(boolean p){
    		this.pauseOut=p;
    	}
    	public boolean getPauseOut(){
    		return this.pauseOut;
    	}
    	public void setNonStop(boolean n){
    		this.nonStop=n;
    	}
    	public boolean getNonStop(){
    		return this.nonStop;
    	} 	
    	public int isTrigger(){
    		return this.trigger;
    	}
    	public void setIsTrigger(int t){
    		this.trigger=t;
    	}
    	public void setMoveable(boolean n){
    		this.moveable=n;
    	}
    	public boolean getMoveable(){
    		return this.moveable;
    	}
    	public void setTrackFile(String n){
    		this.trackFile=n;
    	}
    	public String getTrackFile(){
    		return this.trackFile;
    	}
    	void parseTrackFile(String soundwalk){
    		//parsing track file	
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
     			
     			//read .rss from sdcard or external memory
     			FileInputStream in;
     			//in= new FileInputStream("/sdcard/notours/soundscape.rss");
     			in= new FileInputStream(Environment.getExternalStorageDirectory()+"/notours/" + soundwalk + "/track/hola.kml");
     			Log.v("path to XML",Environment.getExternalStorageDirectory()+"/notours/" + soundwalk + trackFile);
     			
     			//When reading rss from the apk assets
     			//AssetManager mgr = getBaseContext().getAssets();
     			//InputStream in = mgr.open("soundscape.rss");
     			//InputStreamReader isr = new InputStreamReader(in);
     			
     			
     			/** Create handler to handle XML Tags ( extends DefaultHandler ) */
     			TrackXMLHandler myXMLHandler = new TrackXMLHandler();
     			xr2.setContentHandler(myXMLHandler);
     			
     			//For loading from an online server
     			//xr2.parse(new InputSource(sourceUrl.openStream()));
     			
     			//For loading from sdcard or external memory
     			Log.v("antes de parse", "llego");
     			xr2.parse(new InputSource(in));
     			Log.v("despues de parse", "llego");
     			
     		} catch (Exception e) {
     			System.out.println("Track XML Parsing Exception = " + e);
     		}

     		/** Get result from MyXMLHandler SitlesList Object */
     		sitesList = TrackXMLHandler.sitesList;
     		/* Parsing has finished. */
    	}
    }