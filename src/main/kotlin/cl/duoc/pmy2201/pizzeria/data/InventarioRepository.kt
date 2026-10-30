package cl.duoc.pmy2201.pizzeria.data

/**
 * Acceso al inventario de ingredientes.
 */
interface InventarioRepository {

    /** Devuelve el inventario en memoria: nombre del ingrediente → cantidad disponible. */
    fun obtenerInventario(): MutableMap<String, Int>

    /** Devuelve la unidad del [ingrediente], o `null` si no está en el inventario. */
    fun unidadDe(ingrediente: String): String?

    /** Devuelve los errores encontrados al cargar el inventario. */
    fun erroresDeCarga(): List<String>
}
