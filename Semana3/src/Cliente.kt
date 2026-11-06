
class Cliente(
    nombre: String,
    telefono: String,
    val correo: String,
    val direccion: String
) : Usuario(nombre, telefono) {

    // MOSTRAR INFORMACIÓN DEL CLIENTE
    override fun mostrarInformacion(): String {
        return """
            Cliente: $nombre
            Teléfono: $contacto
            Correo: $correo
            Dirección: $direccion
        """.trimIndent()
    }

    // COMPARAR CLIENTES POR NOMBRE Y CORREO
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Cliente) return false

        return nombre.equals(other.nombre, ignoreCase = true) &&
                correo.equals(other.correo, ignoreCase = true)
    }

    // GENERAR HASH CONSISTENTE CON EQUALS
    override fun hashCode(): Int {
        var resultado = nombre.lowercase().hashCode()
        resultado = 31 * resultado + correo.lowercase().hashCode()
        return resultado
    }

    // PERMITIR DESESTRUCTURAR LOS DATOS
    operator fun component1(): String = nombre

    operator fun component2(): String = correo

    operator fun component3(): String = contacto
}
