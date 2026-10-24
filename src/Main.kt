fun main() {
    val nombrePizzeria: String = "Pizzería di Pablo"
    println("Bienvenido a $nombrePizzeria")

    // REGISTRO DEL NOMBRE
    var nombreCliente: String
    var nombreValido = false

    do {
        println("Por favor ingrese su nombre y apellido")
        nombreCliente = readlnOrNull()?.trim().orEmpty()

        if (nombreCliente.isBlank()) {
            println("Error: El campo no puede estar vacío")
        }
        else if (nombreCliente.any { it.isDigit() }) {
            println("Error: El nombre no puede contener números")
        }
        else if (nombreCliente.length !in 3..60) {
            println("Error: El nombre debe tener entre 3 y 60 caracteres")
        }
        else {
            println("Nombre registrado correctamente: $nombreCliente")
            nombreValido = true
        }
    } while (!nombreValido)

    // REGISTRO DE LA DIRECCIÓN
    var direccion: String
    var direccionValida = false

    do {
        println("$nombreCliente, por favor ingrese su dirección")
        direccion = readlnOrNull()?.trim().orEmpty()

        if (direccion.isBlank()) {
            println("Error: El campo no puede estar vacío")
        }
        else if (direccion.length !in 5..100) {
            println("Error: La dirección debe tener entre 5 y 100 caracteres")
        }
        else {
            println("Dirección registrada correctamente: $direccion")
            direccionValida = true
        }
    } while (!direccionValida)

    // REGISTRO DEL TELÉFONO
    var telefono: String
    var telefonoValido = false

    do {
        println("$nombreCliente, por favor ingrese su número de teléfono")
        telefono = readlnOrNull()?.trim().orEmpty()

        if (telefono.isBlank()) {
            println("Error: El campo no puede estar vacío")
        }
        else if (telefono.length != 12) {
            println("Error: El campo debe tener 12 caracteres")
        }
        else if (!telefono.startsWith("+569")) {
            println("Error: El teléfono debe comenzar con +569")
        }
        else if (!telefono.drop(1).all { it.isDigit() }) {
            println("Error: El teléfono solo puede contener números después del +")
        }
        else {
            println("Teléfono registrado correctamente: $telefono")
            telefonoValido = true
        }
    } while (!telefonoValido)

    // CATÁLOGO DE PIZZAS Y PRECIOS
    val pizza1: String = "Pepperoni"
    val valorPizza1: Int = 9990

    val pizza2: String = "Cuatro Quesos"
    val valorPizza2: Int = 10990

    val pizza3: String = "Napolitana"
    val valorPizza3: Int = 8990

    val pizza4: String = "Todas las Carnes"
    val valorPizza4: Int = 14990

    val pizza5: String = "Pollo al Pesto"
    val valorPizza5: Int = 13990

    // INVENTARIO DE PIZZAS
    var stockPepperoni: Int = 20
    var stockCuatroQuesos: Int = 20
    var stockNapolitana: Int = 20
    var stockTodasLasCarnes: Int = 20
    var stockPolloPesto: Int = 20

    // PROCESO DE SELECCIÓN DE PIZZAS
    var opcionPizza: Int?
    var agregarOtraPizzas: Boolean = false

    // CANTIDADES ACUMULADAS DEL PEDIDO
    var cantidadPepperoni = 0
    var cantidadCuatroQuesos = 0
    var cantidadNapolitana = 0
    var cantidadTodasLasCarnes = 0
    var cantidadPolloPesto = 0

    var stockDisponible: Int

    // CICLO PRINCIPAL PARA AGREGAR VARIEDADES
    do {

        // MENÚ Y VALIDACIÓN DE LA SELECCIÓN
        do {
            println("====================================")
            println("         PIZZERÍA DI PABLO")
            println("          MENÚ DE PIZZAS")
            println("====================================")
            println("1. ${pizza1.padEnd(22)} - \$$valorPizza1")
            println("2. ${pizza2.padEnd(22)} - \$$valorPizza2")
            println("3. ${pizza3.padEnd(22)} - \$$valorPizza3")
            println("4. ${pizza4.padEnd(22)} - \$$valorPizza4")
            println("5. ${pizza5.padEnd(22)} - \$$valorPizza5")
            println("====================================")
            print("Por favor seleccione una opción (1-5): ")

            opcionPizza = readlnOrNull()?.trim()?.toIntOrNull()

            // CALCULAR EL STOCK RESTANTE
            stockDisponible = when (opcionPizza) {
                1 -> stockPepperoni - cantidadPepperoni
                2 -> stockCuatroQuesos - cantidadCuatroQuesos
                3 -> stockNapolitana - cantidadNapolitana
                4 -> stockTodasLasCarnes - cantidadTodasLasCarnes
                5 -> stockPolloPesto - cantidadPolloPesto
                else -> 0
            }

            if (opcionPizza !in 1..5) {
                println("Error: Opción no válida")
            }
            else if (stockDisponible <= 0) {
                println("Error: Esta variedad está agotada. Seleccione otra pizza")
            }

        } while (opcionPizza !in 1..5 || stockDisponible <= 0)

        // IDENTIFICAR LA PIZZA SELECCIONADA
        when (opcionPizza) {
            1 -> println("Seleccionaste: $pizza1")
            2 -> println("Seleccionaste: $pizza2")
            3 -> println("Seleccionaste: $pizza3")
            4 -> println("Seleccionaste: $pizza4")
            5 -> println("Seleccionaste: $pizza5")
        }

        // SOLICITAR Y VALIDAR LA CANTIDAD
        var cantidadPizzas = 0

        do {
            cantidadPizzas = 0

            print("¿Cuántas pizzas desea?: ")
            val entrada = readlnOrNull()?.trim()

            if (entrada.isNullOrBlank()) {
                println("Error: El campo no puede estar vacío")
            }
            else if (!entrada.all { it.isDigit() }) {
                println("Error: Ingrese solo dígitos")
            }
            else {
                cantidadPizzas = entrada.toIntOrNull() ?: 0

                if (cantidadPizzas <= 0) {
                    println("Error: Ingrese una cantidad válida")
                }
                else if (cantidadPizzas > stockDisponible) {
                    println("Error: No hay ingredientes suficientes")
                    println("Stock disponible: $stockDisponible")
                }
                else {
                    println("Ingredientes suficientes")
                }
            }

        } while (cantidadPizzas !in 1..stockDisponible)

        // GUARDAR LAS CANTIDADES SELECCIONADAS
        when (opcionPizza) {
            1 -> cantidadPepperoni += cantidadPizzas
            2 -> cantidadCuatroQuesos += cantidadPizzas
            3 -> cantidadNapolitana += cantidadPizzas
            4 -> cantidadTodasLasCarnes += cantidadPizzas
            5 -> cantidadPolloPesto += cantidadPizzas
        }

        // COMPROBAR SI QUEDAN VARIEDADES DISPONIBLES
        val quedanPizzasDisponibles =
            cantidadPepperoni < stockPepperoni ||
                    cantidadCuatroQuesos < stockCuatroQuesos ||
                    cantidadNapolitana < stockNapolitana ||
                    cantidadTodasLasCarnes < stockTodasLasCarnes ||
                    cantidadPolloPesto < stockPolloPesto

        if (!quedanPizzasDisponibles) {
            println("No quedan más pizzas disponibles para agregar.")
            agregarOtraPizzas = false
        }
        else {

            // PREGUNTAR SI DESEA AGREGAR OTRA VARIEDAD
            var respuesta: Int?

            do {
                println("¿Desea agregar otra variedad de pizza?")
                println("1. Sí")
                println("2. No")

                respuesta = readlnOrNull()?.trim()?.toIntOrNull()

                if (respuesta == null) {
                    println("Error: Ingrese una opción numérica")
                }
                else if (respuesta !in 1..2) {
                    println("Error: Respuesta inválida")
                }

            } while (respuesta !in 1..2)

            agregarOtraPizzas = (respuesta == 1)
        }

    } while (agregarOtraPizzas)

    //CALCULAR LA CANTIDAD TOTAL DE PIZZAS
    val totalPizzas = cantidadPepperoni +
            cantidadCuatroQuesos +
            cantidadNapolitana +
            cantidadTodasLasCarnes +
            cantidadPolloPesto
    println("Pepperoni: $cantidadPepperoni")
    println("Cuatro Quesos: $cantidadCuatroQuesos")
    println("Napolitana: $cantidadNapolitana")
    println("Todas las Carnes: $cantidadTodasLasCarnes")
    println("Pollo al Pesto: $cantidadPolloPesto")
    println("Cantidad total de pizzas: $totalPizzas")

    //CALCULAR SUBTOTAL
    val subtotal = cantidadPepperoni * valorPizza1 +
            cantidadCuatroQuesos * valorPizza2 +
            cantidadNapolitana * valorPizza3 +
            cantidadTodasLasCarnes * valorPizza4 +
            cantidadPolloPesto * valorPizza5
    println("Subtotal del pedido: $$subtotal")

    //CALCULAR DESCUENTO > 3
    var descuento: Int

    if (totalPizzas > 3){
        descuento = subtotal * 10 / 100
        println("El pedido tiene un descuento del 10%")
    }
    else {
        descuento = 0
        println("El pedido no tiene descuento")
    }

    //CALCULAR TOTAL DEL PEDIDO
    val totalPedido = subtotal - descuento

    println("Descuento aplicado: $$descuento")
    println("Total del pedido: $$totalPedido")

    //IDENTIFICACIÓN DEL PEDIDO
    val idPedido: Int = 1

    //RESUMEN PREVIO DEL PEDIDO
    println("====================================")
    println("       RESUMEN DEL PEDIDO")
    println("====================================")
    println("Número de pedido: $idPedido")
    println("Cliente: $nombreCliente")
    println("Dirección: $direccion")
    println("Teléfono: $telefono")
    println("------------------------------------")

    if (cantidadPepperoni > 0){
        println("$cantidadPepperoni x $pizza1: ${cantidadPepperoni * valorPizza1} pesos")
    }
    if (cantidadCuatroQuesos > 0){
        println("$cantidadCuatroQuesos x $pizza2: ${cantidadCuatroQuesos * valorPizza2} pesos")
    }
    if (cantidadNapolitana > 0){
        println("$cantidadNapolitana x $pizza3: ${cantidadNapolitana * valorPizza3} pesos")
    }
    if (cantidadTodasLasCarnes > 0){
        println("$cantidadTodasLasCarnes x $pizza4: ${cantidadTodasLasCarnes * valorPizza4} pesos")
    }
    if (cantidadPolloPesto > 0){
        println("$cantidadPolloPesto x $pizza5: ${cantidadPolloPesto * valorPizza5} pesos")
    }

    println("------------------------------------")
    println("Cantidad total: $totalPizzas")
    println("Subtotal: $$subtotal")
    println("Descuento: $$descuento")
    println("TOTAL A PAGAR: $$totalPedido")
    println("====================================")

    //CONFIRMAR O CANCELAR EL PEDIDO
    var respuestaConfirmacion: Int?

    do {
        println("¿Desea confirmar el pedido?")
        println("1. Confirmar")
        println("2. Cancelar")

        respuestaConfirmacion = readlnOrNull()?.trim()?.toIntOrNull()

        if (respuestaConfirmacion !in 1..2) {
            println("Error: Seleccione una opción válida")
        }
    } while (respuestaConfirmacion !in 1..2)

    val pedidoConfirmado: Boolean = (respuestaConfirmacion == 1)
    val estadoPedido: String = if (pedidoConfirmado) {
        "Confirmado"
    }
    else{
        "Cancelado"
    }

    var repartidorAsignado = "Sin asignar"

    if (pedidoConfirmado) {
        // DESCONTAR EL INVENTARIO REAL
        stockPepperoni -= cantidadPepperoni
        stockCuatroQuesos -= cantidadCuatroQuesos
        stockNapolitana -= cantidadNapolitana
        stockTodasLasCarnes -= cantidadTodasLasCarnes
        stockPolloPesto -= cantidadPolloPesto

        println("Pedido confirmado correctamente")
        println("Inventario actualizado")

        //ASIGNAR REPARTIDOR
        do {
            print("Ingrese el nombre del repartidor asignado: ")
            repartidorAsignado = readlnOrNull()?.trim().orEmpty()

            if (repartidorAsignado.isBlank()) {
                println("Error: Debe ingresar un repartidor")
            }

        } while (repartidorAsignado.isBlank())

        //COMPROBANTE FINAL
        println()
        println("====================================")
        println("          PIZZERÍA DI PABLO")
        println("          COMPROBANTE FINAL")
        println("====================================")
        println("Pedido N°: $idPedido")
        println("Cliente: $nombreCliente")
        println("Dirección: $direccion")
        println("Teléfono: $telefono")
        println("------------------------------------")

        if (cantidadPepperoni > 0) {
            println("$cantidadPepperoni x $pizza1 - $$valorPizza1 c/u")
        }
        if (cantidadCuatroQuesos > 0) {
            println("$cantidadCuatroQuesos x $pizza2 - $$valorPizza2 c/u")
        }
        if (cantidadNapolitana > 0) {
            println("$cantidadNapolitana x $pizza3 - $$valorPizza3 c/u")
        }
        if (cantidadTodasLasCarnes > 0) {
            println("$cantidadTodasLasCarnes x $pizza4 - $$valorPizza4 c/u")
        }
        if (cantidadPolloPesto > 0) {
            println("$cantidadPolloPesto x $pizza5 - $$valorPizza5 c/u")
        }

        println("------------------------------------")
        println("Cantidad de pizzas: $totalPizzas")
        println("Subtotal: $$subtotal")
        println("Descuento: $$descuento")
        println("TOTAL: $$totalPedido")
        println("------------------------------------")
        println("Estado del pedido: $estadoPedido")
        println("Repartidor: $repartidorAsignado")
        println("====================================")
        println("¡Gracias por preferir Pizzería di Pablo!")

    } else {

        // PEDIDO CANCELADO
        println()
        println("====================================")
        println("          PEDIDO CANCELADO")
        println("====================================")
        println("Pedido N°: $idPedido")
        println("Cliente: $nombreCliente")
        println("Estado del pedido: $estadoPedido")
        println("Repartidor: $repartidorAsignado")
        println("No se descontaron ingredientes")
        println("====================================")
    }
}

