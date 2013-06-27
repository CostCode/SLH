package edu.cmu.cc.slh.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import edu.cmu.cc.slh.dao.TblManagersDao;
import edu.cmu.cc.slh.dto.TblManagers;
import edu.cmu.cc.slh.exceptions.TblManagersDaoException;
import edu.cmu.cc.slh.factory.TblManagersDaoFactory;
import edu.cmu.cc.slh.formbean.HomeForm;



public class HomeAction extends Action {
	private FormBeanFactory<HomeForm> formBeanFactory = FormBeanFactory.getInstance(HomeForm.class);
	
	private TblManagersDao userDAO;
	
	public HomeAction(){
		userDAO= TblManagersDaoFactory.create();
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "home.do";
	}

	@Override
	public String perform(HttpServletRequest request){
		// TODO Auto-generated method stub
		
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors",errors);

		HomeForm hform;
		
			try {
				hform = formBeanFactory.create(request);
				
				request.setAttribute("form",hform);

				errors.addAll(hform.getValidationErrors());
				if (errors.size() != 0) {
					return "home.jsp";
				}
				
				TblManagers users= null;
				
				users = userDAO.findWhereUsernamePassEquals(hform.getUname(), hform.getUpass());
				if(users==null){
					errors.add("Invalid Username or Password!");
					return "home.jsp";
				}
				else{
					HttpSession session = request.getSession();
					session.setAttribute("auser",users.getUid());
					return "cplan.do";
				}
				
			} catch (FormBeanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TblManagersDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return "home.jsp";
	}

}
