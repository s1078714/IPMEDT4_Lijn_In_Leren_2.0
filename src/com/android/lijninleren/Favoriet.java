package com.android.lijninleren;

public class Favoriet {

	//private variables
	String _id;
	String _titel;

	// Empty constructor
	public Favoriet(){

	}
	// constructor
	public Favoriet(String id, String titel){
		this._id = id;
		this._titel = titel;
	}

	// constructor
	public Favoriet(String titel){
		this._titel = titel;
	}
	// getting ID
	public String getID(){
		return this._id;
	}

	// setting id
	public void setID(String id){
		this._id = id;
	}

	// getting name
	public String getTitel(){
		return this._titel;
	}

	// setting name
	public void setTitel(String titel){
		this._titel = titel;
	}

}

