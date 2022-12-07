package nel.marco

import java.nio.file.Files
import kotlin.io.path.Path

class Solver {


    val VALID_NUMBERS = "[123456789]{9}"

    fun readFile(path: String): MutableList<String> {
        return Files.readAllLines(Path(path))
    }


    fun solve(input: MutableList<MutableList<String>>): Boolean {

        for (x in 0..8) {
            for (y in 0..8) {
                if (input[x][y] == "?") {
                    if (!input[x].contains((y + 1).toString())) {
                        input[x][y] = (y + 1).toString()
                        if (!solve(input)) {
                            input[x][y] = "?"
                        }
                    }
                }
            }
        }

        if (isValid(input)) {
            print(input)
            return true;
        }

        return false
    }

    fun isValid(input: List<List<String>>): Boolean {
        // non unique characters in row
        input.forEach {
            if (it.distinct().size != it.size) {
                return false
            }
        }

        // non numbers
        input.forEach {
            val toString = it.toString()
                .replace(",", "")
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "")
            val matches = toString.matches(VALID_NUMBERS.toRegex())
            if (!matches) {
                return false;
            }
        }

        // is row top to bottom valid
        for (x in 0..8) {
            val tempList = mutableListOf<String>()
            for (y in 0..8) {
                tempList.add(input[y][x])
            }
            val toString = tempList.toString()
                .replace(",", "")
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "")
            val matches = toString.matches(VALID_NUMBERS.toRegex())
            if (!matches) {
                return false;
            }
        }

        return true
    }

    fun convert(text: String): MutableList<MutableList<String>> {
        val rows = text.split("\n")

        return rows.map {
            it.split(",").toMutableList()
        }.toMutableList()
    }
}