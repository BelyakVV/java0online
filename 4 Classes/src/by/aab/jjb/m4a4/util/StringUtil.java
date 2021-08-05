package by.aab.jjb.m4a4.util;

public class StringUtil {
	
	public static boolean isEqual(Object o1, Object o2) {
		if (o1 == o2) return true;
		if (null == o1) return false;
		return o1.equals(o2);
	}	
	
	public static boolean existAndConsecute(String s1, String s2) {
		if (null == s1 || null == s2) return false;
		if (s1 == s2) return true;
		return s1.compareToIgnoreCase(s2) <= 0;
	}
	
	public static boolean existAndEqual(String s1, String s2) {
		if (null == s1 || null == s2) return false;
		if (s1 == s2) return true;
		return s1.equalsIgnoreCase(s2);
	}
	
	public static boolean prefixFitsWithin(String prefix, String floor, String ceiling) {
		if (null == prefix) return true;
		if (null != floor) 
			if (prefix.compareToIgnoreCase(floor) < 0) return false;
		if (null != ceiling)
			if (prefix.compareToIgnoreCase(ceiling) > 0) return false;
		return true;
	}
}
