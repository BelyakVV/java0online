package by.aab.jjb.m4a4.dal;

import by.aab.jjb.m4a4.bean.Account;
import by.aab.jjb.m4a4.bean.Customer;
import by.aab.jjb.m4a4.dal.treemap.AccountDAOTreeMap;
import by.aab.jjb.m4a4.dal.treemap.CustomerDAOTreeMap;

public final class DAOProvider {
	
	private static final DAOProvider INSTANCE = new DAOProvider();
	
	private final AccountDAO accountDAO = new AccountDAOTreeMap();
	
	private final CustomerDAO customerDAO = new CustomerDAOTreeMap();
	
	private DAOProvider() {
		try {
			accountDAO.update(new Account[] {});
		} catch (DAOException e) {
			//This shouldn't happen
			throw new RuntimeException(e);
		}
		try {
			customerDAO.update(new Customer[] {});
		} catch (DAOException e) {
			//This shouldn't happen
			throw new RuntimeException(e);
		}
	}

	public static DAOProvider getInstance() {
		return INSTANCE;
	}

	public AccountDAO getAccountDAO() {
		return accountDAO;
	}

	public CustomerDAO getCustomerDAO() {
		return customerDAO;
	}
}
