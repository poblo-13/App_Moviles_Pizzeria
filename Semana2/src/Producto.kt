
class Producto(
    val nombre: String,
    val precio: Int,
    val ingredientes: Map<String, Int>
) {

    fun detallarProducto(): String {
        val detalleIngredientes = ingredientes.entries.joinToString(", ") {
            "${it.key}: ${it.value}"
        }

        return "$nombre - Precio: \$$precio - Ingredientes: $detalleIngredientes"
    }
}
