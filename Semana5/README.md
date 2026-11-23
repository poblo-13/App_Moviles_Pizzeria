# Pizzería di Pablo - Semana 5

## Desarrollo de Aplicaciones Móviles (PMY2201)

Actividad formativa: Integrando Kotlin en Android Studio.

## Descripción

Continuación del proyecto Pizzería di Pablo, desarrollado
en Kotlin utilizando Android Studio y Jetpack Compose.

La aplicación permite registrar clientes y pedidos,
mostrando un resumen que se actualiza dinámicamente.

## Estructura modular

El proyecto utiliza tres módulos Gradle:

- app: interfaz de usuario, MainActivity, PantallaResumen
  y PizzeriaViewModel.
- data: modelos Kotlin y PizzeriaRepository.
- utils: funciones reutilizables de formato y validación.

## Tecnologías

- Kotlin
- Android Studio
- Jetpack Compose y Material Design
- Gradle
- ViewModel
- LiveData
- Kotlin Coroutines

## Funcionalidades

- Registro de clientes.
- Validación básica del formulario.
- Control de clientes duplicados.
- Registro de pedidos de prueba.
- Asignación de identificadores a los pedidos.
- Visualización de clientes y pedidos.
- Actualización dinámica del total de clientes.
- Actualización dinámica del total de pedidos.
- Visualización del último cliente registrado.

## Gestión de datos

Los datos se almacenan temporalmente en memoria mediante
PizzeriaRepository.

ViewModel coordina las operaciones y LiveData permite
actualizar la interfaz cuando los registros cambian.

Los datos no se conservan después de cerrar completamente
la aplicación.

## Pruebas

- Compilación de depuración mediante Gradle.
- Ejecución en Pixel 4 API 35.
- Registro de clientes y pedidos.
- Verificación de actualización dinámica del resumen.

## Ejecución

1. Abrir PizzeriaApp en Android Studio.
2. Sincronizar Gradle.
3. Seleccionar un emulador compatible.
4. Ejecutar el módulo app.
5. Registrar clientes y pedidos para comprobar el resumen.

## Evidencias

Las capturas se encuentran en la carpeta Evidencias.
