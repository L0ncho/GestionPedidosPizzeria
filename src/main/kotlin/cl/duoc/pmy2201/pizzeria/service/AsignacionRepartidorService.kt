package cl.duoc.pmy2201.pizzeria.service

import cl.duoc.pmy2201.pizzeria.data.RepartidorRepository
import cl.duoc.pmy2201.pizzeria.model.Repartidor

/**
 * Asigna repartidores disponibles a los pedidos.
 */
class AsignacionRepartidorService(private val repartidorRepository: RepartidorRepository) {

    /**
     * Toma el primer repartidor disponible, lo marca como no disponible y lo retorna.
     * Retorna null si no hay ninguno disponible.
     */
    fun asignar(): Repartidor? {
        val repartidor: Repartidor? = repartidorRepository.obtenerTodos()
            .firstOrNull { candidato: Repartidor -> candidato.disponible }
        repartidor?.disponible = false
        return repartidor
    }
}
