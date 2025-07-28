package interpolation

fun main() {
    val age = 1
    val name = 2
    println(
        $$$$$"""
        {
           "name": "${name}",
           "age": $$$$$age,
           "note" : "Marco has $${amount} dollar bills in his account! $!$!$! man!"
        }
    """.trimIndent()
    )
}


/*
When Should You Use Multi-Dollar String Interpolation?

This feature is particularly useful in scenarios like:
    - Configuration Management: Writing templates for build systems (Gradle, Maven, etc.) or configuration files.
    - Dynamic String Templates: Creating strings with placeholders for external systems.
    - Scripting: Embedding code snippets or DSLs that rely heavily on $ symbols.

If you frequently encounter $ in your strings, this feature can save you time and effort.
 */
