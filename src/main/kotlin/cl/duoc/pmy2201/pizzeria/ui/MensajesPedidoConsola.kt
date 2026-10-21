package cl.duoc.pmy2201.pizzeria.ui

import cl.duoc.pmy2201.pizzeria.model.Pedido
import cl.duoc.pmy2201.pizzeria.model.Pizza
import cl.duoc.pmy2201.pizzeria.service.CalculadoraPedido
import cl.duoc.pmy2201.pizzeria.service.ResultadoStock
import kotlin.math.roundToInt

/**
 * Mensajes por consola del flujo de un pedido.
 */
class MensajesPedidoConsola(private val lector: LectorConsola) {

    fun mostrarBienvenida() {
        println("Pizzería - Gestión de Pedidos")
    }

    /** Muestra [errores] como lista; no muestra nada si está vacía. */
    fun mostrarErroresDeCarga(errores: List<String>) {
        if (errores.isEmpty()) return
        println()
        println("Errores de carga:")
        errores.forEach { error: String -> println("- $error") }
    }

    fun mostrarSinPizzas() {
        println("No hay pizzas disponibles en el catálogo. El programa finaliza.")
    }

    /**
     * Informa el resultado de la verificación de stock.
     * Retorna true si el pedido quedó confirmado.
     */
    fun mostrarResultadoStock(pizza: Pizza, resultado: ResultadoStock): Boolean {
        println()
        return when (resultado) {
            is ResultadoStock.Rechazado -> {
                println("No hay ingredientes suficientes para ${pizza.nombrePizza}.")
                println("Solicitadas: ${resultado.cantidadSolicitada} | Disponibles: ${resultado.stockDisponible}")
                println("El pedido no puede completarse con la cantidad solicitada.")
                false
            }
            is ResultadoStock.Confirmado -> {
                println("Pedido confirmado")
                println("Stock restante de ${pizza.nombrePizza}: ${resultado.stockRestante}")
                true
            }
        }
    }

    /**
     * Pregunta si se desea modificar el pedido, aceptando solo s o n.
     * Retorna true si la respuesta es s. Si la entrada termina, se asume n.
     */
    fun preguntarModificarPedido(): Boolean {
        val respuesta: String = lector.leerTextoObligatorio(
            mensaje = "¿Desea modificar el pedido? (s/n): ",
            valorPorDefecto = "n",
            validacion = { texto: String -> texto.lowercase() in setOf("s", "n") },
            mensajeError = "Respuesta inválida. Ingrese s o n."
        ).lowercase()
        return respuesta == "s"
    }

    fun mostrarPedidoCancelado() {
        println("Pedido cancelado.")
    }

    fun mostrarDescuentoAplicado() {
        val porcentajeDescuento: Int = (CalculadoraPedido.PORCENTAJE_DESCUENTO * 100).roundToInt()
        println("¡Se aplicó un $porcentajeDescuento% de descuento a tu pedido!")
    }

    fun avisarSinRepartidor() {
        println("No hay repartidores disponibles en este momento. Tu pedido queda en preparación y será asignado cuando un repartidor se libere.")
    }

    fun mostrarNotificacionCliente(pedido: Pedido) {
        val telefono: String = pedido.cliente.telefono
        val mensaje: String = if (telefono == LectorConsola.TELEFONO_NO_INFORMADO) {
            "No se pudo enviar la notificación: teléfono no informado."
        } else {
            "Notificación enviada al cliente ($telefono): Su pedido N° ${pedido.idPedido} está en estado ${pedido.estadoPedido.descripcion}."
        }
        println(mensaje)
    }
}
