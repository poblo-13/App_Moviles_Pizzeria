
class Cliente(
    nombre: String,
    telefono: String,
    val correo: String,
    val direccion: String
) : Usuario(nombre, telefono) {

    override fun mostrarInformacion(): String {
        return """
            Cliente: $nombre
            Teléfono: $contacto
            Correo: $correo
            Dirección: $direccion
        """.trimIndent()
    }
}
