# Apunte teórico - Programación III

Base conceptual para entender y defender el proyecto. Cada sección primero explica
la **teoría** y después muestra **dónde aparece en BiblioExpress**.

Índice:
- [0. La foto grande: qué es una API REST en capas](#0-la-foto-grande)
- [1. Unidad 1 — Programación Orientada a Objetos](#1-unidad-1--poo)
- [2. Unidad 2 — Arquitectura en capas y validaciones](#2-unidad-2--capas-y-validaciones)
- [3. Unidad 3 — Patrón de diseño Strategy](#3-unidad-3--patrón-strategy)
- [4. Unidad 4 — Estructuras de datos: HashMap y HashSet](#4-unidad-4--hashmap-y-hashset)
- [5. Preguntas trampa y cómo razonarlas](#5-preguntas-trampa)
- [6. Glosario rápido](#6-glosario)

---

## 0. La foto grande

### ¿Qué es una API REST?

Una **API** es una puerta de entrada a un programa: un conjunto de operaciones que
otro software puede pedirle. Una API **REST** es una API que se usa por **HTTP**
(el mismo protocolo que usa el navegador) y que organiza todo alrededor de
**recursos** (cosas: materiales, socios, préstamos).

Cada pedido HTTP tiene:

| Parte | Qué es | Ejemplo en BiblioExpress |
|---|---|---|
| **Método (verbo)** | qué tipo de acción | `GET` (leer), `POST` (crear/ejecutar) |
| **URL / ruta** | qué recurso | `/api/materiales/disponibles` |
| **Body** | datos que mando (en JSON) | `{ "socioId": 1, "codigoMaterial": "L-001" }` |
| **Respuesta** | lo que devuelve | JSON + un **código de estado** |

**Códigos de estado** (los que usás en el examen):

| Código | Significado | Cuándo |
|---|---|---|
| `200 OK` | salió bien | préstamo/devolución/consulta correcta |
| `400 Bad Request` | el cliente mandó algo mal formado | falta `codigoMaterial`, `socioId` negativo |
| `404 Not Found` | el recurso no existe | socio o material con id inexistente |
| `409 Conflict` | el pedido choca con el estado actual | prestar un material ya prestado |

**JSON** es un formato de texto para representar datos (`{ clave: valor }`). Spring
convierte solo el JSON que llega ↔ objetos Java (con una librería llamada Jackson),
y viceversa al responder.

### ¿Qué es Spring Boot?

Un **framework**: un armazón que ya trae resuelto lo repetitivo (el servidor web,
la conversión JSON, la inyección de dependencias) para que vos escribas solo la
lógica de tu problema. Vos ponés anotaciones (`@RestController`, `@Service`…) y
Spring "cablea" todo al arrancar.

### El flujo de un pedido, capa por capa

```
Cliente (Postman)
      │  POST /api/prestamos/prestar   { socioId, codigoMaterial }
      ▼
┌─────────────────┐
│   CONTROLLER    │  recibe el HTTP, valida el formato del JSON (@Valid), llama al service
└────────┬────────┘
         ▼
┌─────────────────┐
│    SERVICE      │  lógica de negocio: ¿existe el socio? ¿el material está disponible?
│                 │  calcula la multa, cambia estados
└────────┬────────┘
         ▼
┌─────────────────┐
│   REPOSITORY    │  guarda y busca datos (en este proyecto: HashMap en memoria)
└─────────────────┘
         ▲
         │  usa objetos del  MODEL  (Material, Socio, Prestamo)
         │  y devuelve  DTOs  hacia afuera (MaterialResponse, DevolverResponse)
```

**Regla de oro:** cada capa habla solo con la de abajo. El controller nunca toca el
repository directo; el repository no sabe nada de HTTP.

---

## 1. Unidad 1 — POO

### Conceptos base

- **Clase**: molde/plano. Ej: `Libro` describe *qué tiene* y *qué hace* un libro.
- **Objeto (instancia)**: algo concreto hecho con ese molde. Ej: `new Libro("L-001", "El Aleph", true, "Borges")`.
- **Atributo (campo)**: un dato que guarda el objeto (`titulo`, `autor`).
- **Método**: una acción que el objeto sabe hacer (`aplicarBeneficio(monto)`).

### Encapsulamiento

Los atributos van **`private`**: nadie los toca de afuera directamente. Se accede
con **getters** (`getTitulo()`) y, si hace falta, **setters** (`setDisponible(...)`).

**Por qué:** controlás cómo se lee y se modifica el estado. Si mañana querés validar
que `disponible` no cambie en ciertos casos, lo hacés en el setter y listo.

### Herencia

Una clase puede **heredar** (`extends`) de otra: recibe sus atributos y métodos y
puede agregar los suyos. Se lee como **"es-un"**:

- `Libro` **es-un** `Material` → `class Libro extends Material`
- `SocioPremium` **es-un** `Socio`

La clase de arriba es **superclase / clase padre**; la de abajo, **subclase / hija**.

- `super(...)` en el constructor de la hija llama al constructor del padre para
  inicializar la parte heredada.
- **Por qué usarla:** evitás repetir código. `codigo`, `titulo`, `disponible` se
  escriben **una sola vez** en `Material`, no en cada tipo de material.

### Clase abstracta

Una clase marcada **`abstract`**:

1. **No se puede instanciar** (`new Material(...)` da error de compilación).
2. Puede tener **métodos abstractos**: declarados sin cuerpo, que las hijas
   **están obligadas** a implementar.

```java
public abstract class Material {
    private String codigo, titulo;
    private boolean disponible;
    // ...
    public abstract String getTipo();   // cada hija DEBE definirlo
}
```

**Por qué `Material` es abstracta:** un "material" a secas no existe en el negocio;
siempre es un libro o una revista. La palabra `abstract` hace que el compilador
garantice esa regla.

### Interfaz

Una **interfaz** es un **contrato puro**: una lista de métodos que quien la
implementa debe tener, **sin código** (históricamente sin estado; puede tener
métodos `default`, pero no lo necesitás).

```java
public interface MultaStrategy {
    double calcular(int diasAtraso);
    String nombre();
}
```

Se implementa con `implements` (y una clase puede implementar **varias** interfaces,
mientras que solo puede `extends` de **una** clase).

### Clase abstracta vs interfaz (pregunta clásica)

| | Clase abstracta | Interfaz |
|---|---|---|
| ¿Tiene atributos/estado? | Sí | No (constantes nada más) |
| ¿Métodos con código? | Sí y no | Solo `default` (evitalo) |
| ¿Cuántas puede heredar/implementar una clase? | 1 sola clase abstracta | varias interfaces |
| ¿Cuándo la elijo? | Cuando las hijas **comparten estado y código** | Cuando solo comparten **la forma** de unos métodos |

En BiblioExpress: `Material` y `Socio` son **abstractas** (comparten atributos).
`MultaStrategy` es **interfaz** (las 3 multas no comparten estado, solo el método).

### Polimorfismo

"Muchas formas". Es poder tratar a objetos de distinto tipo **a través de un tipo
común**, y que cada uno responda **a su manera** al mismo llamado.

```java
Socio socio = socioRepository.buscarPorId(id);   // puede ser Regular o Premium
double aPagar = socio.aplicarBeneficio(multaBase); // NO pregunto de qué tipo es
```

Si `socio` es `SocioPremium`, devuelve la mitad; si es `SocioRegular`, devuelve todo.
El `if` lo resuelve el **mecanismo de polimorfismo** en tiempo de ejecución, no vos.

- **`@Override`**: anotación que marca que un método de la hija **reemplaza** al del
  padre/interfaz. Si te equivocás en la firma, el compilador te avisa.
- **Ventaja:** agregar `SocioEstudiante` no te obliga a tocar el service. Solo creás
  la clase con su `aplicarBeneficio`.

### Resumen de dónde está cada cosa

| Concepto | Archivo |
|---|---|
| Clase abstracta + método abstracto | `model/Material.java`, `model/Socio.java` |
| Herencia (`extends`, `super`) | `Libro`, `Revista`, `SocioRegular`, `SocioPremium` |
| Interfaz (`implements`) | `multas/MultaStrategy.java` + las 3 multas |
| Polimorfismo | `PrestamoService.devolver()` → `socio.aplicarBeneficio(...)` y `estrategiaActiva.calcular(...)` |
| Encapsulamiento | atributos `private` + getters en todo `model/` |

---

## 2. Unidad 2 — Capas y validaciones

### Por qué separar en capas

Cada capa tiene **una única responsabilidad**. Beneficios:

1. **Se entiende mejor**: sabés dónde buscar cada cosa.
2. **Se cambia sin romper**: cambiar el almacenamiento (HashMap → base de datos) toca
   solo el repository.
3. **Se testea por partes**.

### Qué hace cada capa

| Capa | Responsabilidad | En el código | NO hace |
|---|---|---|---|
| **Controller** | Traducir HTTP ↔ Java. Mapear URL, leer body, validar formato, elegir código de estado. | `@RestController`, `@PostMapping`, `@RequestBody`, `@Valid` | Lógica de negocio |
| **Service** | Lógica de negocio: reglas, cálculos, coordinar repos. | `@Service`, `PrestamoService`, `SocioService` | Saber de HTTP |
| **Repository** | Guardar y buscar datos. | `@Repository`, los 3 `...Repository` con `HashMap` | Reglas de negocio |
| **Model** | Representar el dominio (las "cosas"). | `model/` con la herencia | Nada de framework |
| **DTO** | Objetos que viajan en el JSON de entrada/salida. | `dto/` | Lógica |

### DTO (Data Transfer Object)

Un objeto **plano** que solo transporta datos entre el cliente y tu API.

**Por qué no devolver directamente los objetos del `model`:**

1. **Desacoplar**: si cambio un atributo interno de `Libro`, no quiero que se rompa
   el JSON que esperan los clientes.
2. **Formato a medida**: `MaterialResponse` pone `tipo`, `autor` y `numeroEdicion`
   al mismo nivel, aunque en el modelo estén repartidos en clases distintas.
3. **Seguridad**: no expongo campos internos sin querer.

Entrada: `PrestarRequest`, `DevolverRequest`, `DepurarDuplicadosRequest`.
Salida: `MaterialResponse`, `DevolverResponse`, etc.

### Inyección de dependencias (DI)

En vez de que una clase haga `new` de lo que necesita, lo **pide en el constructor**
y Spring se lo pasa ya creado:

```java
@Service
public class PrestamoService {
    private final MaterialRepository materialRepository;
    public PrestamoService(MaterialRepository materialRepository, /* ... */) {
        this.materialRepository = materialRepository;
    }
}
```

- Spring crea **una sola instancia** (un **bean**) de cada `@Service`, `@Repository`,
  `@RestController`, `@Component` y las reparte donde se piden.
- **Por qué:** las clases quedan desacopladas (no dependen de *cómo* se construye lo
  que usan) y es fácil reemplazar piezas.
- Por eso los datos "persisten" mientras corre la app: el `HashMap` vive dentro del
  bean único del repository.

### Validaciones: dos niveles

**1. Validación de formato (en el DTO, con anotaciones):**

```java
public record PrestarRequest(
    @Positive int socioId,
    @NotBlank String codigoMaterial
) {}
```

El `@Valid` en el controller la dispara. Si falla → Spring corta y responde **400**
*antes* de entrar al service. Anotaciones típicas: `@NotNull`, `@NotBlank`,
`@Positive`, `@PositiveOrZero`, `@Min`, `@Max`, `@Size`.

**2. Validación de negocio (en el service, con código):**

- "El socio existe" → si no, `RecursoNoEncontradoException` → **404**
- "El material está disponible" → si no, `ReglaNegocioException` → **409**
- "El préstamo lo tiene ese socio" → si no, **409**

Se hacen con **guard clauses**: `if (condición mala) throw ...;` al principio del
método, así el "camino feliz" queda limpio abajo.

### Validación *iterativa*

Recorrer una colección validando **elemento por elemento** y acumulando resultados.
En `SocioService.depurarDuplicados()`:

```java
for (String dniOriginal : entrada) {
    String dni = dniOriginal == null ? "" : dniOriginal.trim();
    if (!esDniValido(dni)) { invalidos.add(dniOriginal); continue; }
    if (vistos.add(dni)) unicos.add(dni); else duplicados++;
}
```

Recorre una vez, valida cada DNI (formato: solo dígitos, 7–8 caracteres), y separa
válidos únicos / duplicados / inválidos.

### Manejo centralizado de errores

`ManejadorGlobalDeErrores` está anotado con **`@RestControllerAdvice`**: intercepta
las excepciones que tiran los controllers/services y arma **un JSON de error
uniforme** con el código HTTP correcto.

**Por qué:** el service solo tira `throw new ReglaNegocioException("...")` y se
olvida. No repite código de armado de respuestas en cada método.

---

## 3. Unidad 3 — Patrón Strategy

### ¿Qué es un patrón de diseño?

Una **solución conocida y probada** a un problema que aparece seguido al diseñar
software. No es código para copiar y pegar: es una *forma de organizar* las clases.

### El problema que resuelve Strategy

Tenés **una tarea** (calcular una multa) que se puede hacer de **varias maneras**
(NORMAL $100/día, CAMPAÑA $60/día, FIN_DE_SEMANA $100/día + $200), y querés:

- poder **cambiar de manera** en tiempo de ejecución,
- **agregar maneras nuevas** sin tocar el código que la usa.

La solución mala sería un `switch (tipoMulta)` gigante repartido por todos lados.

### La estructura de Strategy

| Rol | Qué es | En BiblioExpress |
|---|---|---|
| **Strategy** (interfaz) | El contrato común | `MultaStrategy` (`calcular`, `nombre`) |
| **Estrategias concretas** | Cada forma de hacerlo | `MultaNormal`, `MultaCampania`, `MultaFinDeSemana` |
| **Contexto** | Quien usa una estrategia sin saber cuál | `PrestamoService` (guarda `estrategiaActiva`) |

```java
// El contexto tiene una referencia al TIPO de la interfaz:
private MultaStrategy estrategiaActiva;

// y la usa sin preguntar cuál es:
double multaBase = estrategiaActiva.calcular(diasAtraso);

// cambiar de estrategia = cambiar esa referencia:
this.estrategiaActiva = estrategiasPorNombre.get("CAMPANIA");
```

### Cómo se cargan las estrategias (detalle Spring)

En el constructor del service pido `List<MultaStrategy>`. Spring encuentra las 3
clases `@Component` que implementan la interfaz y me pasa la lista. Yo las guardo en
un `Map<String, MultaStrategy>` indexadas por `nombre()`.

### Ventaja: Principio Abierto/Cerrado (OCP)

> El código debe estar **abierto a extensión** pero **cerrado a modificación**.

Si mañana piden `MultaVerano`: creás **una clase nueva** que implementa
`MultaStrategy` y ya. **No tocás** `PrestamoService` (no hay `switch` que editar y
arriesgarse a romper). Menos riesgo, menos bugs.

### Otros patrones que podés nombrar (por si preguntan)

- **Repository**: aislar el acceso a datos detrás de una interfaz/clase (lo usás).
- **DTO**: objeto de transporte (lo usás).
- **Singleton**: una sola instancia de una clase — Spring maneja sus beans así.
- **Factory**: un método que fabrica objetos (tu `MaterialResponse.desde(m)` es un
  mini-factory).

---

## 4. Unidad 4 — HashMap y HashSet

### ¿Qué es una estructura de datos?

Una forma de **organizar datos en memoria** para poder guardarlos y buscarlos de
manera eficiente. Distintas estructuras sirven para distintas necesidades.

### Las tres que tenés que distinguir

| Estructura | Guarda | Permite duplicados | Buscar un elemento | Uso típico |
|---|---|---|---|---|
| **List / ArrayList** | elementos en orden, por posición | sí | lento: O(n) (recorre) | cuando el orden importa |
| **Map / HashMap** | pares **clave → valor** | claves no; valores sí | rápido: O(1) por clave | buscar por un id |
| **Set / HashSet** | elementos sueltos, sin repetir | **no** | rápido: O(1) | saber si algo "ya está" |

### HashMap

Guarda **clave → valor**. En BiblioExpress:

```java
Map<String, Material> materialesPorCodigo = new HashMap<>();
materialesPorCodigo.put("L-001", libro);          // guardar
Material m = materialesPorCodigo.get("L-001");     // buscar (instantáneo)
```

**Cómo hace para ser tan rápido (idea simple):** cuando guardás, calcula un número
a partir de la clave (el **hash**) y lo usa como "dirección" para ubicar el valor.
Al buscar, recalcula ese número y va **directo** a esa dirección, sin recorrer nada.

**Por qué acá:** siempre busco por código de material / id de socio, que son
**únicos**. La clave del mapa es exactamente ese identificador.

- `MaterialRepository`: `HashMap<String, Material>` (clave = código)
- `SocioRepository`: `HashMap<Integer, Socio>` (clave = id)
- `PrestamoRepository`: `HashMap<String, Prestamo>` (clave = código de material)

### HashSet

Un conjunto **sin elementos repetidos**. El truco clave:

```java
Set<String> vistos = new HashSet<>();
vistos.add("12345678");  // devuelve true  (no estaba, lo agrega)
vistos.add("12345678");  // devuelve false (ya estaba, no hace nada)
```

`add(x)` devuelve un `boolean`: **true si era nuevo, false si ya estaba**. Con eso,
en **una sola pasada** por la lista, distinguís primeras apariciones de duplicados
(ver el código en `SocioService.depurarDuplicados()`).

### Complejidad (Big O) — explicado fácil

Es una forma de decir "cómo crece el tiempo cuando crecen los datos":

- **O(1)** — constante: tarda lo mismo con 10 o con 10.000 elementos (buscar en HashMap/HashSet).
- **O(n)** — lineal: el doble de datos, el doble de tiempo (recorrer una lista).
- **O(n²)** — cuadrático: el doble de datos, **4 veces** el tiempo. Se dispara.

**Por qué HashSet para deduplicar y no una lista:** con una lista, por cada DNI
tendría que recorrer toda la lista de "ya vistos" para ver si está → O(n) por
elemento → O(n²) en total. Con HashSet, preguntar "¿ya está?" es O(1) → O(n) total.

### "¿Los datos se pierden al reiniciar?"

Sí. Todo vive en `HashMap`s **en memoria RAM**. Al frenar la app se borra. El
enunciado lo pide así ("sin JPA ni base de datos"). Los datos de prueba se
recargan al arrancar con `DatosIniciales` (un `CommandLineRunner`, que Spring
ejecuta una vez al terminar de levantar).

### "¿HashMap/HashSet mantienen el orden?"

**No.** Si necesitás orden de inserción: `LinkedHashMap` / `LinkedHashSet`. Por eso,
para la lista final de DNIs únicos, además del `HashSet` (para chequear) voy llenando
un `ArrayList` aparte en el orden en que aparecen.

---

## 5. Preguntas trampa

Cómo **razonarlas** aunque no sepas la respuesta de memoria:

**"¿Qué pasa si dos hilos prestan el mismo material a la vez?"**
→ El `HashMap` no es thread-safe y no hay sincronización, así que podría haber una
condición de carrera. Para este examen (en memoria, un usuario) no es un problema;
en producción se resolvería con bloqueo o estructuras concurrentes.

**"¿Por qué `record` y no `class` para los DTO?"**
→ Un `record` es una clase inmutable con getters, `equals`, `hashCode` y constructor
generados automáticamente. Ideal para objetos que solo transportan datos y no
cambian.

**"¿Qué diferencia hay entre `@Component`, `@Service` y `@Repository`?"**
→ Funcionalmente casi lo mismo: los tres crean un bean. Cambian la **intención** que
comunican (`@Service` = lógica, `@Repository` = datos) y `@Repository` además
traduce algunas excepciones de acceso a datos.

**"¿Por qué el atributo es `final`?"**
→ Porque se asigna una sola vez en el constructor y no debe cambiar después. Deja
claro que esa dependencia/valor es fijo.

**"Si `Material` es abstracta, ¿cómo la guardás en el `HashMap<String, Material>`?"**
→ El mapa guarda **referencias de tipo `Material`**, pero los objetos reales son
`Libro` o `Revista`. Eso es polimorfismo: la variable es del tipo padre, el objeto
es de la hija.

**"¿Dónde validás y por qué ahí?"**
→ Formato en el DTO (para cortar rápido, antes de gastar lógica). Reglas de negocio
en el service (porque necesitan consultar el estado: repos, disponibilidad).

**"¿Qué es inversión de control?"**
→ En vez de que tu código controle la creación de sus dependencias, ese control lo
tiene el framework (Spring), que te las inyecta. La DI es la forma de lograrlo.

**Si te preguntan algo que no sabés:** decilo con honestidad y razoná en voz alta.
"No lo implementé, pero lo pensaría así…". Muestra criterio, que es lo que evalúan.

---

## 6. Glosario

| Término | Definición corta |
|---|---|
| **API REST** | Interfaz por HTTP organizada en recursos |
| **Endpoint** | Una ruta concreta de la API (`POST /api/prestamos/prestar`) |
| **Framework** | Armazón que resuelve lo repetitivo (Spring Boot) |
| **Bean** | Objeto que Spring crea y administra |
| **DI / Inyección de dependencias** | Recibir las dependencias ya creadas (por constructor) |
| **IoC / Inversión de control** | El framework controla el ciclo de vida de los objetos |
| **Clase** | Molde para crear objetos |
| **Objeto / instancia** | Algo concreto creado con una clase |
| **Herencia** | Una clase reutiliza atributos/métodos de otra (`extends`) |
| **Clase abstracta** | No se instancia; puede obligar a implementar métodos |
| **Interfaz** | Contrato de métodos sin implementación (`implements`) |
| **Polimorfismo** | Mismo llamado, distinta respuesta según el objeto real |
| **Encapsulamiento** | Ocultar el estado (`private`) y exponerlo con métodos |
| **`@Override`** | Marca que un método reemplaza al del padre/interfaz |
| **DTO** | Objeto plano para transportar datos entrada/salida |
| **Capa** | Grupo de clases con una responsabilidad (controller/service/…) |
| **Guard clause** | `if` al inicio que corta con excepción si algo está mal |
| **Patrón de diseño** | Solución estándar a un problema de diseño recurrente |
| **Strategy** | Patrón: familia de algoritmos intercambiables tras una interfaz |
| **OCP (Abierto/Cerrado)** | Extensible sin modificar lo existente |
| **HashMap** | Estructura clave→valor con búsqueda O(1) |
| **HashSet** | Conjunto sin duplicados con chequeo O(1) |
| **O(1) / O(n) / O(n²)** | Cómo escala el tiempo según la cantidad de datos |
| **CommandLineRunner** | Código que Spring corre una vez al arrancar |
| **JSON** | Formato de texto para datos: `{ "clave": valor }` |
| **Jackson** | Librería que convierte JSON ↔ objetos Java |
