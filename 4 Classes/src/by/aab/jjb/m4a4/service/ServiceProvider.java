package by.aab.jjb.m4a4.service;

public final class ServiceProvider {
	
	private static final ServiceProvider instance = new ServiceProvider();
	
	private static final AccountService accountService = new AccountService();
	
	private ServiceProvider() {
	}

	public static ServiceProvider getInstance() {
		return instance;
	}

	public AccountService getAccountservice() {
		return accountService;
	}
}
