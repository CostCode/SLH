//Name: Shubhansha Agrawal
//Andrew Id: shubhana
//Course Id: 15-637
//Date: 25th February, 13

package edu.cmu.cc.slh.formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

/**
 * @author shubhanshaagarwal
 *
 */

public class HomeForm extends FormBean {
	
	private String action;
	private String uname;
	private String upass;
		
	public String getAction() {
		return action;
	}

	public String getUname() {
		return uname;
	}

	public String getUpass() {
		return upass;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public void setUpass(String upass) {
		this.upass = upass;
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();
		if(action!=null){
			
		}
		return errors;
	}
}