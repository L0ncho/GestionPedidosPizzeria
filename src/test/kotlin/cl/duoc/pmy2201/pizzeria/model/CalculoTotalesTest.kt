package cl.duoc.pmy2201.pizzeria.model

import kotlin.test.Test
import kotlin.test.assertEquals

class CalculoTotalesTest {

    private val tolerancia: Double = 0.01

    @Test
    fun tresPizzasNoTienenDescuento() {
        val totales: TotalesPedido = calcularTotales(subtotal = 35970.0, cantidadPizzas = 3)

        assertEquals(35970.0, totales.subtotal, tolerancia)
        assertEquals(0.0, totales.descuento, tolerancia)
        assertEquals(35970.0, totales.total, tolerancia)
    }

    @Test
    fun cuatroPepperoniTienenDescuentoDel10PorCiento() {
        val totales: TotalesPedido = calcularTotales(subtotal = 4 * 11990.0, cantidadPizzas = 4)

        assertEquals(47960.0, totales.subtotal, tolerancia)
        assertEquals(4796.0, totales.descuento, tolerancia)
        assertEquals(43164.0, totales.total, tolerancia)
    }
}
