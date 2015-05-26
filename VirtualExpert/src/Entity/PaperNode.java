package Entity;

import java.util.HashMap;

public class PaperNode implements Node {
	String expertName;
	String[] units;
	String expert_org;
	String abs;
	String [] author_cn;
	HashMap<String,Double> mainkeys;
	String journal_cn;
	String app_date;
	String title;
	Integer textId;

	
	public PaperNode(String expertName,String[] units,
			String expert_org,String abs,String author_cn[],HashMap<String,Double> mainKeys,
			String journal_cn,String app_date,String title,Integer textId){
		this.expertName=expertName;
		this.units=units;
		this.expert_org=expert_org;
		this.app_date=app_date;
		this.abs=abs;
		this.author_cn=author_cn;
		this.mainkeys=mainKeys;
		this.journal_cn=journal_cn;
		this.title=title;	
		this.textId=textId;
	}
	public PaperNode() {
		// TODO Auto-generated constructor stub
	}
	public String toString(){
		return this.units+"--"+this.expert_org+"--"+this.expertName;
	}
	public int hashCode(){
		return this.textId;
	}
	public String getExpertName(){
		return this.expertName;
	}
	public String [] getUnits(){
		return this.units;
	}
	public String getExpert_org(){
		return this.expert_org;
	}
	public String getAbs() {
		return abs;
	}
	public void setAbs(String abs) {
		this.abs = abs;
	}
	public String[] getAuthor_cn() {
		return author_cn;
	}
	public void setAuthor_cn(String[] author_cn) {
		this.author_cn = author_cn;
	}
	public HashMap<String, Double> getMainkeys() {
		return mainkeys;
	}
	public void setMainkeys(HashMap<String, Double> mainkeys) {
		this.mainkeys = mainkeys;
	}
	public String getJournal_cn() {
		return journal_cn;
	}
	public void setJournal_cn(String journal_cn) {
		this.journal_cn = journal_cn;
	}
	public String getApp_date() {
		return app_date;
	}
	public void setApp_date(String app_date) {
		this.app_date = app_date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getTextId() {
		return textId;
	}
	public void setTextId(Integer textId) {
		this.textId = textId;
	}
}
