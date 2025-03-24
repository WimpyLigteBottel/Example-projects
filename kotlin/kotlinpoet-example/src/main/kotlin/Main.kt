package nel.marco

import java.time.OffsetDateTime

public interface HumanI {
    public val age: Int
}

public interface ParentI {
    public val name: String
}

public data class HumanParent(
    override val age: Int,
    override val name: String,
) : HumanI, ParentI


// If has property oneOf
//   - MUST HAVE 'discriminator'
//
sealed class Human(
    open val name: String? = null,
)

data class Parent(
    override val name: String,
    val age: Int,
) : Human(name = name)

data class Alien(
    override val name: String,
    val age: OffsetDateTime,
) : Human(name = name)

fun main() {
    val human = Parent("human", 10)
    val alien = Alien("alien", OffsetDateTime.now().withYear(2000))

    println(human)
    println(alien)
}
