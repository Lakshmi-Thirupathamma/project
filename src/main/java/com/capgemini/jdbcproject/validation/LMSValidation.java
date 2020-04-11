package com.capgemini.jdbcproject.validation;

import java.util.regex.Pattern;

import com.capgemini.jdbcproject.exception.LMSException;

public class LMSValidation {
	public boolean validateId(int id) throws LMSException {
		String idRegEx = "[1-9]{1}[0-9]{5}";
		boolean result = false;
		if(Pattern.matches(idRegEx, String.valueOf(id))) {
			result = true;
		} else {
			throw new LMSException ("id should contain exact 6 digits");
		}
		return result;
		
	}
	
	public boolean validateEmailId(String email) throws LMSException {
		String emailRegEx = "[a-zA-Z0-9][a-zA-Z0-9._]*@gmail[.]com";
		boolean result = false;
		if(Pattern.matches(emailRegEx, String.valueOf(email))) {
			result = true;
		
	} else {
		throw new LMSException("please check emailId");
	}
		return result;

}
	public boolean validateName(String name) throws LMSException {
		String nameRegEx = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$" ;
		boolean result = false;
		if(Pattern.matches(nameRegEx, String.valueOf(name))) {
			result = true;
		} else {
			throw new LMSException("Name should  contains only Alphabates");
		}
		return result;
	}
	
	public boolean validateMobile(long mobile) throws LMSException {
		String mobileRegEx = "(0/91)?[6-9][0-9]{9}" ;
		boolean result = false;
		if(Pattern.matches(mobileRegEx, String.valueOf(mobile))) {
			result = true;
		} else {
			throw new LMSException("Mobile Number  will start from 6 and It should contains 10 numbers ");
		}
		return result;
	}
	
	public boolean validatePassword(String password) throws LMSException {
		String passwordRegEx = "^(?=.*\\d).{4,8}$" ;
		boolean result = false;
		if (Pattern.matches(passwordRegEx, String.valueOf(password))) { 
			result = true;
		} else {
			throw new LMSException ("Password should be between 4 to 8 digits long and include atleast one numeric digit "); 
		}

		return result;
	}


}
