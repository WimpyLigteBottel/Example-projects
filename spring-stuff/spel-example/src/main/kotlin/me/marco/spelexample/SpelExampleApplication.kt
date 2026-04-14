package me.marco.spelexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.expression.Expression
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
class SpelExampleApplication

fun main(args: Array<String>) {
    runApplication<SpelExampleApplication>(*args)
}

class Person {
    var name: String = "Alice"
    var age: Int = 30
}

@RestController
@RequestMapping("/spel")
class SpelDemoController {
    private val parser: ExpressionParser = SpelExpressionParser()

    @GetMapping("/static")
    fun staticExample(): String {
        // http://localhost:8080/spel/static
        val exp: Expression = parser.parseExpression("T(java.lang.Math).random()")
        val result: Double? = exp.getValue(Double::class.java)
        return "Random number: " + result
    }

    @GetMapping("/property")
    fun propertyExample(): String {

        // http://localhost:8080/spel/property
        val person = Person()
        person.name = "Marco"

        val exp: Expression = parser.parseExpression("name")
        val name = exp.getValue(person, String::class.java)

        return "Name from SpEL: " + name
    }

    @GetMapping("/conditional")
    fun conditionalExample(@RequestParam number: Int): String? {

        // http://localhost:8080/spel/conditional?number=9
        // http://localhost:8080/spel/conditional?number=1999
        val exp = parser.parseExpression(
            "#num > 10 ? 'Greater than 10' : '10 or less'"
        )

        val context = StandardEvaluationContext()
        context.setVariable("num", number)

        val result = exp.getValue(context, String::class.java)
        return result
    }

    @GetMapping("/method")
    fun methodExample(): String {
        val input = "hello world"

        val exp = parser.parseExpression("toUpperCase()")
        val result = exp.getValue(input, String::class.java)

        return "Uppercase: " + result
    }

    @GetMapping("/filter")
    fun filterExample(): MutableList<Int> {
        val numbers = mutableListOf<Int?>(1, 5, 10, 15, 20)

        val exp = parser.parseExpression("#nums.?[#this > 10]")

        val context = StandardEvaluationContext()
        context.setVariable("nums", numbers)

        val result = exp.getValue(context) as MutableList<*>

        return result as MutableList<Int>
    }
}



