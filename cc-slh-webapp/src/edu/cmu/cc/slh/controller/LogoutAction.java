package edu.cmu.cc.slh.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LogoutAction extends Action {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "logout.do";
	}

	@Override
	public String perform(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors",errors);
		
		HttpSession session = request.getSession();
		session.setAttribute("auser",null);
		
		return "home.jsp";
	}

}
