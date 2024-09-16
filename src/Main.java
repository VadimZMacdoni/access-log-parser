import java.io.File;
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
        }
      }
    }
