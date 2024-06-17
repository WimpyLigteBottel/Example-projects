package nel.marco

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import kotlin.reflect.KClass


data class ParameterHelperDataHolder(
    val name: String,
    val type: KClass<*>,
    val isMutable: Boolean,
    val defaultValue: Any
)

class DataClassExample {


    fun generateExample(parameter: List<ParameterHelperDataHolder>): TypeSpec {

        val typeSpec = TypeSpec
            .classBuilder("Person")
            .addModifiers(KModifier.DATA)

        //Removing annoying public modifier
        typeSpec.modifiers.remove(KModifier.PUBLIC)

        val primaryConstructor = FunSpec.constructorBuilder()
        val propertySpec = mutableListOf<PropertySpec>()


        parameter.forEach {
            //Add it to the constructor of the data class
            primaryConstructor.addParameter(it.name, it.type)

            //Add to list of properties
            val property = PropertySpec.builder(it.name, it.type)
                .initializer(it.name)
                .mutable(it.isMutable)
            property.modifiers.remove(KModifier.PUBLIC)
            propertySpec.add(property.build())
        }

        typeSpec
            .primaryConstructor(primaryConstructor.build()) // build constructor
            .addProperties(propertySpec) // add all properties


        return typeSpec.build()
    }


}