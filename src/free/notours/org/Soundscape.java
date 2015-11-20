package free.notours.org;

import java.io.IOException;

import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;



public class Soundscape {
	
	
	float initAngle1,initAngle2,initAngle3,initAngle4;
	float endAngle1,endAngle2,endAngle3,endAngle4;    	
	float centerAngle1,centerAngle2,centerAngle3,centerAngle4;
	MediaPlayer mp1;// = new MediaPlayer();// = new MediaPlayer();
	MediaPlayer mp2;// = new MediaPlayer();
	MediaPlayer mp3;// = new MediaPlayer();
	MediaPlayer mp4;// = new MediaPlayer();
	float[] vol=new float[4];
	float[] initAng=new float[4];
	float[] centerAng=new float[4];
	boolean fade=false;
	boolean lastCorner;
	
	double lat;
	double lng;    	
	float radius;
	
	boolean in_fade=false;
	float volume;
	boolean in_stop=false;
	
	String mfile1;
	String mfile2;
	String mfile3;
	String mfile4;
	
	float a1_in;
	float a1_out;
	float a2_in;
	float a2_out;
	float a3_in;
	float a3_out;
	float a4_in;
	float a4_out;
	
	boolean isPlaying;
	
	boolean released=true;
	
	//areas and centers
	float A1,A2,A3,A4;
	float C1,C2,C3,C4;
	float[] C=new float[4];
	float[] A=new float[4];
	
	//public Soundscape(String string) {
	public Soundscape() {
		// TODO Auto-generated constructor stub
		//mymediaPlayer.reset();
		isPlaying=false;
		released=true;
	}
	
	//methods for mediaplayer
	public void initSound(){
		//MediaPlayer mp = new MediaPlayer();
		released=true;
		
		mp1 = new MediaPlayer();
		mp2 = new MediaPlayer();
		mp3 = new MediaPlayer();
		mp4 = new MediaPlayer();
		
		released=false;
		
		/* lo quito de momento por el bug de numero de Mp
		this.mp1.reset();
		this.mp2.reset();
		this.mp3.reset();
		this.mp4.reset();
		*/
		/*
		this.centerAngle1=0.0f;
		this.centerAngle2=90.0f;
		this.centerAngle3=180.0f;
		this.centerAngle4=270.0f;
		centerAng[0]=0.0f;
		centerAng[1]=90.0f;
		centerAng[2]=180.0f;
		centerAng[3]=270.0f;
		*/
	}
	
