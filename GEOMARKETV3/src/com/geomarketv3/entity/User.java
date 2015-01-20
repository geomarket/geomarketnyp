package com.geomarketv3.entity;

import android.graphics.Bitmap;

public class User {
	private String email, name, nric, role, company, title;
	private int contact;
	private double lat, lng;
	private Bitmap image;
	public User(){
		
	}
	
	public User(String email, String pwd, String name,String nric, String comapny, int contact, double lat, double lng) {
		this.email = email;
		this.nric = nric;
		this.setName(name);
		this.company = company;
		this.contact = contact;
		this.role = role;
		this.setLat(lat);
		this.setLng(lng);
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public int getContact() {
		return contact;
	}

	public void setContact(int contact) {
		this.contact = contact;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	
	
}
