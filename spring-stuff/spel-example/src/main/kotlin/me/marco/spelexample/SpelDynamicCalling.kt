package me.marco.spelexample

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.expression.BeanFactoryResolver
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component

@Configuration
class SpelBeanConfig(
    private val parser: ExpressionParser = SpelExpressionParser(),
) {
    @Bean
    fun standardEvaluationContext(applicationContext: ApplicationContext): StandardEvaluationContext {
        val context = StandardEvaluationContext()
        context.beanResolver = BeanFactoryResolver(applicationContext)
        return context
    }
}


@Component("fb")
class FizzBuzzHelper {
    val nums: List<Int> = (1..100).toList()

    fun label(i: Int): String = when {
        i % 15 == 0 -> "FizzBuzz"
        i % 3 == 0 -> "Fizz"
        i % 5 == 0 -> "Buzz"
        else -> i.toString()
    }
}

@Component
class FizzBuzzRunner(
    private val parser: ExpressionParser = SpelExpressionParser(),
    private val standardEvaluationContext: StandardEvaluationContext
) {
    @Value("\${spel.expression.fizzbuzz}")
    private lateinit var fizzbuzzExpression: String

    @Value("\${spel.expression.fizzbuzzcalling}")
    private lateinit var fizzbuzzCalling: String

    @PostConstruct
    fun run() {

        standardEvaluationContext.setVariable("number", 100)

        val result = parser
            .parseExpression(fizzbuzzExpression)
            .getValue(standardEvaluationContext, Any::class.java)

        val resultDynamicCalling = parser
            .parseExpression(fizzbuzzCalling)
            .getValue(standardEvaluationContext, Any::class.java)

        println("=== FizzBuzz via SpEL ===")
        println("dynamic: " + resultDynamicCalling)
        println("in memory: " + result)
    }


}