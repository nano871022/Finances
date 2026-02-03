# Información del Proyecto - Jules

Este documento contiene información clave sobre el proyecto para guiar futuras tareas.

## Estructura de Módulos
- **App**: Módulo principal legado, migrando a Compose. Contiene la navegación principal y MainActivity.
- **Core (japl-android-finances-core)**: Implementa la lógica de negocio y casos de uso (interfaces inbound).
- **Service (japl-android-finances-services)**: Capa de acceso a datos y conexiones externas (SQLite, Google Drive, DAOs).
- **UI**: Componentes personalizados de Jetpack Compose. Debe usarse siempre que sea posible.
- **iports (japl-finances-iports)**: Interfaces de datos compartidos y DTOs.
- **Credit**: Gestión de cuotas de crédito fijo.
- **CreditCard (CreditCardModule)**: Gestión de compras con tarjeta de crédito (crédito variable).
- **Paid (japl-android-finances-paid-module)**: Gestión de pagos en efectivo.
- **Graphs (japl-android-graphs)**: Módulo para gráficas.
- **Inputs (japl-android-finances-inputs)**: Probablemente para manejo de entradas de datos.

## Arquitectura
- Multi-módulo, arquitectura hexagonal.
- Migración de XML a Jetpack Compose con Material 3.
- Inyección de dependencias con Hilt.

## Pautas de Desarrollo
- Usar componentes de `UI` en lugar de básicos cuando existan.
- Al usar `Text`, especificar siempre un color de `MaterialTheme.colorScheme`.
- Seguir el flujo de la arquitectura hexagonal (Core -> iports <- Service).
- No intentar compilar localmente debido a dependencias externas privadas.
- Para disparar un build de prueba: push a una rama con `[BUILD]` en el comentario.

## Configuración de SDK (Ajustado)
- **compileSdk**: 36
- **targetSdk**: 36
- **minSdk**: 26 (Android 8.0)
- **Edge-to-Edge**: Habilitado en `MainActivity`. Los layouts XML usan `fitsSystemWindows="true"` en la Toolbar para integración perfecta con la barra de sistema.
- **Protocolo de Permisos**: Se implementó diálogo de justificación (rationale) para permisos sensibles (SMS) según políticas de Google Play.
- **Codificación de Colores en Cuotas**: En el listado de compras con tarjeta de crédito, la comparación de cuotas (ej. 5/10) cambia de color según las cuotas faltantes: >5: Rojo, 5: Blanco, 4: Azul, 3: Morado, 2: Naranja, 1: Por defecto.
