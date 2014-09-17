/*
 * Game.java
 * 
 * Developer:
 * 		Alessandro Cucci
 * 		alessandro.cucci@gmail.com
 * 		www.alessandrocucci.it
 * 
 * Disclaimer: When I wrote this, only God and I understood what I was doing
 */

package it.alessandrocucci.cuccifeeder;

public class Feed {
	

	int _id;
	String _name;
    String _url;
	

	public Feed(){
		
	}

	public Feed(int id, String name){
		this._id = id;
		this._name = name;
	}
	

	public Feed(String name){
		this._name = name;
	}


    public Feed(String name, String url){

        this._name = name;
        this._url = url;
    }


	public int getID(){
		return this._id;
	}
	

	public void setID(int id){
		this._id = id;
	}
	

	public String getName(){
		return this._name;
	}
	

	public void setName(String name){
		this._name = name;
	}


    public String getUrl(){
        return this._url;
    }


    public void setUrl(String url){
        this._url = url;
    }
	
}
