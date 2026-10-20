# GestionPedidosPizzeria

Aplicación de consola en Kotlin/JVM para registrar un pedido de pizzería por ejecución: registra al cliente, permite elegir una pizza del catálogo y su cantidad, verifica y descuenta el stock, calcula el total (con 10% de descuento cuando se piden más de 3 pizzas), asigna el primer repartidor disponible y muestra un resumen del pedido.

El catálogo de pizzas y la lista de repartidores se cargan desde archivos CSV en `src/main/resources`.

## Requisitos

- JDK 21
- IntelliJ IDEA

## Cómo ejecutar

1. Configurar IntelliJ para leer datos por consola: **Settings → Build, Execution, Deployment → Build Tools → Gradle → Build and run using: IntelliJ IDEA. Run tests using: Gradle.**
2. Abrir el proyecto en IntelliJ IDEA y esperar a que Gradle sincronice.
3. Abrir `src/main/kotlin/cl/duoc/pmy2201/pizzeria/Main.kt`.
4. Presionar ▶ junto a `fun main()`.

## Cómo correr las pruebas

- Desde IntelliJ IDEA: clic derecho sobre `src/test/kotlin` → **Run 'Tests in kotlin'**, o ▶ junto a cada clase de prueba.
- Desde la terminal, en la raíz del proyecto:
  - Windows: `.\gradlew.bat test`
  - macOS/Linux: `./gradlew test`

Las pruebas cubren `CalculadoraPedido`, `InventarioService` y `AsignacionRepartidorService`.

## Datos de prueba

Casos para probar manualmente con los CSV incluidos en `src/main/resources`:

- **Aplica descuento:** elegir Pepperoni con cantidad 4. Se muestra el aviso de descuento y el resumen indica subtotal $47.960, descuento 10% de -$4.796 y total $43.164.
- **Rechazo por stock:** elegir Cuatro Quesos con cantidad 3 (stock 2). Se informa que no hay ingredientes suficientes y se pregunta si se desea modificar el pedido.
- **Sin repartidor:** en `repartidores.csv`, dejar la columna `disponible` en `false` para todos los repartidores. Al confirmar un pedido se muestra el aviso de que no hay repartidores disponibles, y el resumen indica "Sin repartidor disponible". Restaurar los valores originales después de la prueba.

## Estructura

```
src/
├── main/
│   ├── kotlin/cl/duoc/pmy2201/pizzeria/
│   │   ├── Main.kt     Orquesta el flujo y muestra el resumen del pedido
│   │   ├── model/      Entidades: Cliente, Pizza, Repartidor, Pedido, EstadoPedido
│   │   ├── data/       Lectura de CSV y repositorios de pizzas y repartidores
│   │   ├── service/    Lógica: cálculo del total, stock, asignación de repartidor y creación del pedido
│   │   └── ui/         Interacción por consola: lectura de datos, menú y mensajes
│   └── resources/
│       ├── pizzas.csv
│       └── repartidores.csv
└── test/kotlin/cl/duoc/pmy2201/pizzeria/service/   Pruebas unitarias
```

## Decisiones de diseño

Ver [docs/DECISIONES.md](docs/DECISIONES.md).
