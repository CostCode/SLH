package edu.cmu.cc.slh.formbean;

import org.mybeans.form.FormBean;

public class UpdateForm extends FormBean {
	private String action;
	private String hdnsec;
	private String hdncat;
	
	public String getAction() {
		return action;
	}
	public String getHdnsec() {
		return hdnsec;
	}
	public String getHdncat() {
		return hdncat;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public void setHdnsec(String hdnsec) {
		this.hdnsec = hdnsec;
	}
	public void setHdncat(String hdncat) {
		this.hdncat = hdncat;
	}
	
	
}
