package cl.duoc.pmy2201.pizzeria.service

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CalculadoraPedidoTest {

    private val calculadora: CalculadoraPedido = CalculadoraPedido()
    private val tolerancia: Double = 0.01

    @Test
    fun tresPizzasNoTienenDescuento() {
        val resultado: ResultadoCalculo = calculadora.calcular(precioUnitario = 11990.0, cantidadPizzas = 3)

        assertFalse(calculadora.aplicaDescuento(3))
        assertEquals(35970.0, resultado.subtotal, tolerancia)
        assertEquals(0.0, resultado.descuento, tolerancia)
        assertEquals(35970.0, resultado.total, tolerancia)
    }

    @Test
    fun cuatroPepperoniTienenDescuentoDel10PorCiento() {
        val resultado: ResultadoCalculo = calculadora.calcular(precioUnitario = 11990.0, cantidadPizzas = 4)

        assertTrue(calculadora.aplicaDescuento(4))
        assertEquals(47960.0, resultado.subtotal, tolerancia)
        assertEquals(4796.0, resultado.descuento, tolerancia)
        assertEquals(43164.0, resultado.total, tolerancia)
    }
}
