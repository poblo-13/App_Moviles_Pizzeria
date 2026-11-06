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
        PizzaPepperoni(),
        PizzaCuatroQuesos(),
        Producto("Napolitana", 8990, mapOf("Masa" to 1, "Queso" to 2, "Salsa de tomate" to 1, "Tomate" to 2)),
        Producto("Todas las Carnes", 14990, mapOf("Masa" to 1, "Queso" to 2, "Salsa de tomate" to 1, "Carne" to 3)),
        Producto("Pollo al Pesto", 13990, mapOf("Masa" to 1, "Queso" to 2, "Pollo" to 2, "Pesto" to 1))
    ).distinct()

    val repartidores = listOf(
        Repartidor("Ana", "1001", "Centro"),
        Repartidor("Luis", "1002", "San Pedro"),
        Repartidor("Juan", "1003", "Talcahuano")
    )

    var pedidosDelDia = emptyArray<Pedido>()
    val clientesRegistrados = mutableSetOf<Cliente>()
    var opcion: Int?

    do {
        mostrarMenu()
        print("Seleccione una opción: ")
        opcion = readlnOrNull()?.trim()?.toIntOrNull()

        when (opcion) {
            1 -> registrarPedido(
                catalogo,
                inventario,
                repartidores,
                pedidosDelDia.size + 1,
                clientesRegistrados
            )?.let {
                pedidosDelDia += it
            }
            2 -> mostrarInventario(inventario)
            3 -> mostrarPedidosDelDia(pedidosDelDia)
            4 -> actualizarEstadoPedido(pedidosDelDia, repartidores)
            5 -> reabastecerIngredientes(inventario)
            6 -> mostrarAnalisisSistema(catalogo)
            7 -> combinarPedidos(pedidosDelDia)
            8 -> mostrarResumenGeneralSemana3(
                catalogo,
                pedidosDelDia,
                clientesRegistrados
            )
            0 -> println("Gracias por utilizar Pizzería di Pablo.")
            else -> println("Error: Seleccione una opción válida.")
        }
    } while (opcion != 0)
}


