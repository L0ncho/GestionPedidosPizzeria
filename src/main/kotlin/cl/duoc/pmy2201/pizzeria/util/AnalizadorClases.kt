package cl.duoc.pmy2201.pizzeria.util

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.memberProperties

/**
 * Analiza clases en tiempo de ejecución usando reflection de Kotlin (`kotlin.reflect.full`).
 * Las propiedades incluyen las heredadas; los métodos, solo los declarados en la propia clase.
 */
object AnalizadorClases {

    /**
     * Retorna los nombres de las propiedades de [clase] (con [memberProperties]), incluidas las heredadas,
     * ordenados alfabéticamente. Por ejemplo, para `Cliente` incluye `nombre` y `telefono`, que hereda de `Usuario`.
     */
    fun propiedadesDe(clase: KClass<*>): List<String> =
        clase.memberProperties
            .map { propiedad: KProperty1<out Any, *> -> propiedad.name }
            .sorted()

    /**
     * Retorna los nombres de las funciones declaradas en [clase] (con [declaredMemberFunctions]),
     * sin incluir las heredadas, ordenados alfabéticamente.
     */
    fun metodosDe(clase: KClass<*>): List<String> =
        clase.declaredMemberFunctions
            .map { funcion: KFunction<*> -> funcion.name }
            .sorted()
}
