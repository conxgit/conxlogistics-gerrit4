package com.conx.logistics.app.whse.dao.services;

import java.util.List;

import com.conx.logistics.app.whse.domain.location.Location;

public interface ILocationDAOService {
	public Location get(long id);
	
	public List<Location> getAll();
	
	public Location getByCode(String code);	

	public Location add(Location record);

	public void delete(Location record);

	public Location update(Location record);
	
	public Location provide(int row, int column, String level);
}
