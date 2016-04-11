package com.microstrategy.DataService

import groovy.json.*

import com.eviware.soapui.support.JsonUtil

import org.apache.log4j.Logger;
/*
 * @Author jhuang 
 * this class is a helper class for DataService team
 */
class DSHelper {

	/*
	 * this function is used to find the ID for specific DBRole from the result of the search
	 * @param DBRoleName the specific DBRole name
	 * @param response the response from the search action arch.search
	 * @param log the context log
	 * @return the ID of the specific DBRole
	 */
	public static String getDBRoleID(String DBRoleName,String response, Logger log) {
		
		if(response == null || !JsonUtil.isValidJson(response)) {
			log.error "DSHelper: The response input is empty"
			return ""
		}
		
		if(DBRoleName == null || DBRoleName == "") {
			log.error "DSHelper: DBRole Name is empty"
			return ""
		}
		
		def responseJSON = new JsonUtil().parseTrimmedText(response)
		def columns  = responseJSON.dbRoles
		for(int i= 0; i < columns.size(); i++) {
			if(DBRoleName == columns.getJSONObject(i).n) {
				log.info "DSHelper: Found DBRole ${DBRoleName} and Id is ${columns.getJSONObject(i).did}"
				return columns.getJSONObject(i).did
			}
		}
		
		
		log.error "DSHelper: There is no DBRole ${DBRoleName}"
		return ""
	}
	
	/*
	 * this function is used to find the Json object for specific DBRole from the result of the search
	 * @param DBRoleName the specific DBRole name
	 * @param response the response from the search action arch.search
	 * @param log the context log
	 * @return the string of the specific DBRole Json
	 */
	public static String getDBRole(String DBRoleName,String response, Logger log) {
		
		if(response == null || !JsonUtil.isValidJson(response)) {
			log.error "DSHelper: The response input is empty"
			return ""
		}
		
		if(DBRoleName == null || DBRoleName == "") {
			log.error "DSHelper: DBRole Name is empty"
			return ""
		}
		
		def responseJSON = new JsonUtil().parseTrimmedText(response)
		def columns  = responseJSON.dbRoles
		for(int i= 0; i < columns.size(); i++) {
			if(DBRoleName == columns.getJSONObject(i).n) {
				log.info "DSHelper: Found DBRole ${DBRoleName} and Json is ${columns.getJSONObject(i)}"
				return columns.getJSONObject(i).toString()
			}
		}
		
		
		log.error "DSHelper: There is no DBRole ${DBRoleName}"
		return ""
	}

	
	/*
	 * this function is used to format the oAuth 
	 * @param DBRoleName the specific DBRole name
	 * @param response the response from the getDBInstance 
	 * @param filename the file path which stores the oAuth info
	 * @param log the context log
	 * @return the string of the dbroleinfo	for task arch.saveDBRole
	 */
	public static String fillDBRoleInfo(String DBRoleName,String response,String filename, Logger log) {
		
		if(response == null || !JsonUtil.isValidJson(response)) {
			log.error "DSHelper: The response input is empty"
			return ""
		}
		def responseJSON = new JsonUtil().parseTrimmedText(response)
		
		
		if(DBRoleName == null || DBRoleName == "") {
			log.error "DSHelper: DBRole Name is empty"
			return ""
		}
		
		def file = new File(filename)
		log.info "DSHelper: Open configure file ${filename}"
		if (file.exists()){
	
			def list = new JsonUtil().parseTrimmedText(file.text)
			def data = list."${DBRoleName}"
			def oauthinfo = """<prs><ClientID v=\"${data.KEY[0]}\"/><ClientSecret v=\"${data.SECRET[0]}\"/><RedirectURL v=\"${data.LINK[0]}\"/></prs>"""
			log.info "DSHelper: oauthinfo ${oauthinfo}"
			
			responseJSON.dbrs[0].oa = oauthinfo.toString()
			log.info "DSHelper: DBRoleInfo ${responseJSON}"
		}else {
			log.error("DSHelper: The file ${filename} does not exist")
			return ""
		}
		
		log.info("DWHelper: End DWHelper.fillDBRoleInfo for  ${DBRoleName}")
		return responseJSON.dbrs[0].toString()
	}
}
