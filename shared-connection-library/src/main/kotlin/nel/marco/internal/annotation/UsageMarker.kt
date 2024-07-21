package nel.marco.internal.annotation

import nel.marco.internal.dto.ApplicationEnum
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass


/**
 * This marker does not do anything specific but it to enable documenting of functions and classes.
 * When applying layered architecture tests you can make use of this marker to check that everything is correct
 */
@Target(CLASS, FUNCTION)
public annotation class UsageMarker(

    /**
     * This contains the list of applications that is using this method
     */
    val applications: Array<ApplicationEnum>,

    /**
     * This contains the list of business questions that it solve
     *
     * Example:
     * - Get the info to discover if the delivery actually happened
     */
    val businessProblemBeingSolved: Array<String>,

    /**
     * This contains the list of httpClient it is consuming which you need
     * to get the correct permissions to external services otherwise your call will fail
     */
    val consumingServices: Array<KClass<*>>
)
