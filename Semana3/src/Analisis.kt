import java.time.LocalDate

fun mostrarAnalisisSistema(catalogo: List<Producto>) {

    println()
    println("========================================")
    println("       PROMOCIONES DISPONIBLES")
    println("========================================")

    var cantidadPromocionables = 0

    // IDENTIFICAR PIZZAS CON LA ANOTACION @Promocionable
    for (producto in catalogo) {

        val anotacion = producto.javaClass.getAnnotation(
            Promocionable::class.java
        )

        if (anotacion != null) {
            println("Pizza: ${producto.nombre}")
            println("Promoción: ${anotacion.descripcion}")
            println("Descuento declarado: ${anotacion.descuento}%")
            println("----------------------------------------")

            cantidadPromocionables++
        }
    }

    println("Productos promocionables: $cantidadPromocionables")

    // INSPECCIONAR CLASES CON REFLECTION
    println()
    println("========================================")
    println("        ANALISIS DE CLASES")
    println("========================================")

    val clases = listOf(
        Cliente::class.java,
        Pedido::class.java
    )

    for (clase in clases) {

        println()
        println("CLASE: ${clase.simpleName}")
        println("----------------------------------------")

        println("PROPIEDADES:")

        val propiedades = clase.declaredFields
            .filterNot { it.isSynthetic }
            .sortedBy { it.name }

        for (propiedad in propiedades) {
            println("- ${propiedad.name}: ${propiedad.type.simpleName}")
        }

        println()
        println("METODOS:")

        val metodos = clase.declaredMethods
            .filterNot {
                it.isSynthetic || it.name.contains('$')
            }
            .sortedBy { it.name }

        for (metodo in metodos) {
            println("- ${metodo.name}()")
        }

        println("----------------------------------------")
    }
    verificarProductosDuplicados(catalogo)
}

fun verificarProductosDuplicados(catalogo: List<Producto>) {
    verificarFechasPromocionales(catalogo)
    println()
    println("========================================")
    println("       COMPARACIÓN DE PRODUCTOS")
    println("========================================")

    val productoOriginal = catalogo.first {
        it.nombre == "Pepperoni"
    }

    // Producto de prueba con el mismo nombre y categoría
    val productoDuplicado = Producto(
        nombre = "PEPPERONI",
        precio = 9990,
        ingredientes = productoOriginal.ingredientes,
        categoria = "Pizza"
    )

    println("Producto original: ${productoOriginal.nombre}")
    println("Producto de prueba: ${productoDuplicado.nombre}")

    println("¿Son iguales con ==? ${productoOriginal == productoDuplicado}")
    println("¿Son la misma instancia con ===? ${productoOriginal === productoDuplicado}")

    // ELIMINAR DUPLICADOS MEDIANTE SET
    val catalogoConDuplicado = catalogo + productoDuplicado
    val productosUnicos = catalogoConDuplicado.toSet()

    println("Productos antes de eliminar duplicados: ${catalogoConDuplicado.size}")
    println("Productos únicos después de usar Set: ${productosUnicos.size}")

    println("========================================")
}

fun verificarFechasPromocionales(catalogo: List<Producto>) {

    println()
    println("========================================")
    println("       VALIDACIÓN DE PROMOCIONES")
    println("========================================")

    val pizzaPromocionable = catalogo.firstOrNull {
        it.javaClass.isAnnotationPresent(Promocionable::class.java)
    }

    if (pizzaPromocionable == null) {
        println("No existen pizzas promocionables.")
        return
    }

    val clientePrueba = Cliente(
        "Cliente Prueba",
        "+56 (9) 1234-5678",
        "prueba@ejemplo.com",
        "Dirección de prueba"
    )

    val fechasPrueba = listOf(
        LocalDate.of(2026, 10, 31),
        LocalDate.of(2026, 11, 15),
        LocalDate.of(2026, 12, 1)
    )

    for (fecha in fechasPrueba) {

        val pedidoPrueba = Pedido(
            id = 0,
            cliente = clientePrueba,
            productos = listOf(DetallePedido(pizzaPromocionable, 1)),
            zonaEntrega = "Centro",
            fechaPedido = fecha
        )

        val vigente = Promociones.estaVigente(fecha)
        val descuento = pedidoPrueba.calcularDescuentoPromocional()

        println("Fecha: $fecha")
        println("Promoción vigente: ${if (vigente) "Sí" else "No"}")
        println("Descuento aplicado: $$descuento")
        println("----------------------------------------")
    }

    println("Prueba realizada sin registrar pedidos.")
    println("========================================")
}

