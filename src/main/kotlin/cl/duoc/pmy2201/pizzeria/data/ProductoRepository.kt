package cl.duoc.pmy2201.pizzeria.data

import cl.duoc.pmy2201.pizzeria.model.Producto

/**
 * Acceso al catálogo de productos.
 */
interface ProductoRepository {

    /** Devuelve todos los productos cargados correctamente. */
    fun obtenerTodos(): List<Producto>

    /** Devuelve los errores encontrados al cargar el catálogo. */
    fun erroresDeCarga(): List<String>
}
