/**
 * 
 */
package aabyodj.epamgrow.java0online.m6t2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author master
 *
 */
class Notepad {
	List<Note> notes;
	File file;
	boolean changed;
	
	static final Pattern NOTE_PTRN = Pattern.compile("(.*?);\\s*(\\d*)\\s*;(.*?);(.*)");
	
	void load() throws IOException {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			notes = new LinkedList<>();
			String line;
			while ((line = in.readLine()) != null) {
				Matcher matcher = NOTE_PTRN.matcher(line);
				if (matcher.find()) {
					try {
						notes.add(new Note(matcher, this));
					} catch (Exception e) {
						//Заметки с ошибками игнорируются
					}					
				}
			}
		}		
		changed = false;
	}
	
	void save() throws IOException {
		if (!changed) return;
		createIfNeeded(file);
		try (PrintStream out = new PrintStream(file)) {
			for (var note: notes) {
				out.println(note.encode());
			}
			changed = false;
		}		
	}

    /**
     * Создать файл в случае отсутствия
     * @param file Требуемый файл
     * @throws IOException 
     */
    public static void createIfNeeded(File file) throws IOException {
        if (file.exists()) {
            return;
        }
        File parent = file.getParentFile();
        if (parent != null) {
            if (!parent.exists()) {
                parent.mkdirs();
            } else if (!parent.isDirectory()) {
                throw new RuntimeException('\"' + parent.getCanonicalPath() + '\"'
                                           + " не является каталогом");
            }
        }
        file.createNewFile();
    }
}
