package nel.marco.generate

data class GenerateModelBase(
    val method:String,
    val url: String,
    val methodName:String = "random",
)