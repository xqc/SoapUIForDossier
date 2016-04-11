package com.microstrategy.DataService

public enum DSoAuthGUIDs {
	DROPBOX("897B0012B9A24BF9AB297BA654F21482"),
	GOOGLEANALYTICS("1304214B5640429EBC5FA0820E64DEC4");
	
	
	private String guid;
	private DSoAuthGUIDs(String str){
		guid = str;
	}
	
	public String getGUID(){
		return guid;
	}
	
}