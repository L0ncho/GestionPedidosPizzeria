package cl.duoc.pmy2201.pizzeria.model

/**
 * Pizza clásica del menú; su categoría siempre es [Categoria.PIZZA] y califica para promociones especiales.
 *
 * @param nombre Nombre de la pizza.
 * @param precio Precio unitario, con IVA incluido.
 * @param ingredientes Receta: nombre del ingrediente → cantidad para una unidad.
 */
@Promocionable
class PizzaClasica(
    nombre: String,
    precio: Double,
    ingredientes: Map<String, Int>
) : Producto(
    nombre = nombre,
    precio = precio,
    ingredientes = ingredientes,
    categoria = Categoria.PIZZA
)
