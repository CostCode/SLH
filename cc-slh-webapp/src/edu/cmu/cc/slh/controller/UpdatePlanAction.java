package edu.cmu.cc.slh.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import edu.cmu.cc.slh.dao.TblCategoriesDao;
import edu.cmu.cc.slh.dao.TblFloormapsDao;
import edu.cmu.cc.slh.dao.TblHotspotsDao;
import edu.cmu.cc.slh.dao.TblManagersDao;
import edu.cmu.cc.slh.dao.TblWarehouseDao;
import edu.cmu.cc.slh.dto.TblCategories;
import edu.cmu.cc.slh.dto.TblFloormaps;
import edu.cmu.cc.slh.dto.TblHotspots;
import edu.cmu.cc.slh.dto.TblManagers;
import edu.cmu.cc.slh.dto.TblWarehouse;
import edu.cmu.cc.slh.exceptions.TblCategoriesDaoException;
import edu.cmu.cc.slh.exceptions.TblFloormapsDaoException;
import edu.cmu.cc.slh.exceptions.TblHotspotsDaoException;
import edu.cmu.cc.slh.exceptions.TblManagersDaoException;
import edu.cmu.cc.slh.exceptions.TblWarehouseDaoException;
import edu.cmu.cc.slh.factory.TblCategoriesDaoFactory;
import edu.cmu.cc.slh.factory.TblFloormapsDaoFactory;
import edu.cmu.cc.slh.factory.TblHotspotsDaoFactory;
import edu.cmu.cc.slh.factory.TblManagersDaoFactory;
import edu.cmu.cc.slh.factory.TblWarehouseDaoFactory;
import edu.cmu.cc.slh.formbean.UpdateForm;

public class UpdatePlanAction extends Action{
	private FormBeanFactory<UpdateForm> formBeanFactory = FormBeanFactory.getInstance(UpdateForm.class);
	
	private TblManagersDao userDAO;
	private TblWarehouseDao warDAO;
	private TblFloormapsDao mapDAO;
	private TblHotspotsDao hspotsDAO;
	private TblCategoriesDao catDAO;
	
	
	public UpdatePlanAction(){
		userDAO= TblManagersDaoFactory.create();
		warDAO= TblWarehouseDaoFactory.create();
		mapDAO= TblFloormapsDaoFactory.create();
		hspotsDAO= TblHotspotsDaoFactory.create();
		catDAO= TblCategoriesDaoFactory.create();
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "uplan.do";
	}

	@Override
	public String perform(HttpServletRequest request) {
		// TODO Auto-generated method stub
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors",errors);
		int userId;
		
		TblManagers users;
		TblWarehouse[] whouse;
		TblFloormaps[] maps;
		TblHotspots[] hotspots,uhspot;
		TblCategories[] cat;
		int current_version;
		
		HttpSession session = request.getSession();
		userId=Integer.parseInt(session.getAttribute("auser").toString());
		
		try {
			
			UpdateForm uform;
			
				uform = formBeanFactory.create(request);
				
				request.setAttribute("form",uform);
				
				users=userDAO.findByPrimaryKey(userId);
				whouse=warDAO.findWhereUidEquals(userId);
				maps= mapDAO.findWhereWidEquals(whouse[0].getWid());
				hotspots=hspotsDAO.findWhereMidEquals(maps[0].getId());
				cat=catDAO.findAll();
				
				request.setAttribute("plan", maps[0]);
				request.setAttribute("hotspot", hotspots);
				request.setAttribute("categories", cat);
				
				
				if (!uform.isPresent()) {
					return "updateplan.jsp";
				}

				
				errors.addAll(uform.getValidationErrors());
				if (errors.size() != 0) {
					return "updateplan.jsp";
				}
				
				if(uform.getAction().equals("Update")){
					uhspot=hspotsDAO.findWhereCoordinatesEquals(uform.getHdnsec());
					uhspot[0].setSid(Integer.parseInt(uform.getHdncat().toString()));
					hspotsDAO.secupdate(uhspot[0],uhspot[0]);
					
					current_version=maps[0].getVersion();
					if(current_version!=0){
						current_version++;
					}
					else
						current_version=1;
					maps[0].setVersion(current_version);
					mapDAO.versionupdate(maps[0], maps[0]);
					
					errors.add("Section Updated Successfully!");
					return "updateplan.jsp";
				}
			
			
		} catch (TblManagersDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TblWarehouseDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TblFloormapsDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TblHotspotsDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TblCategoriesDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FormBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "updateplan.jsp";
	}


}
