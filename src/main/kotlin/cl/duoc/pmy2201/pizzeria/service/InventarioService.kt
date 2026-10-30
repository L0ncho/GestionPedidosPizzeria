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
     * Multiplica cada cantidad de la receta [ingredientes] por [cantidadPizzas].
     */
    fun calcularIngredientesLote(ingredientes: Map<String, Int>, cantidadPizzas: Int): Map<String, Int> =
        ingredientes.mapValues { entrada: Map.Entry<String, Int> -> entrada.value * cantidadPizzas }

    /**
     * Suma los ingredientes necesarios para todos los [items] del pedido.
     */
    fun calcularNecesarios(items: List<ItemPedido>): Map<String, Int> {
        val necesarios: MutableMap<String, Int> = mutableMapOf()

        items.forEach { item: ItemPedido ->
            calcularIngredientesLote(item.producto.ingredientes, item.cantidad).forEach { (ingrediente: String, cantidad: Int) ->
                necesarios[ingrediente] = (necesarios[ingrediente] ?: 0) + cantidad
            }
        }

        return necesarios.toMap()
    }

    /**
     * Verifica si el inventario alcanza para los [items]; no modifica el inventario.
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
     * Descuenta del inventario las cantidades [necesarios]. Se espera que antes se haya llamado a [verificar];
     * los ingredientes que no existen en el inventario se ignoran.
     */
    fun descontar(necesarios: Map<String, Int>) {
        val inventario: MutableMap<String, Int> = inventarioRepository.obtenerInventario()

        necesarios.forEach { (ingrediente: String, cantidad: Int) ->
            val disponible: Int? = inventario[ingrediente]
            if (disponible != null) {
                inventario[ingrediente] = disponible - cantidad
            }
        }
    }

    /**
     * Calcula los ingredientes para [cantidadPizzas] según la receta [ingredientes], los descuenta
     * del inventario y retorna lo usado. Se espera que antes se haya llamado a [verificar].
     */
    fun prepararLote(ingredientes: Map<String, Int>, cantidadPizzas: Int): Map<String, Int> {
        val usados: Map<String, Int> = calcularIngredientesLote(ingredientes, cantidadPizzas)
        descontar(usados)
        return usados
    }

    /**
     * Suma [cantidad] al stock de [ingrediente].
     *
     * @throws IllegalArgumentException si [cantidad] es menor o igual a 0 o si [ingrediente] no existe en el inventario.
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
                inventario[ingrediente] = disponible + cantidad
        }
    }
}
