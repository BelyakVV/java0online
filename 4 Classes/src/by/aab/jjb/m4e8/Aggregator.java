package by.aab.jjb.m4e8;

import static by.aab.jjb.m4e8.Customer.MAX_CARD_NUMBER;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/**
 * Создать класс, агрегирующий массив типа Customer, с подходящими
 * конструкторами и методами.
 * 
 * Найти и вывести:
 * 
 * a) список покупателей в алфавитном порядке;
 * 
 * b) список покупателей, у которых номер кредитной карточки находится в
 * заданном интервале.
 * 
 * @author aabyodj
 */
public class Aggregator {
	
	private Customer[] customers;
	
	public Aggregator() {
		customers = new Customer[0];
	}
	
	public Aggregator(Customer[] customers) {
		this.customers = customers;		
	}
	
	public Collection<Customer> selectAll() {
		return Arrays.asList(customers);
	}
	
	public Collection<Customer> selectByCardRange(long floor, long ceiling) {
		Collection<Customer> result = new LinkedList<>();
		if (ceiling < floor || ceiling < 0 || floor > MAX_CARD_NUMBER) return result;
		for (var customer: customers) {
			if (customer.getCardNumber() >= floor && customer.getCardNumber() <= ceiling) {
				result.add(customer);
			}
		}
		return result;
	}
	
	public static Collection<Customer> sort (Collection<Customer> customers, Comparator<Customer> comparator) {
		if (customers instanceof List) {
			((List<Customer>) customers).sort(comparator);
			return customers;
		} else {
			Collection<Customer> result = new TreeSet<>(comparator);
			result.addAll(customers);
			return result;
		}
	}
	
	public static int compareAlphabetically(Customer c1, Customer c2) {
		int result = c1.getSurname().compareToIgnoreCase(c2.getSurname());
		if (result != 0) return result;
		result = c1.getName().compareToIgnoreCase(c2.getName());
		if (result != 0) return result;
		result = c1.getPatronymic().compareToIgnoreCase(c2.getPatronymic());
		return result;
	}
}
