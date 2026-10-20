# Decisiones de diseño — GestionPedidosPizzeria

## 1. Alcance del pedido

Se decidió que cada ejecución registre un único pedido, compuesto por un tipo de pizza y su cantidad.

## 2. Fuente de datos

El catálogo de pizzas y la lista de repartidores se cargan desde archivos CSV ubicados en `resources`, con separador `;`, codificación UTF-8 y fila de encabezado. Las filas inválidas se informan como errores de carga y se omiten.

## 3. Stock de ingredientes

- `ingredientesDisponibles` representa la cantidad de pizzas que se pueden preparar durante el turno.
- Cada pizza preparada consume 1 porción.
- Se maneja un solo tamaño: Familiar.

## 4. Descuento

- Se aplica un descuento de 10% cuando `cantidadPizzas` es mayor que 3.
- Bajo el título del catálogo se muestra la promoción vigente.
- Tras confirmar el stock, si corresponde, se informa que el descuento fue aplicado.
- El porcentaje y el umbral se toman de las constantes de `CalculadoraPedido`, por lo que los mensajes se mantienen consistentes con el cálculo.

## 5. Identificador del pedido

`idPedido` es autoincremental y parte en 1.

## 6. Asignación de repartidor

- Se asigna el primer repartidor disponible y se marca como no disponible.
- Si no hay ninguno, se informa: "No hay repartidores disponibles en este momento. Tu pedido queda en preparación y será asignado cuando un repartidor se libere."
- En el resumen, el campo repartidor muestra "Sin repartidor disponible".

## 7. Falta de stock

- Si el stock no alcanza, se informan la cantidad solicitada y la disponible, y se indica que el pedido no puede completarse con la cantidad solicitada.
- Se implementó un reintento: se pregunta "¿Desea modificar el pedido? (s/n)".
  - Con `s`, se vuelve a elegir pizza y cantidad.
  - Con `n`, se muestra "Pedido cancelado." y el programa termina sin resumen.
- Solo se aceptan `s` o `n`. Si la entrada termina, se asume `n`.
- Un pedido rechazado no modifica el stock.

## 8. Validación del teléfono

El teléfono debe tener formato chileno: 9 dígitos, con o sin prefijo `+56` o `56`, y se normaliza como `+56XXXXXXXXX`. Después de 3 intentos inválidos se registra como "No informado".

## 9. Organización de la consola y resumen

- Los mensajes de consola se implementaron en la capa `ui` (`MensajesPedidoConsola`, `RegistroClienteConsola`, `MenuPizzasConsola`).
- `Main.kt` solo orquesta el flujo y genera el resumen.
- El resumen se mantiene en `Main.kt` por exigencia del Paso 6, como única excepción a la regla de capas.
- El resumen muestra el tamaño Familiar en la sección de la pizza.

## 10. Lectura de consola

Se decidió usar `readlnOrNull()`, equivalente actual de `readLine()` (deprecada en la versión de Kotlin del proyecto).

## 11. Validación de nombre y dirección

- Nombre: solo letras (incluidas tildes, ñ y ü), espacios, `'` y `-`; mínimo 2 caracteres y al menos una letra.
- Dirección: mínimo 5 caracteres y al menos una letra.
- Si el dato no cumple, se muestra el motivo y se vuelve a pedir.

## 12. Cantidad por pedido

Entre 1 y 20 pizzas (`MAXIMO_PIZZAS_POR_PEDIDO`). No se limita al stock, para que la verificación de ingredientes del Paso 4 se aplique siempre.

## 13. Numeración del enunciado

Las instrucciones pasan del Paso 4 al Paso 6. Se interpretó como un error de numeración. La asignación de repartidor se implementó porque el Paso 6 la exige en el resumen.

## Mejoras futuras (posibles)

- **Tamaños de pizza:** incorporar tamaños distintos al Familiar, con su precio y consumo de ingredientes.
- **Fichas técnicas por ingrediente:** reemplazar el stock por pizza por un inventario de ingredientes y una receta por pizza.
- **Varios sabores por pedido:** agregar un detalle de pedido con una línea por pizza y su cantidad.
- **Varios pedidos por ejecución:** permitir registrar más de un pedido sin reiniciar el programa.
- **Persistencia de datos:** guardar pedidos, stock y disponibilidad de repartidores entre ejecuciones.

