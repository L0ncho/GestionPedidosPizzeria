package cl.duoc.pmy2201.pizzeria.service

import cl.duoc.pmy2201.pizzeria.data.InventarioRepository
import cl.duoc.pmy2201.pizzeria.model.ItemPedido

/**
 * Calcula, verifica, descuenta y reabastece los ingredientes del inventario.
 */
class InventarioService(
    private val inventarioRepository: InventarioRepository
) {

    /**
     * Multiplica cada cantidad de la receta [ingredientes] por [cantidadPizzas] usando [Math.multiplyExact].
     *
     * @throws ArithmeticException si algún producto supera el máximo de Int.
     */
    fun calcularIngredientesLote(ingredientes: Map<String, Int>, cantidadPizzas: Int): Map<String, Int> =
        ingredientes.mapValues { entrada: Map.Entry<String, Int> -> Math.multiplyExact(entrada.value, cantidadPizzas) }

    /**
     * Suma los ingredientes necesarios para todos los [items] del pedido usando [Math.addExact].
     *
     * @throws ArithmeticException si alguna cantidad calculada o acumulada supera el máximo de Int.
     */
    fun calcularNecesarios(items: List<ItemPedido>): Map<String, Int> {
        val necesarios: MutableMap<String, Int> = mutableMapOf()

        items.forEach { item: ItemPedido ->
            calcularIngredientesLote(item.producto.ingredientes, item.cantidad).forEach { (ingrediente: String, cantidad: Int) ->
                necesarios[ingrediente] = Math.addExact(necesarios[ingrediente] ?: 0, cantidad)
            }
        }

        return necesarios.toMap()
    }

    /**
     * Verifica si el inventario alcanza para los [items]; no modifica el inventario.
     *
     * @throws ArithmeticException si las cantidades necesarias superan el máximo de Int (ver [calcularNecesarios]).
     */
    fun verificar(items: List<ItemPedido>): ResultadoVerificacion {
        val inventario: Map<String, Int> = inventarioRepository.obtenerInventario()
        val faltantes: MutableMap<String, Int> = mutableMapOf()

        calcularNecesarios(items).forEach { (ingrediente: String, cantidadNecesaria: Int) ->
            val disponible: Int? = inventario[ingrediente]
            when {
                disponible == null -> faltantes[ingrediente] = cantidadNecesaria
                disponible < cantidadNecesaria -> faltantes[ingrediente] = cantidadNecesaria - disponible
            }
        }

        return if (faltantes.isEmpty()) {
            ResultadoVerificacion.Disponible
        } else {
            ResultadoVerificacion.Insuficiente(faltantes = faltantes.toMap())
        }
    }

    /**
     * Descuenta del inventario las cantidades [necesarios] en una sola operación de todo o nada:
     * primero valida todos los ingredientes y solo si todos son válidos los descuenta.
     * Si alguna validación falla, no se modifica ningún ingrediente.
     *
     * @throws IllegalArgumentException si alguna cantidad es menor o igual a 0, si un ingrediente
     * no existe en el inventario o si su stock disponible es menor que la cantidad a descontar.
     */
    fun descontar(necesarios: Map<String, Int>) {
        val inventario: MutableMap<String, Int> = inventarioRepository.obtenerInventario()

        necesarios.forEach { (ingrediente: String, cantidad: Int) ->
            require(cantidad > 0) {
                "La cantidad a descontar de '$ingrediente' debe ser mayor que 0 (se recibió $cantidad)."
            }
            val disponible: Int? = inventario[ingrediente]
            require(disponible != null) { "El ingrediente '$ingrediente' no existe en el inventario." }
            require(disponible >= cantidad) {
                "Stock insuficiente de '$ingrediente': disponible $disponible, se necesitan $cantidad."
            }
        }

        necesarios.forEach { (ingrediente: String, cantidad: Int) ->
            inventario[ingrediente] = inventario.getValue(ingrediente) - cantidad
        }
    }

    /**
     * Calcula con [calcularNecesarios] las cantidades acumuladas de todos los [items] del pedido,
     * las descuenta del inventario con una sola llamada a [descontar] y retorna lo descontado.
     *
     * @throws IllegalArgumentException si el inventario no alcanza; en ese caso no se modifica ningún ingrediente.
     * @throws ArithmeticException si las cantidades acumuladas superan el máximo de Int; no se modifica ningún ingrediente.
     */
    fun prepararLote(items: List<ItemPedido>): Map<String, Int> {
        val necesarios: Map<String, Int> = calcularNecesarios(items)
        descontar(necesarios)
        return necesarios
    }

    /**
     * Suma [cantidad] al stock de [ingrediente] usando [Math.addExact].
     *
     * @throws IllegalArgumentException si [cantidad] es menor o igual a 0 o si [ingrediente] no existe en el inventario.
     * @throws ArithmeticException si el nuevo stock supera el máximo de Int; en ese caso el stock no cambia.
     */
    fun reabastecer(ingrediente: String, cantidad: Int) {
        val inventario: MutableMap<String, Int> = inventarioRepository.obtenerInventario()
        val disponible: Int? = inventario[ingrediente]

        when {
            cantidad <= 0 ->
                throw IllegalArgumentException("La cantidad a reabastecer debe ser mayor que 0 (se recibió $cantidad).")
            disponible == null ->
                throw IllegalArgumentException("El ingrediente '$ingrediente' no existe en el inventario.")
            else ->
                inventario[ingrediente] = Math.addExact(disponible, cantidad)
        }
    }
}