fun mostrarResumenGeneralSemana3(
    catalogo: List<Producto>,
    pedidos: Array<Pedido>,
    clientesRegistrados: Set<Cliente>
) {

    println()
    println("========================================")
    println("        RESUMEN GENERAL SEMANA 3")
    println("           PIZZERÍA DI PABLO")
    println("========================================")

    // DATOS REALES DEL SISTEMA
    println("Pedidos confirmados: ${pedidos.size}")
    println("Clientes únicos registrados: ${clientesRegistrados.size}")
    println("Productos disponibles en catálogo: ${catalogo.size}")

    println()
    println("Las siguientes pruebas no modifican")
    println("los pedidos ni el inventario real.")

    // PROMOCIONES, ANOTACIONES, REFLECTION Y PRODUCTOS
    mostrarAnalisisSistema(catalogo)

    // COMPARACIÓN DE CLIENTES
    println()
    println("========================================")
    println("        COMPARACIÓN DE CLIENTES")
    println("========================================")

    val clientePrueba = clientesRegistrados.firstOrNull()
        ?: Cliente(
            "Cliente Prueba",
            "+56 (9) 1234-5678",
            "prueba@ejemplo.com",
            "Dirección de prueba"
        )

    val clienteDuplicado = Cliente(
        nombre = clientePrueba.nombre.uppercase(),
        telefono = clientePrueba.contacto,
        correo = clientePrueba.correo.uppercase(),
        direccion = clientePrueba.direccion
    )

    println("Primer cliente: ${clientePrueba.nombre}")
    println("Segundo cliente: ${clienteDuplicado.nombre}")

    println("¿Son iguales?: ${clientePrueba == clienteDuplicado}")

    val clientesPrueba = setOf(
        clientePrueba,
        clienteDuplicado
    )

    println("Clientes únicos en prueba: ${clientesPrueba.size}")

    // COMBINACIÓN DE PEDIDOS MEDIANTE OPERADOR +
    println()
    println("========================================")
    println("       COMBINACIÓN DE PEDIDOS")
    println("========================================")

    val fechaPrueba = LocalDate.of(2026, 11, 15)

    val pedidoUno = Pedido(
        id = 101,
        cliente = clientePrueba,
        productos = listOf(
            DetallePedido(catalogo[0], 2)
        ),
        zonaEntrega = "Centro",
        fechaPedido = fechaPrueba
    )

    val pedidoDos = Pedido(
        id = 102,
        cliente = clientePrueba,
        productos = listOf(
            DetallePedido(catalogo[2], 2)
        ),
        zonaEntrega = "Centro",
        fechaPedido = fechaPrueba
    )

    val pedidoCombinado = pedidoUno + pedidoDos

    println("Pedido de prueba #101: 2 Pepperoni")
    println("Pedido de prueba #102: 2 Napolitana")

    println("Cantidad combinada: ${pedidoCombinado.calcularCantidad()}")
    println("Subtotal combinado: $${pedidoCombinado.calcularSubtotal()}")
    println("Descuento por cantidad: $${pedidoCombinado.calcularDescuento()}")
    println("Descuento promocional: $${pedidoCombinado.calcularDescuentoPromocional()}")
    println("IVA: $${pedidoCombinado.calcularImpuestos()}")
    println("Total combinado: $${pedidoCombinado.calcularTotal()}")

    // DESESTRUCTURACIÓN DE PEDIDO
    println()
    println("========================================")
    println("       DESESTRUCTURACIÓN DE DATOS")
    println("========================================")

    val (cliente, productos, total) = pedidoCombinado

    // DESESTRUCTURACIÓN DE CLIENTE
    val (nombre, correo, telefono) = cliente

    println("Cliente: $nombre")
    println("Correo: $correo")
    println("Teléfono: $telefono")

    println("Productos del pedido:")

    for (detalle in productos) {
        println("- ${detalle.cantidad} x ${detalle.producto.nombre}")
    }

    println("Total obtenido mediante desestructuración: $$total")

    println()
    println("========================================")
    println("       RESUMEN FINALIZADO")
    println("========================================")
    println("Pruebas realizadas correctamente.")
    println("No se registraron ventas adicionales.")
    println("El inventario permanece sin cambios.")
    println("========================================")
}
