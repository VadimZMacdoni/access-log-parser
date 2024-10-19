import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int count = 1;
        while (true){
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if(isDirectory) {
                System.out.println("Указанный путь является путём к папке");
                continue;
            } else if (!fileExists) {
                System.out.println("Указанный файл не существует");
                continue;
            } else {
                System.out.println("Путь указан верно");
                System.out.println("Это файл номер "+count);
                count++;
            }

            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);
                String line;
                int countLines = 0;
                int minLength = reader.readLine().length();
                int maxLength = reader.readLine().length();
                while ((line = reader.readLine()) != null) {
                        int length = line.length();
                        if(length>1024) throw new RuntimeException("Файл содержит строку длиннее 1024 символов");
                        if(length<minLength) {
                            minLength=length;
                        }
                        if(length>maxLength) {
                            maxLength=length;
                        }
                        countLines++;
                }
                System.out.println("Количество строк: "+countLines);
                System.out.println("Длина самой длинной строки: "+maxLength);
                System.out.println("Длина самой короткой строки: "+minLength);
            }catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}