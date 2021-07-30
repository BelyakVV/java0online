package by.aab.jjb.m4e8;

import static by.aab.jjb.m4e8.Customer.MAX_CARD_NUMBER;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

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
	
	private static final Customer[] ZERO_ARRAY = new Customer[0];

	private static final String EMPTY = "пусто" + System.lineSeparator();
	
	private Customer[] customers;
	
	public Aggregator() {
		customers = ZERO_ARRAY;
	}
	
	public Aggregator(Customer[] customers) {
		this.customers = customers;		
	}
	
	public Aggregator selectByCardRange(long floor, long ceiling) {
		if (ceiling < floor || ceiling < 0 || floor > MAX_CARD_NUMBER) {
			return new Aggregator();
		}
		Collection<Customer> result = new LinkedList<>();
		for (var customer: customers) {
			if (customer.getCardNumber() >= floor && customer.getCardNumber() <= ceiling) {
				result.add(customer);
			}
		}
		return new Aggregator(result.toArray(ZERO_ARRAY));
	}
	
	public Aggregator sort(Comparator<Customer> comparator) {
		Arrays.sort(customers, comparator);
		return this;
	}
	
	@Override
	public String toString() {
		if (customers.length < 1) return EMPTY;
		StringBuilder result = new StringBuilder();
		for (var customer: customers) {
			result.append(customer).append(System.lineSeparator());
		}
		return result.toString();
	}
}
