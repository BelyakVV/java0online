package by.aab.jjb.m4a4.service;

import static by.aab.jjb.m4a4.service.AccountService.customersToDTO;

import java.util.Collection;

import by.aab.jjb.m4a4.bean.Customer;
import by.aab.jjb.m4a4.bean.CustomerDTO;

public class CustomersCache {
	
	private final Collection<CustomerDTO> cache;
	
	CustomersCache(Collection<Customer> data) {
		cache = customersToDTO(data);
	}
	
	public CustomerDTO get(long id) {
		for (CustomerDTO c : cache) {
			if (c.getId() == id) return c;
		}
		return null;
	}
	
	public long[] getIdArray() {
		long[] result = new long[cache.size()];
		int i = 0;
		for (CustomerDTO c : cache) {
			result[i++] = c.getId();
		}
		return result;
	}
}
