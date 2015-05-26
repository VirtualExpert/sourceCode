package Entity;

import java.util.ArrayList;

public class ExpertNode implements Node{
	String expertName;
	String [] units;
	String expert_org;
	public ExpertNode(String expertName,String[] units,String expert_org){
		this.expertName=expertName;
		this.units=units;
		this.expert_org=expert_org;
	}
	public String toString(){
		return this.units+"--"+this.expert_org+"--"+this.expertName;
	}
	public int hashCode(){
		//¸øtextIdÁô¿Õ¼ä
		return this.expertName.hashCode();
	}
	public String getExpertName(){
		return this.expertName;
	}
	public String[] getUnits(){
		return this.units;
	}
	public String getExpert_org(){
		return this.expert_org;
	}
}	
