package com.bj.scb.service;

import org.springframework.stereotype.Component;

@Component
public class EmergencyBeanService{
	private volatile static EmergencyBeanService instance = null;// volatile关键字来保证其线程间的可见性

	public String emergencyString;

	public String getEmergencyString() {
		return emergencyString;
	}

	public void setEmergencyString(String emergencyString) {
		this.emergencyString = emergencyString;
	}

	private EmergencyBeanService() {
	}

	public static EmergencyBeanService getInstance() {
		if (instance == null) {
			synchronized (EmergencyBeanService.class) {
				if (instance == null) {
					instance = new EmergencyBeanService();
				}
			}
		}
		return instance;
	}
}
