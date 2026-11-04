package cl.duoc.pmy2201.pizzeria.model

import kotlin.reflect.full.hasAnnotation

/**
 * Identifica las pizzas que califican para promociones especiales.
 * Se aplica sobre clases y se conserva en tiempo de ejecución, por lo que puede consultarse por reflexión.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Promocionable

/**
 * Indica, mediante reflection de Kotlin, si la clase de [producto] está marcada con [Promocionable].
 */
fun esPromocionable(producto: Producto): Boolean = producto::class.hasAnnotation<Promocionable>()
