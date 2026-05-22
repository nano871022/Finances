# Módulo de Declaración de Renta (DIAN)

Este módulo se encarga de calcular y proyectar la declaración de renta para personas naturales en Colombia, basándose en la normativa de la DIAN.

## ¿Qué hace el módulo?

1.  **Estimación de Declaración**: Calcula si el usuario está obligado a declarar renta en el año fiscal actual o anterior.
2.  **Cálculo de Impuesto**: Estima el valor del impuesto a pagar utilizando el sistema de rangos (UVT) progresivo.
3.  **Historial**: Muestra un registro de declaraciones pasadas.
4.  **Proyecciones**: Realiza proyecciones financieras para estimar la obligación tributaria futura basándose en el comportamiento de gastos e ingresos actuales.
5.  **Gestión de Patrimonio**: Permite registrar activos de forma manual para el cálculo del patrimonio bruto.

## Origen de los Datos

Los datos provienen de tres fuentes principales a través de puertos definidos en la arquitectura:

*   **Datos Financieros Internos (`ExternalFinancialDataPort`)**: 
    *   Ingresos brutos acumulados (YTD).
    *   Compras con tarjeta de crédito.
    *   Pagos por débito (consumos).
*   **Patrimonio (`PatrimonyPersistencePort`)**: 
    *   Activos registrados manualmente por el usuario en la aplicación.
*   **Configuración Tributaria (`TaxConfigurationPort`)**: 
    *   Valores de la UVT (Unidad de Valor Tributario) por año.
    *   Topes legales para declarar.
    *   Tablas de retención y rangos de impuesto.

## Servicios y Cálculos

El núcleo de la lógica reside en `GetTaxDeclarationUseCaseImpl` dentro del core. Los cálculos principales incluyen:

1.  **Validación de Topes**: Se verifica si se supera alguno de los siguientes límites (valores configurables en UVT):
    *   Ingresos Brutos > 1,400 UVT.
    *   Compras con Tarjeta de Crédito > 1,400 UVT.
    *   Consumos Totales > 1,400 UVT.
    *   Patrimonio Bruto > 4,500 UVT.
2.  **Cálculo Progresivo**: 
    *   Convierte la base gravable a UVT.
    *   Busca el rango correspondiente en la tabla de tarifas.
    *   Aplica la fórmula: `((Base en UVT - Límite Inferior) * Tarifa Marginal) + Impuesto Base`.

## Configuración y Ajustes

Para cambiar los valores de la UVT, los topes o los rangos de las tablas sin modificar el código, se debe editar el archivo:

**Ruta:** `japl-android-finances-services/src/main/assets/dian_config.properties`

### Parámetros Ajustables:
*   `uvt.value.[año]`: Define el valor de la UVT para un año específico (ej. `uvt.value.2024=47065.0`).
*   `threshold.[tipo].uvt`: Define los topes de ingresos, consumos o patrimonio.
*   `bracket.[index]`: Define los rangos de la tabla de impuestos con el formato: `limite_inferior|limite_superior|tarifa_marginal|impuesto_base_uvt`.

## Estructura del Código

*   **UI**: Localizada en `com.nano871022.finances.features.declaracion_renta_dian.ui`.
*   **ViewModel**: `TaxDeclarationViewModel.kt` gestiona el estado y la carga de datos.
*   **Lógica de Negocio**: `GetTaxDeclarationUseCaseImpl.kt` en el módulo `:japl-android-finances-core`.
*   **Implementación de Configuración**: `TaxConfigurationPortImpl.kt` en el módulo `:japl-android-finances-services`.
