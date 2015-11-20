package free.notours.org;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


import android.util.Log;
import free.notours.org.R;

public class TrackXMLHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	String temporalCircleValue = null;
	String temporalTitle = null;
	public static CoordinatesTrackList sitesList = null;
	
	Boolean in_soundpoint=false;
	Boolean in_soundscape=false;
	Boolean is_folder=false;
	Boolean is_file=false;
	Boolean is_milestone=false;

	public static CoordinatesTrackList getSitesList() {
		return sitesList;
	}

	public static void setSitesList(SitesList sitesList) {
		MyXMLHandler.sitesList = sitesList;
	}

	/** Called when tag starts ( ex:- <name>AndroidPeople</name> 
	 * -- <name> )*/
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("channel"))
		{
			/** Start */ 
			sitesList = new CoordinatesTrackList();
			 //Log.v("XMLPARRRRRRRRRRRSER",localName );
		}   else if (localName.equalsIgnoreCase("soundpoint")){
			in_soundpoint=true;
			in_soundscape=false;
			sitesList.setTypeSoundPoint();
			sitesList.setTitle(temporalTitle);
			sitesList.setPointCoordinates(currentValue);
			//Log.v("XMLPARRRR____SOUNDPOIINNTTTT",currentValue);
			
		}   else if (localName.equalsIgnoreCase("file")){
			is_file=true;
			is_folder=false;
			sitesList.setTypeHasFile();
			//sitesList.setPointFile2(currentValue);
			//Log.v("XMLPARRRR____IS A FILEEEEEE",currentValue);
		}
		
		else if (localName.equalsIgnoreCase("folder")){
			is_file=false;
			is_folder=true;
			sitesList.setTypeHasFolder();
			//sitesList.setPointFile2(currentValue);
			//Log.v("XMLPARRRR____IS A FOLDEERRRR",currentValue);
			
		} else if (localName.equalsIgnoreCase("soundscape")){
			in_soundscape=true;
			in_soundpoint=false;
			sitesList.setTypeSoundScape();
			sitesList.setScpTitle(temporalTitle);
			sitesList.setScpCoordinates(currentValue);
			//Log.v("XMLPARRRR____SOUNDSCAPEEEEEEE",currentValue);
		
		}
			

	}

	/** Called when tag closing ( ex:- <name>AndroidPeople</name> 
	 * -- </name> )*/
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currentElement = false;
		boolean stickyExists=false;

		//Log.v("XMLPARRRR_____________",currentValue);
		
		/** set value */
		if (localName.equalsIgnoreCase("coordinates"))
		{
			sitesList.setCoordinates(currentValue);
			stickyExists=true;
			Log.v("Coooooooordinatessssss",currentValue);
			
		}
		if (localName.equalsIgnoreCase("sticky"))
		{
			sitesList.setSticky(currentValue);
			stickyExists=true;
			//Log.v("STIIIIIIIICKKKKKKYYYYYY",currentValue);
			
		} else if (localName.equalsIgnoreCase("link")){
			//sitesList.setTitle(currentValue);
			sitesList.setLink(currentValue);
			//Log.v("XMLPARRRR____tiiitle",currentValue);
			
		} else if  (localName.equalsIgnoreCase("guid")){
			//sitesList.setTitle(currentValue);
			
			//Log.v("XMLPARRRR____tiiitle",currentValue);	
			
		} else if (localName.equalsIgnoreCase("title")){
			//sitesList.setTitle(currentValue);
			temporalTitle=currentValue;
			//Log.v("XMLPARRRR____tiiitle",currentValue);
			
		} else if (localName.equalsIgnoreCase("circle")){
			//save the value and use it when we know the type
			temporalCircleValue=currentValue;
			Log.v("XMLPARRRR____CIRCLEEEEE",temporalCircleValue);
			//Log.v("XMLPARRRR____CIIIIIIRCLLLEEE",currentValue);
		
		} else if (localName.equalsIgnoreCase("attributes")&& in_soundpoint){
			sitesList.setPointAttributes(currentValue);
			//Log.v("XMLPARRRR____ATRIBUUUTES",currentValue);
			
		} else if (localName.equalsIgnoreCase("level")&& in_soundpoint){ //LEVEL
			sitesList.setPointLevel(currentValue);
			//Log.v("XMLPARRRR____VOLUMEEEE",currentValue);		
		
		} else if (localName.equalsIgnoreCase("track")&& in_soundpoint){ //TRACK
			sitesList.setTrack(currentValue);
			//Log.v("XMLPARRRR____VOLUMEEEE",currentValue);		
		
		} else if (localName.equalsIgnoreCase("milestone")&& in_soundpoint){ //MILESTONE
			sitesList.setPointMilestone(currentValue);
			//Log.v("XMLPARRRR____MILESTONE",currentValue);	
		
		} else if(is_file && in_soundpoint && localName.equalsIgnoreCase("file")) {							
			//FILE
			sitesList.setPointFile(currentValue);
			sitesList.setPointFolder("none"); //to keep the size in all arrays
			Log.v("XMLPARRRR____FILEEEEEE_circleeeeee",currentValue);
			
		} else if (localName.equalsIgnoreCase("folder")&& in_soundpoint && is_folder){ //FOLDER
			sitesList.setPointFolder(currentValue);
			sitesList.setPointFile("none"); //to keep the size in all arrays
			//Log.v("XMLPARRRR____FOOOOLDER",currentValue);
		
		} else if (localName.equalsIgnoreCase("volume")&& in_soundpoint){
			sitesList.setPointVolume(currentValue);
			//Log.v("XMLPARRRR____VOLUMEEEE",currentValue);
		
		} else if (localName.equalsIgnoreCase("angle")&& in_soundpoint){
			sitesList.setPointAngle(currentValue);
			//Log.v("XMLPARRRR____ANGLEEEEEE",currentValue);		
		
		} else if (localName.equalsIgnoreCase("attributes")&& in_soundscape){
			sitesList.setScpAttributes(currentValue);
			//Log.v("XMLPARRRR____ATTRIBUUUTES___SCPPPP",currentValue);
		
		} else if (localName.equalsIgnoreCase("file")&& in_soundscape){
			sitesList.setScpFile(currentValue);
			//Log.v("XMLPARRRR____FILEEEEEE___SCPPPP",currentValue);
		
		} else if (localName.equalsIgnoreCase("volume")&& in_soundscape){
			sitesList.setScpVolume(currentValue);
			//Log.v("XMLPARRRR____VOLUMEEEE___SCPPPP",currentValue);
		
		} else if (localName.equalsIgnoreCase("angle")&& in_soundscape){
			sitesList.setScpAngle(currentValue);
			//Log.v("XMLPARRRR____ANGLEEEEEE___SCPPPP",currentValue);		
		} 
		
		//if(stickyExists==false){
		//	sitesList.setSticky("0");
		//}
		
		
	}    
		

	

	/** Called to get tag characters ( ex:- <name>AndroidPeople</name> 
	 * -- to get AndroidPeople Character ) */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (currentElement) {
			currentValue = new String(ch, start, length);
			currentElement = false;
		}

	}

}