package cl.duoc.pmy2201.pizzeria.ui

import cl.duoc.pmy2201.pizzeria.model.EstadoPedido
import cl.duoc.pmy2201.pizzeria.model.PORCENTAJE_DESCUENTO
import cl.duoc.pmy2201.pizzeria.model.Pedido
import cl.duoc.pmy2201.pizzeria.service.PedidoService
import cl.duoc.pmy2201.pizzeria.service.ResultadoVerificacion
import cl.duoc.pmy2201.pizzeria.util.CORREO_POR_DEFECTO
import kotlin.math.roundToInt

/**
 * Mensajes por consola del sistema de gestión de pedidos.
 */
class MensajesPedidoConsola {

    fun mostrarBienvenida() {
        println("Sistema de gestión - Pizzería (uso interno)")
    }

    /** Muestra [errores] como lista; no muestra nada si está vacía. */
    fun mostrarErroresDeCarga(errores: List<String>) {
        if (errores.isEmpty()) return
        println()
        println("Errores de carga:")
        errores.forEach { error: String -> println("- $error") }
    }

    fun mostrarSinProductos() {
        println("No hay productos disponibles en el catálogo. El programa finaliza.")
    }

    fun mostrarDespedida() {
        println("Sesión finalizada. ¡Hasta luego!")
    }

    fun mostrarMenuPrincipal() {
        println()
        println("Menú principal")
        println("1. Registrar pedido")
        println("2. Reporte del día")
        println("3. Cambiar estado de un pedido")
        println("4. Inventario")
        println("5. Resumen de funcionalidades")
        println("6. Salir")
    }

    /**
     * Lista los ingredientes faltantes de [resultado], por ejemplo "Queso azul (faltan 30 g)",
     * y explica cómo reabastecer. [unidadDe] entrega la unidad de cada ingrediente, o null si no se conoce.
     */
    fun mostrarIngredientesInsuficientes(resultado: ResultadoVerificacion.Insuficiente, unidadDe: (String) -> String?) {
        println()
        println("No hay ingredientes suficientes para el pedido. Faltan:")
        resultado.ingredientesFaltantes.forEach { ingrediente: String ->
            val cantidad: Int = resultado.faltantes.getValue(ingrediente)
            val unidad: String = unidadDe(ingrediente) ?: ""
            println("- $ingrediente (faltan ${"$cantidad $unidad".trim()})")
        }
        println()
        println("Para reabastecer:")
        println("1. Cancelar el pedido y volver al menú principal.")
        println("2. Elegir la opción 4. Inventario.")
        println("3. Seleccionar el ingrediente e ingresar la cantidad recibida.")
    }

    fun mostrarDescuentoAplicado() {
        val porcentajeDescuento: Int = (PORCENTAJE_DESCUENTO * 100).roundToInt()
        println("Se aplicó un $porcentajeDescuento% de descuento al pedido.")
    }

    fun mostrarPedidoCancelado() {
        println("Pedido cancelado.")
    }

    fun mostrarSinRepartidor() {
        println("No hay repartidores disponibles en este momento. El pedido queda en preparación sin repartidor asignado.")
    }

    /**
     * Simula el envío de una notificación al correo del cliente. No se envía si el cliente
     * no tiene correo o si tiene [CORREO_POR_DEFECTO].
     */
    fun mostrarNotificacion(pedido: Pedido) {
        pedido.cliente.correo
            ?.takeIf { correo: String -> correo != CORREO_POR_DEFECTO }
            ?.let { correo: String ->
                println("Notificación enviada a $correo: Su pedido N° ${pedido.idPedido} está en estado ${pedido.estado.descripcion}.")
            }
            ?: println("No se envió notificación: el cliente no tiene correo registrado.")
    }

    /** Lista los [pedidos] pendientes con su N° y estado. */
    fun mostrarPedidosPendientes(pedidos: List<Pedido>) {
        println()
        println("Pedidos pendientes:")
        pedidos.forEach { pedido: Pedido -> println("N° ${pedido.idPedido} - ${pedido.estado.descripcion}") }
    }

    fun mostrarSinPedidosPendientes() {
        println("No hay pedidos pendientes.")
    }

    fun mostrarPedidoNoEncontrado() {
        println("Pedido no encontrado.")
    }

    fun mostrarNoSePuedeDespachar() {
        println("No se puede despachar: el pedido no tiene repartidor asignado.")
    }

    fun mostrarNoSePuedeEntregar() {
        println("No se puede entregar: el pedido no tiene repartidor asignado.")
    }

    fun mostrarPedidoYaEntregado(pedido: Pedido) {
        println("El pedido N° ${pedido.idPedido} ya fue entregado.")
    }

    fun mostrarCambioEstado(pedido: Pedido, estadoAnterior: EstadoPedido) {
        println("Pedido N° ${pedido.idPedido}: ${estadoAnterior.descripcion} → ${pedido.estado.descripcion}")
    }

    fun mostrarSinCapacidad() {
        println("Se alcanzó la capacidad máxima de ${PedidoService.CAPACIDAD_PEDIDOS_DIA} pedidos del día. No se pueden registrar más pedidos.")
    }
}
