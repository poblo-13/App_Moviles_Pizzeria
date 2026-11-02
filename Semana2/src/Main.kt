fun main() {
    // las cantidades representan porciones de ingredientes
    val inventario = mutableMapOf(
        "Masa" to 40,
        "Queso" to 80,
        "Salsa de tomate" to 40,
        "Pepperoni" to 20,
        "Tomate" to 20,
        "Carne" to 20,
        "Pollo" to 20,
        "Pesto" to 20
    )

    // precios netos: el IVA se agrega en Pedido.calcularImpuestos()
    val catalogo = listOf(
        Producto("Pepperoni", 9990, mapOf("Masa" to 1, "Queso" to 2, "Salsa de tomate" to 1, "Pepperoni" to 2)),
        Producto("Cuatro Quesos", 10990, mapOf("Masa" to 1, "Queso" to 4, "Salsa de tomate" to 1)),
        Producto("Napolitana", 8990, mapOf("Masa" to 1, "Queso" to 2, "Salsa de tomate" to 1, "Tomate" to 2)),
        Producto("Todas las Carnes", 14990, mapOf("Masa" to 1, "Queso" to 2, "Salsa de tomate" to 1, "Carne" to 3)),
        Producto("Pollo al Pesto", 13990, mapOf("Masa" to 1, "Queso" to 2, "Pollo" to 2, "Pesto" to 1))
    )

    val repartidores = listOf(
        Repartidor("Ana", "1001", "Centro"),
        Repartidor("Luis", "1002", "San Pedro"),
        Repartidor("Juan", "1003", "Talcahuano")
    )

    var pedidosDelDia = emptyArray<Pedido>()
    var opcion: Int?

    do {
        mostrarMenu()
        print("Seleccione una opción: ")
        opcion = readlnOrNull()?.trim()?.toIntOrNull()

        when (opcion) {
            1 -> registrarPedido(catalogo, inventario, repartidores, pedidosDelDia.size + 1)?.let {
                pedidosDelDia += it
            }
            2 -> mostrarInventario(inventario)
            3 -> mostrarPedidosDelDia(pedidosDelDia)
            4 -> actualizarEstadoPedido(pedidosDelDia)
            5 -> reabastecerIngredientes(inventario)
            0 -> println("Gracias por utilizar Pizzería di Pablo.")
            else -> println("Error: Seleccione una opción válida.")
        }
    } while (opcion != 0)
}

fun mostrarMenu() {
    println("""

        ========================================
                    PIZZERÍA DI PABLO
                   SISTEMA DE GESTIÓN
        ========================================
        1. Registrar nuevo pedido
        2. Consultar inventario
        3. Ver pedidos del día
        4. Actualizar estado de pedido
        5. Reabastecer ingredientes
        0. Salir del sistema
        ========================================
    """.trimIndent())
}

fun solicitarTextoObligatorio(mensaje: String): String {
    var valor: String
    do {
        print(mensaje)
        valor = readlnOrNull()?.trim().orEmpty()
        if (valor.isBlank()) println("Error: Este campo no puede estar vacío.")
    } while (valor.isBlank())
    return valor
}

fun calcularIngredientes(detalles: List<DetallePedido>): Map<String, Int> {
    val necesarios = mutableMapOf<String, Int>()
    for (detalle in detalles) {
        for ((ingrediente, porciones) in detalle.producto.ingredientes) {
            necesarios[ingrediente] = (necesarios[ingrediente] ?: 0) + porciones * detalle.cantidad
        }
    }
    return necesarios
}

fun descontarIngredientes(detalles: List<DetallePedido>, inventario: MutableMap<String, Int>) {
    for ((ingrediente, cantidad) in calcularIngredientes(detalles)) {
        inventario[ingrediente] = (inventario[ingrediente] ?: 0) - cantidad
    }
}

fun buscarRepartidor(zona: String, repartidores: List<Repartidor>): Repartidor? {
    // Cercanía simulada por zonas. No utiliza distancias reales ni GPS.
    val prioridades = when (zona) {
        "Centro" -> listOf("Centro", "San Pedro", "Talcahuano")
        "San Pedro" -> listOf("San Pedro", "Centro", "Talcahuano")
        else -> listOf("Talcahuano", "Centro", "San Pedro")
    }
    for (zonaPreferida in prioridades) {
        repartidores.find { it.disponible && it.zona == zonaPreferida }?.let { return it }
    }
    return null
}

