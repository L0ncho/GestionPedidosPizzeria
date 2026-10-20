package cl.duoc.pmy2201.pizzeria.data

import cl.duoc.pmy2201.pizzeria.model.Repartidor

/**
 * Repositorio de repartidores que lee `repartidores.csv` una sola vez y mantiene los datos en memoria.
 */
class RepartidorRepositoryCsv(
    private val lectorCsv: LectorCsv = LectorCsv()
) : RepartidorRepository {

    private val carga: CargaRepartidores by lazy { cargar() }

    override fun obtenerTodos(): List<Repartidor> = carga.repartidores

    override fun erroresDeCarga(): List<String> = carga.errores

    private fun cargar(): CargaRepartidores {
        val resultado: ResultadoLectura = lectorCsv.leer(NOMBRE_ARCHIVO)
        val repartidores: MutableList<Repartidor> = mutableListOf()
        val errores: MutableList<String> = resultado.errores.toMutableList()

        resultado.filas.forEachIndexed { indice: Int, fila: List<String> ->
            val repartidor: Repartidor? = convertirFila(fila)
            if (repartidor != null) {
                repartidores.add(repartidor)
            } else {
                errores.add("Fila ${indice + 1} inválida en $NOMBRE_ARCHIVO: ${fila.joinToString(";")}")
            }
        }

        return CargaRepartidores(repartidores = repartidores.toList(), errores = errores.toList())
    }

    private fun convertirFila(fila: List<String>): Repartidor? {
        if (fila.size != CANTIDAD_COLUMNAS) return null

        val id: Int? = fila[0].toIntOrNull()
        val nombre: String = fila[1]
        val telefono: String = fila[2]
        val disponible: Boolean? = fila[3].toBooleanStrictOrNull()

        return if (
            id != null && id > 0 &&
            nombre.isNotEmpty() &&
            disponible != null
        ) {
            Repartidor(
                id = id,
                nombre = nombre,
                telefono = telefono,
                disponible = disponible
            )
        } else {
            null
        }
    }

    private data class CargaRepartidores(
        val repartidores: List<Repartidor>,
        val errores: List<String>
    )

    private companion object {
        const val NOMBRE_ARCHIVO: String = "repartidores.csv"
        const val CANTIDAD_COLUMNAS: Int = 4
    }
}
