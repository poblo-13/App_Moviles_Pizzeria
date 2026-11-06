
class Repartidor(
    nombre: String,
    contacto: String,
    val zona: String,
    var disponible: Boolean = true
) : Usuario(nombre, contacto) {

    override fun mostrarInformacion(): String {
        return """
            Repartidor: $nombre
            Contacto: $contacto
            Zona: $zona
            Disponible: ${if (disponible) "Sí" else "No"}
        """.trimIndent()
    }

    fun entregarPedido(): String {
        disponible = true
        return "El repartidor $nombre ha completado la entrega."
    }
}
