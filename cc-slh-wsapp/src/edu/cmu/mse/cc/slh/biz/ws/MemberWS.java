/**
 * %W% %E% M. A. Riveros T.
 *
 * Copyright (c) 2012-2013 MSE, Carnegie Mellon University. All Rights Reserved.
 * 
 */

package edu.cmu.mse.cc.slh.biz.ws;

import java.util.Random;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 * WS offering membership related services
 * 
 * @version 1.0.0. 21 Jun 2013
 * @author M. A. Riveros T.
 */
@WebService(portName = "MemberWSPort", 
	serviceName = "MemberWS", 
	targetNamespace = "http://ws.biz.slh.cc.mse.cmu.edu/", 
	endpointInterface = "edu.cmu.mse.cc.slh.biz.ws.MemberWS")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class MemberWS {

	/**
	 * 
	 * Indicates if a membership is still valid
	 * 
	 * @param membershipID
	 *            Member identifier
	 * 
	 * @return valid A boolean indicating whether the membership is valid or not
	 * 
	 */
	@WebMethod(action = "validateMembership")
	@WebResult(name = "validity")
	public String validateMembership(
			@WebParam(name = "membershipID") String membershipID) {

		if (membershipID == null || membershipID.isEmpty())
			return null;

		Random random = new Random();
		return Boolean.toString(random.nextBoolean());
	}

}
