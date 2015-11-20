package free.notours.org;


public class Sequencer  {
	
	int level;
	
	public Sequencer(){
		this.level=1;
	}
	
	public int getLevel(){
		return this.level;
	}
	public void setLevel(int lvl){
		this.level=lvl;
	}
	public void incrementLevel(){
		this.level=this.level+1;
	}
}