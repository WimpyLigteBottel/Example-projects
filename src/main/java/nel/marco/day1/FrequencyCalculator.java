package nel.marco.day1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class FrequencyCalculator {


    public FrequencyCalculator() throws IOException {

        System.out.println("part 1 answer = " + FrequencyCalculator.part1());
        System.out.println("part 2 answer = " + FrequencyCalculator.part2());


    }


    public static void main(String[] args) throws IOException {
        new FrequencyCalculator();
    }

    public static int part1() throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("G:\\Code repositery\\adventofcode\\src\\main\\resources\\day1\\Numbers.txt"));
        String line = "";


        int total = 0;

        while ((line = br.readLine()) != null) {
            if (line.startsWith("-")) {
                total -= Integer.valueOf(line.substring(1));
            } else {
                total += Integer.valueOf(line.substring(1));
            }
        }

        return total;

    }

    public static int part2() throws IOException {

        String line = "";

        Set<String> history = new HashSet<>();


        int total = 0;
        while (true) {
            try (BufferedReader br = new BufferedReader(new FileReader("G:\\Code repositery\\adventofcode\\src\\main\\resources\\day1\\Numbers.txt"))) {
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("-")) {
                        total -= Integer.valueOf(line.substring(1));
                    } else {
                        total += Integer.valueOf(line.substring(1));
                    }

                    if (history.contains(total + "")) {
                        return total;
                    }
                    history.add(total + "");
                }

            }

        }


    }

}
