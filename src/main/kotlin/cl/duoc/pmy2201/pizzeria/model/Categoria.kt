package cl.duoc.pmy2201.pizzeria.model

/**
 * Categorías a las que puede pertenecer un [Producto].
 *
 * @property descripcion Texto legible de la categoría.
 */
enum class Categoria(val descripcion: String) {
    PIZZA("Pizza"),
    BEBIDA("Bebida"),
    INGREDIENTE("Ingrediente")
}
