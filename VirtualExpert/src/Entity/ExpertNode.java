package Entity;

public class ExpertNode implements Node{
	String expertName;
	String unit;
	String expert_org;
	public ExpertNode(String expertName,String unit,String expert_org){
		this.expertName=expertName;
		this.unit=unit;
		this.expert_org=expert_org;
	}
	public String toString(){
		return this.unit+"--"+this.expert_org+"--"+this.expertName;
	}
	public int hashCode(){
		return this.expertName.hashCode();
	}
	public String getExpertName(){
		return this.expertName;
	}
	public String getUnit(){
		return this.unit;
	}
	public String getExpert_org(){
		return this.expert_org;
	}
}	
