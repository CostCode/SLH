/**
 * 
 */
package edu.cmu.cc.slh.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



/**
 * @author shubhanshaagarwal
 *
 */
public class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void init() throws ServletException{
		Action.add(new HomeAction());
		Action.add(new CurrentPlanAction());
		Action.add(new UpdatePlanAction());
		Action.add(new LogoutAction());
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String nextPage = performTheAction(request);
        sendToNextPage(nextPage,request,response);
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 doGet(request, response);
    }
    
    private String performTheAction(HttpServletRequest request) {
        String servletPath = request.getServletPath();
   
        String action = getActionName(servletPath);
        return Action.perform(action,request);
    }
    
    private String getActionName(String path) {
        int slash = path.lastIndexOf('/');
        return path.substring(slash+1);
    }

    private void sendToNextPage(String nextPage, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	if (nextPage == null) {
    		response.sendError(HttpServletResponse.SC_NOT_FOUND,request.getServletPath());
    		return;
    	}
    	
    	if (nextPage.contains(".do")) {
			response.sendRedirect(nextPage);
			return;
    	}
    	
    	if (nextPage.contains(".jsp")) {
    		//RequestDispatcher d = request.getRequestDispatcher("WEB-INF/" + nextPage);
    		RequestDispatcher d = request.getRequestDispatcher(nextPage);
    		if(d!=null){
    			d.forward(request,response);
    		}
	   		return;
    	}
    	
    	throw new ServletException(Controller.class.getName()+".sendToNextPage(\"" + nextPage + "\"): invalid extension.");
    }
		
}
