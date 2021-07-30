package by.aab.jjb.m4e10;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.BiFunction;

public class Aggregator {

	private static final Airline[] ZERO_ARRAY = new Airline[0];
	private static final String EMPTY = "пусто" + System.lineSeparator();
	
	private Airline[] flights;
	
	public Aggregator() {
		flights = ZERO_ARRAY;
	}
	
	public Aggregator(Airline[] flights) {
		this.flights = flights;
	}
	
	public Aggregator(Collection<Airline> flights) {
		this(flights.toArray(ZERO_ARRAY));
	}

	public Aggregator select(Filter filter) {
		Collection<Airline> result = new LinkedList<>();
		for (var flight: flights) {
			if (filter.check.apply(flight, filter.criterion)) {
				result.add(flight);
			}
		}
		return new Aggregator(result);
	}
	
	public Aggregator selectAnd(Filter[] filters) {
		Collection<Airline> result = new LinkedList<>();
		flightsLoop:
		for (var flight: flights) {
			for (var filter: filters) {
				if (!filter.check.apply(flight, filter.criterion)) 
					continue flightsLoop;
			}
			result.add(flight);
		}
		return new Aggregator(result);
	}
	
	@Override
	public String toString() {
		if (flights.length < 1) return EMPTY;
		StringBuilder result = new StringBuilder();
		for (var flight: flights) {
			result.append(flight).append(System.lineSeparator());
		}
		return result.toString();
	}
	
	public static class Filter {
		public final BiFunction<Airline, Object, Boolean> check;
		public final Object criterion;
		
		public Filter(BiFunction<Airline, Object, Boolean> check, Object criterion) {
			this.check = check;
			this.criterion = criterion;
		}		
	}
}
