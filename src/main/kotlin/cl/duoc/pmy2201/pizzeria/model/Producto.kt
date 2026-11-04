package cl.duoc.pmy2201.pizzeria.model

import cl.duoc.pmy2201.pizzeria.util.FormatoMoneda

/**
 * Producto del menú con su receta.
 *
 * @property nombre Nombre del producto.
 * @property precio Precio unitario, con IVA incluido.
 * @property ingredientes Receta: nombre del ingrediente → cantidad para una unidad.
 * @property categoria Categoría a la que pertenece el producto.
 */
open class Producto(
    val nombre: String,
    val precio: Double,
    val ingredientes: Map<String, Int>,
    val categoria: Categoria
) {

    /**
     * Retorna una descripción completa, por ejemplo:
     * "Pepperoni - $11.990 | Categoría: Pizza | Ingredientes: Masa (1), Salsa de tomate (150) | Promocionable".
     * El sufijo " | Promocionable" solo se agrega si [esPromocionable] es verdadero.
     */
    fun detallarProducto(): String {
        val detalleIngredientes: String = ingredientes.entries.joinToString(", ") { ingrediente: Map.Entry<String, Int> ->
            "${ingrediente.key} (${ingrediente.value})"
        }
        val detallePromocion: String = if (esPromocionable(this)) " | Promocionable" else ""
        return "$nombre - ${FormatoMoneda.formatear(precio)} | Categoría: ${categoria.descripcion} | Ingredientes: $detalleIngredientes$detallePromocion"
    }

    /**
     * Dos productos son iguales si tienen el mismo nombre (sin distinguir mayúsculas ni espacios
     * en los extremos) y la misma [categoria]; el precio y los ingredientes no se comparan.
     * El operador `==` de Kotlin llama a este método, por lo que esta implementación define
     * cómo se comparan los productos con `==`.
     * Usa `is Producto` para que también funcione con futuras subclases.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is Producto &&
            nombreNormalizado() == other.nombreNormalizado() &&
            categoria == other.categoria
    }

    /**
     * Calcula el hash con los mismos atributos normalizados que [equals], de modo que dos productos
     * iguales tengan el mismo hash (necesario para usarlos en `Set` o como claves de `Map`).
     */
    override fun hashCode(): Int {
        val hashNombre: Int = nombreNormalizado().hashCode()
        return 31 * hashNombre + categoria.hashCode()
    }

    private fun nombreNormalizado(): String = nombre.trim().lowercase()
}
