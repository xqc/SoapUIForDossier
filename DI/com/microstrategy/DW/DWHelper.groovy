package com.microstrategy.DW
import groovy.json.*  
import com.eviware.soapui.support.JsonUtil
import java.io.File 
import org.apache.log4j.Logger;
import java.net.URLEncoder

/*
 * @Author Jhuang
 * This class is used to prepare the postData for Data Wrangling
 */
class DWHelper{


	/*
	 * this function is used to prepare the postData for Data Wrangling at the situation there is no facet on the data
	 * here is the example on how to use the function for FindAndReplace in case special char in cell
	 * import com.microstrategy.DW.DWHelper
	 * import com.microstrategy.DW.DWFunctions
     * def fcfile = testRunner.testCase.getProperty("function.dwt").getValue().toString() 
     * String[] action = ["FRCharInCell","@","#"]
     * def data = DWHelper.getPostData(DWFunctions.FINDANDREPLACECHARINCELL,action,null,fcfile,log)
     * testRunner.testCase.getTestStepByName("DataWranglerServer.FindAndReplace.CharInCell").setPropertyValue("postData",data)
     * 
     * @param fcName it is the function name which want to be test at data wrangling, we sugguest you to use the value at DWFunctions
     * @param action is used to store the actions that to apply, the first one should be the column name
     * @param response if the function will cause the numbers of column change, the response is required for getting the specific column 
     *        current index.this response is coming from the action DataWranglerServer - get models
     * @param filename the configuration file for DW which stores the PostData template
     * @param log the context log
     * @return  the good to use PostData for function DataWranglerServer - apply operations
	 */
	public static String getPostData(String fcName,String[] action,String response,String filename, Logger log){
		def data = ""
		
		if(fcName == null || fcName.length() == 0){
			log.error("DWHelper: The Function is Empty")
			return ""
		}
		
		if(action == null || action.length == 0){
			 log.error("DWHelper: The Action is Empty")
			 return ""
		}

		//get the column name
		String cName = action[0]
		log.info "DWHelper: Use action ${fcName} on column ${cName}"

		//get the index place of the column
		def columnInsertIndex = 0
		if(response != null && JsonUtil.isValidJson(response)){
			def responseJSON = new JsonUtil().parseTrimmedText(response)
			def columns  = responseJSON.columnModel.columns
			for(int i= 0; i < columns.size(); i++){
				if(cName == columns.getJSONObject(i).name){
					columnInsertIndex = i
			    }
				if(action.length > 1 && action[1] == columns.getJSONObject(i).name){
					columnInsertIndex = columnInsertIndex > i ? columnInsertIndex : i
				}
			}
			columnInsertIndex++
			log.info "DWHelper: columnInsertIndex = ${columnInsertIndex}"
		}

		//open the function configuration file and prepare the postData
		def file = new File(filename) 
		log.info "DWHelper: Open configure file ${filename}" 
		if (file.exists()){
	
			def list = new XmlParser().parseText(file.text)
			data = list."${fcName}".text()
			
			data = data.replaceAll("columnInsertIndexValue",columnInsertIndex.toString())
			data = data.replaceAll("CName",URLEncoder.encode(cName,"UTF-8"))
			for(int i = 1; i < action.length; i++){
				data = data.replaceAll("Action${i}",URLEncoder.encode(action[i],"UTF-8"))
			}
			
		}else {
			log.error("DWHelper: The file ${filename} does not exist")
			return "" 
		}

		log.info("DWHelper: End DWHelper.getPostData for function ${fcName}")
		return data
	}


         public static String getPostData(String fcName,String[] action,String response,String filename, Logger log, int regex){
		def data = ""
		
		if(fcName == null || fcName.length() == 0){
			log.error("DWHelper: The Function is Empty")
			return ""
		}
		
		if(action == null || action.length == 0){
			 log.error("DWHelper: The Action is Empty")
			 return ""
		}

		//get the column name
		String cName = action[0]
		log.info "DWHelper: Use action ${fcName} on column ${cName}"

		//get the index place of the column
		def columnInsertIndex = 0
		if(response != null && JsonUtil.isValidJson(response)){
			def responseJSON = new JsonUtil().parseTrimmedText(response)
			def columns  = responseJSON.columnModel.columns
			for(int i= 0; i < columns.size(); i++){
				if(cName == columns.getJSONObject(i).name){
					columnInsertIndex = i
			    }
				if(action.length > 1 && action[1] == columns.getJSONObject(i).name){
					columnInsertIndex = columnInsertIndex > i ? columnInsertIndex : i
				}
			}
			columnInsertIndex++
			log.info "DWHelper: columnInsertIndex = ${columnInsertIndex}"
		}

		//open the function configuration file and prepare the postData
		def file = new File(filename) 
		log.info "DWHelper: Open configure file ${filename}" 
		if (file.exists()){
	
			def list = new XmlParser().parseText(file.text)
			data = list."${fcName}".text()
			
			data = data.replaceAll("columnInsertIndexValue",columnInsertIndex.toString())
			data = data.replaceAll("CName",URLEncoder.encode(cName,"UTF-8"))
			for(int i = 1; i < action.length; i++){
				data = data.replaceAll("Action${i}",URLEncoder.encode(action[i],"UTF-8"))
			}
			if(regex == 1){
				data = data.replaceAll(URLEncoder.encode("\"regex\":false","UTF-8"),URLEncoder.encode("\"regex\":true","UTF-8"))
			}
		}
			
		
		else {
			log.error("DWHelper: The file ${filename} does not exist")
			return "" 
		}

		log.info("DWHelper: End DWHelper.getPostData for function ${fcName}")
		return data
	}

	public static String getClusterElement(ArrayList ClusterElement, String Origin, String New){
	if(ClusterElement == null ){
		//log.info "No Cluster Element"
		return ""
	} else{
		int index = ClusterElement.size
		for(int i=0; i< index; i++){
			if(Origin in ClusterElement[i]){

				def data = ClusterElement[i].toString()
				data = data.replace("[","[\"").replace("]","\"]").replaceAll(",","\",\"")
				data = URLEncoder.encode(data,"UTF-8")
				return data
			}
		}
		}
 
		

}
       
}