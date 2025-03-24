@file:Suppress("ktlint:standard:no-wildcard-imports")

package nel.marco

import com.squareup.kotlinpoet.*

class InterfaceGeneratorExample {
    fun generateExample(
        interfaceName: String = "Parent",
        parameter: List<ParameterHelperDataHolder>,
    ): TypeSpec {
        val primaryConstructor = FunSpec.constructorBuilder()
        val propertySpec = mutableListOf<PropertySpec>()

        parameter.forEach {
            // Add it to the constructor of the data class
            primaryConstructor.addParameter(it.name, it.type)

            // Add to list of properties
            val property =
                PropertySpec
                    .builder(it.name, it.type)
                    .mutable(it.isMutable)

            property.modifiers.remove(KModifier.PUBLIC)

            propertySpec.add(property.build())
        }

        return TypeSpec
            .interfaceBuilder(interfaceName)
            .addProperties(propertySpec)
            .apply {
                modifiers.remove(KModifier.PUBLIC)
            }.build()
    }

    fun combine(
        className: TypeSpec,
        others: List<TypeSpec>,
    ): TypeSpec {
        val propertySpec = mutableListOf<PropertySpec>()

        val primaryConstructor = FunSpec.constructorBuilder()


        // Collect properties from other interfaces
        (others + className).forEach { spec ->
            spec.propertySpecs.forEach { property ->
                propertySpec.add(
                    PropertySpec
                        .builder(property.name, property.type)
                        .addModifiers(KModifier.OVERRIDE)
                        .build(),
                )
                primaryConstructor.addParameter(
                    ParameterSpec
                        .builder(property.name, property.type)
                        .build(),
                )
            }
        }

        return TypeSpec
            .classBuilder(className.name!!)
            .addSuperinterfaces(others.map { it.name }.map { ClassName.bestGuess(it!!) })
            .addModifiers(KModifier.DATA)
            .primaryConstructor(primaryConstructor.build())
            .addProperties(
                propertySpec.map { it.toBuilder().initializer(it.name).build() }
            )
            .build()
    }
}
