package oop1;

import textfile.Directory;
import textfile.File;
import textfile.FileException;
import textfile.TextFile;

/**
 * 5. Basics of OOP. Задача 1.
 * Создать объект класса Текстовый файл, используя классы Файл, Директория. 
 * Методы: создать, переименовать, вывести на консоль содержимое, дополнить, 
 * удалить.
 * @author aabyodj
 */
public class OOP1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileException {
        //Корневая директория
        Directory root = new Directory("");
        root.print();
        System.out.print("\nСоздаём новую директорию...");
        root.append(new Directory("Новая директория"));
        System.out.println("ок.\n");
        root.print();
        System.out.println();
        Directory directory = (Directory) root.findFirst("Новая директория");
        directory.print();
        System.out.print("\nСоздаём новый текстовый файл...");
        directory.append(new TextFile("Текстовый файл.txt"));
        System.out.println("ок.\n");
        directory.print();
        System.out.println();
        File file = directory.findFirst("Текстовый файл.txt");
        file.print();
        System.out.print("Добавляем в файл немного текста...");
        file.append("Немного текста");
        System.out.println("ок.\n");
        file.print();
        System.out.print("\nУдаляем файл...");
        file.remove();
        System.out.println("ок.\n");
        directory.print();
    }    
}
