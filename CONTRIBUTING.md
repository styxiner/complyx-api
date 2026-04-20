# Cómo contribuir a Complyx-api

Esta guía explica recomendaciones de cómo trabajar con Git para que el código de todos los participantes conviva sin problemas. Está escrita pensando en personas con poca experiencia en Git, se recomienda no saltarse ningún paso por muy obvio que parezca el contenido.

---

## La regla más importante

> **Nunca trabajes directamente sobre `main`.**

`main` es la rama estable. Nadie hace cambios ahí directamente. Todo el trabajo se hace en una rama `feature/` propia y llega a `main` únicamente mediante una Pull Request revisada.

---

## Estructura de ramas

| Rama | Propósito |
|---|---|
| `main` | Código estable y revisado. Protegida. |
| `feature/<nombre>` | Tu trabajo en curso. Una rama por tarea. |

Eso es todo. No hay más ramas con las que preocuparse.

---

## Flujo de trabajo paso a paso

### 1. Antes de empezar: actualiza tu copia local

Antes de crear una rama nueva, asegúrate de tener lo último de `main`:

```bash
git checkout main
git pull origin main
```

Si no haces esto, tu rama puede partir de una versión desactualizada y tendrás conflictos más adelante.

---

### 2. Crea tu rama de trabajo

El nombre debe describir brevemente la tarea. Usa minúsculas y guiones, sin espacios ni caracteres especiales:

```bash
git checkout -b feature/login-jwt
```

Ejemplos de nombres válidos:

```
feature/login-jwt
feature/endpoint-agentes
feature/fix-validacion-email
feature/risk-management
```

---

### 3. Trabaja en tu rama

Haz tus cambios con normalidad. Cuando quieras guardar el progreso:

```bash
git add -A .
git commit -m "Añade validación de email en UserService"
```

**Sobre los mensajes de commit:**

- Escríbelos en español.
- Describe *qué* hace el cambio, no *cómo* lo hiciste.
- Evita mensajes como `"cambios"`, `"arreglado"` o `"wip"` —no dicen nada útil.

Puedes hacer tantos commits como quieras en tu rama. No pasa nada por tener muchos.

---

### 4. Sube tu rama al repositorio remoto

```bash
git push origin feature/login-jwt
```

Si es la primera vez que subes esa rama, Git te pedirá que establezcas el destino:

```bash
git push --set-upstream origin feature/login-jwt
```

Desde la segunda vez, basta con `git push`.

---

### 5. Abre una Pull Request

Ve a GitHub, localiza tu rama y abre una Pull Request hacia `main`.

En la descripción de la PR explica brevemente:

- Qué hace este cambio.
- Si hay algo que el revisor deba probar o tener en cuenta.

Con dos o tres líneas es suficiente si el cambio es pequeño.

---

### 6. Revisión

Al menos otra persona del equipo debe revisar la PR antes de que se fusione. Si el revisor pide cambios. El revisor por defecto será @styxiner:

```bash
# Haz los cambios en local
git add .
git commit -m "Corrige observación de revisión: nombre de variable"
git push
```

El PR se actualiza automáticamente. No hace falta cerrarla y abrir una nueva.

---

### 7. Fusión y limpieza

Una vez aprobada, el PR se fusiona desde la interfaz web. No uses `git merge` en local para fusionar hacia `main`.

---

## Situaciones frecuentes

### "Main ha avanzado mientras yo trabajaba"

Es normal. Antes de abrir el PR, actualiza tu rama con los últimos cambios de `main`:

```bash
git checkout feature/login-jwt
git pull origin main
```

Si hay conflictos, Git te los marcará en los archivos afectados. Resuélvelos, luego:

```bash
git add .
git commit -m "Resuelve conflictos con main"
git push
```

Si no estás seguro de cómo resolver un conflicto, **pide ayuda** antes de hacer nada más. **Es mejor preguntar** que perder parte del trabajo.

---

### "He hecho cambios en main por error"

Para, no hagas más cambios. Avisa al grupo ahora. Seguramente se podrá devolver el commit a un estado anterior. A una mala, puedes devolver los cambios al último commit con:
```bash
git reset --hard
```

---

### "He borrado algo que no debía"

Git casi nunca pierde cosas definitivamente. Avisa al equipo antes de intentar recuperarlo por tu cuenta.

---

### "No sé si mi rama está actualizada"

```bash
git log main..feature/mi-rama --oneline   # commits que tienes tú pero no main
git log feature/mi-rama..main --oneline   # commits de main que te faltan a ti
```

Si el segundo comando devuelve resultados, necesitas hacer `git pull origin main` en tu rama.

---

## Resumen visual

```
main         ──────────────────────────────────●── (protegida)
                                               ↑
feature/X    ──────●──●──●──────────────────── PR aprobada
```

Creas tu rama desde `main`, trabajas en ella, y vuelve a `main` por Pull Request. Nunca al revés.

---