package nel.marco

import com.squareup.kotlinpoet.FunSpec

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    val build = FunSpec.Companion.builder("person")
        .build()



    println(build)
}