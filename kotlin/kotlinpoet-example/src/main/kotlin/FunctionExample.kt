package nel.marco

import com.squareup.kotlinpoet.*
import kotlin.reflect.KClass


data class FunctionParameter(
    val name: String,
    val type: KClass<*>,
    val defaultValue: Any
)


data class FunctionAnnotation(
    val name: String,
    val type: KClass<*>,
    val message: String
)

data class FunctionDataHolder(
    val name: String = "main",
    val returnType: KClass<*> = Unit::class,
    val parameters: List<FunctionParameter> = emptyList(),
    val annotations: List<FunctionAnnotation> = emptyList()
)

class FunctionExample {


    fun generateExample(data: FunctionDataHolder): FunSpec {
        val functionSpec = FunSpec.builder(data.name)

        addReturnType(data, functionSpec)
        addAnnotations(data, functionSpec)
        addParameters(data, functionSpec)

        //To be done
        addDocs(data,functionSpec)

        return functionSpec.build()
    }

    private fun addDocs(data: FunctionDataHolder, functionSpec: FunSpec.Builder) {
        //Add how to add java docs
        //Example method that still needs to be done
    }

    private fun addReturnType(data: FunctionDataHolder, functionSpec: FunSpec.Builder) {
        if (data.returnType != Unit::class) {
            functionSpec.returns(data.returnType)
        }
    }

    private fun addParameters(data: FunctionDataHolder, functionSpec: FunSpec.Builder) {
        data.parameters.forEach {
            val parameter = ParameterSpec
                .builder(
                    name = it.name,
                    type = it.type,
                )
                .build()

            functionSpec.addParameter(parameter)
        }
    }

    private fun addAnnotations(data: FunctionDataHolder, functionSpec: FunSpec.Builder) {
        data.annotations.forEach {
            val annotation = AnnotationSpec
                .builder(it.type.asTypeName())
                .addMember(CodeBlock.of("\"%L\"", it.message))
                .build()

            functionSpec.addAnnotation(annotation)
        }
    }


}