fun mostrarMenu() {
    println()
    println("========================================")
    println("            PIZZERÍA DI PABLO")
    println("           SISTEMA DE GESTIÓN")
    println("========================================")
    println("1. Registrar nuevo pedido")
    println("2. Consultar inventario")
    println("3. Ver pedidos del día")
    println("4. Actualizar estado de pedido")
    println("5. Reabastecer ingredientes")
    println("6. Promociones y análisis del sistema")
    println("7. Combinar pedidos")
    println("8. Resumen general del sistema")
    println("0. Salir del sistema")
    println("========================================")
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
    id: Int,
    clientesRegistrados: MutableSet<Cliente>
): Pedido? {
    println("\n--- NUEVO PEDIDO ---")
    val nombre = solicitarNombreCliente()
    val direccion = solicitarTextoObligatorio("Dirección: ")

    val telefono = solicitarTelefonoFormateado()

    val correo = solicitarCorreoValido()

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
        val cantidad = solicitarCantidadPizza(producto.nombre)


        val limitePorPedido = 20
        val cantidadAcumulada = seleccionados.sumOf { it.cantidad }

        if (cantidadAcumulada + cantidad > limitePorPedido) {
            println("Error: El máximo permitido es de $limitePorPedido pizzas por pedido.")
            println("Actualmente tiene $cantidadAcumulada pizzas seleccionadas.")
            println("Puede agregar como máximo ${limitePorPedido - cantidadAcumulada} pizzas adicionales.")
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

    val pedido = Pedido(id, cliente, seleccionados.toList(), zona, notas)
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

    if (clientesRegistrados.add(cliente)) {
        println("Nuevo cliente registrado correctamente.")
    } else {
        println("Cliente existente: no se generó un duplicado.")
    }
    println("Clientes únicos registrados: ${clientesRegistrados.size}")

    descontarIngredientes(pedido.productos, inventario)

    println("\nPedido confirmado. Inventario actualizado.")
    println("El repartidor se asignará cuando el pedido esté listo.")

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


    // DEMOSTRACIÓN DE DESESTRUCTURACIÓN
    println()
    println("========================================")
    println("       DESESTRUCTURACIÓN DE DATOS")
    println("========================================")

    // DESESTRUCTURAR UN OBJETO PEDIDO
    val primerPedido = pedidos.first()
    val (cliente, productos, total) = primerPedido

    // DESESTRUCTURAR UN OBJETO CLIENTE
    val (nombre, correo, telefono) = cliente

    println("Pedido N°: ${primerPedido.id}")
    println("Cliente: $nombre")
    println("Correo: $correo")
    println("Teléfono: $telefono")

    println("Productos:")
    for (detalle in productos) {
        println("- ${detalle.cantidad} x ${detalle.producto.nombre}")
    }

    println("Total del pedido: $$total")
    println("========================================")

}


fun actualizarEstadoPedido(
    pedidos: Array<Pedido>,
    repartidores: List<Repartidor>
) {
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
        println("El pedido ya fue entregado.")
        return
    }

    println("Estado actual: ${pedido.estado}")
    println("1. En preparación  2. Listo  3. En camino  4. Entregado")
    print("Nuevo estado: ")

    val nuevoEstado = when (readlnOrNull()?.trim()?.toIntOrNull()) {
        1 -> "En preparación"
        2 -> "Listo"
        3 -> "En camino"
        4 -> "Entregado"
        else -> null
    }

    if (nuevoEstado == null) {
        println("Error: Seleccione un estado válido.")
        return
    }

    // Si está listo para salir y no tiene repartidor, buscar uno.
    if (pedido.estado == "Listo" &&
        nuevoEstado == "En camino" &&
        pedido.repartidor == null
    ) {
        val disponible = buscarRepartidor(
            pedido.zonaEntrega,
            repartidores
        )

        if (disponible == null) {
            println("No hay repartidores disponibles.")
            println("El pedido permanece en estado Listo.")
            return
        }

        pedido.repartidor = disponible
        disponible.disponible = false
        println("Repartidor asignado: ${disponible.nombre}")
    }

    if (!pedido.cambiarEstado(nuevoEstado)) {
        println("Error: Transición no permitida.")
        println("Debe avanzar al siguiente estado.")
        return
    }

    // Asignar repartidor cuando el pedido esté listo.
    if (nuevoEstado == "Listo") {
        val disponible = buscarRepartidor(
            pedido.zonaEntrega,
            repartidores
        )

        if (disponible != null) {
            pedido.repartidor = disponible
            disponible.disponible = false
            println("Repartidor asignado: ${disponible.nombre}")
        } else {
            println("No hay repartidores disponibles por ahora.")
        }
    }

    // Liberar al repartidor después de la entrega.
    if (nuevoEstado == "Entregado") {
        pedido.repartidor?.let {
            println(it.entregarPedido())
        }
    }

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


fun solicitarTelefonoFormateado(): String {
    val formatoChile = Regex("""56(9)(\d{4})(\d{4})""")
    val formatoInternacional = Regex("""(\d{2})(\d{3})(\d{3})(\d{4})""")

    while (true) {
        print("Teléfono del cliente: ")
        val entrada = readlnOrNull()?.trim().orEmpty()

        if (!entrada.matches(Regex("""[+\d\s()-]+"""))) {
            println("Error: El teléfono contiene caracteres no permitidos.")
            continue
        }

        val digitos = entrada.filter { it.isDigit() }

        val chile = formatoChile.matchEntire(digitos)
        if (chile != null) {
            val (_, prefijo, grupo1, grupo2) = chile.groupValues
            return "+56 ($prefijo) $grupo1-$grupo2"
        }

        val internacional = formatoInternacional.matchEntire(digitos)
        if (internacional != null) {
            val (_, pais, grupo1, grupo2, grupo3) = internacional.groupValues
            return "+$pais ($grupo1) $grupo2-$grupo3"
        }

        println("Error: Formato telefónico inválido.")
        println("Ejemplo chileno: +56912345678")
    }
}


fun solicitarCorreoValido(): String {
    val formatoCorreo = Regex(
        """^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$"""
    )

    while (true) {
        print("Correo electrónico: ")
        val correo = readlnOrNull()?.trim().orEmpty()

        if (formatoCorreo.matches(correo)) {
            return correo
        } else {
            println("Error: Formato de correo inválido.")
            println("Ejemplo válido: cliente@ejemplo.com")
        }
    }
}


fun combinarPedidos(pedidos: Array<Pedido>) {

    println()
    println("========================================")
    println("          COMBINAR PEDIDOS")
    println("========================================")

    if (pedidos.size < 2) {
        println("Se necesitan al menos dos pedidos registrados.")
        return
    }

    // MOSTRAR LOS PEDIDOS DISPONIBLES
    for (pedido in pedidos) {
        println(
            "Pedido #${pedido.id} | " +
                    "${pedido.cliente.nombre} | " +
                    "Total: $${pedido.calcularTotal()}"
        )
    }

    println("----------------------------------------")

    print("Ingrese el número del primer pedido: ")
    val idPrimero = readlnOrNull()?.trim()?.toIntOrNull()

    print("Ingrese el número del segundo pedido: ")
    val idSegundo = readlnOrNull()?.trim()?.toIntOrNull()

    // VALIDAR IDENTIFICADORES
    if (idPrimero == null || idSegundo == null) {
        println("Error: Ingrese números de pedido válidos.")
        return
    }

    if (idPrimero == idSegundo) {
        println("Error: No puede combinar un pedido consigo mismo.")
        return
    }

    // BUSCAR LOS PEDIDOS EN EL ARREGLO
    val primerPedido = pedidos.find { it.id == idPrimero }
    val segundoPedido = pedidos.find { it.id == idSegundo }

    if (primerPedido == null || segundoPedido == null) {
        println("Error: Uno o ambos pedidos no existen.")
        return
    }

    // UTILIZAR EL OPERADOR + SOBRECARGADO
    try {
        val pedidoCombinado = primerPedido + segundoPedido

        println()
        println("========================================")
        println("       COMBINACIÓN EXITOSA")
        println("========================================")
        println("Pedido original #${primerPedido.id}")
        println("Pedido original #${segundoPedido.id}")
        println("----------------------------------------")

        println(pedidoCombinado.generarResumen())

        println("Resumen de consulta: no se registró una nueva venta.")
        println("El inventario y los pedidos originales no cambiaron.")

    } catch (error: IllegalArgumentException) {
        println("Error al combinar pedidos: ${error.message}")
    }
}


fun solicitarCantidadPizza(nombrePizza: String): Int {

    while (true) {
        print("Cantidad de $nombrePizza: ")

        val cantidad = readlnOrNull()?.trim()?.toIntOrNull()

        when {
            cantidad == null -> {
                println("Error: Ingrese una cantidad numérica válida.")
            }

            cantidad !in 1..100 -> {
                println("Error: La cantidad debe estar entre 1 y 100.")
            }

            else -> {
                return cantidad
            }
        }
    }
}
