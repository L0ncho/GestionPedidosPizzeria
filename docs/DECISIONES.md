# Decisiones de diseño — GestionPedidosPizzeria

## 1. Descripción

Aplicación de consola de uso interno para el personal de una pizzería. Permite registrar pedidos, revisar el reporte del día, avanzar el estado de cada pedido y reabastecer el inventario de ingredientes. Por ahora no incluye inicio de sesión.

## 2. Decisiones vigentes

### Menú y pedidos del día

- **Menú principal:** 1. Registrar pedido, 2. Reporte del día, 3. Cambiar estado de un pedido, 4. Inventario, 5. Salir. El menú se repite hasta elegir Salir.
- **Pedidos del día:** se guardan en un arreglo con capacidad para 100 pedidos. Al llegar al máximo, se informa que no se pueden registrar más pedidos.
- **Identificador:** el N° de pedido es autoincremental y parte en 1.
- **Reporte del día:** lista cada pedido con su N°, cliente, pizzas, total y estado, y luego los agrupa en completados (Entregado) y pendientes.

### Registro del pedido

- **Cliente:** cada pedido registra al cliente con sus datos validados.
- **Validaciones:** cada dato se vuelve a pedir mientras no sea válido.
  - Nombre: solo letras (incluidas tildes, ñ y ü), espacios, `'` y `-`; mínimo 2 caracteres y al menos una letra.
  - Dirección: mínimo 5 caracteres y al menos una letra.
  - Teléfono: formato chileno de 9 dígitos, con o sin prefijo `+56` o `56`; se guarda como `+56XXXXXXXXX`. Tras 3 intentos inválidos se registra `+56 9 0000 0000`.
  - Correo: opcional (Enter para omitir) y con formato `usuario@dominio.ext`. Tras 3 intentos inválidos se registra `sin.correo@example.com`.
- **Productos:** un pedido puede tener varios productos, cada uno con su cantidad. Si se elige un producto ya agregado, se suma la cantidad. El total no puede superar 20 pizzas.
- **Notas:** son opcionales; si no se ingresan, se muestra "Sin notas".

### Inventario y verificación de ingredientes

- **Datos de origen:** el catálogo, las recetas, el inventario y los repartidores se cargan desde archivos CSV (separador `;`, UTF-8, con encabezado). Las filas inválidas se informan como errores de carga y se omiten; una pizza sin receta no se ofrece.
- **Inventario por ingrediente:** cada ingrediente tiene su cantidad disponible y su unidad (`g` o `unidades`). Cada pizza tiene una receta con la cantidad de cada ingrediente.
- **Verificación:** se verifica al agregar cada pizza (considerando lo ya agregado al pedido) y nuevamente al registrar el pedido. Los ingredientes solo se descuentan cuando el pedido queda registrado.
- **Ingredientes faltantes:** se informa cada ingrediente que falta y cuánto, por ejemplo "Queso azul (faltan 30 g)", junto con los pasos para reabastecer.
- **Opciones al faltar ingredientes:** elegir otro producto o una cantidad menor, terminar el pedido con los productos ya agregados (solo si hay alguno) o cancelar el pedido.
- **Reabastecimiento:** desde la opción Inventario se elige un ingrediente y se ingresa la cantidad recibida, que debe ser un número entero mayor que 0.

### Precios, descuento y resumen

- **Descuento:** 10% en pedidos de más de 3 pizzas. La promoción se muestra junto al catálogo.
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

## 3. Errores encontrados en pruebas y su resolución

- **Revisión tardía de ingredientes:** la falta se detectaba al final del pedido; ahora se verifica al agregar cada pizza.
- **Sin forma de cancelar o terminar el pedido:** al faltar ingredientes ahora se puede elegir otro producto, terminar con lo agregado o cancelar.
- **Repartidores que no se liberaban:** ahora el repartidor vuelve a quedar disponible al entregar el pedido.
- **Pedido sin repartidor detenido en Listo:** ahora se intenta asignar un repartidor al despacharlo.
- **Búsqueda de clientes precargados que confundía el flujo:** se eliminó; cada pedido registra al cliente.
- **Sin forma de salir de la opción 3:** ahora se puede ingresar 0 para volver al menú.

## 4. Mejoras futuras

- **Registro persistente de clientes:** identificación única por teléfono, sin duplicados.
- **Estado Cancelado:** permitir cancelar pedidos ya registrados y devolver sus ingredientes al inventario.
- **Catálogo:** mostrar las unidades de cada ingrediente y mejorar el formato del catálogo.
- **Login y roles:** inicio de sesión con roles diferenciados para el personal.
- **Ubicación real de repartidores:** reemplazar la distancia simulada por la ubicación real de cada repartidor.
- **Persistencia de datos:** guardar pedidos, inventario y disponibilidad de repartidores entre ejecuciones.
