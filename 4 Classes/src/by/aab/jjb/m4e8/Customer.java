package by.aab.jjb.m4e8;

import java.util.Formatter;

/**
 * Класс Customer: id, фамилия, имя, отчество, адрес, номер кредитной карточки,
 * номер банковского счета.
 * 
 * Определить конструкторы, set- и get- методы и метод toString()
 * 
 * @author aabyodj
 */
public class Customer {
	public static final long MAX_CARD_NUMBER = 9999_9999_9999_9999L;
	
	private int id;
	private String surname;
	private String name;
	private String patronymic;
	private String address;
	private long cardNumber;
	private String iban;
	
	public Customer() {
	}

	public Customer(int id, String surname, String name, String patronymic, 
			String address, long cardNumber, String iban) {
		this.id = id;
		this.surname = surname;
		this.name = name;
		this.patronymic = patronymic;
		this.address = address;
		this.iban = iban;
		setCardNumber(cardNumber);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(long cardNumber) {
		if (cardNumber < 0 || cardNumber > MAX_CARD_NUMBER) {
			throw new RuntimeException("Invalid card number");
		}
		this.cardNumber = cardNumber;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	@Override
	public String toString() {		
		StringBuilder result = new StringBuilder();
		Formatter formatter = new Formatter(result);
		
		result.append("id: ").append(id)
				.append(", ФИО: ").append(surname).append(' ').append(name)
				.append(' ').append(patronymic).append(", адрес: ").append(address);
		
		formatter.format(", номер карты: %016d", cardNumber).close();
		
		result.append(", номер счёта: ").append(iban);
		
		return result.toString();
	}	
}
