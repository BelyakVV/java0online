package by.aab.jjb.m4e5;

import java.util.Arrays;

/**
 * Integer represented as an array of decimal digits
 * 
 * @author aabyodj
 *
 */
class Decimal implements Cloneable, Comparable<Decimal> {
	
	public static final Decimal MAX_INT = new Decimal(Integer.MAX_VALUE, false);
	public static final Decimal MIN_INT = new Decimal(Integer.MIN_VALUE, false);
	
	private byte[] digits;
	private int sign;
	private boolean isMutable = true;
	

	public Decimal(int value) {
		this(value, true);
	}
	
	public Decimal(int value, boolean isMutable) {
		set(value);
		this.isMutable = isMutable;		
	}

	public Decimal(Decimal decimal) {
		this(decimal, true);
	}
	
	public Decimal(Decimal decimal, boolean isMutable) {
		digits = decimal.digits.clone();
		sign = decimal.sign;
		this.isMutable = isMutable;
	}

	public void inc() {
		if (!isMutable) throw new RuntimeException("This instance is immutable");
		if (sign > 0) {
			absInc();
		} else if (sign < 0) {
			absDec();
		} else {
			digits[0] = 1;
			sign = 1;			
		}
	}

	public void dec() {
		if (!isMutable) throw new RuntimeException("This instance is immutable");
		if (sign > 0) {
			absDec();
		} else if (sign < 0) {
			absInc();
		} else {
			digits[0] = 1;
			sign = -1;	
		}
	}

	private void absInc() {
		boolean carry = true;
		for (int i = 0; i < digits.length; i++) {
			if (carry) digits[i]++;
			carry = digits[i] > 9 ? true : false;
			if (!carry) return;
			digits[i] = 0;
		}
		byte[] newDigits = new byte[digits.length + 1];
		System.arraycopy(digits, 0, newDigits, 0, digits.length);
		newDigits[digits.length] = 1;
		digits = newDigits;
	}

	private void absDec() {
		boolean borrow = true;
		boolean allZero = true;
		for (int i = 0; i < digits.length; i++) {
			if (borrow) digits[i]--;
			borrow = digits[i] < 0 ? true : false;
			if (borrow) {
				digits[i] = 9;
				allZero = false;
			} else {
				allZero &= 0 == digits[i] ? true : false;
				if (!allZero) return;
			}			
		}
		if (allZero) sign = 0;
	}

	public void set(int value) {
		if (!isMutable) throw new RuntimeException("This instance is immutable");
		if (Integer.MIN_VALUE == value) {
			set(value + 1);
			dec();
			return;
		}
		sign = Integer.compare(value, 0);
		value = Math.abs(value);
		int length = (Math.max(0, (int) Math.log10(value))) + 1;
		digits = new byte[length];
		if (0 == value) return;
		int i = 0;
		while (value != 0) {
			digits[i++] = (byte) (value % 10);
			value = value / 10;
		}
	}
	
	public void set(Decimal value) {
		if (!isMutable) throw new RuntimeException("This instance is immutable");
		sign = value.sign;
		int length = getHiPos(value.digits);
		if (length > digits.length) {
			digits = new byte[length];
		} else {
			Arrays.fill(digits, (byte) 0);
		}
		System.arraycopy(value.digits, 0, digits, 0, length);
	}

	
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Object result = super.clone();
		digits = digits.clone();
		return result;
	}

	@Override
	public int compareTo(Decimal o) {
		int result = Integer.compare(sign,o.sign);
		if (result != 0) return result;
		if (0 == sign) return 0;
		int myHiPos = getHiPos(digits);
		int otherHiPos = getHiPos(o.digits);
		result = Integer.compare(myHiPos, otherHiPos);
		if (result != 0) return result * sign;
		for (int i = myHiPos; i >= 0; i--) {
			result = Byte.compare(digits[i], o.digits[i]);
			if (result != 0) return result * sign;
		}
		return 0;
	}	
	
	public int toInt() {
		int minIntCompare = compareTo(MIN_INT);
		if (compareTo(MAX_INT) > 0 || minIntCompare < 0) {
			throw new RuntimeException("Out of range");			
		}
		if (0 == sign) return 0;
		if (0 == minIntCompare) return Integer.MIN_VALUE;
		int result = 0;
		for (int i = digits.length - 1; i >= 0; i--) {
			result = result * 10 + digits[i];
		}
		return result * sign;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		if (sign < 0) result.append('-');
		for (int i = getHiPos(digits); i >= 0; i--) {
			result.append(digits[i]);
		}
		return result.toString();
	}

	/**
	 * Get position of most significant digit
	 * @param digits
	 * @return
	 */
	private static int getHiPos(byte[] digits) {
		for (int i = digits.length - 1; i >= 0; i--) {
			if (digits[i] != 0) return i;
		}
		return 0;
	}

}