package by.aab.jjb.m4a4.service;

import static by.aab.jjb.m4a4.service.AccountDTOComparator.IBAN_ASC;
import static by.aab.jjb.m4a4.service.AccountDTOComparator.OWNER_THEN_BALANCE_ASC;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeSet;

import by.aab.jjb.m4a4.bean.Account;
import by.aab.jjb.m4a4.bean.AccountDTO;
import by.aab.jjb.m4a4.bean.AccountFilter;
import by.aab.jjb.m4a4.bean.Customer;
import by.aab.jjb.m4a4.bean.CustomerDTO;
import by.aab.jjb.m4a4.dal.AccountDAO;
import by.aab.jjb.m4a4.dal.CustomerDAO;
import by.aab.jjb.m4a4.dal.DAOException;
import by.aab.jjb.m4a4.dal.DAOProvider;

public class AccountService {
	
	private static final DAOProvider DAL = DAOProvider.getInstance();
	
	private static AccountDAO accounts = DAL.getAccountDAO();
	
	private static CustomerDAO customers = DAL.getCustomerDAO();
	
	//Set the default sort order
	private static Comparator<AccountDTO> accountComparator = IBAN_ASC;	
	
	/**
	 * Check if account identified by its iban is blocked.
	 * 
	 * @param iban
	 * @return
	 * @throws DAOException
	 * @throws ServiceException 
	 */
	public boolean isBlocked(String iban) throws DAOException, ServiceException {
		Account a = accounts.get(iban);
		if (null == a) throw new ServiceException("Счёт " + iban + "не найден");
		return a.isBlocked();
	}
	
	/**
	 * Set blocked status of an account identified by its iban.
	 * 
	 * @param iban
	 * @param blocked
	 * @throws DAOException
	 * @throws ServiceException 
	 */
	public void setBlocked(String iban, boolean blocked) throws DAOException, ServiceException {
		Account a = accounts.get(iban);
		if (null == a) throw new ServiceException("Счёт " + iban + "не найден");
		a.setBlocked(blocked);
		accounts.update(a);
	}
	
	/**
	 * Provide a Comparator for Collection where result of search is stored.
	 *  
	 * @param comparator
	 */
	public void setAccountComparator(Comparator<AccountDTO> comparator) {
		assert comparator != null;
		AccountService.accountComparator = comparator;
	}
	
	public void setOrderIban() {
		setAccountComparator(IBAN_ASC);
	}
	
	public void setOrderOwnerThenBalance() {
		setAccountComparator(OWNER_THEN_BALANCE_ASC);
	}
	
	/**
	 * Search accounts using the AccountFilter given and then sort with 
	 * Comparator set by setAccountComparayor method.
	 * 
	 * @param filter Search criterion
	 * @return
	 * @throws DAOException
	 */
	public Collection<AccountDTO> select(AccountFilter filter) throws DAOException {
		return accountsToDTO(accounts.select(filter));
	}
	
	/**
	 * Calculate total balance of all accounts.
	 * 
	 * @return
	 * @throws DAOException
	 */
	public long calcTotal() throws DAOException {
		return accounts.calcBalanceSum(null);
	}
	
	/**
	 * Calculate total balance of all positive accounts.
	 *  
	 * @return
	 * @throws DAOException
	 */
	public long calcPositiveTotal() throws DAOException {
		AccountFilter filter = new AccountFilter();
		filter.setBalanceFloor(0L);
		return accounts.calcBalanceSum(filter);
	}
	
	/**
	 * Calculate total balance of all negative accounts.
	 * 
	 * @return
	 * @throws DAOException
	 */
	public long calcNegativeTotal() throws DAOException {
		AccountFilter filter = new AccountFilter();
		filter.setBalanceCeiling(0L);
		return accounts.calcBalanceSum(filter);
	}
	
	static Collection<AccountDTO> accountsToDTO(Collection<Account> source, CustomerDTO owner) {
		Collection<AccountDTO> result = new TreeSet<>(accountComparator);
		for (Account a : source) {
			result.add(new AccountDTO(a, owner));
		}
		return result;
	}
	
	static Collection<AccountDTO> accountsToDTO(Collection<Account> source, CustomersCache cache) {
		Collection<AccountDTO> result = new TreeSet<>(accountComparator);
		for (Account a : source) {
			try {
				result.add(new AccountDTO(a, cache));
			} catch (OrphanAccountException e) {
				// Just do nothing
			}
		}
		return result;
	}
	
	static Collection<AccountDTO> accountsToDTO(Collection<Account> source) throws DAOException {
		if (source.isEmpty()) return new TreeSet<>(accountComparator);
		return accountsToDTO(source, new CustomersCache(customers.getAll()));
	}
	
	static Collection<CustomerDTO> customersToDTO(Collection<Customer> source) {
		Collection<CustomerDTO> result = new LinkedList<>();
		for (Customer c : source) {
			result.add(new CustomerDTO(c));
		}
		return result;
	}
}
