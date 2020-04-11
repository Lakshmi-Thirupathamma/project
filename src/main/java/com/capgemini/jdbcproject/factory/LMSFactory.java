package com.capgemini.jdbcproject.factory;

import com.capgemini.jdbcproject.dao.UserDao;
import com.capgemini.jdbcproject.dao.UserDaoImp;
import com.capgemini.jdbcproject.service.UserService;
import com.capgemini.jdbcproject.service.UserServiceImp;

public class LMSFactory {
	public static UserDao getUserDAO() {
		return new UserDaoImp();
		
	}
	public static UserService getUserService() {
		return new UserServiceImp();
	}

}
