# Decisiones de diseño — GestionPedidosPizzeria

## 1. Descripción

Aplicación de consola de uso interno para el personal de una pizzería. Permite registrar pedidos, revisar el reporte del día, avanzar el estado de cada pedido y reabastecer el inventario de ingredientes. Por ahora no incluye inicio de sesión.

## 2. Decisiones vigentes

### Menú y pedidos del día

- **Menú principal:** 1. Registrar pedido, 2. Reporte del día, 3. Cambiar estado de un pedido, 4. Inventario, 5. Resumen de funcionalidades, 6. Salir. El menú se repite hasta elegir Salir.
- **Pedidos del día:** se guardan en un arreglo con capacidad para 100 pedidos. Al llegar al máximo, se informa que no se pueden registrar más pedidos.
- **Identificador:** el N° de pedido es autoincremental y parte en 1.
- **Reporte del día:** lista cada pedido con su N°, cliente, pizzas, total y estado, y luego los agrupa en completados (Entregado) y pendientes.

### Registro del pedido

- **Cliente:** cada pedido registra al cliente con sus datos validados.
- **Validaciones:** cada dato se vuelve a pedir mientras no sea válido.
  - Nombre: solo letras (incluidas tildes, ñ y ü), espacios, `'` y `-`; mínimo 2 caracteres y al menos una letra.
  - Dirección: mínimo 5 caracteres y al menos una letra.
  - Teléfono: formato chileno de 9 dígitos, con o sin prefijo `+56` o `56`; se guarda como `+56XXXXXXXXX`. Tras 3 intentos inválidos se guarda `+56900000000`, que se muestra como `+56 (9) 0000-0000`.
  - Correo: opcional (Enter para omitir) y con formato `usuario@dominio.ext`. Tras 3 intentos inválidos se registra `sin.correo@example.com`.
- **Productos:** un pedido puede tener varios productos, cada uno con su cantidad. Si se elige un producto ya agregado, se suma la cantidad. Cada cantidad y el total del pedido deben estar en el rango 1..100, es decir, como máximo 100 pizzas por pedido.
- **Notas:** son opcionales; si no se ingresan, se muestra "Sin notas".

### Inventario y verificación de ingredientes

- **Datos de origen:** el catálogo, las recetas, el inventario y los repartidores se cargan desde archivos CSV (separador `;`, UTF-8, con encabezado). Las filas inválidas se informan como errores de carga y se omiten; una pizza sin receta no se ofrece.
- **Inventario por ingrediente:** cada ingrediente tiene su cantidad disponible y su unidad (`g` o `unidades`). Cada pizza tiene una receta con la cantidad de cada ingrediente.
- **Verificación:** se verifica al agregar cada pizza (considerando lo ya agregado al pedido) y nuevamente al registrar el pedido. Los ingredientes solo se descuentan cuando el pedido queda registrado.
- **Ingredientes faltantes:** se informa cada ingrediente que falta y cuánto, por ejemplo "Queso azul (faltan 30 g)", junto con los pasos para reabastecer.
- **Opciones al faltar ingredientes:** elegir otro producto o una cantidad menor, terminar el pedido con los productos ya agregados (solo si hay alguno) o cancelar el pedido.
- **Reabastecimiento:** desde la opción Inventario se elige un ingrediente y se ingresa la cantidad recibida, que debe ser un número entero mayor que 0.

### Precios, descuento y resumen

