package cl.duoc.pmy2201.pizzeria.service

import cl.duoc.pmy2201.pizzeria.model.Pizza
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class InventarioServiceTest {

    private val inventarioService: InventarioService = InventarioService()

    @Test
    fun stockJustoConfirmaYDejaStockEnCero() {
        val pizza: Pizza = Pizza(nombrePizza = "Cuatro Quesos", precioUnitario = 13990.0, ingredientesDisponibles = 2)

        val resultado: ResultadoStock = inventarioService.verificarYDescontar(pizza, cantidadPizzas = 2)

        val confirmado: ResultadoStock.Confirmado = assertIs<ResultadoStock.Confirmado>(resultado)
        assertEquals(0, confirmado.stockRestante)
        assertEquals(0, pizza.ingredientesDisponibles)
    }

    @Test
    fun stockInsuficienteRechazaSinModificarStock() {
        val pizza: Pizza = Pizza(nombrePizza = "Cuatro Quesos", precioUnitario = 13990.0, ingredientesDisponibles = 2)

        val resultado: ResultadoStock = inventarioService.verificarYDescontar(pizza, cantidadPizzas = 3)

        val rechazado: ResultadoStock.Rechazado = assertIs<ResultadoStock.Rechazado>(resultado)
        assertEquals(2, rechazado.stockDisponible)
        assertEquals(3, rechazado.cantidadSolicitada)
        assertEquals(2, pizza.ingredientesDisponibles)
    }
}
