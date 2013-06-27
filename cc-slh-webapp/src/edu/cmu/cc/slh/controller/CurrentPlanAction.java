package edu.cmu.cc.slh.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.cmu.cc.slh.dao.TblFloormapsDao;
import edu.cmu.cc.slh.dao.TblHotspotsDao;
import edu.cmu.cc.slh.dao.TblManagersDao;
import edu.cmu.cc.slh.dao.TblWarehouseDao;
import edu.cmu.cc.slh.dto.TblFloormaps;
import edu.cmu.cc.slh.dto.TblHotspots;
import edu.cmu.cc.slh.dto.TblManagers;
import edu.cmu.cc.slh.dto.TblWarehouse;
import edu.cmu.cc.slh.exceptions.TblFloormapsDaoException;
import edu.cmu.cc.slh.exceptions.TblHotspotsDaoException;
import edu.cmu.cc.slh.exceptions.TblManagersDaoException;
import edu.cmu.cc.slh.exceptions.TblWarehouseDaoException;
import edu.cmu.cc.slh.factory.TblFloormapsDaoFactory;
import edu.cmu.cc.slh.factory.TblHotspotsDaoFactory;
import edu.cmu.cc.slh.factory.TblManagersDaoFactory;
import edu.cmu.cc.slh.factory.TblWarehouseDaoFactory;

public class CurrentPlanAction extends Action {

	private TblManagersDao userDAO;
	private TblWarehouseDao warDAO;
	private TblFloormapsDao mapDAO;
	private TblHotspotsDao hspotsDAO;
	
	
	public CurrentPlanAction(){
		userDAO= TblManagersDaoFactory.create();
		warDAO= TblWarehouseDaoFactory.create();
		mapDAO= TblFloormapsDaoFactory.create();
		hspotsDAO= TblHotspotsDaoFactory.create();
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "cplan.do";
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
		TblHotspots[] hotspots;
		
		HttpSession session = request.getSession();
		userId=Integer.parseInt(session.getAttribute("auser").toString());
		
		try {
			users=userDAO.findByPrimaryKey(userId);
			whouse=warDAO.findWhereUidEquals(userId);
			maps= mapDAO.findWhereWidEquals(whouse[0].getWid());
			hotspots=hspotsDAO.findWhereMidEquals(maps[0].getId());
			
			request.setAttribute("plans", maps[0]);
			request.setAttribute("hotspots", hotspots);
			request.setAttribute("manager", users.getFirstName());
			request.setAttribute("warehouse", whouse[0].getAddress());
			
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
		}
		
		return "floorplan.jsp";
	}

}
