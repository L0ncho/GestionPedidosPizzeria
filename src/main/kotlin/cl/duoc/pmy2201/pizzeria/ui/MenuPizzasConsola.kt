package cl.duoc.pmy2201.pizzeria.ui

import cl.duoc.pmy2201.pizzeria.model.Pizza
import cl.duoc.pmy2201.pizzeria.service.CalculadoraPedido
import kotlin.math.roundToInt

/**
 * Muestra el catálogo de pizzas por consola y permite elegir una.
 */
class MenuPizzasConsola(private val lector: LectorConsola) {

    /**
     * Muestra [pizzas] como lista numerada y retorna la pizza elegida.
     * [pizzas] no debe estar vacía.
     */
    fun elegirPizza(pizzas: List<Pizza>): Pizza {
        println()
        println("Catálogo de pizzas")
        val porcentajeDescuento: Int = (CalculadoraPedido.PORCENTAJE_DESCUENTO * 100).roundToInt()
        println("Promoción: $porcentajeDescuento% de descuento en pedidos de más de ${CalculadoraPedido.UMBRAL_PIZZAS_DESCUENTO} pizzas")
        pizzas.forEachIndexed { indice: Int, pizza: Pizza ->
            println("${indice + 1}. ${pizza.nombrePizza} - ${FormatoMoneda.formatear(pizza.precioUnitario)} (stock: ${pizza.ingredientesDisponibles})")
        }

        val opcion: Int = lector.leerEnteroEnRango("Elija una pizza: ", 1, pizzas.size)
        return pizzas[opcion - 1]
    }
}
