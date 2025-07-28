sealed class ResponseCodes(open val status: Int) {

    data class Accepted(override val status: Int) : ResponseCodes(status)
    data class Success(override val status: Int, val body: Any) : ResponseCodes(status)
    data class Error(override val status: Int, val body: Any) : ResponseCodes(status)
}

fun main() {


    listOf(
        ResponseCodes.Accepted(203),
        ResponseCodes.Accepted(205),
        ResponseCodes.Success(200, "success"),
        ResponseCodes.Error(500, "error"),
    )
        .forEach { response ->
            when (response) {
                is ResponseCodes.Error -> println(response.body)
                is ResponseCodes.Accepted if response.status in (205..209) -> println("Accepted with (${response.status})")
                is ResponseCodes.Accepted -> println("Accepted")
                is ResponseCodes.Success -> println(response.body)
            }
        }
}