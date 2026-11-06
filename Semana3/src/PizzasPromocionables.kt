
@Promocionable(
    descripcion = "Promoción especial de Pepperoni",
    descuento = 5
)
class PizzaPepperoni : Producto(
    nombre = "Pepperoni",
    precio = 9990,
    ingredientes = mapOf(
        "Masa" to 1,
        "Queso" to 2,
        "Salsa de tomate" to 1,
        "Pepperoni" to 2
    )
)

@Promocionable(
    descripcion = "Promoción especial de Cuatro Quesos",
    descuento = 5
)
class PizzaCuatroQuesos : Producto(
    nombre = "Cuatro Quesos",
    precio = 10990,
    ingredientes = mapOf(
        "Masa" to 1,
        "Queso" to 4,
        "Salsa de tomate" to 1
    )
)
