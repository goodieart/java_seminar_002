import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

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

        if (answer.equals("y"))
            interactive = true;


        // TODO: values[...] - отDRYить
        if (interactive) {
            System.out.print("Введите число x: ");
            values[0] = iScanner.nextInt();

            System.out.print("Введите степень n: ");
            values[1] = iScanner.nextInt();
        } else
            values = loadValuesFromFile("data/input.txt");

        if (values != null) {
            x = values[0];
            n = values[1];
            /**
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

    /**
     * @param x
     * @param n
     * @return
     */
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

    /**
     * @param path
     * @return
     */
    public static int[] loadValuesFromFile(String path) throws IOException {
        int[] result = new int[] { 0, 0 };
        File file = new File(path);
        FileReader fr = new FileReader(file);
        Scanner iScanner = new Scanner(fr);
        int flag = 0;
        while (iScanner.hasNextLine()) {
            String temp = iScanner.nextLine();
            String[] words = temp.split(" ");
            if (words[0].equals("a")) {
                result[0] = Integer.parseInt(words[1]);
                flag++;
            } else if (words[0].equals("b")) {
                result[1] = Integer.parseInt(words[1]);
                flag++;
            }
        }
        iScanner.close();
        fr.close();

        if (flag > 1) {
            return result;
        } else
            return null;
    }

    /**
     * @param path
     * @param value
     * @throws IOException
     */
    public static void saveResultToFile(String path, String value) throws IOException {
        File file = new File(path);
        FileWriter fr = new FileWriter(file);

        fr.write(value);
        fr.close();
    }
}
