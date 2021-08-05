package by.aab.jjb.m4a4.dal;

import java.util.Collection;

import by.aab.jjb.m4a4.bean.Account;
import by.aab.jjb.m4a4.bean.AccountFilter;

public interface AccountDAO extends AbstractDAO<Account, String, AccountFilter> {
	
	@Override
	public Account get(String iban) throws DAOException;

	@Override 
	public Collection<Account> getAll() throws DAOException;
	
	@Override
	public Collection<Account> select(AccountFilter filter) throws DAOException;
	
	@Override
	public void update(Account source) throws DAOException;
	
	@Override
	public void update(Account[] source) throws DAOException;
	
	@Override
	public void update(Collection<Account> source) throws DAOException;
	
	@Override
	public boolean remove(String iban) throws DAOException;
	
	@Override
	public int removeAll(AccountFilter filter) throws DAOException;
	
	@Override
	public int count(AccountFilter filter) throws DAOException;
	
	public long calcBalanceSum(AccountFilter filter) throws DAOException;
}
