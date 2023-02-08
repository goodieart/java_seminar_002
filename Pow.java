import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.lang.NumberFormatException;

/**
 * Pow
 */
public class Pow {
    public static void main(String[] args) throws IOException {
        Scanner iScanner = new Scanner(System.in);
        boolean interactive = false;
        int x = 0;
        int n = 0;
        int[] values = null;
        String answer = "";

        while (!(answer.equals("y") || answer.equals("n"))) {
            System.out.print("Интерактивный режим? (y/n): ");
            answer = iScanner.next();
        }

        // Переменная interactive введена для улучшения
        // читаемости кода

        if (answer.equals("y"))
            interactive = true;

        if (interactive) {
            values = new int[] { 0, 0 };
            try {
                for (int i = 0; i < 2; i++) {
                    System.out.printf("Введите число %s: ", i == 0 ? "x" : "n");
                    values[i] = iScanner.nextInt();
                }
            } catch (InputMismatchException e) {
                System.out.println("Неверное значение!");
                System.exit(1);
            }
        } else
            values = loadValuesFromFile("data/input.txt");

        if (values != null) {
            x = values[0];
            n = values[1];
            /*
             * Здесь проверка на "Ноль в степени ноль" вынесена в тело main,
             * так как существует соглашение о "0 ^ 0 = 1". Источник:
             * https://ru.wikipedia.org/wiki/Возведение_в_степень
             */
            if (x == 0 && n == 0) {
                if (interactive)
                    System.out.printf("Выражение не имеет смысла и неопределено");
                else
                    saveResultToFile("data/output.txt", "Неопределено");
            } else {
                String sPow = Float.toString(fastPow(x, n));
                if (interactive)
                    System.out.printf("Число %d в степени %d равняется %s", x, n, sPow);
                else
                    saveResultToFile("data/output.txt", sPow);
            }
            iScanner.close();
        } else {
            System.out.println("Неверный синтаксис входного файла!");
        }
    }

    public static float fastPow(int x, int n) {
        float fX = x;
        float result = 1;
        if (n < 0) {
            fX = 1 / fX;
            n *= -1;
        }
        while (n > 0) {
            if ((n & 1) == 0) {
                fX *= fX;
                n >>= 1;
            } else {
                result *= fX;
                n--;
            }
        }
        return result;
    }

    public static int[] loadValuesFromFile(String path) throws IOException {
        int[] result = new int[] { 0, 0 };
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("Файл не существует!");
        }
        FileReader fr = new FileReader(file);
        Scanner iScanner = new Scanner(fr);
        int flag = 0;

        while (iScanner.hasNextLine()) {
            String line = iScanner.nextLine();
            /*
             * Здесь можно использовать Map, но из-за необходимости
             * анализировать литерал это лишь усложнит код
             */
            String[] parts = line.split(" ");
            try {
                if (parts[0].equals("a")) {
                    result[0] = Integer.parseInt(parts[1]);
                    flag++;
                } else if (parts[0].equals("b")) {
                    result[1] = Integer.parseInt(parts[1]);
                    flag++;
                }
            } catch (NumberFormatException e) {
                System.out.printf("Ошибка входных данных в файле %s", path);
                System.exit(1);
            }
        }
        iScanner.close();
        fr.close();

        if (flag > 1) {
            return result;
        } else
            return null;
    }

    public static void saveResultToFile(String path, String value) throws IOException {
        File file = new File(path);
        FileWriter fr = new FileWriter(file);

        fr.write(value);
        fr.close();
    }
}
