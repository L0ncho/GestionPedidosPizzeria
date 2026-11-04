package cl.duoc.pmy2201.pizzeria.model

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CalculoTotalesTest {

    private val tolerancia: Double = 0.01

    private val fechaFueraDePromocion: LocalDate = LocalDate.of(2026, 11, 18)
    private val fechaEnPromocion: LocalDate = LocalDate.of(2026, 11, 4)

    private val pepperoni: PizzaClasica = PizzaClasica(
        nombre = "Pepperoni",
        precio = 11990.0,
        ingredientes = mapOf("Masa" to 1, "Pepperoni" to 80)
    )

    private val cuatroQuesos: PizzaEspecial = PizzaEspecial(
        nombre = "Cuatro Quesos",
        precio = 13990.0,
        ingredientes = mapOf("Queso mozzarella" to 150, "Queso azul" to 60)
    )

    @Test
    fun tresPizzasNoTienenDescuento() {
        val totales: TotalesPedido = calcularTotales(
            subtotal = 35970.0,
            cantidadPizzas = 3,
            productos = listOf(pepperoni),
            fecha = fechaFueraDePromocion
        )

        assertEquals(35970.0, totales.subtotal, tolerancia)
        assertEquals(0.0, totales.descuentoCantidad, tolerancia)
        assertEquals(0.0, totales.descuentoPromocion, tolerancia)
        assertEquals(35970.0, totales.total, tolerancia)
    }

    @Test
    fun cuatroPepperoniTienenDescuentoDel10PorCiento() {
        val totales: TotalesPedido = calcularTotales(
            subtotal = 4 * 11990.0,
            cantidadPizzas = 4,
            productos = listOf(pepperoni),
            fecha = fechaFueraDePromocion
        )

        assertEquals(47960.0, totales.subtotal, tolerancia)
        assertEquals(4796.0, totales.descuentoCantidad, tolerancia)
        assertEquals(0.0, totales.descuentoPromocion, tolerancia)
        assertEquals(43164.0, totales.total, tolerancia)
    }

    @Test
    fun cuatroPepperoniEnPeriodoDePromocionSumanAmbosDescuentos() {
        val totales: TotalesPedido = calcularTotales(
            subtotal = 4 * 11990.0,
            cantidadPizzas = 4,
            productos = listOf(pepperoni),
            fecha = fechaEnPromocion
        )

        assertEquals(47960.0, totales.subtotal, tolerancia)
        assertEquals(4796.0, totales.descuentoCantidad, tolerancia)
        assertEquals(7194.0, totales.descuentoPromocion, tolerancia)
        assertEquals(35970.0, totales.total, tolerancia)
    }

    @Test
    fun pepperoniFueraDelPeriodoNoTienePromocion() {
        val totales: TotalesPedido = calcularTotales(
            subtotal = 11990.0,
            cantidadPizzas = 1,
            productos = listOf(pepperoni),
            fecha = fechaFueraDePromocion
        )

        assertEquals(0.0, totales.descuentoPromocion, tolerancia)
        assertEquals(11990.0, totales.total, tolerancia)
    }

    @Test
    fun pizzaEspecialEnPeriodoNoTienePromocion() {
        val totales: TotalesPedido = calcularTotales(
            subtotal = 13990.0,
            cantidadPizzas = 1,
            productos = listOf(cuatroQuesos),
            fecha = fechaEnPromocion
        )

        assertEquals(0.0, totales.descuentoPromocion, tolerancia)
        assertEquals(13990.0, totales.total, tolerancia)
    }

    @Test
    fun limitesDelPeriodoEstanDentroDeLaPromocion() {
        assertTrue(estaEnPeriodoPromocion(LocalDate.of(2026, 11, 3)))
        assertTrue(estaEnPeriodoPromocion(LocalDate.of(2026, 11, 17)))
    }

    @Test
    fun diasFueraDelPeriodoNoEstanEnLaPromocion() {
        assertFalse(estaEnPeriodoPromocion(LocalDate.of(2026, 11, 2)))
        assertFalse(estaEnPeriodoPromocion(LocalDate.of(2026, 11, 18)))
    }
}
