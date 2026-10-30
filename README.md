# GestionPedidosPizzeria

Sistema de consola en Kotlin/JVM, de uso interno, para gestionar los pedidos de una pizzería: registro de pedidos con verificación de ingredientes, reporte del día, cambio de estado de los pedidos y reabastecimiento del inventario.

## Requisitos

- JDK 21
- IntelliJ IDEA

## Configuración de IntelliJ

**Settings → Build, Execution, Deployment → Build Tools → Gradle**

- **Build and run using:** IntelliJ IDEA
- **Run tests using:** Gradle

## Cómo ejecutar

Abrir `src/main/kotlin/cl/duoc/pmy2201/pizzeria/Main.kt` y presionar ▶ junto a `fun main()`.

## Cómo correr las pruebas

Clic derecho sobre `src/test/kotlin` → **Run 'All Tests'**.

## Menú

1. **Registrar pedido:** registra al cliente, permite elegir varios productos con su cantidad, verifica los ingredientes y muestra el resumen.
2. **Reporte del día:** lista los pedidos del día y los agrupa en completados y pendientes.
3. **Cambiar estado de un pedido:** avanza un pedido al estado siguiente (En preparación → Listo → En camino → Entregado).
4. **Inventario:** muestra el stock de ingredientes y permite reabastecer uno.
5. **Salir:** termina la aplicación.

## Estructura

```
src/main/
├── kotlin/cl/duoc/pmy2201/pizzeria/
│   ├── Main.kt     Orquesta el menú y los flujos
│   ├── model/      Entidades: Usuario, Cliente, Repartidor, Producto, ItemPedido, Pedido, EstadoPedido y cálculo de totales
│   ├── data/       Lectura de CSV y repositorios de productos, inventario y repartidores
│   ├── service/    Lógica de negocio: inventario, registro de pedidos y asignación de repartidor
│   ├── ui/         Interacción por consola: lectura de datos, menús, mensajes, resumen, reporte e inventario
│   └── util/       Validaciones y formato de moneda
└── resources/
    ├── pizzas.csv        Catálogo: nombre y precio
    ├── recetas.csv       Ingredientes y cantidades de cada pizza
    ├── inventario.csv    Stock y unidad de cada ingrediente
    └── repartidores.csv  Repartidores, disponibilidad y distancia
```

## Datos de prueba

Con los CSV incluidos:

- **Aplica descuento:** registrar un pedido con 4 Pepperoni. Se muestra el aviso de descuento y el resumen indica subtotal $47.960, descuento -$4.796 y total $43.164.
- **Faltan ingredientes:** registrar un pedido con 3 Cuatro Quesos. Se informa "Queso azul (faltan 30 g)", los pasos para reabastecer y las opciones para elegir otro producto, terminar o cancelar el pedido.

Las decisiones de diseño, los errores encontrados en pruebas, las limitaciones conocidas y las mejoras futuras están en [docs/DECISIONES.md](docs/DECISIONES.md).
