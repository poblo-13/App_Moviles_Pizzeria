
import kotlin.math.roundToInt

// REPRESENTA UN PRODUCTO Y SU CANTIDAD
data class DetallePedido(
    val producto: Producto,
    val cantidad: Int
)

// CLASE PARA GESTIONAR LOS PEDIDOS
class Pedido(
    val id: Int,
    val cliente: Cliente,
    val productos: List<DetallePedido>,
    val notas: String? = null,
    var repartidor: Repartidor? = null
) {

    var estado: String = "En preparación"

    // CALCULAR CANTIDAD TOTAL DE PIZZAS
    fun calcularCantidad(): Int {
        return productos.sumOf { it.cantidad }
    }

    // CALCULAR SUBTOTAL
    fun calcularSubtotal(): Int {
        return productos.sumOf {
            it.producto.precio * it.cantidad
        }
    }

    // APLICAR DESCUENTO DEL 10%
    fun calcularDescuento(): Int {
        return if (calcularCantidad() > 3) {
            calcularSubtotal() * 10 / 100
        } else {
            0
        }
    }

    // CALCULAR IVA DEL 19%
    fun calcularImpuestos(): Int {
        val montoNeto = calcularSubtotal() - calcularDescuento()
        return (montoNeto * 0.19).roundToInt()
    }

    // CALCULAR TOTAL FINAL
    fun calcularTotal(): Int {
        return calcularSubtotal() -
                calcularDescuento() +
                calcularImpuestos()
    }

    // CAMBIAR ESTADO DEL PEDIDO
    fun cambiarEstado(nuevoEstado: String): Boolean {
        val estadosValidos = listOf(
            "En preparación",
            "Listo",
            "En camino",
            "Entregado"
        )

        return if (nuevoEstado in estadosValidos) {
            estado = nuevoEstado
            true
        } else {
            false
        }
    }

    // GENERAR RESUMEN DEL PEDIDO

    fun generarResumen(): String {
        val notasPedido = notas?.takeIf {
            it.isNotBlank()
        } ?: "Sin notas"

        val nombreRepartidor = repartidor?.nombre ?: "Sin asignar"

        return buildString {
            appendLine("========================================")
            appendLine("            PIZZERÍA DI PABLO")
            appendLine("             RESUMEN PEDIDO")
            appendLine("========================================")
            appendLine("Pedido N°: $id")
            appendLine(cliente.mostrarInformacion())
            appendLine("----------------------------------------")
            appendLine("PRODUCTOS:")

            for (detalle in productos) {
                val importe = detalle.producto.precio * detalle.cantidad
                appendLine(
                    "${detalle.cantidad} x ${detalle.producto.nombre} - $$importe"
                )
            }

            appendLine("----------------------------------------")
            appendLine("Cantidad: ${calcularCantidad()}")
            appendLine("Subtotal: $${calcularSubtotal()}")
            appendLine("Descuento: $${calcularDescuento()}")
            appendLine("IVA (19%): $${calcularImpuestos()}")
            appendLine("TOTAL: $${calcularTotal()}")
            appendLine("----------------------------------------")
            appendLine("Estado: $estado")
            appendLine("Repartidor: $nombreRepartidor")
            appendLine("Notas: $notasPedido")
            appendLine("========================================")
        }
    }

}
