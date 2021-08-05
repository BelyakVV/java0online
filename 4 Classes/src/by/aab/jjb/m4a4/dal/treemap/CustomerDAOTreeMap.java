package by.aab.jjb.m4a4.dal.treemap;

import static by.aab.jjb.m4a4.util.StringUtil.existAndConsecute;
import static by.aab.jjb.m4a4.util.StringUtil.existAndEqual;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import by.aab.jjb.m4a4.bean.Customer;
import by.aab.jjb.m4a4.bean.CustomerFilter;
import by.aab.jjb.m4a4.dal.CustomerDAO;
import by.aab.jjb.m4a4.dal.DAOException;

public class CustomerDAOTreeMap extends AbstractDAOTreeMap<Customer, Long, CustomerFilter> implements CustomerDAO {
		
	public CustomerDAOTreeMap() {
	}
	
	public CustomerDAOTreeMap(Customer[] data) throws DAOException {
		super(data);
	}

	Predicate<Customer> idIs(long id) {
		return new Predicate<>() {
			@Override
			public boolean test(Customer c) {
				return c.getId() == id;
			}};
	}
	
	Predicate<Customer> nameContainsIgnoreCase(String part) {
		String key = part.toUpperCase();
		return new Predicate<>() {
			@Override
			public boolean test(Customer c) {
				return c.getName().toUpperCase().contains(key);
			}};
	}
	
	Predicate<Customer> nameEqualsIgnoreCase(String name) {
		String key = name.toUpperCase();
		return new Predicate<>() {
			@Override
			public boolean test(Customer c) {
				return c.getName().toUpperCase().contentEquals(key);
			}};
	}
	
	Predicate<Customer> nameAboveOrEquals(String floor) {
		String key = floor.toUpperCase();
		return new Predicate<>() {
			@Override
			public boolean test(Customer c) {
				return c.getName().toUpperCase().compareTo(key) >= 0;
			}};
	}
	
	Predicate<Customer> nameBelowOrEquals(String ceiling) {
		String key = ceiling.toUpperCase();
		return new Predicate<>() {
			@Override
			public boolean test(Customer c) {
				return c.getName().toUpperCase().compareTo(key) <= 0;
			}};
	}
	
	@Override
	Customer duplicate(Customer source) {
		return new Customer(source);
	}

	@Override
	Long toIndex(Customer source) {
		return source.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	Predicate<Customer> toPredicate(CustomerFilter filter) {
		if (null == filter) return MATCH_ANY;
		List<Predicate<Customer>> result = new LinkedList<>();
		
		String nameFloor = filter.getNameFloor() != null ? 
				filter.getNameFloor().toUpperCase() : null;
		String nameCeiling = filter.getNameCeiling() != null ?
				filter.getNameCeiling().toUpperCase() : null;
		
		if (existAndEqual(nameFloor, nameCeiling)) {
				result.add(nameEqualsIgnoreCase(nameFloor));
			
		} else if (existAndConsecute(nameFloor, nameCeiling)) {				
				result.add(nameAboveOrEquals(nameFloor));
				result.add(nameBelowOrEquals(nameCeiling));
			
		} else if (nameFloor != null && nameCeiling != null) {
			return null;
			
		} else if (nameFloor != null) {			
			result.add(nameAboveOrEquals(nameFloor));
			
		} else if (nameCeiling != null) {
			result.add(nameBelowOrEquals(nameCeiling));						
		}
		
		if (filter.getNamePart() != null) {
			result.add(nameContainsIgnoreCase(filter.getNamePart()));
		}
		
		if (filter.getId() != null) 
			result.add(idIs(filter.getId()));
		return matchAll(result);
	}	
}