fun registrarPedido(
    catalogo: List<Producto>,
    inventario: MutableMap<String, Int>,
    repartidores: List<Repartidor>,
    id: Int
): Pedido? {
    println("\n--- NUEVO PEDIDO ---")
    val nombre = solicitarNombreCliente()
    val direccion = solicitarTextoObligatorio("Dirección: ")

    print("Teléfono (+569XXXXXXXX): ")
    val telefonoIngresado = readlnOrNull()?.trim().orEmpty()
    val telefono = telefonoIngresado.takeIf { it.matches(Regex("""\+569\d{8}""")) }
        ?: "(000) 000-0000"
    if (telefono != telefonoIngresado) println("Formato inválido; se usará el teléfono predeterminado.")

    print("Correo electrónico: ")
    val correoIngresado = readlnOrNull()?.trim().orEmpty()
    val correo = correoIngresado.takeIf {
        it.matches(Regex("""[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}"""))
    } ?: "correo@invalido.com"
    if (correo != correoIngresado) println("Formato inválido; se usará el correo predeterminado.")

    print("Notas del pedido (Enter para omitir): ")
    val notas = readlnOrNull()?.trim()?.takeIf { it.isNotEmpty() }
    val cliente = Cliente(nombre, telefono, correo, direccion)

    val zonas = listOf("Centro", "San Pedro", "Talcahuano")
    var opcionZona: Int?
    do {
        println("Zona de entrega: 1. Centro  2. San Pedro  3. Talcahuano")
        print("Seleccione la zona: ")
        opcionZona = readlnOrNull()?.trim()?.toIntOrNull()
        if (opcionZona !in 1..3) println("Error: Seleccione una zona válida.")
    } while (opcionZona !in 1..3)
    val zona = zonas[opcionZona!! - 1]

    val seleccionados = mutableListOf<DetallePedido>()
    while (true) {
        println("\n--- MENÚ DE PIZZAS ---")
        for ((indice, producto) in catalogo.withIndex()) {
            println("${indice + 1}. ${producto.nombre} - $${producto.precio}")
        }
        println("0. Terminar selección")
        print("Seleccione una pizza: ")
        val opcionPizza = readlnOrNull()?.trim()?.toIntOrNull()

        if (opcionPizza == 0) break
        if (opcionPizza == null || opcionPizza !in 1..catalogo.size) {
            println("Error: Opción inválida.")
            continue
        }

        val producto = catalogo[opcionPizza - 1]
        println(producto.detallarProducto())
        print("Cantidad de ${producto.nombre}: ")
        val cantidad = try {
            readlnOrNull()?.trim()?.toInt() ?: throw NumberFormatException()
        } catch (_: NumberFormatException) {
            println("Error: Ingrese una cantidad numérica válida.")
            continue
        }
        if (cantidad !in 1..100) {
            println("Error: La cantidad debe estar entre 1 y 100.")
            continue
        }

        val propuesta = seleccionados + DetallePedido(producto, cantidad)
        val necesarios = calcularIngredientes(propuesta)
        val faltantes = necesarios.filter { (ingrediente, solicitado) ->
            solicitado > (inventario[ingrediente] ?: 0)
        }

        when {
            faltantes.isEmpty() -> {
                seleccionados.add(DetallePedido(producto, cantidad))
                println("${cantidad} x ${producto.nombre} agregadas al pedido.")
            }
            else -> {
                println("No se puede agregar esta cantidad. Ingredientes insuficientes:")
                for ((ingrediente, solicitado) in faltantes) {
                    println("- $ingrediente: requiere $solicitado porciones; hay ${inventario[ingrediente] ?: 0}.")
                }
                println("Para reabastecer: vuelva al menú principal, elija opción 5 y agregue las porciones faltantes.")
            }
        }
    }

    if (seleccionados.isEmpty()) {
        println("Pedido sin pizzas: no se registró.")
        return null
    }

    val pedido = Pedido(id, cliente, seleccionados.toList(), notas)
    println("\n${pedido.generarResumen()}")

    var confirmacion: Int?
    do {
        print("¿Confirmar pedido? (1. Sí / 2. Cancelar): ")
        confirmacion = readlnOrNull()?.trim()?.toIntOrNull()
        if (confirmacion !in 1..2) println("Error: Escriba 1 o 2.")
    } while (confirmacion !in 1..2)

    if (confirmacion == 2) {
        println("Pedido cancelado. El inventario no cambió.")
        return null
    }

    descontarIngredientes(pedido.productos, inventario)
    pedido.repartidor = buscarRepartidor(zona, repartidores)
    pedido.repartidor?.disponible = false
    println("\nPedido confirmado. Inventario actualizado.")
    if (pedido.repartidor == null) println("No hay repartidores disponibles; asignación pendiente.")

    correo.takeIf { it != "correo@invalido.com" }?.let {
        println("Notificación simulada al correo: $it")
    }
    println(pedido.generarResumen())
    return pedido
}

