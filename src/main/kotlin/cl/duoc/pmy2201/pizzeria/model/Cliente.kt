package cl.duoc.pmy2201.pizzeria.model

/**
 * Cliente que realiza el pedido.
 *
 * @param nombre Nombre del cliente.
 * @param telefono Teléfono de contacto, o "No informado" si no se entregó uno válido.
 * @property correo Correo electrónico, o `null` si no se informó.
 * @property direccion Dirección de entrega.
 */
class Cliente(
    nombre: String,
    telefono: String,
    val correo: String?,
    val direccion: String
) : Usuario(nombre, telefono) {

    override fun describir(): String =
        "${super.describir()} - Correo: ${correo ?: "Sin correo"} - Dirección: $direccion"

    /**
     * Dos clientes son iguales si tienen el mismo nombre (sin distinguir mayúsculas ni espacios en los extremos)
     * y el mismo [correo] (sin distinguir mayúsculas; dos correos null cuentan como iguales).
     * La dirección y el teléfono no se comparan.
     * El operador `==` de Kotlin llama a este método, por lo que esta implementación define
     * cómo se comparan los clientes con `==`.
     * Usa `is Cliente` para que también funcione con futuras subclases.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is Cliente &&
            nombreNormalizado() == other.nombreNormalizado() &&
            correoNormalizado() == other.correoNormalizado()
    }

    /**
     * Calcula el hash con los mismos atributos normalizados que [equals], de modo que dos clientes
     * iguales tengan el mismo hash (necesario para usarlos en `Set` o como claves de `Map`).
     */
    override fun hashCode(): Int {
        val hashNombre: Int = nombreNormalizado().hashCode()
        return 31 * hashNombre + (correoNormalizado()?.hashCode() ?: 0)
    }

    /**
     * Funciones de desestructuración: permiten escribir `val (nombre, correo, telefono) = cliente`.
     * [component1] retorna el nombre, [component2] el correo y [component3] el teléfono.
     */
    operator fun component1(): String = nombre

    /** Retorna el [correo] del cliente (ver [component1]). */
    operator fun component2(): String? = correo

    /** Retorna el teléfono del cliente (ver [component1]). */
    operator fun component3(): String = telefono

    private fun nombreNormalizado(): String = nombre.trim().lowercase()

    private fun correoNormalizado(): String? = correo?.lowercase()
}
