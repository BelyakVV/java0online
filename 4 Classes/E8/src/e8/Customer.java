package e8;

import java.util.Scanner;

/**
 *   Класс Customer: id, фамилия, имя, отчество, адрес, номер кредитной 
 * карточки, номер банковского счета.
 * @author aabyodj
 */
class Customer {
    /** Идентификатор покупателя */
    private int id;
    /** Фамилия покупателя */
    private String surname;
    /** Имя покупателя */
    private String name;
    /** Отчество покупателя */
    private String patronymic;
    /** Адрес покупателя */
    private String address;
    /** Номер банковской карты покупателя */
    private long card;
    /** Номер банковского счёта покупателя */
    private String iban;
    
    /**
     * Создание объекта и заполнение его информацией из заданной строки
     * @param line 
     */
    Customer (String line) {
        Scanner in = new Scanner(line).useDelimiter("\\s*;\\s*");
        id = in.nextInt();
        surname = in.next();
        name = in.next();
        patronymic = in.next();
        address = in.next();
        card = in.nextLong();
        iban = in.next();
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

    public long getCard() {
        return card;
    }

    public void setCard(long card) {
        this.card = card;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    } 

    @Override
    public String toString() {
        return "id: " + id + ", ФИО: " + surname + " " + name + " " + patronymic 
                + ", адрес: " + address + ", карта: " + card + ", счёт: " + iban;
    }
}
