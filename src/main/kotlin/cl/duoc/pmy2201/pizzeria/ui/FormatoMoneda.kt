package cl.duoc.pmy2201.pizzeria.ui

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Formatea montos en pesos chilenos, sin decimales y con punto de miles (ej.: $11.990).
 */
object FormatoMoneda {

    private val formato: DecimalFormat = DecimalFormat("#,##0", DecimalFormatSymbols(Locale.of("es", "CL"))).apply {
        roundingMode = RoundingMode.HALF_UP
    }

    fun formatear(monto: Double): String = "\$${formato.format(monto)}"
}