- **Descuentos:** 10% por cantidad en pedidos de más de 3 pizzas y 15% de promoción especial en las fechas y condiciones descritas en [Semana 3](#semana-3); ambos se suman. Las promociones se muestran junto al catálogo.
- **Impuestos:** los precios incluyen IVA (19%), que se desglosa en neto e IVA.
- **Resumen del pedido:** muestra cliente (nombre, teléfono, correo y dirección), productos (nombre, cantidad, precio unitario y subtotal), notas, subtotal, descuento si corresponde, total, neto, IVA, estado y repartidor.

### Repartidores y estados

- **Asignación:** se asigna el repartidor disponible más cercano a la pizzería, según una distancia simulada. Si no hay ninguno, el pedido queda sin repartidor y se informa.
- **Estados:** avanzan en orden: En preparación → Listo → En camino → Entregado.
  - Para pasar de Listo a En camino el pedido necesita repartidor; si no tiene, se intenta asignar uno en ese momento.
  - Al entregar el pedido, el repartidor vuelve a quedar disponible.
  - En la opción 3 se puede ingresar 0 para volver al menú sin cambios.
- **Notificación:** al registrar el pedido se simula el envío de una notificación al correo del cliente. Si el cliente no tiene correo o tiene el correo por defecto, se informa que no se envió.

### Datos

- **Solo en memoria:** pedidos, inventario y disponibilidad de repartidores se mantienen mientras la aplicación está abierta; al cerrarla, los cambios se pierden.

### Semana 3

- **Formato de teléfono:** se muestra como `+56 (9) XXXX-XXXX` usando Regex. Es una adaptación del formato `+XX (XXX) XXX-XXXX` a números chilenos de 9 dígitos.
- **Correo:** se valida con Regex y se vuelve a pedir mientras no sea válido (ya existía).
- **Cantidad:** cada producto y el total del pedido deben estar en el rango 1..100.
- **Promoción especial:** 15% entre el 03-11-2026 y el 17-11-2026, ambos incluidos. Se aplica al total del pedido si contiene al menos una pizza `@Promocionable` y se suma al 10% por más de 3 pizzas.
- **Anotación `@Promocionable`:** está en `PizzaClasica` (Pepperoni, Napolitana, Hawaiana) y no en `PizzaEspecial`. El tipo de cada pizza se indica en la columna `tipo` de `pizzas.csv`.
- **Categorías:** `Categoria` tiene PIZZA, BEBIDA e INGREDIENTE, aunque por ahora solo se venden pizzas.
- **Igualdad:** dos productos son iguales si tienen el mismo nombre y categoría; dos clientes, si tienen el mismo nombre y correo. En ambos casos no se distinguen mayúsculas.
- **Clientes sin duplicados:** `ClienteService` guarda los clientes en un `Set`; si se registra un cliente igual a uno existente, se reutiliza.
- **Operador `+`:** combina dos pedidos solo si son del mismo cliente, sin modificar los pedidos originales.
- **Reflection:** con `kotlin-reflect` se listan las propiedades (incluidas las heredadas) y los métodos (solo los declarados en la clase) de `Pedido`, `Producto` y `Cliente`.
- **Opción 5 "Resumen de funcionalidades":** solo muestra datos; no modifica pedidos, clientes ni inventario.
- **Archivo de resumen:** el "archivo de resumen en la consola" del enunciado se interpreta como un resumen mostrado en consola, según la pauta.

#### Requisitos de la semana 3

| Paso | Requisito | Archivo | Función |
|---|---|---|---|
| 1 | Correo con Regex y reingreso | `util/Validaciones.kt`, `ui/LectorConsola.kt` | `esCorreoValido`, `leerCorreo` |
| 1 | Formato de teléfono con Regex | `util/Validaciones.kt` | `formatearTelefono`, `normalizarTelefono` |
| 1 | Rango de fechas de la promoción | `model/CalculoTotales.kt` | `PERIODO_PROMOCION`, `estaEnPeriodoPromocion`, `calcularTotales` |
| 1 | Cantidad en el rango 1..100 con mensaje y reingreso | `util/Validaciones.kt`, `ui/MenuPizzasConsola.kt`, `ui/LectorConsola.kt` | `RANGO_CANTIDAD_PIZZAS`, `esCantidadValida`, `pedirCantidad`, `leerEnteroEnRango` |
| 2 | Anotación `@Promocionable` para identificar productos elegibles | `model/Promocionable.kt`, `model/CalculoTotales.kt` | `esPromocionable`, `calcularTotales` |
| 2 | Reflection de `Pedido`, `Producto` y `Cliente` | `util/AnalizadorClases.kt`, `ui/ResumenFuncionalidadesConsola.kt` | `propiedadesDe`, `metodosDe`, `mostrarAnalisisDeClases` |
| 3 | Operador `+` que combina pedidos y recalcula con descuentos | `model/Pedido.kt` | `plus` |
| 3 | `==` en `Producto` por nombre y categoría | `model/Producto.kt` | `equals`, `hashCode` |
| 4 | Desestructuración de `Cliente` (nombre, correo, teléfono) | `model/Cliente.kt`, `ui/ResumenFuncionalidadesConsola.kt` | `component1..3`, `mostrarCliente` |
| 4 | Desestructuración de `Pedido` (cliente, productos, total) | `model/Pedido.kt`, `ui/ResumenFuncionalidadesConsola.kt` | `component1..3`, `mostrarPedidosCombinados` |
| 5 | `equals` y `hashCode` de `Cliente` para no duplicar clientes | `model/Cliente.kt`, `service/ClienteService.kt`, `ui/RegistroClienteConsola.kt` | `equals`, `hashCode`, `registrar`, `buscar` |
| 5 | `equals` y `hashCode` de `Producto` para no duplicar productos | `data/ProductoRepositoryCsv.kt`, `ui/MenuPizzasConsola.kt` | `cargar`, `agregarItem` |
| 6 | Resumen en consola desde `Main.kt` | `Main.kt`, `ui/ResumenFuncionalidadesConsola.kt` | `main` (opción 5), `mostrar` |

### Ajustes por retroalimentación de la semana 2

- **Descuento de todo o nada:** `descontar` valida primero cada ingrediente con `require` (cantidad mayor que 0, el ingrediente existe en el inventario y el stock alcanza) y solo después modifica el inventario. Si alguna validación falla, no se modifica ningún ingrediente.
- **Una sola actualización por pedido:** `prepararLote` recibe los items del pedido, calcula las cantidades acumuladas con `calcularNecesarios` y las descuenta en una sola actualización. `PedidoService` la llama una vez por pedido, en vez de ítem por ítem.
- **Sin desbordamientos:** `calcularIngredientesLote` usa `Math.multiplyExact`, y `calcularNecesarios` y `reabastecer` usan `Math.addExact`. Si un resultado supera el máximo de Int se lanza `ArithmeticException` en vez de quedar un número negativo. Al reabastecer, la consola informa "La cantidad supera el máximo permitido para el inventario.", no modifica el stock y vuelve a pedir la cantidad.

## 3. Errores encontrados en pruebas y su resolución

- **Revisión tardía de ingredientes:** la falta se detectaba al final del pedido; ahora se verifica al agregar cada pizza.
- **Sin forma de cancelar o terminar el pedido:** al faltar ingredientes ahora se puede elegir otro producto, terminar con lo agregado o cancelar.
- **Repartidores que no se liberaban:** ahora el repartidor vuelve a quedar disponible al entregar el pedido.
- **Pedido sin repartidor detenido en Listo:** ahora se intenta asignar un repartidor al despacharlo.
- **Búsqueda de clientes precargados que confundía el flujo:** se eliminó; cada pedido registra al cliente.
- **Sin forma de salir de la opción 3:** ahora se puede ingresar 0 para volver al menú.

## 4. Limitaciones

- **Recetas por nombre exacto:** la receta de cada pizza se busca por su nombre exacto en `recetas.csv`.
- **Clientes sin correo:** dos clientes con el mismo nombre y sin correo se consideran el mismo cliente.
- **Fecha de la promoción:** la promoción depende de la fecha del sistema.
- **Cierre forzado de la entrada:** si la entrada de la consola se cierra (por ejemplo, al detener la ejecución con la entrada abierta), algunos menús quedan en un ciclo infinito porque la lectura devuelve el valor por defecto en cada vuelta. En uso normal no ocurre.
- **Recetas con cantidades excesivas:** si `recetas.csv` tuviera cantidades absurdamente grandes, la verificación de ingredientes lanza `ArithmeticException` y el programa se detiene, en vez de continuar con valores incorrectos.

## 5. Mejoras futuras

- **Registro persistente de clientes:** identificación única por teléfono, sin duplicados.
- **Estado Cancelado:** permitir cancelar pedidos ya registrados y devolver sus ingredientes al inventario.
- **Catálogo:** mostrar las unidades de cada ingrediente y mejorar el formato del catálogo.
- **Login y roles:** inicio de sesión con roles diferenciados para el personal.
- **Ubicación real de repartidores:** reemplazar la distancia simulada por la ubicación real de cada repartidor.
- **Persistencia de datos:** guardar pedidos, inventario y disponibilidad de repartidores entre ejecuciones.
- **Mensaje de despacho:** informar claramente cuando no se puede despachar porque no hay repartidores disponibles.
- **Aviso de promoción:** avisar al registrar el pedido cuando se aplica la promoción especial.
- **Otros productos:** vender bebidas e ingredientes adicionales.
- **Diagrama de clases del modelo:** documentar las clases del modelo y sus relaciones en un diagrama.
