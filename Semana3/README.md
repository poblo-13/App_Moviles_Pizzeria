
# Pizzería di Pablo

## Desarrollo de Aplicaciones Móviles - Semana 3

Sistema de gestión de pedidos para una pizzería, desarrollado en Kotlin mediante una aplicación de consola.

Este proyecto continúa el trabajo realizado durante las semanas 1 y 2, incorporando nuevas herramientas del lenguaje Kotlin para validar datos, analizar clases y manipular objetos.

## Tecnologías utilizadas

- Kotlin
- IntelliJ IDEA
- JDK 22
- Programación orientada a objetos

## Funcionalidades del sistema

1. Registrar nuevos pedidos y clientes.
2. Consultar el inventario de ingredientes.
3. Visualizar los pedidos del día.
4. Actualizar los estados de los pedidos.
5. Reabastecer ingredientes.
6. Consultar promociones y analizar clases.
7. Combinar pedidos mediante sobrecarga de operadores.
8. Visualizar el resumen general de la Semana 3.

## Funcionalidades incorporadas en la Semana 3

### Validaciones con Regex y Ranges

- Validación del correo electrónico mediante expresiones regulares.
- Validación y formato de números telefónicos.
- Control de cantidades mediante el rango 1..100.
- Límite acumulado de 20 pizzas por pedido.
- Validación de fechas promocionales mediante LocalDate y rangos.

### Anotaciones personalizadas

Se implementó la anotación @Promocionable para identificar productos que participan en promociones.

Las pizzas Pepperoni y Cuatro Quesos cuentan con esta anotación y un descuento promocional del 5%.

### Reflection

Se utiliza Reflection para:

- Identificar dinámicamente productos promocionables.
- Consultar las propiedades de las clases Cliente y Pedido.
- Mostrar los métodos disponibles en estas clases.

### Sobrecarga de operadores

Se implementó el operador + en la clase Pedido para combinar los productos de dos pedidos compatibles.

El sistema recalcula cantidades, descuentos, impuestos y total.

La combinación es una simulación de consulta y no modifica el inventario ni registra una nueva venta.

### Igualdad de objetos

Se implementaron equals() y hashCode() en las clases Cliente y Producto.

Esto permite reconocer elementos equivalentes y evitar registros duplicados mediante colecciones Set.

### Desestructuración

Se incorporaron las funciones component1(), component2() y component3() en las clases Cliente y Pedido.

Estas funciones permiten extraer los datos principales mediante declaraciones de desestructuración.

## Promociones

El sistema contempla una promoción vigente desde el 1 hasta el 30 de noviembre de 2026.

Las pizzas identificadas con @Promocionable pueden recibir un descuento adicional del 5% durante ese período.

También se mantiene el descuento del 10% por pedidos de más de tres pizzas.

## Ejecución del proyecto

1. Abrir la carpeta Semana3 en IntelliJ IDEA.
2. Verificar que el proyecto utilice un JDK compatible.
3. Abrir el archivo src/Main.kt.
4. Ejecutar la función main().
5. Seleccionar una opción del menú mediante la consola.

## Comprobación de requisitos

La opción 8, "Resumen general del sistema", permite comprobar:

- Anotaciones personalizadas y Reflection.
- Promociones y fechas de vigencia.
- Comparación de productos y clientes.
- Eliminación de elementos duplicados.
- Sobrecarga del operador +.
- Desestructuración de Cliente y Pedido.

Las validaciones de correo, teléfono y cantidad pueden comprobarse mediante la opción 1, "Registrar nuevo pedido".

Las demostraciones de la opción 8 utilizan datos temporales y no modifican las ventas ni el inventario.

## Estructura del código

- Main.kt: menú principal y gestión del sistema.
- Usuario.kt: clase base de usuarios.
- Cliente.kt: información y comparación de clientes.
- Repartidor.kt: gestión de repartidores.
- Producto.kt: catálogo e igualdad de productos.
- Pedido.kt: pedidos, cálculos y sobrecarga de operadores.
- Promocionable.kt: anotación personalizada.
- PizzasPromocionables.kt: productos promocionables.
- Promociones.kt: fechas y descuentos.
- Analisis.kt: Reflection y demostraciones de la Semana 3.

## Consideraciones

El sistema funciona por consola y mantiene sus datos en memoria durante la ejecución.

Las notificaciones de correo y la asignación de repartidores son simuladas.

Proyecto desarrollado con fines académicos para la asignatura Desarrollo de Aplicaciones Móviles.
