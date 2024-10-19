import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                int countQueryFromYandex = 0;
                int countQueryFromGoogle = 0;
                while ((line = reader.readLine()) != null) {
                        int length = line.length();
                        if(length>1024) throw new RuntimeException("Файл содержит строку длиннее 1024 символов");
                        countLines++;
                        Pattern pattern = Pattern.compile("[(](.*?)[)]");
                        Matcher matcher = pattern.matcher(line);
                        List<String> lst = new ArrayList<>();
                        while (matcher.find()){
                            lst.add(matcher.group(1));
                        }
                        if(lst.size()>0){
                            String firstBrackets = lst.get(0);
                            String[] parts = firstBrackets.split(";");
                            if (parts.length >= 2) {
                                String fragment = parts[1].replaceAll(" ", "");
                                String result = (fragment.indexOf('/') != -1) ? fragment.substring(0, fragment.indexOf('/')) : fragment;

                                if(result.equals("YandexBot")) {
                                    countQueryFromYandex++;
                                }
                                if(result.equals("Googlebot")) {
                                    countQueryFromGoogle++;
                                }
                            }
                        }
                }
                System.out.println("Количество строк: "+countLines);
                System.out.println("Доля запросов от YandexBot: "+(double)countQueryFromYandex/countLines);
                System.out.println("Доля запросов от Googlebot: "+(double)countQueryFromGoogle/countLines);

            }catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}