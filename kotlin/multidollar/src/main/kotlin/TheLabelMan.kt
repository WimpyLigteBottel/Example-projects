

fun main() {

//    basic()
//    noGotchaYet()
//    thereIsGotcha()
//    forLoopLabels()

}

fun basic() {
    for (x in 0..10) {
        print(x)
        print(", ")
    }
}

fun noGotchaYet() {
    val gotcha = "_____".let {
        val numbers = (0..5).map { value -> value }

        numbers
    }

    println(gotcha) // 1,2,3,4,5
}

fun thereIsGotcha() {
    val gotcha = "_____".let {
        val numbers = (0..5).map { x ->
            if (x == 3)
                return@let x
            x
        }
        numbers
    }

    println(gotcha) // What gets printed?

}

fun forLoopLabels() {
    // Notice label
    loop@ for (z in 0..1000000) {

        for (x in 0..10) {
            if (x == 5) {
                // notice this break
                break@loop
            }
            print(x) // What gets printed?
        }
    }
}