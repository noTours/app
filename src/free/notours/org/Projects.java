package free.notours.org;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

//import com.noTours.escoitar.noToursCore;

//import com.noTours.escoitar.Menu.Decompress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import freeHttp.notours.org.Credits;
import freeHttp.notours.org.Informations;
import free.notours.org.R;




//ORIGINAL CLASS - LIST OF PROJECTS


public class Projects extends ListActivity {
       	
	ListView lista;
		
	private ArrayList<String> item = null;
	private ArrayList<String> projectsArray = null;
	
	public ArrayList datos;
	
	boolean fileExists=true;
	
	boolean mExternalStorageAvailable = false;
	boolean mExternalStorageWriteable = false;
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      //window properties, called before layout is loaded
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.projects);
        
		//check availability of the external storage	
		String state = Environment.getExternalStorageState();

		/*
		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		    AlertDialog alertDialog = new AlertDialog.Builder(Projects.this).create();
          	alertDialog.setTitle("your external sdcard is not available or you have not downloaded any walk to your phone!");
          	alertDialog.setMessage("Please mount it in your phone");
          	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
          	   public void onClick(DialogInterface dialog, int which) {
          	      // here you can add functions
          		   //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
          		 //Projects.this.finish();
          		 System.exit(1);
          	   }
          	});
          	alertDialog.setIcon(R.drawable.icon);
          	alertDialog.show();
		}
		
		if(mExternalStorageAvailable){
		//Read all the files in the directory
        item = new ArrayList<String>();
         
        File f = new File(Environment.getExternalStorageDirectory()+"/notours");
        File[] files = f.listFiles();
        
        
        datos = new ArrayList();
        if(files != null){
        	for(int i=0; i < files.length; i++){
                File file = files[i];
                //path.add(file.getPath());
                if(file.isDirectory()) {
                 //item.add(file.getName() + "/");
             	item.add(file.getName());
                	//Log.v("item", projectsArray[i]);
                	//datos.add(file.getName());
                 }
              }
        	//VISUALIZATION AND FULLFILLMENT OF THE LIST
            ArrayAdapter<String> adaptador;
            rellenaDatos();
            //lista = (ListView) findViewById(R.id.list);
            lista=getListView();
            
            adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, datos);
            lista.setAdapter(adaptador);
                   
            //LISTENER OF THE LIST
            lista.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                  // When clicked, show a toast with the TextView text
                  Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                      Toast.LENGTH_SHORT).show();
                
                //Check if directory and file exists   
          		File testDirectory = 
                      new File(Environment.getExternalStorageDirectory() + "/notours/" + (String)((TextView) view).getText() +"/sound");
                      if(!testDirectory.exists()){
                      	fileExists=false;
                      	AlertDialog alertDialog = new AlertDialog.Builder(Projects.this).create();
                      	alertDialog.setTitle("your sound folder doesn�t exist");
                      	alertDialog.setMessage("Please add your audio files to /sdcard/notours/YourProject/sound");
                      	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                      	   public void onClick(DialogInterface dialog, int which) {
                      	      // here you can add functions
                      		   //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                      		 //Projects.this.finish();
                      		 System.exit(1);
                      	   }
                      	});
                      	alertDialog.setIcon(R.drawable.icon);
                      	alertDialog.show();
                      }
                  File testDirectory2 = 
                          new File(Environment.getExternalStorageDirectory()+"/notours/" + (String)((TextView) view).getText() + "/soundscape.rss");
                          if(!testDirectory2.exists()){
                          	fileExists=false;
                          	AlertDialog alertDialog = new AlertDialog.Builder(Projects.this).create();
                          	alertDialog.setTitle("soundscape.rss doesn�t exist!");
                          	alertDialog.setMessage("Please edit your walk and store the result in /sdcard/notours");
                          	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                          	   public void onClick(DialogInterface dialog, int which) {
                          	      // here you can add functions
                          		   //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                          		   //Projects.this.finish();
                          		   System.exit(1);
                          	   }
                          	});
                          	alertDialog.setIcon(R.drawable.icon);
                          	alertDialog.show();
                          }
                  
                  
                  
                  
                //IF EVERYTHING IS OK GO TO THE MAP
                
               if(fileExists){           
                          
                //which soundwalk we will open
          		Bundle bundle = new Bundle();
          		bundle.putString("soundwalk",(String) ((TextView) view).getText());
          		
          		//LO QUE SE HACIA ANTES, MANDANDO UN intent
                Intent intent_back=new Intent();
                intent_back.setClass(Projects.this, noToursMap.class);
                intent_back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent_back.putExtras(bundle);
                startActivity(intent_back);
                  
          		//startActivity(new Intent("free.notours.org.noToursMap"));
                
                Projects.this.finish(); 
               }                          
            }
        });
        	
        	
        } else {
        	AlertDialog alertDialog = new AlertDialog.Builder(Projects.this).create();
          	alertDialog.setTitle("your sdcard is not ready for reading");
          	alertDialog.setMessage("Please mount your external sdcard in the phone");
          	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
          	   public void onClick(DialogInterface dialog, int which) {
          	      // here you can add functions
          		   //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
          		 //Projects.this.finish();
          		 System.exit(1);
          	   }
          	});
          	alertDialog.setIcon(R.drawable.icon);
          	alertDialog.show();
        }
        
		
		
	 } ///FINAL DEL IF DE mExternalStorageAvailable
	
		
		
		
	*/
		
		
		if(true){
		//Read all the files in the directory
        item = new ArrayList<String>();
         
        File f = new File("/storage/emulated/0/notours/");
        File[] files = f.listFiles();
        
        
        datos = new ArrayList();
        if(files != null){
        	for(int i=0; i < files.length; i++){
                File file = files[i];
                //path.add(file.getPath());
                if(file.isDirectory()) {
                 //item.add(file.getName() + "/");
             	item.add(file.getName());
                	//Log.v("item", projectsArray[i]);
                	//datos.add(file.getName());
                 }
              }
        	//VISUALIZATION AND FULLFILLMENT OF THE LIST
            ArrayAdapter<String> adaptador;
            rellenaDatos();
            //lista = (ListView) findViewById(R.id.list);
            lista=getListView();
            
            adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, datos);
            lista.setAdapter(adaptador);
                   
            //LISTENER OF THE LIST
            lista.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                  // When clicked, show a toast with the TextView text
                  Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                      Toast.LENGTH_SHORT).show();
                
                //Check if directory and file exists   
          		File testDirectory = 
                      new File("/storage/emulated/0/notours/" + (String)((TextView) view).getText() +"/sound");
                      if(!testDirectory.exists()){
                      	fileExists=false;
                      	AlertDialog alertDialog = new AlertDialog.Builder(Projects.this).create();
                      	alertDialog.setTitle("your sound folder doesn�t exist");
                      	alertDialog.setMessage("Please add your audio files to /storage/emulated/0/notours/YourProject/sound");
                      	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                      	   public void onClick(DialogInterface dialog, int which) {
                      	      // here you can add functions
                      		   //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                      		 //Projects.this.finish();
                      		 System.exit(1);
                      	   }
                      	});
                      	alertDialog.setIcon(R.drawable.icon);
                      	alertDialog.show();
                      }
                  File testDirectory2 = 
                          new File("/storage/emulated/0/notours/" + (String)((TextView) view).getText() + "/soundscape.rss");
                          if(!testDirectory2.exists()){
                          	fileExists=false;
                          	AlertDialog alertDialog = new AlertDialog.Builder(Projects.this).create();
                          	alertDialog.setTitle("soundscape.rss doesn�t exist!");
                          	alertDialog.setMessage("Please edit your walk and store the result in /storage/emulated/0/notours/");
                          	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                          	   public void onClick(DialogInterface dialog, int which) {
                          	      // here you can add functions
                          		   //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                          		   //Projects.this.finish();
                          		   System.exit(1);
                          	   }
                          	});
                          	alertDialog.setIcon(R.drawable.icon);
                          	alertDialog.show();
                          }
                  
                  
                  
                  
                //IF EVERYTHING IS OK GO TO THE MAP
                
               if(fileExists){           
                          
                //which soundwalk we will open
          		Bundle bundle = new Bundle();
          		bundle.putString("soundwalk",(String) ((TextView) view).getText());
          		
          		//LO QUE SE HACIA ANTES, MANDANDO UN intent
                Intent intent_back=new Intent();
                intent_back.setClass(Projects.this, noToursMap.class);
                intent_back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent_back.putExtras(bundle);
                startActivity(intent_back);
                  
          		//startActivity(new Intent("free.notours.org.noToursMap"));
                
                Projects.this.finish(); 
               }                          
            }
        });
        	
        	
        } else {
        	AlertDialog alertDialog = new AlertDialog.Builder(Projects.this).create();
          	alertDialog.setTitle("your sdcard is not ready for reading");
          	alertDialog.setMessage("Please mount your external sdcard in the phone");
          	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
          	   public void onClick(DialogInterface dialog, int which) {
          	      // here you can add functions
          		   //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
          		 //Projects.this.finish();
          		 System.exit(1);
          	   }
          	});
          	alertDialog.setIcon(R.drawable.icon);
          	alertDialog.show();
        }
        
		
		
	 } ///FINAL DEL IF DE mExternalStorageAvailable
		
		
		
		
		
		
		
    }
    
    private void rellenaDatos(){
    	for(int i=0;i<item.size();i++){
    		datos.add(item.get(i));
    	}
    	//datos.add("hola");
    }

    
}



