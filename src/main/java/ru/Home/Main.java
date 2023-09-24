package ru.Home;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    /**
     1.  Создать 2 текстовых файла, примерно по 50-100 символов в каждом(особого значения не имеет);
     2.  Написать метод, «склеивающий» эти файлы, то есть вначале идет текст из первого файла, потом текст из второго.
     3.* Написать метод, который проверяет, присутствует ли указанное пользователем слово в файле (работаем только с латиницей).
     4.* Написать метод, проверяющий, есть ли указанное слово в папке
     */

    private static final Random random = new Random();
    private static final int CHAR_BOUND_L = 65;
    private static final int CHAR_BOUND_H = 90;
    private static final String TO_SEARCH = "GeekBrains";

    public static void main(String[] args) throws IOException {


        System.out.println(generateSymbols(30));
        writeFileContents("sample01.txt", 30);
        writeFileContents("sample02.txt", 30, 1);
        concatenate("sample01.txt", "sample02.txt", "sample03_res.txt");

        if (searchInFile("sample03_res.txt", TO_SEARCH))
            System.out.printf("Файл %s содержит искомое слово %s\n", "sample03_res.txt", TO_SEARCH);

        String[] fileNames = new String[10];
        for (int i = 0; i < fileNames.length; i++) {
            fileNames[i] = "file_" + i + ".txt";
            writeFileContents(fileNames[i], 30, 3);
            System.out.printf("Файл %s создан.\n", fileNames[i]);
        }

        List<String> result = searchMatch(new File("."), TO_SEARCH);
        for (String s : result) {
            System.out.printf("Файл %s содержит искомое слово %s\n", s, TO_SEARCH);
        }

        backup(new File("."), 1);

    }

    private static List<String> searchMatch(File dir, String search) throws IOException{
        dir = new File(dir.getCanonicalPath());
        List<String> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files == null)
            return list;
        for (int i = 0; i < files.length; i++){
            if (files[i].isDirectory())
                continue;
            if (searchInFile(files[i].getName(), search)){
                list.add(files[i].getName());
            }
        }
        return list;
    }


    /**
     * Метод генерации некоторой последовательности символов
     * @param count кол-во символов
     * @return последовательность символов
     */
    private static String generateSymbols(int count){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++){
            stringBuilder.append((char) random.nextInt(CHAR_BOUND_L));
        }
        return stringBuilder.toString();
    }

    /**
     * Записать последовательность символов в файл
     * @param fileName имя файла
     * @param length кол-во символов
     * @throws IOException
     */
    private static void writeFileContents(String fileName, int length) throws IOException {
        try(FileOutputStream fileOutputStream = new FileOutputStream(fileName)){
            fileOutputStream.write(generateSymbols(length).getBytes());
        }
    }

    private static void writeFileContents(String fileName, int length, int i) throws IOException {
        try(FileOutputStream fileOutputStream = new FileOutputStream(fileName)){
            if (random.nextInt(i) == 0){
                fileOutputStream.write(TO_SEARCH.getBytes());
            }
            fileOutputStream.write(generateSymbols(length).getBytes());
        }
    }

    private static void concatenate(String fileIn1, String fileIn2, String fileOut) throws IOException{
        // На запись
        try(FileOutputStream fileOutputStream = new FileOutputStream(fileOut)){

            int c;
            // На чтение
            try(FileInputStream fileInputStream = new FileInputStream(fileIn1)){
                while ( (c = fileInputStream.read()) != -1)
                    fileOutputStream.write(c);
            }

            // На чтение
            try(FileInputStream fileInputStream = new FileInputStream(fileIn2)){
                while ( (c = fileInputStream.read()) != -1)
                    fileOutputStream.write(c);
            }
        }
    }

    /**
     * Определить, содержится ли в файле искомое слово
     * @param fileName имя файла
     * @param search слово
     * @return результат поиска
     * @throws IOException
     */
    private static boolean searchInFile(String fileName, String search) throws IOException{
        try(FileInputStream fileInputStream = new FileInputStream(fileName)){
            byte[] searchData = search.getBytes();
            int c;
            int i = 0;
            while ( (c = fileInputStream.read()) != -1){
                if (c == searchData[i]) {
                    i++;
                }
                else{
                    i = 0;
                    if (c == searchData[i])
                        i++;
                    continue;
                }
                if (i == searchData.length){
                    return true;
                }
            }
            return false;
        }
    }

    private static void backup(File file, int ind) throws IOException {

        File[] files = file.listFiles();
        if (files == null)
            return;

        File directory = new File(name(String.valueOf(file)));
        directory.mkdir();

        for (int i = ind; i < files.length; i++){
            if (files[i].isDirectory()){
                backup(files[i], 0);
            }
            else {
                copyFileUsingStream(files[i], new File(name(String.valueOf(files[i]))));
            }

        }

    }

    private static String name(String tree){
        String output = "./.backup/";
        for (int i = 2; i < tree.length(); i++) {
            output += tree.charAt(i);
        }
        return output;
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

}

