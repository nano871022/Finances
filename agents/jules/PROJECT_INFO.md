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
- **minSdk**: 21 (Android 5.0) - Requerido para Jetpack Compose.
- **Edge-to-Edge**: Habilitado en `MainActivity`. Los layouts XML usan `fitsSystemWindows="true"`.
