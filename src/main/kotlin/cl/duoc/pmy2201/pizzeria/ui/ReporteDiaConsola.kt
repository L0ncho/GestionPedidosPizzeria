package cl.duoc.pmy2201.pizzeria.ui

import cl.duoc.pmy2201.pizzeria.model.ItemPedido
import cl.duoc.pmy2201.pizzeria.model.Pedido
import cl.duoc.pmy2201.pizzeria.util.FormatoMoneda

/**
 * Muestra por consola el reporte de los pedidos del día.
 */
class ReporteDiaConsola {

    /**
     * Lista cada pedido (N°, cliente, pizzas, total y estado) y luego los agrupa en completados y pendientes.
     */
    fun mostrar(pedidos: Array<Pedido>) {
        println()
        println(SEPARADOR)
        println("REPORTE DEL DÍA")
        println(SEPARADOR)

        if (pedidos.isEmpty()) {
            println("No hay pedidos registrados hoy.")
            println(SEPARADOR)
            return
        }

        for (pedido: Pedido in pedidos) {
            val pizzas: String = pedido.items.joinToString(", ") { item: ItemPedido -> "${item.producto.nombre} x ${item.cantidad}" }
            println("Pedido N° ${pedido.idPedido}")
            println("  Cliente: ${pedido.cliente.nombre}")
            println("  Pizzas: $pizzas")
            println("  Total: ${FormatoMoneda.formatear(pedido.calcularTotal().total)}")
            println("  Estado: ${pedido.estado.descripcion}")
        }

        val completados: List<Pedido> = pedidos.filter { pedido: Pedido -> pedido.estado.completado }
        val pendientes: List<Pedido> = pedidos.filter { pedido: Pedido -> !pedido.estado.completado }

        println(SEPARADOR_SECCION)
        println("Completados (${completados.size}): ${numerosDePedido(completados)}")
        println("Pendientes (${pendientes.size}): ${numerosDePedido(pendientes)}")
        println(SEPARADOR)
    }

    private fun numerosDePedido(pedidos: List<Pedido>): String =
        if (pedidos.isEmpty()) {
            "-"
        } else {
            pedidos.joinToString(", ") { pedido: Pedido -> "N° ${pedido.idPedido}" }
        }

    private companion object {
        val SEPARADOR: String = "=".repeat(40)
        val SEPARADOR_SECCION: String = "-".repeat(40)
    }
}