	public void loadSound(String path) { 
	  if(released==false){
		String f1,f2,f3,f4;
		//String absolute="/sdcard/notours";
		//String absolute=Environment.getExternalStorageDirectory()+"/notours";
		String absolute=path;
		
		//f1=absolute.concat(path);
		f1=absolute.concat(mfile1);
		Log.v("mf1", f1);
		//f2=absolute.concat(path);
		f2=absolute.concat(mfile2);
		Log.v("mf2", f2);
		//f3=absolute.concat(path);
		f3=absolute.concat(mfile3);
		Log.v("mf3", f3);
		//f4=absolute.concat(path);
		f4=absolute.concat(mfile4);
		Log.v("mf4", f4);
		
		try {
			this.mp1.setDataSource(f1);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			this.mp2.setDataSource(f2);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			this.mp3.setDataSource(f3);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			this.mp4.setDataSource(f4);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}
	
	public void playSound() {
	 if(released==false){
		if(this.mp1.isPlaying()==false) {
			try {		
				this.mp1.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(released==false){
			this.mp1.setLooping(true);
			this.mp1.setVolume(0.0f, 0.0f);
			this.mp1.start();
			isPlaying=true;
			}
		}
		
		if(this.mp2.isPlaying()==false) {
			try {		
				this.mp2.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(released==false){
			this.mp2.setLooping(true);
			this.mp2.setVolume(0.0f, 0.0f);
			this.mp2.start();
			isPlaying=true;
			}
		}
		
		if(this.mp3.isPlaying()==false) {
			try {		
				this.mp3.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(released==false){
			this.mp3.setLooping(true);
			this.mp3.setVolume(0.0f, 0.0f);
			this.mp3.start();
			isPlaying=true;
			}
		}
		
		if(this.mp4.isPlaying()==false) {
			try {		
				this.mp4.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(released==false){
			this.mp4.setLooping(true);
			this.mp4.setVolume(0.0f, 0.0f);
			this.mp4.start();
			isPlaying=true;
			}
		}
		
		
		//Fade in for the soundscape
		/*
		Thread fadein = new Thread() {
			public void run() {
				//Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
				//setPriority(Thread.MAX_PRIORITY);
				double volumeValue=0.0f;
				in_fade=true;
				if(true){
					for(int i=0;i<81;i++) {
						try {
							volumeValue=(float) (Math.pow(10.0f,((float)i/80.0)*3.0))/1000.0;
							//mp.setVolume(0.0f, 0.0f);
							mp1.setVolume((float)volumeValue,(float) volumeValue);
							mp2.setVolume((float)volumeValue,(float) volumeValue);
							mp3.setVolume((float)volumeValue,(float) volumeValue);
							mp4.setVolume((float)volumeValue,(float) volumeValue);
							volume=(float)volumeValue;
							
							//Log.v("fadeiiiiiiiiiiiiiiiiiiinnnnnnnnnnn", Double.toString(volumeValue));
							//Log.v("fadeeeeee valueeeeee", Double.toString((Math.pow(10.0f,((float)i/80.0)*3.0))/1000.0));
							//Log.v("iteracion", Integer.toString(i));
							
							//setValor(sndCircles[fade_circle].volume);
							//handlerAudios.sendEmptyMessage(0);
							sleep(100);
						
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					//sndCircles[fade_circle].setVolume(1.0f,1.0f);
					//in_fade=false;
					in_fade=false;
				}
			}
		};
		fadein.start();
		*/
	 }
	}
	
	public void stopSound() {
	 if(released==false){
		
		if(this.mp1.isPlaying()) {
            
			//if(speaker==0.0f){ //if there is not speaker effect
				
			
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
										if(mp1 != null && mp2 != null && mp3 != null && mp4 != null){
											mp1.setVolume((float)volumeValue,(float) volumeValue);
											mp2.setVolume((float)volumeValue,(float) volumeValue);
											mp3.setVolume((float)volumeValue,(float) volumeValue);
											mp4.setVolume((float)volumeValue,(float) volumeValue);
										}
									}
									volume=(float)volumeValue;
							
									/*
									Log.v("fadeooooooooooooout", Double.toString(volumeValue));
									Log.v("fadeeeeee oooooouuuut valllluuuue", Double.toString((Math.pow(10.0f,((float)i/80.0)*3.0))/1000.0));
									Log.v("iteracion", Integer.toString(i));
									 */
									//setValor(sndCircles[fade_circle].volume);
									//handlerAudios.sendEmptyMessage(0);
									sleep(100);
					
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							//sndCircles[fade_circle].setVolume(0.0f,0.0f);
							//fade=false;
								if(released==false){
									mp1.stop();
									isPlaying=false;
									mp1.release();//mp1.reset();
									released=true;
								}	
								if(released==false){
								mp2.stop();
								isPlaying=false;
								mp2.release();//mp2.reset();
								released=true;
								}
								if(released==false){
								mp3.stop();
								isPlaying=false;
								mp3.release();//mp3.reset();
								released=true;
								}
								if(released==false){
								mp4.stop();
								isPlaying=false;
								mp4.release();//mp4.reset();
								released=true;
								}
								in_fade=false;
								in_stop=false;
								//Log.v("Release sound", "releeeeeassssssse sooouunnnnd");
							
							
							
							
							//in_fade_out=false;
				
					
						}
					};
					fadeout.start();  			
				
			}

		}
	 }
		
		
		/*
		if(this.mp1.isPlaying()) {
			this.mp1.stop();
			this.mp1.reset();
		}
		if(this.mp2.isPlaying()) {
			this.mp2.stop();
			this.mp2.reset();
		}
		if(this.mp3.isPlaying()) {
			this.mp3.stop();
			this.mp3.reset();
		}
		if(this.mp4.isPlaying()) {
			this.mp4.stop();
			this.mp4.reset();
		}*/
	}
	
	public void pauseSound() {
		if(this.mp1.isPlaying()) this.mp1.pause();
		if(this.mp2.isPlaying()) this.mp2.pause();
		if(this.mp3.isPlaying()) this.mp3.pause();
		if(this.mp4.isPlaying()) this.mp4.pause();
	}
	
	public void releaseSound(){
		this.mp1.release();
		this.mp2.release();
		this.mp3.release();
		this.mp4.release();
	}
	
	public void setVolume(float v1,float v2,float v3,float v4){
		this.mp1.setVolume(v1, v1);
		this.mp2.setVolume(v2, v2);
		this.mp3.setVolume(v3, v3);
		this.mp4.setVolume(v4, v4);
	}
	
	//methods for storing and getting the info
 	  	
	//public double getLat() {
		//return this.lat;
	//}
	public void setInitAngle(float a1, float a2, float a3, float a4) {
		this.initAngle1=a1;
		this.initAngle1=a2;
		this.initAngle1=a3;
		this.initAngle1=a4;
		
	}
	//public double getLng() {
		//return this.lng;
	//}
	public void setLng(float e1, float e2, float e3, float e4) {
		this.endAngle1=e1;
		this.endAngle2=e2;
		this.endAngle3=e3;
		this.endAngle4=e4;
	}
	//public float getRad() {
		//return this.radius;
	//}
	public void setCenterAngle(float c1, float c2, float c3, float c4) {
		this.centerAngle1=c1;
		this.centerAngle2=c2;
		this.centerAngle3=c3;
		this.centerAngle4=c4;
		centerAng[0]=c1;
		centerAng[1]=c2;
		centerAng[2]=c3;
		centerAng[3]=c4;
		
	}
	
	public void updateAngle(float angle){
		/*		
		// SI NO ESTAMOS EN FADE_IN!! METERLOOOOO
		
        //synchronized (this) {
	            if(angle<45f || angle>315f){
            		lastCorner=true;
            	}else{
            		lastCorner=false;
            	}
	            
	            if(lastCorner) {
	            	
	            	if(angle<=45f){
	            		vol[0]=(float)(1f - (Math.abs((angle-centerAngle1))/90f)*(Math.abs((angle-centerAngle1))/90f));
	            		vol[3]=(float)(1f - (Math.abs((angle-(centerAngle4-360.0f)))/90f)*(Math.abs((angle-(centerAngle4-360.0f)))/90f));	
	            	}
	            	if(angle>=315f){
	            		vol[3]=(float)(1f - (Math.abs((angle-centerAngle4))/90f)*(Math.abs((angle-centerAngle4))/90f));
	            		vol[0]=(float)(1f - (Math.abs(((angle-360.0f)-centerAngle1))/90f)*(Math.abs(((angle-360.0f)-centerAngle1))/90f));
	            	}
	            	
	            	//descomentar si se quiere focalizar mas el sonido
	            	//for(int k=0;k<4;k++){
	            		//if(vol[k]<0.2f) vol[k]=0.0f;	
	            	//}
	            	
	            		mp1.setVolume(vol[0],vol[0]);
	            		mp4.setVolume(vol[3],vol[3]);
	            	
	            		mp2.setVolume(0.0f, 0.0f);
	            		mp3.setVolume(0.0f, 0.0f);
	            	
	            }
	            else {
	            
	            	for(int k=0;k<4;k++){
	            	
	            		if(Math.abs((angle-centerAng[k]))<90f) {
	            			vol[k]=(float)(1f - (Math.abs((angle-centerAng[k]))/90f)*(Math.abs((angle-centerAng[k]))/90f));
	            		} else{
	            			vol[k]=0.0f;	
	            		}
	            		
	            		//descomentar si se quiere focalizar mas el sonido
		            	//if(vol[k]<0.2f) vol[k]=0.0f;	
		            	
	            		//sndCircles[k].setVolume(vol[k], vol[k]);
	            	}
	            	
	            	mp1.setVolume(vol[0], vol[0]);
	            	mp2.setVolume(vol[1], vol[1]);
	            	mp3.setVolume(vol[2], vol[2]);
	            	mp4.setVolume(vol[3], vol[3]);

	            }
	                        
            //}
        //}
		*/
		
		//NEW SOUNDSCAPER
		
		
		//Log.v("angulo",Float.toString(angle));
		if(in_fade==false){
			for(int k=0;k<4;k++){
				
				if(Math.abs((angle-C[k]))<(A[k]+40)/2.0f) {
					vol[k]=(float)(1f - (Math.abs((angle-C[k]))/(A[k]+40)/2.0f)*(Math.abs((angle-C[k]))/(A[k]+40)/2.0f));
				} else{
					vol[k]=0.05f;	
				}
    		
    		//descomentar si se quiere focalizar mas el sonido
        	//if(vol[k]<0.15f) vol[k]=0.0f;	
        	
    		
			}
			if(released==false){
				if(mp1 != null && mp2 != null && mp3 != null && mp4 != null){					
					mp1.setVolume(vol[0], vol[0]);
					mp2.setVolume(vol[1], vol[1]);
					mp3.setVolume(vol[2], vol[2]);
					mp4.setVolume(vol[3], vol[3]);
				}
			}
			/*
			Log.v("vol1",Float.toString(vol[0]));
			Log.v("vol2",Float.toString(vol[1]));
			Log.v("vol3",Float.toString(vol[2]));
			Log.v("vol4",Float.toString(vol[3]));
			 */
		}
		
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
	}
	public void setFilee(String path1,String path2,String path3,String path4){
		this.mfile1=path1;
		this.mfile2=path2;
		this.mfile3=path3;
		this.mfile4=path4;
	}
	public String getFilee(){
		return mfile1+mfile2+mfile3+mfile4;
	}
	
	public void setAngles(float a1_in,float a1_out,float a2_in,float a2_out, float a3_in, float a3_out, float a4_in, float a4_out){
		this.a1_in=a1_in;
		this.a2_in=a2_in;
		this.a3_in=a3_in;
		this.a4_in=a4_in;
		this.a1_out=a1_out;
		this.a2_out=a2_out;
		this.a3_out=a3_out;
		this.a4_out=a4_out;
		
		//We must see ig any area is crossing 360�
		
		
		//calculo de areas y centros
		if(a1_in<a1_out) A[3]=Math.abs(a1_in-a1_out);
		if(a1_in>a1_out) A[3]=Math.abs(360-a1_in+a1_out);
		A[2]=Math.abs(a2_in-a2_out);
		A[1]=Math.abs(a3_in-a3_out);		
		A[0]=Math.abs(a4_in-a4_out);
		
		if(a1_in<a1_out) C[3]=Math.abs((a1_in+a1_out)/2.0f);
		if(a1_in>a1_out) C[3]=Math.abs((a1_in+a1_out+360.0f)/2.0f);
		C[2]=Math.abs((a2_in+a2_out)/2.0f);
		C[1]=Math.abs((a3_in+a3_out)/2.0f);
		C[0]=Math.abs((a4_in+a4_out)/2.0f);
		
		Log.v("A[0]",Float.toString(A[0]));
		Log.v("A[1]",Float.toString(A[1]));
		Log.v("A[2]",Float.toString(A[2]));
		Log.v("A[3]",Float.toString(A[3]));
		
		Log.v("C[0]",Float.toString(C[0]));
		Log.v("C[1]",Float.toString(C[1]));
		Log.v("C[2]",Float.toString(C[2]));
		Log.v("C[3]",Float.toString(C[3]));
		
	}
	
	//public boolean getFade() {
		//return this.fade;
	//}
	//public void setFade(boolean fade) {
		//this.fade=fade;
	//}
}