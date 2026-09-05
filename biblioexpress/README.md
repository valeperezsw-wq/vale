# BiblioExpress

API REST de una biblioteca barrial para prestar y devolver materiales.
Proyecto de práctica para el **examen final de Programación III**.

- **Tecnologías:** Java 21, Spring Boot 3.3, Maven
- **Persistencia:** en memoria (HashMap), sin base de datos ni JPA

---

## Cómo ejecutar

Desde la carpeta `biblioexpress/`:

```bash
mvn spring-boot:run
```

La API queda en `http://localhost:8080`. Al arrancar carga datos de prueba
(5 materiales, 3 socios). Para frenarla: `Ctrl + C` en la terminal.

---

## Arquitectura por capas (Unidad 2)

```
controller/   -> recibe HTTP, valida el JSON, delega. Sin lógica.
service/      -> lógica de negocio: reglas, cálculo de multas, coordinación.
repository/   -> almacenamiento en memoria (HashMap).
model/        -> clases del dominio (Material, Socio, Prestamo). Con herencia.
dto/          -> objetos que viajan en el JSON. El modelo NO se expone directo.
multas/       -> patrón Strategy para calcular multas.
exception/    -> excepciones propias + manejador global de errores.
config/       -> carga de datos iniciales.
```

## Dónde está cada concepto evaluado

| Unidad | Concepto | Archivos |
|---|---|---|
| 1 | Herencia | `model/Material.java` → `Libro`, `Revista` · `model/Socio.java` → `SocioRegular`, `SocioPremium` |
| 1 | Interfaz | `multas/MultaStrategy.java` |
| 2 | Capas | paquetes `controller` / `service` / `repository` / `model` / `dto` |
| 2 | Validación iterativa | `service/SocioService.java` (recorre y valida cada DNI) |
| 3 | Strategy | `multas/` + `PrestamoService` elige la estrategia activa |
| 4 | HashMap | `repository/MaterialRepository.java`, `SocioRepository`, `PrestamoRepository` |
| 4 | HashSet | `service/SocioService.java` (deduplicación en una pasada) |

---

## Endpoints

### POST `/api/prestamos/prestar`
```json
{ "socioId": 1, "codigoMaterial": "L-001" }
```
Marca el material como no disponible y registra el préstamo.
Falla con **409** si ya está prestado, **404** si el socio o el material no existen.

### POST `/api/prestamos/devolver`
```json
{ "socioId": 1, "codigoMaterial": "L-001", "diasAtraso": 5 }
```
Calcula la multa con la **estrategia activa** y le aplica el **beneficio del socio**
(Premium paga 50%). Devuelve el desglose y libera el material.

### GET `/api/materiales/disponibles`
Lista los materiales con `disponible = true`.

### POST `/api/socios/depurar-duplicados`
```json
{ "dnis": ["12345678", "12345678", "87654321", "abc"] }
```
Devuelve los DNIs únicos (una sola pasada, con HashSet) e informa cuáles eran inválidos.

### Extra — cambiar la estrategia de multa
```
POST /api/prestamos/estrategia?tipo=CAMPANIA     (NORMAL | CAMPANIA | FIN_DE_SEMANA)
GET  /api/prestamos/estrategia
```

## Estrategias de multa

| Estrategia | Cálculo |
|---|---|
| `NORMAL` | $100 × día de atraso |
| `CAMPANIA` | $60 × día de atraso |
| `FIN_DE_SEMANA` | $100 × día + $200 fijo |

## Datos de prueba cargados al inicio

Materiales: `L-001` El Aleph, `L-002` Rayuela, `L-003` Ficciones, `R-001` El Gráfico, `R-002` Nature
Socios: `1` Vale Perez (Regular), `2` Joaquin Perez (Premium), `3` Ana Gomez (Regular)

---

## Postman

Importar `BiblioExpress.postman_collection.json` en Postman. Trae las 9 pruebas listas.
Ver `EVIDENCIAS.md` para las capturas.
