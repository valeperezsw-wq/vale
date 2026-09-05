# EVIDENCIAS - BiblioExpress

> Completar con capturas de Postman. Para sacar la captura: en Postman, botón derecho
> sobre el panel de respuesta → o simplemente `Win + Shift + S` y recortar.
> Guardar las imágenes en la carpeta `evidencias/` y enlazarlas acá.

---

## 1. GET /api/materiales/disponibles

Respuesta esperada: lista con los 5 materiales, todos `disponible: true`.

![disponibles](evidencias/01-disponibles.png)

---

## 2. POST /api/prestamos/prestar

Body: `{ "socioId": 1, "codigoMaterial": "L-001" }`
Respuesta esperada: **200**, mensaje "Prestamo registrado correctamente."

![prestar](evidencias/02-prestar.png)

Verificación: volver a llamar GET disponibles → `L-001` ya no aparece.

![disponibles-despues](evidencias/03-disponibles-despues.png)

---

## 3. POST /api/prestamos/prestar (material ya prestado)

Body: `{ "socioId": 2, "codigoMaterial": "L-001" }`
Respuesta esperada: **409 Conflict**, "El material 'L-001' no esta disponible...".

![prestar-409](evidencias/04-prestar-409.png)

---

## 4. POST /api/prestamos/devolver con estrategia CAMPANIA

Paso previo: `POST /api/prestamos/estrategia?tipo=CAMPANIA`
Body: `{ "socioId": 1, "codigoMaterial": "L-001", "diasAtraso": 5 }`
Respuesta esperada: `multaBase: 300` (5 × 60), socio REGULAR, `multaFinal: 300`.

![devolver-campania](evidencias/05-devolver-campania.png)

---

## 5. POST /api/prestamos/devolver con socio PREMIUM

Estrategia NORMAL, `diasAtraso: 10`, socio 2 (Premium).
Respuesta esperada: `multaBase: 1000`, `multaFinal: 500` (50% de descuento).

![devolver-premium](evidencias/06-devolver-premium.png)

---

## 6. POST /api/socios/depurar-duplicados

Body: `{ "dnis": ["12345678","12345678","87654321","abc","999","40222111","87654321"] }`
Respuesta esperada: `cantidadRecibidos: 7`, `cantidadUnicos: 3`,
`duplicadosDescartados: 2`, `dnisInvalidos: ["abc","999"]`.

![depurar](evidencias/07-depurar-duplicados.png)

---

## 7. POST /api/prestamos/prestar con datos inválidos

Body: `{ "socioId": 0, "codigoMaterial": "" }`
Respuesta esperada: **400 Bad Request** con el detalle de los campos.

![validacion-400](evidencias/08-validacion-400.png)
