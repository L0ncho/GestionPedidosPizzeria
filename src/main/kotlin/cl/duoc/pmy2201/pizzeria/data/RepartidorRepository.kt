package cl.duoc.pmy2201.pizzeria.data

import cl.duoc.pmy2201.pizzeria.model.Repartidor

/**
 * Acceso a la lista de repartidores.
 */
interface RepartidorRepository {

    /** Devuelve todos los repartidores cargados correctamente. */
    fun obtenerTodos(): List<Repartidor>

    /** Devuelve los errores encontrados al cargar los repartidores. */
    fun erroresDeCarga(): List<String>
}
