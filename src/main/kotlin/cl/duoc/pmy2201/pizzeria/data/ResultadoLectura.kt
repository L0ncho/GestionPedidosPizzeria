package cl.duoc.pmy2201.pizzeria.data

/**
 * Resultado de leer un archivo CSV.
 *
 * @property filas Filas de datos (sin encabezado), cada una como lista de valores.
 * @property errores Mensajes de error encontrados durante la lectura.
 */
data class ResultadoLectura(
    val filas: List<List<String>>,
    val errores: List<String>
)