fun mostrarInventario(inventario: Map<String, Int>) {
    println("\n--- INVENTARIO DISPONIBLE ---")
    for ((ingrediente, cantidad) in inventario) {
        println("${ingrediente.padEnd(20)} $cantidad porciones")
    }
}

fun reabastecerIngredientes(inventario: MutableMap<String, Int>) {
    val ingredientes = inventario.keys.toList()
    println("\n--- REABASTECER INGREDIENTES ---")
    for ((i, ingrediente) in ingredientes.withIndex()) println("${i + 1}. $ingrediente")
    print("Seleccione un ingrediente: ")
    val opcion = readlnOrNull()?.trim()?.toIntOrNull()
    if (opcion == null || opcion !in 1..ingredientes.size) {
        println("Error: Ingrediente inválido.")
        return
    }
    val ingrediente = ingredientes[opcion - 1]
    try {
        print("Cantidad de porciones para agregar: ")
        val cantidad = readlnOrNull()?.trim()?.toInt() ?: throw NumberFormatException()
        if (cantidad <= 0) throw IllegalArgumentException("La cantidad debe ser mayor que cero.")
        val anterior = inventario[ingrediente] ?: 0
        if (cantidad > Int.MAX_VALUE - anterior) throw IllegalArgumentException("Cantidad demasiado grande.")
        inventario[ingrediente] = anterior + cantidad
        println("$ingrediente: $anterior -> ${inventario[ingrediente]} porciones.")
    } catch (_: NumberFormatException) {
        println("Error: Ingrese una cantidad numérica válida.")
    } catch (e: IllegalArgumentException) {
        println("Error: ${e.message}")
    }
}

fun mostrarPedidosDelDia(pedidos: Array<Pedido>) {
    if (pedidos.isEmpty()) {
        println("No hay pedidos registrados hoy.")
        return
    }
    println("\n--- REPORTE DE PEDIDOS DEL DÍA ---")
    for (pedido in pedidos) {
        val pizzas = pedido.productos.joinToString(", ") { "${it.cantidad} x ${it.producto.nombre}" }
        println("#${pedido.id} | ${pedido.cliente.nombre} | $pizzas | $${pedido.calcularTotal()} | ${pedido.estado}")
    }
    val estadosCompletados = setOf("Entregado")
    val completados = pedidos.filter { it.estado in estadosCompletados }
    val pendientes = pedidos.filter { it.estado !in estadosCompletados }
    println("\nCOMPLETADOS (${completados.size}):")
    for (pedido in completados) println("- Pedido #${pedido.id}: ${pedido.cliente.nombre}")
    println("PENDIENTES (${pendientes.size}):")
    for (pedido in pendientes) println("- Pedido #${pedido.id}: ${pedido.estado}")
}

fun actualizarEstadoPedido(pedidos: Array<Pedido>) {
    if (pedidos.isEmpty()) {
        println("No hay pedidos para actualizar.")
        return
    }
    print("Ingrese número de pedido: ")
    val id = readlnOrNull()?.trim()?.toIntOrNull()
    val pedido = pedidos.find { it.id == id }
    if (pedido == null) {
        println("Error: Pedido no encontrado.")
        return
    }
    if (pedido.estado == "Entregado") {
        println("El pedido ya fue entregado y no puede volver a un estado anterior.")
        return
    }
    println("1. En preparación  2. Listo  3. En camino  4. Entregado")
    print("Nuevo estado: ")
    val nuevoEstado = when (readlnOrNull()?.trim()?.toIntOrNull()) {
        1 -> "En preparación"
        2 -> "Listo"
        3 -> "En camino"
        4 -> "Entregado"
        else -> null
    }
    if (nuevoEstado == null || !pedido.cambiarEstado(nuevoEstado)) {
        println("Error: Estado inválido.")
        return
    }
    if (nuevoEstado == "Entregado") pedido.repartidor?.let { println(it.entregarPedido()) }
    println("Pedido #${pedido.id}: ${pedido.estado}")
}

// FUNCIÓN PARA VALIDAR EL NOMBRE DEL CLIENTE
fun solicitarNombreCliente(): String {
    while (true) {
        print("Nombre del cliente: ")
        val nombre = readlnOrNull()?.trim().orEmpty()

        when {
            nombre.length !in 3..60 -> {
                println("Error: El nombre debe tener entre 3 y 60 caracteres.")
            }
            !nombre.all {
                it.isLetter() || it.isWhitespace() ||
                        it == '-' || it == '\''
            } -> {
                println("Error: Ingrese un nombre válido.")
            }
            else -> return nombre
        }
    }
}
