package com.springservice.controller;

public class mainFile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
CaseResolutionController crc = new CaseResolutionController();
CaseResolution test = crc.getGreeting(681256448);
System.out.println(test.toString());
	}

}
