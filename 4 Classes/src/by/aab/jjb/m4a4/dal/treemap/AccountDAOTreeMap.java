package by.aab.jjb.m4a4.dal.treemap;

import static by.aab.jjb.m4a4.util.StringUtil.existAndConsecute;
import static by.aab.jjb.m4a4.util.StringUtil.existAndEqual;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import by.aab.jjb.m4a4.bean.Account;
import by.aab.jjb.m4a4.bean.AccountFilter;
import by.aab.jjb.m4a4.dal.AccountDAO;
import by.aab.jjb.m4a4.dal.DAOException;

public class AccountDAOTreeMap extends AbstractDAOTreeMap<Account, String, AccountFilter> implements AccountDAO {
	
	public AccountDAOTreeMap() {
	}

	public AccountDAOTreeMap(Account[] accounts) throws DAOException {
		super(accounts);
	}
	
	Predicate<Account> ibanAboveOrEqual(String other) {
		assert other != null;
		return new Predicate<>() {
			@Override
			public boolean test(Account a) {
				return a.getIban().compareTo(other) >= 0;
			}};
	}
	
	Predicate<Account> ibanBelowOrEqual(String other) {
		assert other != null;
		return new Predicate<>() {
			@Override
			public boolean test(Account a) {
				return a.getIban().compareTo(other) <= 0;
			}};
	}

	Predicate<Account> ibanContains(String part) {
		assert part != null;
		return new Predicate<>() {
			@Override
			public boolean test(Account a) {
				return a.getIban().contains(part);
			}};
	}
	
	Predicate<Account> ibanIs(String iban) {
		assert iban != null;
		return new Predicate<>() {
			@Override
			public boolean test(Account a) {
				return a.getIban().contentEquals(iban);
			}};
	}
	
	Predicate<Account> balanceEquals(long amount) {
		return new Predicate<>() {
			@Override
			public boolean test(Account a) {
				return a.getBalance() == amount;
			}};
	}
	
	Predicate<Account> balanceLess(long amount) {
		return new Predicate<>() {
			@Override
			public boolean test(Account a) {
				return a.getBalance() < amount;
			}
		};
	}
	
	Predicate<Account> balanceMore(long amount) {
		return new Predicate<>() {
			@Override
			public boolean test(Account a) {
				return a.getBalance() > amount;
			}
		};
	}
	
	Predicate<Account> blockIs(boolean state) {
		return new Predicate<>() {
			@Override
			public boolean test(Account a) {
				return a.isBlocked() == state;
			}
		};
	}
	
	Predicate<Account> ownerIs(long id) {
		return new Predicate<>() {
			@Override
			public boolean test(Account a) {
				return a.getOwnerId() == id;
			}
		};
	}

	@Override
	Account duplicate(Account bean) {
		return new Account(bean);
	}

	@Override
	String toIndex(Account bean) {
		return bean.getIban();
	}

	@SuppressWarnings("unchecked")
	@Override
	Predicate<Account> toPredicate(AccountFilter filter) {
		if (null == filter) return MATCH_ANY;
		List<Predicate<Account>> result = new LinkedList<>();
		
		if (existAndEqual(filter.getIbanFloor(), filter.getIbanCeiling())) {
			result.add(ibanIs(filter.getIbanFloor()));
			
		} else if (existAndConsecute(filter.getIbanFloor(), filter.getIbanCeiling())) {
			result.add(ibanAboveOrEqual(filter.getIbanFloor()));
			result.add(ibanBelowOrEqual(filter.getIbanCeiling()));
			
		} else if (filter.getIbanFloor() != null && filter.getIbanCeiling() != null) {
			return null;
			
		} else if (filter.getIbanFloor() != null) {
			result.add(ibanAboveOrEqual(filter.getIbanFloor()));
			
		} else if (filter.getIbanCeiling() != null) {
			result.add(ibanBelowOrEqual(filter.getIbanCeiling()));			
		}
		
		if (filter.getIbanPart() != null) {
			result.add(ibanContains(filter.getIbanPart()));
		}
		
		if (filter.getBalanceFloor() != null) {
			if (filter.getBalanceCeiling() != null) {
				long bdc = Long.compare(filter.getBalanceFloor(), filter.getBalanceCeiling());
				if (bdc > 0) {
					return null;
				} else if (0 == bdc) {
					result.add(balanceEquals(filter.getBalanceCeiling()));
				} else {
					result.add(balanceMore(filter.getBalanceFloor()));
					result.add(balanceLess(filter.getBalanceCeiling()));
				}
			} else result.add(balanceMore(filter.getBalanceFloor()));
		} else if (filter.getBalanceCeiling() != null) {
			result.add(balanceLess(filter.getBalanceCeiling()));
		}
		
		if (filter.getOwnerId() != null) {
			result.add(ownerIs(filter.getOwnerId()));
		}
		
		if (filter.getBlocked() != null) {
			result.add(blockIs(filter.getBlocked()));
		}
		
		if (result.size() < 1) return null;
		if (result.size() == 1) return result.get(0);
		return matchAll(result);
	}

	@Override
	public long calcBalanceSum(AccountFilter filter) throws DAOException {
		Predicate<Account> predicate = toPredicate(filter);
		if (null == predicate) return 0;
		long[] result = {0};
		data.forEach((k, v) -> {
			if (predicate.test(v)) result[0] += v.getBalance();
		});
		return result[0];
	}
}
