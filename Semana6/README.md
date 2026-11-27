# Semana 6 - Desarrollo App Móviles I

## Avanzando hacia Android Studio

**Asignatura:** Desarrollo App Móviles I (PMY2201)
**Actividad:** Experiencia 2 - Semana 6
**Tipo:** Evaluación individual

## 1. Descripción del proyecto

Aplicación Android desarrollada con Kotlin y Jetpack Compose para la gestión básica de una veterinaria.

Permite registrar dueños, asociar mascotas a sus responsables y registrar consultas veterinarias, manteniendo un resumen dinámico de la información.

El proyecto reutiliza y mejora la arquitectura desarrollada en las semanas anteriores.

## 2. Arquitectura modular

El proyecto contiene cuatro módulos:

- app: punto de entrada mediante MainActivity.
- ui: pantallas Jetpack Compose, ViewModel y tema visual.
- data: modelos de negocio y repositorio veterinario.
- utils: funciones reutilizables de validación.

## 3. Tecnologías utilizadas

- Kotlin
- Android Studio
- Gradle
- Jetpack Compose y Material Design 3
- ViewModel
- LiveData
- Kotlin Coroutines
- AnimatedVisibility
- DropdownMenu
- CircularProgressIndicator
- JUnit 4

## 4. Funcionalidades

### Gestión veterinaria
- Registro y listado de dueños.
- Registro de mascotas asociadas a dueños.
- Registro de consultas asociadas a mascotas.
- Resumen dinámico de registros.
- Validaciones de formularios.
- Prevención de registros duplicados según las reglas implementadas.
- Generación automática de identificadores en el repositorio.

### Navegación
Menú desplegable con íconos y cuatro secciones:
Inicio, Dueños, Mascotas y Consultas.

### Animaciones
Transiciones Fade In y Fade Out implementadas mediante AnimatedVisibility al iniciar y cambiar entre secciones.

### Indicadores de progreso
Indicador circular controlado mediante LiveData y Coroutines.

Se utiliza una espera demostrativa de 1,2 segundos para visualizar el estado de carga. Los datos se consultan desde un repositorio en memoria.

## 5. Pruebas

Se realizaron pruebas unitarias mediante JUnit:

- ValidacionesTest: 5 casos.
- VeterinariaRepositoryTest: 7 casos.

Comandos utilizados:

    .\gradlew.bat :utils:testDebugUnitTest
    .\gradlew.bat :data:testDebugUnitTest
    .\gradlew.bat :app:assembleDebug

También se comprobó manualmente el funcionamiento en un emulador Pixel 4 API 35.

## 6. Evidencias

La carpeta Evidencias contiene:

- Capturas de registro de dueños, mascotas y consultas.
- Captura del resumen dinámico.
- Captura del menú desplegable.
- Captura del indicador de progreso.
- Video MP4 de las animaciones Fade In/Out.
- Reportes XML de las pruebas unitarias.

## 7. Ejecución

1. Abrir la carpeta PizzeriaApp en Android Studio.
2. Esperar la sincronización de Gradle.
3. Seleccionar un emulador Android compatible.
4. Ejecutar el módulo app.

## 8. Limitaciones

Los registros se almacenan temporalmente en memoria, por lo que no existe persistencia permanente mediante una base de datos.

El indicador de progreso incluye una espera demostrativa, ya que las consultas al repositorio local se realizan casi instantáneamente.