//if you wanna create a custom app

//
//public class Projects extends Activity {
//
//	ImageButton imgButton1;
//	ImageButton imgButton2;
//	ImageButton imgButton3;
//	ImageButton imgButton4;
//	
//	int soundwalk=1;
//	boolean fileExists=true;
//	
//	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
//    public static final int DIALOG_UNZIP_PROGRESS = 1;
//    private Button proyecto159, laberinto, parking_2, dePoetica_Espacial, noToursCimadevilla, danzarOMorir, presenciaSonora, spaceTrack, lesMotsDesAnimoux;
//    private ProgressDialog mProgressDialog;
//    String mURL;
//    
//    ListView lista;
//	
//	private ArrayList<String> item = null;
//	private ArrayList<String> projectsArray = null;
//	
//	public ArrayList datos;
//	
//	boolean mExternalStorageAvailable = false;
//	boolean mExternalStorageWriteable = false;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//		WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//				
//		setContentView(R.layout.menucontinent);
//		
//		//detect actions on buttons
//		
//		//Parcour
//		imgButton1 = (ImageButton) findViewById(R.id.commencez);
//        imgButton1.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            	Log.w("notours","clickkkkkkkk");
//            	
//                
//                /////////////////////////////////////////////
//        		//check availability of the external storage	
//        		String state = Environment.getExternalStorageState();
//
//        		if (Environment.MEDIA_MOUNTED.equals(state)) {
//        		    // We can read and write the media
//        		    mExternalStorageAvailable = mExternalStorageWriteable = true;
//        		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
//        		    // We can only read the media
//        		    mExternalStorageAvailable = true;
//        		    mExternalStorageWriteable = false;
//        		} else {
//        		    // Something else is wrong. It may be one of many other states, but all we need
//        		    //  to know is we can neither read nor write
//        		    mExternalStorageAvailable = mExternalStorageWriteable = false;
//        		    AlertDialog alertDialog = new AlertDialog.Builder(Projects.this).create();
//                  	alertDialog.setTitle("Your SDcard is not mounted or you have not downloaded the contents of the walk!");
//                  	alertDialog.setMessage("Please donwload the contents at the previous menu!");
//                  	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                  	   public void onClick(DialogInterface dialog, int which) {
//                  	      // here you can add functions
//                  		   //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
//                  		 //Projects.this.finish();
//                  		 System.exit(1);
//                  	   }
//                  	});
//                  	alertDialog.setIcon(R.drawable.icon);
//                  	alertDialog.show();
//        		}
//        		
//        		if(mExternalStorageAvailable){
//        		//Read all the files in the directory
//                item = new ArrayList<String>();
//                 
//                File f = new File(Environment.getExternalStorageDirectory()+"/notours");
//                File[] files = f.listFiles();
//                
//                
//                datos = new ArrayList();
//                if(files != null){
//                	for(int i=0; i < files.length; i++){
//                        File file = files[i];
//                        //path.add(file.getPath());
//                        if(file.isDirectory()) {
//                         //item.add(file.getName() + "/");
//                     	item.add(file.getName());
//                        	//Log.v("item", projectsArray[i]);
//                        	//datos.add(file.getName());
//                         }
//                      }
//                	
//                           
//                    //LISTENER OF THE LIST
//                    //lista.setOnItemClickListener(new OnItemClickListener() {
//                      //  public void onItemClick(AdapterView<?> parent, View view,
//                        //    int position, long id) {
//                          // When clicked, show a toast with the TextView text
//                          //Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
//                          //    Toast.LENGTH_SHORT).show();
//                        
//                        //Check if directory and file exists   
//                  		File testDirectory = 
//                              new File(Environment.getExternalStorageDirectory() + "/notours/" + "Written-in-Water_Portrait-of-a-Town.1" +"/sound");
//                              if(!testDirectory.exists()){
//                              	fileExists=false;
//                              	AlertDialog alertDialog = new AlertDialog.Builder(Projects.this).create();
//                              	alertDialog.setTitle("You have not correctly downloaded the contents");
//                              	alertDialog.setMessage("Please download them again");
//                              	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                              	   public void onClick(DialogInterface dialog, int which) {
//                              	      // here you can add functions
//                              		   //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
//                              		 //Projects.this.finish();
//                              		 System.exit(1);
//                              	   }
//                              	});
//                              	alertDialog.setIcon(R.drawable.icon);
//                              	alertDialog.show();
//                              }
//                          File testDirectory2 = 
//                                  new File(Environment.getExternalStorageDirectory()+"/notours/" + "Written-in-Water_Portrait-of-a-Town.1" + "/soundscape.rss");
//                                  if(!testDirectory2.exists()){
//                                  	fileExists=false;
//                                  	AlertDialog alertDialog = new AlertDialog.Builder(Projects.this).create();
//                                  	alertDialog.setTitle("soundscape.rss does not exist!");
//                                  	alertDialog.setMessage("Please download again the contents!");
//                                  	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                                  	   public void onClick(DialogInterface dialog, int which) {
//                                  	      // here you can add functions
//                                  		   //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
//                                  		   //Projects.this.finish();
//                                  		   System.exit(1);
//                                  	   }
//                                  	});
//                                  	alertDialog.setIcon(R.drawable.icon);
//                                  	alertDialog.show();
//                                  }
//                          
//                          
//                          
//                          
//                        //IF EVERYTHING IS OK GO TO THE MAP
//                        
//                       if(fileExists){           
//                                  
//                    	 //which soundwalk we will open
//                     		Bundle bundle = new Bundle();
//                     		bundle.putString("soundwalk","Written-in-Water_Portrait-of-a-Town.1");
//                     		
//                     		//LO QUE SE HACIA ANTES, MANDANDO UN intent
//                           Intent intent_back=new Intent();
//                           intent_back.setClass(Projects.this, noToursMap.class);
//                           intent_back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                           intent_back.putExtras(bundle);
//                           startActivity(intent_back);
//                             
//                     		//startActivity(new Intent("free.notours.org.noToursMap"));
//                           
//                           Projects.this.finish();
//                       }                          
//                        //}
//                      //});
//                	
//                	
//                } else {
//                	AlertDialog alertDialog = new AlertDialog.Builder(Projects.this).create();
//                  	alertDialog.setTitle("Error reading your SDcard");
//                  	alertDialog.setMessage("Please, be sure that your SDcard is correctly mounted and try again");
//                  	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                  	   public void onClick(DialogInterface dialog, int which) {
//                  	      // here you can add functions
//                  		   //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
//                  		 //Projects.this.finish();
//                  		 System.exit(1);
//                  	   }
//                  	});
//                  	alertDialog.setIcon(R.drawable.icon);
//                  	alertDialog.show();
//                }
//                
//        		
//        		
//        	 }
//                
//                
//                
//            }  //fin del primer boton
//        });
//        
//        //Download
//        imgButton2 = (ImageButton) findViewById(R.id.telecharger);
//        imgButton2.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            	Log.w("notours", "telecharger");
//         	
//            	startDownload("Written-in-Water.zip");
//            	
//            }
//        });
//        
//        
//        //Infos
//        imgButton3 = (ImageButton) findViewById(R.id.informations);
//        imgButton3.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            	Log.w("notours", "Informations");
//            	
//            	Intent intent_back3=new Intent();
//                intent_back3.setClass(Projects.this, Informations.class);
//                intent_back3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                //intent_back2.putExtras(bundle);
//                startActivity(intent_back3);
//            }
//        });
//        
//        //Credits
//        imgButton4 = (ImageButton) findViewById(R.id.credits);
//        imgButton4.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            	Log.w("notours", "credits");
//            	
//            	Intent intent_back4=new Intent();
//                intent_back4.setClass(Projects.this, Credits.class);
//                intent_back4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                //intent_back2.putExtras(bundle);
//                startActivity(intent_back4);
//               
//            }
//        });
//	}
//
//	/*
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu_continent, menu);
//		return true;
//	}
//	*/
//	
//	private void startDownload(String url) {
//
//		new DownloadFileAsync().execute(url);
//	}
//
//	@Override
//	protected Dialog onCreateDialog(int id) {
//	    switch (id) {
//	        case DIALOG_DOWNLOAD_PROGRESS:
//	            mProgressDialog = new ProgressDialog(this);
//	            mProgressDialog.setMessage("Downloading..");
//	            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//	            mProgressDialog.setCancelable(false);
//	            mProgressDialog.show();
//	            break;
//	        case DIALOG_UNZIP_PROGRESS:
//	        	mProgressDialog = new ProgressDialog(this);
//	        	mProgressDialog.setMessage("Unzipping.. Please, wait until this message dissapears. It can take a few minutes!");
//	        	//mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//	        	mProgressDialog.setCancelable(false);
//	        	mProgressDialog.show();
//	            break;             
//	        default:
//	            return null;
//	    }
//	    return mProgressDialog;
//	}
//	
//	 
//    class DownloadFileAsync extends AsyncTask<String, String, String> {
//        
//    	String path;
//    	String urlPath="http://benjaminmawson.com/NoTours_Project/Written-in-Water/";
//    	
//    	/*
//        public DownloadFileAsync(String zipPath) { 
//        	Log.v("download","constructor de FileAsync");
//            path=zipPath;
//          }
//    	*/
//    	
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            showDialog(DIALOG_DOWNLOAD_PROGRESS);
//        }
//
//        @Override
//        protected String doInBackground(String... aurl) {
//            int count;
//
//            try {
//                URL url = new URL("http://benjaminmawson.com/NoTours_Project/Written-in-Water/" + aurl[0]);
//                URLConnection conexion = url.openConnection();
//                conexion.connect();
//
//                int lenghtOfFile = conexion.getContentLength();
//                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
//
//                InputStream input = new BufferedInputStream(url.openStream());
//                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + aurl[0]);
//
//                byte data[] = new byte[1024];
//
//                long total = 0;
//
//                while ((count = input.read(data)) != -1) {
//                    total += count;
//                    publishProgress(""+(int)((total*100)/lenghtOfFile));
//                    output.write(data, 0, count);
//                }
//
//                output.flush();
//                output.close();
//                input.close();
//                
//                //dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
//                
//                //now decompress
//                String zipFile = Environment.getExternalStorageDirectory() + "/"+ aurl[0]; 
//            	String unzipLocation = Environment.getExternalStorageDirectory() + "/notours/"; 
//            	Decompress d = new Decompress(zipFile, unzipLocation); 
//            	d.execute(zipFile, unzipLocation);
//                
//                
//            } catch (Exception e) {}
//            return null;
//
//        }
//        protected void onProgressUpdate(String... progress) {
//             //Log.d("ANDRO_ASYNC",progress[0]);
//             mProgressDialog.setProgress(Integer.parseInt(progress[0]));
//        }
//
//        @Override
//        protected void onPostExecute(String unused) {
//            
//        	dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
//        	removeDialog(DIALOG_DOWNLOAD_PROGRESS);
//        	showDialog(DIALOG_UNZIP_PROGRESS);
//        }
//    }
//    
//    /** 
//     * 
//     * @author jon 
//     */ 
//    public class Decompress extends AsyncTask<String, String, String> { 
//      private String _zipFile; 
//      private String _location; 
//      
//      @Override
//      protected void onPreExecute() {
//          super.onPreExecute();
//          //showDialog(DIALOG_DOWNLOAD_PROGRESS);
//      }
//      
//      protected void onProgressUpdate(String... progress) {
//          //Log.d("ANDRO_ASYNC",progress[0]);
//          mProgressDialog.setProgress(Integer.parseInt(progress[0]));
//     }
//     
//      public Decompress(String zipFile, String location) { 
//    	Log.v("download","constructor de Decompress");
//        _zipFile = zipFile; 
//        _location = location; 
//     
//        _dirChecker(""); 
//      } 
//      
//      private void _dirChecker(String dir) { 
//        File f = new File(_location + dir); 
//     
//        if(!f.isDirectory()) { 
//          f.mkdirs(); 
//        } 
//      }
//
//    @Override
//    protected String doInBackground(String... arg0) {
//    	// TODO Auto-generated method stub
//    	Log.v("download","entro en doInBackground");
//    	try  { 
//    		int count;
//    		
//            FileInputStream fin = new FileInputStream(_zipFile); 
//            ZipInputStream zin = new ZipInputStream(fin); 
//            ZipEntry ze = null; 
//            long total = 0;
//            
//            while ((ze = zin.getNextEntry()) != null) { 
//              Log.v("Decompress", "Unzipping " + ze.getName()); 
//       
//              if(ze.isDirectory()) { 
//                _dirChecker(ze.getName()); 
//              } else { 
//              	byte[] buffer = new byte[1024];
//              	int length;
//                FileOutputStream fout = new FileOutputStream(_location + ze.getName()); 
//                //for (int c = zin.read(); c != -1; c = zin.read()) { 
//                //  fout.write(c); 
//                //}
//                
//                // replace for loop with:
//                while ((length = zin.read(buffer))>0) {
//                	total += length;
//                    publishProgress(""+(int)((total*100)/length));
//                    fout.write(buffer, 0, length);
//                }
//       
//                zin.closeEntry(); 
//                fout.close(); 
//              } 
//               
//            } 
//            zin.close(); 
//            Log.e("Decompress", "end of unzip files"); 
//          } catch(Exception e) { 
//            Log.e("Decompress", "unzip", e); 
//          } 
//    	
//    	
//    	return null;
//    } 
//    
//    @Override
//    protected void onPostExecute(String unused) {
//        dismissDialog(DIALOG_UNZIP_PROGRESS);
//        removeDialog(DIALOG_UNZIP_PROGRESS);
//        //dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
//    	
//    }
//    
//    }
//	
//	
//	
//	
//}///fin de projects Activity
//
//
//
