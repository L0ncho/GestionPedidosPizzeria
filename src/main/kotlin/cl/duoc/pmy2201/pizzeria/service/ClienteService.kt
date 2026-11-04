package cl.duoc.pmy2201.pizzeria.service

import cl.duoc.pmy2201.pizzeria.model.Cliente

/**
 * Mantiene los clientes registrados en el día, sin duplicados.
 * Los guarda en un [MutableSet], que usa `equals` y `hashCode` de [Cliente] para no aceptar
 * dos clientes iguales (mismo nombre y correo).
 */
class ClienteService {

    private val clientes: MutableSet<Cliente> = mutableSetOf()

    /**
     * Retorna el cliente registrado que es igual (==) a uno con [nombre] y [correo], o null si no existe.
     * La dirección y el teléfono del cliente buscado no influyen, porque [Cliente.equals] no los compara.
     */
    fun buscar(nombre: String, correo: String?): Cliente? {
        val buscado: Cliente = Cliente(nombre = nombre, telefono = "", correo = correo, direccion = "")
        return clientes.find { cliente: Cliente -> cliente == buscado }
    }

    /**
     * Registra [cliente]. Retorna false si ya existía un cliente igual, en cuyo caso no se agrega.
     */
    fun registrar(cliente: Cliente): Boolean = clientes.add(cliente)

    /** Retorna una copia de los clientes registrados en el día. */
    fun clientesDelDia(): Set<Cliente> = clientes.toSet()
}
