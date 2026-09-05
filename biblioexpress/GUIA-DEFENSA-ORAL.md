# Guía de defensa oral - BiblioExpress

Preguntas típicas que hace el profe y cómo responderlas. Leé esto varias veces
hasta poder decirlo con tus palabras. **No memorices: entendé el porqué.**

---

## Preguntas generales de arquitectura

**¿Por qué separaste en capas controller / service / repository?**
Para que cada parte tenga una sola responsabilidad:
- el **controller** solo entiende de HTTP (URLs, códigos de estado, leer el JSON),
- el **service** tiene la lógica de negocio (las reglas: "no se presta algo ya prestado"),
- el **repository** solo guarda y busca datos.
Si mañana cambio de HashMap a una base de datos, solo toco el repository. El resto no se entera.

**¿Qué es un DTO y por qué no devolvés directamente las clases del modelo?**
DTO = Data Transfer Object, un objeto que solo transporta datos hacia/desde el cliente.
No expongo el modelo directo porque:
1. No quiero atar el formato del JSON a cómo están hechas mis clases internas.
2. Puedo dar un formato plano y parejo (ej: `MaterialResponse` pone `tipo`, `autor` y
   `numeroEdicion` al mismo nivel, aunque en el modelo estén en clases distintas).
3. Seguridad: no filtro campos internos sin querer.

**¿Cómo se conectan las clases entre sí? ¿Vos hacés `new`?**
Casi nunca. Spring usa **inyección de dependencias**: yo pido lo que necesito en el
constructor (ej: `PrestamoService(MaterialRepository repo, ...)`) y Spring me pasa la
instancia ya creada. Las clases marcadas con `@RestController`, `@Service`,
`@Repository`, `@Component` las administra Spring.

---

## Unidad 1 - Herencia e interfaces

**¿Dónde usás herencia?**
Dos jerarquías:
- `Material` (abstracta) → `Libro` (agrega `autor`) y `Revista` (agrega `numeroEdicion`).
- `Socio` (abstracta) → `SocioRegular` y `SocioPremium`.
Puse en la clase padre lo común (código, título, disponible) y cada hija agrega lo suyo.

**¿Por qué `Material` es abstracta?**
Porque un "material a secas" no existe en el negocio: siempre es un libro o una revista.
`abstract` impide hacer `new Material(...)` y me obliga a que cada hija implemente `getTipo()`.

**¿Qué es el polimorfismo y dónde lo usás?**
Es que un mismo llamado se comporta distinto según el objeto real.
En `PrestamoService.devolver()` hago `socio.aplicarBeneficio(multaBase)` **sin preguntar
qué tipo de socio es**. Si es `SocioPremium` devuelve la mitad; si es `SocioRegular`
devuelve el monto completo. El `if` lo resuelve el polimorfismo, no yo.

**¿Dónde usás una interfaz y qué diferencia tiene con una clase abstracta?**
`MultaStrategy` es una interfaz: solo define el contrato (`calcular`, `nombre`), sin
código. Una clase abstracta puede tener atributos y métodos ya implementados.
Usé interfaz para las multas porque las tres estrategias no comparten nada de estado,
solo la "forma" del método.

---

## Unidad 2 - Capas y validaciones iterativas

**¿Dónde está la validación iterativa?**
En `SocioService.depurarDuplicados()`. Recorro la lista de DNIs con un `for` y, en cada
vuelta, valido el formato (`esDniValido`: solo dígitos, 7 u 8 caracteres). Los que no
pasan van a una lista `dnisInvalidos`. Es iterativa porque valido elemento por elemento
mientras recorro, acumulando resultados.

**¿Y las validaciones de los otros endpoints?**
Dos niveles:
1. **Formato** (en el DTO): anotaciones `@NotBlank`, `@Positive`. Si fallan, Spring
   responde 400 antes de entrar al service. Lo dispara `@Valid` en el controller.
2. **Negocio** (en el service): "el material existe", "está disponible", "el préstamo
   es de ese socio". Si fallan, tiro una excepción propia.

**¿Cómo manejás los errores?**
Con excepciones propias (`RecursoNoEncontradoException` → 404,
`ReglaNegocioException` → 409) y un `@RestControllerAdvice`
(`ManejadorGlobalDeErrores`) que las atrapa y arma un JSON de error uniforme.
Así el service solo tira la excepción y no se ocupa de armar la respuesta.

---

## Unidad 3 - Patrón Strategy

**Explicá el patrón Strategy con tu código.**
Tengo un problema (calcular una multa) con varias soluciones intercambiables
(NORMAL $100/día, CAMPANIA $60/día, FIN_DE_SEMANA $100/día + $200).
Cada solución es una clase que implementa `MultaStrategy`. El `PrestamoService`
guarda una referencia `estrategiaActiva` de tipo `MultaStrategy` y llama
`estrategiaActiva.calcular(dias)` **sin saber cuál es**.
Cambiar de estrategia es cambiar esa referencia (`cambiarEstrategia("CAMPANIA")`).

**¿Qué ventaja tiene sobre un `if/switch` con el tipo de multa?**
Principio Abierto/Cerrado: si agregan `MultaVerano`, creo una clase nueva y listo.
No toco el service ni el `switch` (que habría que buscar y modificar, arriesgando romper algo).

**¿Cómo sabe el service cuáles estrategias existen?**
En el constructor pido `List<MultaStrategy>`. Spring detecta las tres clases
`@Component` que implementan la interfaz y me pasa la lista. Yo las indexo por nombre
en un `Map` (`estrategiasPorNombre`).

---

## Unidad 4 - HashMap y HashSet

**¿Por qué guardás los materiales en un HashMap y no en una lista?**
Porque busco siempre por código, que es único. En un `HashMap<String, Material>` la
búsqueda por clave es O(1) (instantánea). En una lista tendría que recorrerla entera,
O(n).

**¿Por qué HashSet para depurar duplicados?**
`HashSet` no admite repetidos y `add(x)` devuelve `true` si el elemento es nuevo o
`false` si ya estaba. Con eso, en **una sola pasada** por la lista, sé si cada DNI ya
lo vi. Preguntar "¿ya está?" en un HashSet es O(1); en una lista sería O(n) por
elemento → O(n²) total.

**¿Qué pasa con el orden en un HashMap / HashSet?**
No garantizan orden. Por eso, para la lista final de DNIs únicos, además del HashSet
(que uso para chequear) voy llenando un `ArrayList` en el orden de aparición.

**¿Los datos se pierden al reiniciar?**
Sí, todo está en memoria (los HashMap viven mientras corre la app). El enunciado pide
exactamente eso: sin base de datos. Los datos de prueba se recargan al arrancar con
`DatosIniciales` (un `CommandLineRunner`).

---

## Si te preguntan algo que no sabés

Decí la verdad con criterio: "Eso lo resolví con ayuda de la IA, pero lo que hace es
_[explicás con tus palabras qué hace ese pedazo]_". Lo que evalúan es que **entiendas
tu código**, no que lo hayas tipeado de memoria.

## Puntos flojos conocidos (por si los marca)

- `FIN_DE_SEMANA`: interpreté "$200 al total" como recargo fijo que se suma. Si el
  enunciado quería otra cosa, es una línea en `MultaFinDeSemana.calcular()`.
- `diasAtraso` llega en el request en vez de calcularse con fechas. Simplificación
  consciente para no depender del reloj en la demo.
- No hay endpoint para crear socios/materiales: se cargan al inicio. Se podría agregar.
