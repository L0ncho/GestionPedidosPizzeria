package cl.duoc.pmy2201.pizzeria.ui

import cl.duoc.pmy2201.pizzeria.model.Cliente
import cl.duoc.pmy2201.pizzeria.model.ItemPedido
import cl.duoc.pmy2201.pizzeria.model.Pedido
import cl.duoc.pmy2201.pizzeria.model.TASA_IVA
import cl.duoc.pmy2201.pizzeria.model.TotalesPedido
import cl.duoc.pmy2201.pizzeria.util.FormatoMoneda
import kotlin.math.roundToInt

/**
 * Muestra por consola el resumen completo de un pedido.
 */
class ResumenPedidoConsola {

    fun mostrar(pedido: Pedido) {
        val cliente: Cliente = pedido.cliente
        val totales: TotalesPedido = pedido.calcularTotal()
        val porcentajeIva: Int = (TASA_IVA * 100).roundToInt()

        println()
        println(SEPARADOR)
        println("RESUMEN DEL PEDIDO N° ${pedido.idPedido}")
        println(SEPARADOR)

        println("Cliente")
        println("  Nombre: ${cliente.nombre}")
        println("  Teléfono: ${cliente.telefonoParaMostrar()}")
        println("  Correo: ${cliente.correo ?: "Sin correo"}")
        println("  Dirección: ${cliente.direccion}")

        println(SEPARADOR_SECCION)
        println("Productos")
        for (item: ItemPedido in pedido.items) {
            val nombre: String = item.producto.nombre
            val precioUnitario: String = FormatoMoneda.formatear(item.producto.precio)
            val subtotalItem: String = FormatoMoneda.formatear(item.subtotal())
            println("  $nombre x ${item.cantidad} - $precioUnitario c/u - Subtotal: $subtotalItem")
        }

        println(SEPARADOR_SECCION)
        println("Notas: ${pedido.notasParaMostrar}")

        println(SEPARADOR_SECCION)
        println("Subtotal: ${FormatoMoneda.formatear(totales.subtotal)}")
        if (totales.descuento > 0) {
            println("Descuento: -${FormatoMoneda.formatear(totales.descuento)}")
        }
        println("Total: ${FormatoMoneda.formatear(totales.total)}")
        println("Neto: ${FormatoMoneda.formatear(totales.neto)}")
        println("IVA $porcentajeIva%: ${FormatoMoneda.formatear(totales.iva)}")

        println(SEPARADOR_SECCION)
        println("Estado: ${pedido.estado.descripcion}")
        println("Repartidor: ${pedido.repartidor?.nombre ?: "Sin repartidor disponible"}")
        println(SEPARADOR)
    }

    private companion object {
        val SEPARADOR: String = "=".repeat(40)
        val SEPARADOR_SECCION: String = "-".repeat(40)
    }
}
