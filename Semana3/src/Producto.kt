
open class Producto(
    val nombre: String,
    val precio: Int,
    val ingredientes: Map<String, Int>,
    val categoria: String = "Pizza"
) {

    // MOSTRAR INFORMACIÓN DEL PRODUCTO
    fun detallarProducto(): String {
        val detalleIngredientes = ingredientes.entries.joinToString(", ") {
            "${it.key}: ${it.value}"
        }

        return "$nombre - Precio: \$$precio - " +
                "Categoría: $categoria - " +
                "Ingredientes: $detalleIngredientes"
    }

    // COMPARAR PRODUCTOS POR NOMBRE Y CATEGORÍA
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Producto) return false

        return nombre.equals(other.nombre, ignoreCase = true) &&
                categoria.equals(other.categoria, ignoreCase = true)
    }

    // GENERAR UN CÓDIGO CONSISTENTE CON EQUALS
    override fun hashCode(): Int {
        var resultado = nombre.lowercase().hashCode()
        resultado = 31 * resultado + categoria.lowercase().hashCode()
        return resultado
    }
}
