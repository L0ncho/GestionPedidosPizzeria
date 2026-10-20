package cl.duoc.pmy2201.pizzeria.data

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream

/**
 * Lee archivos CSV desde resources (UTF-8, separador `;`, con encabezado).
 */
class LectorCsv {

    /**
     * Lee [nombreArchivo] desde resources.
     *
     * Quita el BOM si existe, descarta las líneas vacías, salta el encabezado
     * y separa cada línea por `;` quitando espacios de cada valor.
     * No lanza excepciones: los problemas se informan en [ResultadoLectura.errores].
     */
    fun leer(nombreArchivo: String): ResultadoLectura {
        val flujo: InputStream? = LectorCsv::class.java.classLoader.getResourceAsStream(nombreArchivo)

        return if (flujo == null) {
            ResultadoLectura(
                filas = emptyList(),
                errores = listOf("No se encontró el archivo '$nombreArchivo' en resources.")
            )
        } else {
            try {
                val contenido: String = flujo.bufferedReader(Charsets.UTF_8).use { lector: BufferedReader -> lector.readText() }
                val filas: List<List<String>> = contenido
                    .removePrefix(BOM)
                    .lines()
                    .filter { linea: String -> linea.isNotBlank() }
                    .drop(1)
                    .map { linea: String -> linea.split(SEPARADOR).map { valor: String -> valor.trim() } }
                ResultadoLectura(filas = filas, errores = emptyList())
            } catch (excepcion: IOException) {
                ResultadoLectura(
                    filas = emptyList(),
                    errores = listOf("Error al leer el archivo '$nombreArchivo': ${excepcion.message}")
                )
            }
        }
    }

    private companion object {
        const val BOM: String = "\uFEFF"
        const val SEPARADOR: String = ";"
    }
}
