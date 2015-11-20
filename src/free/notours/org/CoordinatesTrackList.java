package free.notours.org;

import free.notours.org.R;
import java.util.ArrayList;

/** Contains getter and setter method for varialbles  */
public class CoordinatesTrackList {

	/** Variables */
	private ArrayList<String> title = new ArrayList<String>();
	
	private ArrayList<String> scpTitle = new ArrayList<String>();
	private ArrayList<String> audioFile = new ArrayList<String>();
	private ArrayList<String> pointCoordinates = new ArrayList<String>();
	private ArrayList<String> scpCoordinates = new ArrayList<String>();
	private ArrayList<String> attributes = new ArrayList<String>();
	private ArrayList<String> scpAttributes = new ArrayList<String>();
	private ArrayList<String> file = new ArrayList<String>();
	private ArrayList<String> scpFile = new ArrayList<String>();
	private ArrayList<String> volume = new ArrayList<String>();
	private ArrayList<String> scpVolume = new ArrayList<String>();
	private ArrayList<String> angle = new ArrayList<String>();
	private ArrayList<String> scpAngle = new ArrayList<String>();
	private ArrayList<String> level = new ArrayList<String>();
	private ArrayList<String> milestone = new ArrayList<String>();
	private ArrayList<String> folder = new ArrayList<String>();
	
	private ArrayList<String> type = new ArrayList<String>();
	private ArrayList<String> typeFile = new ArrayList<String>();
	private ArrayList<String> typeMilestone = new ArrayList<String>();

	private ArrayList<String> sticky = new ArrayList<String>();
	private ArrayList<String> link = new ArrayList<String>();
	private ArrayList<String> track = new ArrayList<String>();
	private ArrayList<String> fullCoordinates = new ArrayList<String>();
	
	
	/** In Setter method default it will return arraylist 
	 *  change that to add  */
	
	public ArrayList<String> getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title.add(title);
	}
	
	public ArrayList<String> getSticky() {
		return sticky;
	}

	public void setSticky(String stick) {
		this.sticky.add(stick);
	}
	
	public ArrayList<String> getScpTitle() {
		return scpTitle;
	}

	public void setScpTitle(String title) {
		this.scpTitle.add(title);
	}
	
	public ArrayList<String> getPointCoordinates() {
		return pointCoordinates;
	}

	public void setPointCoordinates(String coordinates) {
		this.pointCoordinates.add(coordinates);
	}
	
	public ArrayList<String> getScpCoordinates() {
		return scpCoordinates;
	}

	public void setScpCoordinates(String coordinates) {
		this.scpCoordinates.add(coordinates);
	}

	public ArrayList<String> getPointAttributes() {
		return attributes;
	}

	public void setPointAttributes(String attributes) {
		this.attributes.add(attributes);
	}
	
	public void setTypeMilestone() {
		this.typeMilestone.add("milestone");
	}
	public ArrayList<String> getTypeMilestone() {
		return typeMilestone;
	}
	
	public void setPointMilestone(String attributes) {
		this.milestone.add(attributes);
	}
	
	public ArrayList<String> getPointMilestone() {
		return milestone;
	}
	
	public void setPointFolder(String attributes) {
		this.folder.add(attributes);
	}
	
	public ArrayList<String> getPointFolder() {
		return folder;
	}
	
	public ArrayList<String> getScpAttributes() {
		return scpAttributes;
	}

	public void setScpAttributes(String attributes) {
		this.scpAttributes.add(attributes);
	}
	
	public ArrayList<String> getPointFile() {
		return file;
	}

	public void setPointFile(String file) {
		this.file.add(file);
	}
	
	public ArrayList<String> getScpFile() {
		return scpFile;
	}

	public void setScpFile(String file) {
		this.scpFile.add(file);
	}
	
	public ArrayList<String> getPointVolume() {
		return volume;
	}

	public void setPointVolume(String volume) {
		this.volume.add(volume);
	}
	
	public void setPointLevel(String level) {
		this.level.add(level);
	}
	
	public ArrayList<String> getTrack(){
		return this.track;
	}
	
	public void setTrack(String mytrack) {
		this.track.add(mytrack);
	}
	
	public ArrayList<String> getPointLevel(){
		return this.level;
	}
	
	public ArrayList<String> getScpVolume() {
		return scpVolume;
	}

	public void setScpVolume(String volume) {
		this.scpVolume.add(volume);
	}
	
	public ArrayList<String> getPointAngle() {
		return angle;
	}

	public void setPointAngle(String angle) {
		this.angle.add(angle);
	}
	
	public ArrayList<String> getScpAngle() {
		return scpAngle;
	}

	public void setScpAngle(String angle) {
		this.scpAngle.add(angle);
	}
	
	public ArrayList<String> getLink() {
		return link;
	}

	public void setLink(String mylink) {
		this.link.add(mylink);
	}
	
	public ArrayList<String> getType() {
		return type;
	}

	public void setTypeSoundPoint() {
		this.type.add("point");
	}
	
	public void setTypeSoundScape() {
		this.type.add("soundscape");
	}
	
	public void setTypeHasFile() {
		this.typeFile.add("file");
	}
	
	public void setTypeHasFolder() {
		this.typeFile.add("folder");
	}
	
	public ArrayList<String> getTypeFile() {
		return typeFile;
	}
	public void setCoordinates(String coordinates) {
		this.fullCoordinates.add(coordinates);
	}
	
	public ArrayList<String> getCoordinates() {
		return fullCoordinates;
	}
}