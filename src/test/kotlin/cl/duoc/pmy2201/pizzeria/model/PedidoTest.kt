package cl.duoc.pmy2201.pizzeria.model

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class PedidoTest {

    private val tolerancia: Double = 0.01

    private val fecha: LocalDate = LocalDate.of(2026, 11, 4)

    private val pepperoni: PizzaClasica = PizzaClasica(
        nombre = "Pepperoni",
        precio = 11990.0,
        ingredientes = mapOf("Masa" to 1, "Pepperoni" to 80)
    )

    private val napolitana: PizzaClasica = PizzaClasica(
        nombre = "Napolitana",
        precio = 10990.0,
        ingredientes = mapOf("Masa" to 1, "Tomate" to 150)
    )

    private val ana: Cliente = Cliente(
        nombre = "Ana Pérez",
        telefono = "+56931717552",
        correo = "ana@example.com",
        direccion = "Av. Siempre Viva 123"
    )

    private val pedro: Cliente = Cliente(
        nombre = "Pedro Soto",
        telefono = "+56223456789",
        correo = "pedro@example.com",
        direccion = "Calle Falsa 456"
    )

    private fun crearPedido(idPedido: Int, cliente: Cliente, producto: Producto, cantidad: Int): Pedido = Pedido(
        idPedido = idPedido,
        cliente = cliente,
        items = listOf(ItemPedido(producto = producto, cantidad = cantidad)),
        notas = null,
        fecha = fecha
    )

    @Test
    fun sumarPepperoniYNapolitanaDelMismoClienteCombinaLineasYRecalculaTotal() {
        val pedidoPepperoni: Pedido = crearPedido(idPedido = 1, cliente = ana, producto = pepperoni, cantidad = 2)
        val pedidoNapolitana: Pedido = crearPedido(idPedido = 2, cliente = ana, producto = napolitana, cantidad = 2)

        val combinado: Pedido = pedidoPepperoni + pedidoNapolitana

        assertEquals(2, combinado.items.size)
        assertEquals(4, combinado.cantidadPizzas())
        assertEquals(34470.0, combinado.calcularTotal().total, tolerancia)
    }

    @Test
    fun sumarPepperoniConPepperoniDejaUnaSolaLineaConCantidadSumada() {
        val primero: Pedido = crearPedido(idPedido = 1, cliente = ana, producto = pepperoni, cantidad = 2)
        val segundo: Pedido = crearPedido(idPedido = 2, cliente = ana, producto = pepperoni, cantidad = 2)

        val combinado: Pedido = primero + segundo

        assertEquals(1, combinado.items.size)
        assertEquals(pepperoni, combinado.items.first().producto)
        assertEquals(4, combinado.items.first().cantidad)
    }

    @Test
    fun sumarPedidosDeClientesDistintosLanzaExcepcion() {
        val pedidoAna: Pedido = crearPedido(idPedido = 1, cliente = ana, producto = pepperoni, cantidad = 2)
        val pedidoPedro: Pedido = crearPedido(idPedido = 2, cliente = pedro, producto = pepperoni, cantidad = 2)

        assertFailsWith<IllegalArgumentException> {
            pedidoAna + pedidoPedro
        }
    }

    @Test
    fun desestructuracionEntregaClienteProductosYTotal() {
        val pedido: Pedido = crearPedido(idPedido = 1, cliente = ana, producto = pepperoni, cantidad = 2)

        val (cliente: Cliente, productos: List<ItemPedido>, total: Double) = pedido

        assertSame(ana, cliente)
        assertEquals(pedido.items, productos)
        assertEquals(pedido.calcularTotal().total, total, tolerancia)
    }
}
