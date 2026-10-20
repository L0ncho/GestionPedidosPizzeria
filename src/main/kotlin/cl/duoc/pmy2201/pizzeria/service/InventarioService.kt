package cl.duoc.pmy2201.pizzeria.service

import cl.duoc.pmy2201.pizzeria.model.Pizza

/**
 * Verifica y descuenta el stock de ingredientes de las pizzas.
 */
class InventarioService {

    /**
     * Si hay porciones suficientes para [cantidadPizzas], las descuenta de [pizza] y retorna
     * [ResultadoStock.Confirmado]; si no, retorna [ResultadoStock.Rechazado] sin modificar el stock.
     */
    fun verificarYDescontar(pizza: Pizza, cantidadPizzas: Int): ResultadoStock {
        val porcionesRequeridas: Int = cantidadPizzas * PORCIONES_POR_PIZZA

        return if (pizza.ingredientesDisponibles >= porcionesRequeridas) {
            pizza.ingredientesDisponibles -= porcionesRequeridas
            ResultadoStock.Confirmado(stockRestante = pizza.ingredientesDisponibles)
        } else {
            ResultadoStock.Rechazado(
                stockDisponible = pizza.ingredientesDisponibles,
                cantidadSolicitada = cantidadPizzas
            )
        }
    }

    companion object {
        const val PORCIONES_POR_PIZZA: Int = 1
    }
}
