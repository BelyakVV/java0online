package aabyodj.datafiles;

import java.util.regex.Pattern;

/**
 *
 * @author aabyodj
 */
public class Const {
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final char ARR_DLM = ',';
    public static final String ARR_DLM_REGEX = "\\s*" + ARR_DLM + "\\s*";
    public static final char FLD_DLM = ';';
    public static final Pattern FLD_DLM_PTTRN = Pattern.compile("\\s*" + FLD_DLM + "\\s*");    
}
