package by.aab.jjb.m4a4.dal;

import java.util.Collection;

import by.aab.jjb.m4a4.bean.Customer;
import by.aab.jjb.m4a4.bean.CustomerFilter;

public interface CustomerDAO extends AbstractDAO<Customer, Long, CustomerFilter> {
	
	@Override
	public Customer get(Long id) throws DAOException;
	
	@Override
	public Collection<Customer> getAll() throws DAOException;

	@Override
	public Collection<Customer> select(CustomerFilter filter) throws DAOException;
	
	@Override
	public void update(Customer source) throws DAOException;
	
	@Override
	public void update(Customer[] source) throws DAOException;
	
	@Override
	public void update(Collection<Customer> source) throws DAOException;
	
	@Override
	public boolean remove(Long id) throws DAOException;
	
	@Override
	public int removeAll() throws DAOException;
	
	@Override
	public int removeAll(CustomerFilter filter) throws DAOException;
	
	@Override
	public int count(CustomerFilter filter) throws DAOException;
}
