package cl.duoc.pmy2201.pizzeria.data

import cl.duoc.pmy2201.pizzeria.model.Pizza

/**
 * Acceso al catálogo de pizzas.
 */
interface PizzaRepository {

    /** Devuelve todas las pizzas cargadas correctamente. */
    fun obtenerTodas(): List<Pizza>

    /** Devuelve los errores encontrados al cargar el catálogo. */
    fun erroresDeCarga(): List<String>
}
