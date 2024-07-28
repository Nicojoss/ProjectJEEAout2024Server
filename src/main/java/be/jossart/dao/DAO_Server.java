package be.jossart.dao;

import java.sql.Connection;

public abstract class DAO_Server<T> {
	protected Connection connect = null;
	
	public DAO_Server(Connection conn){
		this.connect = conn;
	}
	
	public abstract boolean create(T obj);
	
	public abstract boolean delete(T obj);
	
	public abstract boolean update(T obj);
	
	public abstract T find(int id);
}
