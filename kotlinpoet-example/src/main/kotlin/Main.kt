package nel.marco

public interface HumanI {
    public val age: kotlin.Int
}

public interface ParentI {
    public val name: kotlin.String
}

data class Parent(
    override val name: kotlin.String,
) : ParentI

data class Human(
    override val age: kotlin.Int,
) : HumanI

public data class HumanParent(
    override val age: kotlin.Int,
    override val name: kotlin.String,
) : HumanI,
    ParentI {
    fun toHuman() = Human(age)

    fun toParent() = Parent(name)
}

fun main() {
    val parent = Parent("parent")
    val human = Human(10)

    val humanParent = HumanParent(human.age, parent.name)

    val humanI = humanParent.toHuman()
    val parentI = humanParent.toParent()
    println("HumanParent is ${humanParent is ParentI}   && $humanI")
    println("HumanParent is ${humanParent is HumanI}    && $parentI")

    println(humanParent)
}
