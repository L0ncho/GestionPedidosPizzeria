package cl.duoc.pmy2201.pizzeria.model

import cl.duoc.pmy2201.pizzeria.util.FormatoMoneda

/**
 * Producto del menú con su receta.
 *
 * @property nombre Nombre del producto.
 * @property precio Precio unitario, con IVA incluido.
 * @property ingredientes Receta: nombre del ingrediente → cantidad para una unidad.
 */
class Producto(
    val nombre: String,
    val precio: Double,
    val ingredientes: Map<String, Int>
) {

    /**
     * Retorna una descripción completa, por ejemplo: "Pepperoni - $11.990 | Ingredientes: Masa (1), Salsa de tomate (150)".
     */
    fun detallarProducto(): String {
        val detalleIngredientes: String = ingredientes.entries.joinToString(", ") { ingrediente: Map.Entry<String, Int> ->
            "${ingrediente.key} (${ingrediente.value})"
        }
        return "$nombre - ${FormatoMoneda.formatear(precio)} | Ingredientes: $detalleIngredientes"
    }
}
