package com.conx.logistics.mdm.dao.services.user;

import java.util.List;

import com.conx.logistics.mdm.domain.user.User;


public interface IUserDAOService {
	public User get(long id);
	
	public List<User> getAll();
	
	public User getByCode(String code);	

	public User add(User record);

	public void delete(User record);

	public User update(User record);
	
	public User provide(User record);
	
	public User provide(String code, String name);
	
	public void provideDefaults();

	User getByEmailOrScreenname(String email, String screenName);
}
