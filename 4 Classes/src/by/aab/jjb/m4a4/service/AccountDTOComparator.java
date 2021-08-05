package by.aab.jjb.m4a4.service;

import java.util.Comparator;

import by.aab.jjb.m4a4.bean.AccountDTO;

public class AccountDTOComparator {
	private AccountDTOComparator() {
	}
	
	public static final Comparator<AccountDTO> ACTIVE_FIRST = new Comparator<>() {
		@Override
		public int compare(AccountDTO o1, AccountDTO o2) {
			return Boolean.compare(o1.isBlocked(), o2.isBlocked());
		}
	};
	
	public static final Comparator<AccountDTO> BALANCE_ASC = new Comparator<>() {
		@Override
		public int compare(AccountDTO o1, AccountDTO o2) {
			return Long.compare(o1.getBalance(), o2.getBalance());
		}
	};
	
	public static final Comparator<AccountDTO> IBAN_ASC = new Comparator<>() {
		@Override
		public int compare(AccountDTO o1, AccountDTO o2) {
			return o1.getIban().compareTo(o2.getIban());
		}};
	
	public static final Comparator<AccountDTO> OWNER_NAME_ASC = new Comparator<>() {
		@Override
		public int compare(AccountDTO o1, AccountDTO o2) {
			return o1.getOwner().getName().compareToIgnoreCase(o2.getOwner().getName());
		}
	};
	
	@SuppressWarnings("unchecked")
	public static final Comparator<AccountDTO> OWNER_THEN_BALANCE_ASC = ofChain(
			new Comparator[] { OWNER_NAME_ASC, BALANCE_ASC });


	static Comparator<AccountDTO> ofChain(Comparator<AccountDTO>[] chain) {
		if (chain.length < 1) throw new RuntimeException("Empty chain");
		return new Comparator<>() {
			@Override
			public int compare(AccountDTO a1, AccountDTO a2) {
				for (Comparator<AccountDTO> c : chain) {
					int result = c.compare(a1, a2);
					if (result != 0) return result;
				}
				return 0;
			}
		};
	}
}
