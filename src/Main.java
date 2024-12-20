import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        int count = 1;
        while (true) {
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if (isDirectory) {
                System.out.println("Указанный путь является путём к папке");
                continue;
            } else if (!fileExists) {
                System.out.println("Указанный файл не существует");
                continue;
            } else {
                System.out.println("Путь указан верно");
                System.out.println("Это файл номер " + count);
                count++;
            }

            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);
                String line;
                int countLines = 0;
                int countQueryFromYandex = 0;
                int countQueryFromGoogle = 0;

                //Создаем Объект класса Statistics
                Statistics stat = new Statistics();

                //Читаем строки файла
                while ((line = reader.readLine()) != null) {

                    //Проверка длины строки
                    int length = line.length();
                    if (length > 1024) throw new LineLenghtException("Файл содержит строку длиннее 1024 символов");

                    //Счетчик строк файла
                    countLines++;

                    //Расчет количества запросов от YandexBot и Googlebot
                    Pattern pattern = Pattern.compile("[(](.*?)[)]");
                    Matcher matcher = pattern.matcher(line);
                    List<String> lst = new ArrayList<>();
                    while (matcher.find()) {
                        lst.add(matcher.group(1));
                    }
                    if (lst.size() > 0) {
                        String firstBrackets = lst.get(0);
                        String[] parts = firstBrackets.split(";");
                        if (parts.length >= 2) {
                            String fragment = parts[1].replaceAll(" ", "");
                            String result = (fragment.indexOf('/') != -1) ? fragment.substring(0, fragment.indexOf('/')) : fragment;

                            if (result.equals("YandexBot")) {
                                countQueryFromYandex++;
                            }
                            if (result.equals("Googlebot")) {
                                countQueryFromGoogle++;
                            }
                        }
                    }

                    //Расчет общего объема трафика
                    LogEntry logEntry = new LogEntry(line);
                    stat.addEntry(logEntry);

                }

                //Вывод количества строк в файле и доли запросов от YandexBot и Googlebot
                System.out.println("Количество строк: " + countLines);
                System.out.println("Доля запросов от YandexBot: " + (double) countQueryFromYandex / countLines);
                System.out.println("Доля запросов от Googlebot: " + (double) countQueryFromGoogle / countLines);

                //Расчет и вывод среднего объема трафика
                System.out.println("Объем часового трафика в Мб: " + stat.getTrafficRate());

                //Расчет и вывод среднего количества посещений
                System.out.println("Среднее количество посещений в час: " + stat.getAvgNumberVisits());

                //Расчет и вывод среднего количества запросов с ошибками
                System.out.println("Среднее количество ошибочных запросов в час: " + stat.getAvgNumberErrorQuery());

                //Расчет и вывод средней посещаемости одним пользователем
                System.out.println("Средняя посещаемость одним пользователем: " + stat.getAvgNumberVisitsByUser());

                //Расчет и вывод максимальной посещаемости одним пользователем
                System.out.println("Максимальная посещаемость одним пользователем: " + stat.getMaxNumberUserVisit());

                //Расчет и вывод максимальной посещаемости в секунду
                System.out.println("Максимальная посещаемость в секунду: " + stat.getMaxNumberVisitsPerSec());

                //Вывод всех существующих страниц сайта
                //System.out.println(stat.getExistingPages());

                //Вывод всех несуществующих страниц сайта
                //System.out.println(stat.getNotExistingPages());

                //Вывод всех доменов (referers)
                System.out.println(stat.getReferers());

                //Вывод статистики по операционным системам и браузерам
                System.out.println();
                System.out.println("Вывод статистики по операционным системам");
                System.out.println(stat.getStat(stat.getOpSystemsStat()));
                System.out.println();
                System.out.println("Вывод статистики по браузерам");
                System.out.println(stat.getStat(stat.getBrowsersStat()));

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (LineLenghtException ex) {
                ex.printStackTrace();
            }
        }
    }
}