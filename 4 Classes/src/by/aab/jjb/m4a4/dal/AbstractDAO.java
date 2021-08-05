package by.aab.jjb.m4a4.dal;

import java.util.Collection;

public interface AbstractDAO<T, I, F> {
	
	public T get(I index) throws DAOException;

	public Collection<T> getAll() throws DAOException;

	public Collection<T> select(F filter) throws DAOException;

	public void update(T source) throws DAOException;

	public void update(T[] source) throws DAOException;
	
	public void update(Collection<T> source) throws DAOException;
	
	public boolean remove(I index) throws DAOException;
	
	public int removeAll() throws DAOException;	
	
	public int removeAll(F filter) throws DAOException;	
	
	public int count(F filter) throws DAOException;

}