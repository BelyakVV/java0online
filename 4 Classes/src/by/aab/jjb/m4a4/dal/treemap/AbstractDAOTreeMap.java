package by.aab.jjb.m4a4.dal.treemap;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.function.Predicate;

import by.aab.jjb.m4a4.dal.AbstractDAO;
import by.aab.jjb.m4a4.dal.DAOException;

abstract class AbstractDAOTreeMap<T, I, F> implements AbstractDAO<T, I, F> {

	private static final String NO_FILTERS = "No filters";
	
	@SuppressWarnings("rawtypes")
	static final Predicate MATCH_ANY = o -> true;
	
	final TreeMap<I, T> data = new TreeMap<>();
	
	AbstractDAOTreeMap() {
	}
	
	AbstractDAOTreeMap(T[] source) throws DAOException {
		update(source);
	}
	
	AbstractDAOTreeMap(Collection<T> source) throws DAOException {
		update(source);
	}
	
	@Override
	public T get(I index) throws DAOException {
		T result =  duplicate(data.get(index));
		return null == result ? null : duplicate(result);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<T> getAll() throws DAOException {
		T[] result = (T[]) new Object[data.size()];
		int[] c = {0};
		data.forEach((k, v) -> {
			result[c[0]++] = duplicate(v);
		});
		return Arrays.asList(result);
	}
	
	@Override
	public Collection<T> select(F filter) throws DAOException {
		Predicate<T> predicate = toPredicate(filter);
		Collection<T> result = new LinkedList<>();
		if (null == predicate) return result;
		data.forEach((k, v) -> {
			if (predicate.test(v)) result.add(duplicate(v));
		});
		return result;
	}

	@Override
	public void update(T source) throws DAOException {
		assert source != null;
		data.put(toIndex(source), duplicate(source));
	}
	
	@Override
	public void update(T[] source) throws DAOException {
		for (T o : source) {
			update(o);
		}
	}
	
	@Override
	public void update(Collection<T> source) throws DAOException {
		for (T o : source) {
			update(o);
		}
	}
	
	@Override
	public boolean remove(I index) throws DAOException {
		return null != data.remove(index);	//There are no nulls in the tree
	}

	@Override
	public int removeAll() throws DAOException {
		int result = data.size();
		data.clear();
		return result - data.size();
	}
	
	@Override
	public int removeAll(F filter) throws DAOException {
		Predicate<T> predicate = toPredicate(filter);
		if (null == predicate) return 0;
		int result = data.size();
		data.forEach((k, v) -> {
			if (predicate.test(v)) data.remove(k);
		});
		return result - data.size();
	}
	
	@Override
	public int count(F filter) throws DAOException {
		Predicate<T> predicate = toPredicate(filter);
		if (null == predicate) return 0;
		int[] result = {0};
		data.forEach((k, v) -> {
			if (predicate.test(v)) result[0]++;
		});
		return result[0];
	}
	
	Predicate<T> matchAll(Predicate<T>[] filters) {
		assert filters.length > 0 : NO_FILTERS;
		return new Predicate<>() {
			@Override
			public boolean test(T a) {
				for (Predicate<T> f : filters) {
					boolean result = f.test(a);
					if (!result) return false;
				}
				return true;
			}
		};
	}		
	
	@SuppressWarnings("unchecked")
	Predicate<T> matchAll(Collection<Predicate<T>> filters) {
		return matchAll((Predicate<T>[]) filters.toArray());
	}

	Predicate<T> matchAny(Predicate<T>[] filters) {
		assert filters.length > 0 : NO_FILTERS;
		return new Predicate<>() {
			@Override
			public boolean test(T a) {
				for (Predicate<T> f : filters) {
					boolean result = f.test(a);
					if (result) return true;
				}
				return false;
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	Predicate<T> matchAny(Collection<Predicate<T>> filters) {
		return matchAny((Predicate<T>[]) filters.toArray());
	}
	
	abstract T duplicate(T bean);
	
	abstract I toIndex(T bean);
	
	abstract Predicate<T> toPredicate(F filter);
}
