
import kotlin.math.roundToInt
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
    val zonaEntrega: String,
    val notas: String? = null,
    var repartidor: Repartidor? = null,
    val fechaPedido: LocalDate = LocalDate.now()
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

    // APLICAR DESCUENTO DEL 10% POR CANTIDAD
    fun calcularDescuento(): Int {
        return if (calcularCantidad() > 3) {
            calcularSubtotal() * 10 / 100
        } else {
            0
        }
    }

    // APLICAR DESCUENTO PROMOCIONAL POR FECHA

    fun calcularDescuentoPromocional(): Int {

        // Comprobar la vigencia de la promoción
        if (!Promociones.estaVigente(fechaPedido)) {
            return 0
        }

        val subtotal = calcularSubtotal()

        if (subtotal <= 0) {
            return 0
        }

        // Monto después del descuento por cantidad
        val montoNeto = subtotal - calcularDescuento()

        // Revisar las anotaciones de cada producto
        return productos.sumOf { detalle ->

            val anotacion = detalle.producto.javaClass
                .getAnnotation(Promocionable::class.java)

            if (anotacion != null) {

                val importe =
                    detalle.producto.precio * detalle.cantidad

                // Distribuir proporcionalmente el descuento
                // por cantidad antes de aplicar la promoción
                val basePromocional =
                    importe.toDouble() * montoNeto / subtotal

                (basePromocional * anotacion.descuento / 100.0)
                    .roundToInt()

            } else {
                0
            }
        }
    }

    // CALCULAR IVA DEL 19%
    fun calcularImpuestos(): Int {
        val montoNeto = calcularSubtotal() -
                calcularDescuento() -
                calcularDescuentoPromocional()

        return (montoNeto * 0.19).roundToInt()
    }

    // CALCULAR TOTAL FINAL
    fun calcularTotal(): Int {
        return calcularSubtotal() -
                calcularDescuento() -
                calcularDescuentoPromocional() +
                calcularImpuestos()
    }

    // CAMBIAR ESTADO DEL PEDIDO
    fun cambiarEstado(nuevoEstado: String): Boolean {
        val estados = listOf(
            "En preparación",
            "Listo",
            "En camino",
            "Entregado"
        )

        val posicionActual = estados.indexOf(estado)
        val posicionNueva = estados.indexOf(nuevoEstado)

        return if (
            posicionActual >= 0 &&
            posicionNueva == posicionActual + 1
        ) {
            estado = nuevoEstado
            true
        } else {
            false
        }
    }


    // COMBINAR DOS PEDIDOS MEDIANTE EL OPERADOR +
    operator fun plus(otro: Pedido): Pedido {

        require(
            cliente.nombre.equals(otro.cliente.nombre, ignoreCase = true) &&
                    cliente.correo.equals(otro.cliente.correo, ignoreCase = true)
        ) {
            "Solo se pueden combinar pedidos del mismo cliente."
        }

        require(zonaEntrega == otro.zonaEntrega) {
            "Los pedidos deben tener la misma zona de entrega."
        }

        require(fechaPedido == otro.fechaPedido) {
            "Los pedidos deben tener la misma fecha."
        }

        return Pedido(
            id = 0,
            cliente = cliente,
            productos = productos + otro.productos,
            zonaEntrega = zonaEntrega,
            notas = "Resumen combinado de pedidos #$id y #${otro.id}",
            fechaPedido = fechaPedido
        )
    }

    // DESESTRUCTURACIÓN DEL PEDIDO
    operator fun component1(): Cliente = cliente

    operator fun component2(): List<DetallePedido> = productos

    operator fun component3(): Int = calcularTotal()

    // GENERAR RESUMEN DEL PEDIDO
    fun generarResumen(): String {
        val notasPedido = notas?.takeIf {
            it.isNotBlank()
        } ?: "Sin notas"

        val nombreRepartidor =
            repartidor?.nombre ?: "Sin asignar"

        val formatoFecha =
            DateTimeFormatter.ofPattern("dd/MM/yyyy")

        return buildString {
            appendLine("========================================")
            appendLine("            PIZZERÍA DI PABLO")
            appendLine("             RESUMEN PEDIDO")
            appendLine("========================================")
            appendLine("Pedido N°: $id")
            appendLine("Fecha: ${fechaPedido.format(formatoFecha)}")
            appendLine(cliente.mostrarInformacion())
            appendLine("----------------------------------------")
            appendLine("PRODUCTOS:")

            for (detalle in productos) {
                val importe =
                    detalle.producto.precio * detalle.cantidad

                appendLine(
                    "${detalle.cantidad} x ${detalle.producto.nombre} - $$importe"
                )
            }

            appendLine("----------------------------------------")
            appendLine("Cantidad: ${calcularCantidad()}")
            appendLine("Subtotal: $${calcularSubtotal()}")
            appendLine("Descuento por cantidad: $${calcularDescuento()}")
            appendLine("Descuento promoción: $${calcularDescuentoPromocional()}")
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
