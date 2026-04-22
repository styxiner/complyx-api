# DEVELOPMENT.md — API REST de Complyx

> Guía técnica para desarrolladores que se incorporan al proyecto. Explica la estructura de la API REST, los conceptos de Spring Boot usados y cómo está organizado el código.

---

## 1. Stack tecnológico

| Tecnología | Versión | Para qué se usa |
|---|---|---|
| Java | 21 | Lenguaje principal de la API |
| Spring Boot | 3.x | Framework base: servidor web, inyección de dependencias, configuración |
| Spring Security | 6.x | Autenticación JWT, control de acceso por roles |
| Spring Data JPA | 3.x | Acceso a base de datos mediante repositorios y entidades |
| Hibernate | 6.x | Implementación de JPA: mapeo objeto-relacional |
| PostgreSQL | 18+ | Base de datos relacional |
| MapStruct | 1.5+ | Generación de código para convertir entidades en DTOs y viceversa |

---

## 2. Estructura del proyecto

El código está organizado **por dominio** (package-by-feature), no por capa técnica. Cada área funcional agrupa todas sus clases juntas:

```
src/main/java/com/complyx/
│
├── common/                  # Clases compartidas por todos los dominios
│   └── BaseEntity.java      # Clase base con id, createdDate, lastModified
│
├── security/                # Autenticación y configuración de seguridad
│   ├── AuthController.java
│   ├── AuthService.java
│   ├── JwtUtil.java
│   ├── JwtAuthFilter.java
│   ├── SecurityConfig.java
│   ├── UserDetailsServiceImpl.java
│   └── dto/                 # LoginDTO, TokenResponseDTO
│
├── users/                   # Gestión de usuarios y roles
│   ├── UserController.java
│   ├── UserService.java
│   ├── UserRepository.java
│   ├── RoleRepository.java
│   ├── UserEntity.java
│   ├── RoleEntity.java
│   ├── UserMapper.java
│   ├── UserSpecifications.java
│   └── dto/                 # UserDTO, UserCreateDTO, UserUpdateDTO, UserFilter
│
├── agents/                  # Agentes y grupos
│   └── (misma estructura)
│
├── policies/                # Políticas de seguridad
│   └── (misma estructura)
│
├── regulations/             # Normativas y secciones
│   └── (misma estructura)
│
└── risk_modeling/           # Amenazas y riesgos
    └── (misma estructura)
```

> Cada dominio es autónomo: su controlador, servicio, repositorio, entidades, mappers y DTOs conviven en el mismo paquete. No hay carpetas globales `controllers/` ni `services/`.

---

## 3. Conceptos clave de Spring Boot

Esta sección explica los bloques de construcción de la API y el papel que juega cada uno.

### 3.1 `@Entity` — Entidades

Una **entidad** es una clase Java que representa una tabla de la base de datos. Cada instancia de la clase equivale a una fila. Hibernate se encarga de generar el SQL necesario para leer y escribir esos datos.

```java
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles")
    private Set<RoleEntity> roles;
}
```

**Regla fundamental**: las entidades **nunca salen del servicio**. Los controladores no reciben ni devuelven entidades — solo DTOs.

Las anotaciones de JPA más frecuentes:

| Anotación | Significado |
|---|---|
| `@Entity` | Declara la clase como tabla de base de datos |
| `@Table(name = "...")` | Especifica el nombre de la tabla |
| `@Id` | Clave primaria |
| `@GeneratedValue` | La PK se genera automáticamente (UUID, secuencia…) |
| `@Column` | Configura una columna: nombre, nulabilidad, unicidad |
| `@OneToMany` | Relación uno a muchos (ej. Policy → PolicyElement) |
| `@ManyToMany` | Relación muchos a muchos (ej. User ↔ Role) |
| `@ManyToOne` | Relación muchos a uno (ej. Risk → Agent) |
| `@JoinTable` | Define la tabla intermedia de una relación M:N |
| `@JoinColumn` | Define la columna de clave foránea |

### 3.2 DTO — Data Transfer Object

Un **DTO** es una clase Java simple (sin lógica de negocio) que define exactamente qué datos viajan entre el cliente y la API. Su propósito es desacoplar la forma en que se almacenan los datos (entidad) de la forma en que se exponen (contrato de API).

Hay varios tipos de DTO según su uso:

| Tipo | Ejemplo | Cuándo se usa |
|---|---|---|
| **Response DTO** | `UserDTO` | Se devuelve al cliente en respuestas GET |
| **Create DTO** | `UserCreateDTO` | Se recibe del cliente en peticiones POST |
| **Update DTO** | `UserUpdateDTO` | Se recibe en peticiones PUT/PATCH |
| **Summary DTO** | `PolicySummaryDTO` | Versión ligera para listados paginados |
| **Detail DTO** | `PolicyDetailDTO` | Versión completa para consultas por ID |
| **Filter DTO** | `UserFilter` | Parámetros de búsqueda/filtrado en GET listados |

Ejemplo de para qué sirve cada uno en la práctica:

```
GET /api/policies          →  devuelve  Page<PolicySummaryDTO>   (id, nombre, severidad)
GET /api/policies/{id}     →  devuelve  PolicyDetailDTO           (todo: elementos, checks, remediaciones)
POST /api/policies         →  recibe    PolicyCreateDTO           (nombre, versión, descripción, severidad)
PUT  /api/policies/{id}    →  recibe    PolicyUpdateDTO           (solo los campos que se pueden modificar)
```

### 3.3 `@Repository` — Repositorios

Un **repositorio** es una interfaz que extiende `JpaRepository` o `JpaSpecificationExecutor`. Spring Data JPA genera automáticamente la implementación en tiempo de ejecución: no hace falta escribir SQL para las operaciones básicas.

```java
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID>,
                                        JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByUsername(String username);
}
```

Solo hay que añadir métodos cuando la consulta no existe ya en `JpaRepository`. Spring Data infiere el SQL a partir del nombre del método (`findBy...`, `existsBy...`, `countBy...`).

### 3.4 `@Service` — Servicios

El **servicio** contiene la lógica de negocio. Es el único componente que trabaja con entidades directamente. Se encarga de:

- Recibir un DTO del controlador
- Convertirlo a entidad (usando el mapper)
- Llamar al repositorio para persistir o consultar
- Convertir la entidad resultante a DTO de respuesta
- Lanzar excepciones de dominio si algo va mal

```java
@Service
@Transactional(readOnly = true)   // todas las operaciones son read-only por defecto
public class UserService {

    @Transactional                // esta sí modifica datos, override del default
    public UserDTO create(UserCreateDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateUsernameException(dto.getUsername());
        }
        UserEntity entity = userMapper.toEntity(dto);
        entity.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        return userMapper.toDTO(userRepository.save(entity));
    }
}
```

`@Transactional` garantiza que si algo falla a mitad de la operación, todos los cambios en base de datos se deshacen automáticamente (rollback).

### 3.5 `@RestController` — Controladores

El **controlador** es el punto de entrada HTTP. Su única responsabilidad es:

1. Recibir la petición HTTP y deserializar el body a un DTO
2. Delegar al servicio
3. Devolver la respuesta HTTP con el DTO resultado y el código de estado correcto

No contiene ninguna lógica de negocio.

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO dto) {
        UserDTO created = userService.create(dto);
        URI location = URI.create("/api/users/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }
}
```

Anotaciones HTTP más usadas:

| Anotación | Método HTTP | Uso típico |
|---|---|---|
| `@GetMapping` | GET | Consultar recursos |
| `@PostMapping` | POST | Crear un recurso nuevo |
| `@PutMapping` | PUT | Reemplazar un recurso completo |
| `@PatchMapping` | PATCH | Modificar parcialmente un recurso |
| `@DeleteMapping` | DELETE | Eliminar un recurso |
| `@PathVariable` | — | Extraer un valor de la URL (`/users/{id}`) |
| `@RequestParam` | — | Extraer un parámetro de query string (`?page=0`) |
| `@RequestBody` | — | Deserializar el cuerpo JSON de la petición |
| `@Valid` | — | Activa la validación de campos del DTO (`@NotNull`, `@Size`…) |

### 3.6 `Specification` — Filtros dinámicos

Para los endpoints de listado con múltiples filtros opcionales se usa el patrón `Specification<T>` de Spring Data. Cada clase `*Specifications` construye condiciones de consulta que se combinan dinámicamente según los parámetros que lleguen.

```java
// Si el usuario pasa ?username=john, se añade el filtro; si no, se ignora
public static Specification<UserEntity> hasUsername(String username) {
    return (root, query, cb) ->
        username == null ? null : cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
}

public static Specification<UserEntity> build(UserFilter filter) {
    return Specification
        .where(hasUsername(filter.getUsername()))
        .and(hasEmail(filter.getEmail()))
        .and(hasRole(filter.getRoleId()));
}
```

### 3.7 `Mapper` — Conversión entidad ↔ DTO

Los **mappers** convierten objetos entre capas. Se declaran como interfaces y MapStruct genera la implementación en tiempo de compilación.

```java
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(UserEntity entity);
    UserEntity toEntity(UserCreateDTO dto);
}
```

MapStruct mapea campos por nombre automáticamente. Si el nombre o tipo no coincide se anota con `@Mapping(source = "...", target = "...")`.

---

## 4. Capas de la arquitectura

El flujo de datos siempre sigue esta dirección. Cada capa solo conoce a la inmediatamente inferior:

```
Cliente HTTP
     │
     ▼
┌──────────────────────────────────────────────┐
│  Controller  «@RestController»               │
│  Recibe DTO de entrada, devuelve DTO salida  │
└──────────────────────┬───────────────────────┘
                       │ llama a
                       ▼
┌──────────────────────────────────────────────┐
│  Service  «@Service»                         │
│  Lógica de negocio, trabaja con Entidades    │
│  Usa: Repository, Mapper, Specifications     │
└──────────┬───────────┬────────────┬──────────┘
           │           │            │
           ▼           ▼            ▼
      Repository    Mapper    Specifications
     «@Repository» (entidad↔DTO)  (filtros)
           │
           ▼
     Base de datos (PostgreSQL)
```

---

## 5. Contextos de dominio y endpoints

La API está dividida en seis contextos. Cada uno tiene su propio controlador, servicio y repositorios.

### 5.1 Seguridad — `/api/auth`

Gestiona la autenticación. No requiere token para sus propios endpoints.

| Método | Ruta | Body entrada | Respuesta | Descripción |
|---|---|---|---|---|
| `POST` | `/api/auth/login` | `LoginDTO` | `TokenResponseDTO` | Autentica usuario y devuelve tokens |
| `POST` | `/api/auth/refresh` | `refreshToken` (header) | `TokenResponseDTO` | Renueva el access token |
| `POST` | `/api/auth/logout` | — | `204 No Content` | Invalida el token actual |

**DTOs involucrados:**

`LoginDTO`: `{ username, password }`

`TokenResponseDTO`: `{ accessToken, refreshToken, expiresIn }`

### 5.2 Usuarios — `/api/users`

CRUD de usuarios y asignación de roles.

| Método | Ruta | Body entrada | Respuesta | Descripción |
|---|---|---|---|---|
| `GET` | `/api/users` | `UserFilter` (query params) | `Page<UserDTO>` | Listado paginado y filtrable |
| `GET` | `/api/users/{id}` | — | `UserDTO` | Detalle de un usuario |
| `POST` | `/api/users` | `UserCreateDTO` | `201 + UserDTO` | Crear usuario |
| `PUT` | `/api/users/{id}` | `UserUpdateDTO` | `UserDTO` | Actualizar email o contraseña |
| `DELETE` | `/api/users/{id}` | — | `204 No Content` | Eliminar usuario |
| `POST` | `/api/users/{id}/roles/{roleId}` | — | `204 No Content` | Asignar rol |
| `DELETE` | `/api/users/{id}/roles/{roleId}` | — | `204 No Content` | Quitar rol |

**Filtros disponibles** (`UserFilter`): `username`, `email`, `roleId`

### 5.3 Agentes — `/api/agents`

Gestión de los agentes registrados y su pertenencia a grupos.

| Método | Ruta | Body entrada | Respuesta | Descripción |
|---|---|---|---|---|
| `GET` | `/api/agents` | `AgentFilter` (query params) | `Page<AgentDTO>` | Listado filtrable |
| `GET` | `/api/agents/{id}` | — | `AgentDTO` | Detalle de un agente |
| `POST` | `/api/agents/{id}/groups/{groupId}` | — | `204 No Content` | Añadir agente a grupo |
| `DELETE` | `/api/agents/{id}/groups/{groupId}` | — | `204 No Content` | Quitar agente de grupo |
| `PATCH` | `/api/agents/{id}/enable` | — | `AgentDTO` | Activar agente |
| `PATCH` | `/api/agents/{id}/disable` | — | `AgentDTO` | Desactivar agente |
| `DELETE` | `/api/agents/{id}` | — | `204 No Content` | Eliminar agente |

**Filtros disponibles** (`AgentFilter`): `ip`, `hostname`, `osName`, `enabled`, `groupId`

### 5.4 Políticas — `/api/policies`

Gestión de políticas de seguridad y su asignación a agentes o grupos.

| Método | Ruta | Body entrada | Respuesta | Descripción |
|---|---|---|---|---|
| `GET` | `/api/policies` | `PolicyFilter` (query params) | `Page<PolicySummaryDTO>` | Listado resumido y filtrable |
| `GET` | `/api/policies/{id}` | — | `PolicyDetailDTO` | Detalle completo con elementos, checks y remediaciones |
| `POST` | `/api/policies` | `PolicyCreateDTO` | `201 + PolicyDetailDTO` | Crear política |
| `PUT` | `/api/policies/{id}` | `PolicyUpdateDTO` | `PolicyDetailDTO` | Actualizar metadatos |
| `DELETE` | `/api/policies/{id}` | — | `204 No Content` | Eliminar política (cascada a todos sus hijos) |
| `POST` | `/api/policies/{id}/agents/{agentId}` | — | `204 No Content` | Asignar política a un agente |
| `POST` | `/api/policies/{id}/groups/{groupId}` | — | `204 No Content` | Asignar política a un grupo |
| `GET` | `/api/agents/{agentId}/policies` | — | `List<PolicySummaryDTO>` | Políticas asignadas a un agente |

**Filtros disponibles** (`PolicyFilter`): `name`, `severity`, `assignedToAgentId`, `assignedToGroupId`

**Jerarquía interna de una política:**

```
PolicyEntity
 └── PolicyElementEntity  (agrupación lógica)
      └── PolicyCheckEntity  (condición verificable)
           └── PolicyRemediationEntity  (corrección automática)
```

Cuando se elimina una política, la cascada borra en cadena todos sus elementos, checks y remediaciones (`ON DELETE CASCADE` en base de datos, `CascadeType.ALL` + `orphanRemoval = true` en JPA).

### 5.5 Normativas — `/api/regulations`

Gestión de documentos normativos (ISO 27001, ENS, NIS 2) y sus secciones.

| Método | Ruta | Body entrada | Respuesta | Descripción |
|---|---|---|---|---|
| `GET` | `/api/regulations` | `RegulationFilter` (query params) | `Page<RegulationSummaryDTO>` | Listado filtrable |
| `GET` | `/api/regulations/{id}` | — | `RegulationDetailDTO` | Detalle con todas sus secciones |
| `POST` | `/api/regulations` | `RegulationCreateDTO` | `201 + RegulationDetailDTO` | Crear normativa |
| `PUT` | `/api/regulations/{id}` | `RegulationUpdateDTO` | `RegulationDetailDTO` | Actualizar metadatos |
| `DELETE` | `/api/regulations/{id}` | — | `204 No Content` | Eliminar normativa |
| `POST` | `/api/regulations/{id}/pdf` | `MultipartFile` | `204 No Content` | Subir el PDF del documento |
| `POST` | `/api/regulations/{id}/sections` | `RegSectionCreateDTO` | `RegulationDetailDTO` | Añadir sección a la normativa |

**Filtros disponibles** (`RegulationFilter`): `name`, `hasSectionWithTitle`

Las secciones de normativa (`RegulationSectionEntity`) se enlazan con los checks de política a través de la tabla intermedia `check_regulation_sections`. Esto permite la trazabilidad: *"este check técnico satisface el control 8.9 de ISO 27001"*.

### 5.6 Gestión de riesgos — `/api/risks` y `/api/threats`

**Amenazas** (`/api/threats`): catálogo reutilizable de tipos de ataque o fallo.

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/threats` | Listado paginado |
| `GET` | `/api/threats/{id}` | Detalle |
| `POST` | `/api/threats` | Crear amenaza |
| `PUT` | `/api/threats/{id}` | Actualizar |
| `DELETE` | `/api/threats/{id}` | Eliminar (falla si tiene riesgos abiertos asociados) |

**Riesgos** (`/api/risks`): instanciación de una amenaza sobre un agente concreto.

| Método | Ruta | Body entrada | Respuesta | Descripción |
|---|---|---|---|---|
| `GET` | `/api/risks` | `RiskFilter` (query params) | `Page<RiskDTO>` | Listado filtrable |
| `GET` | `/api/risks/{id}` | — | `RiskDetailDTO` | Detalle con políticas mitigadoras |
| `POST` | `/api/risks` | `RiskCreateDTO` | `201 + RiskDTO` | Crear riesgo (vincula amenaza + agente) |
| `PUT` | `/api/risks/{id}` | `RiskUpdateDTO` | `RiskDTO` | Actualizar impacto, probabilidad o fecha revisión |
| `PATCH` | `/api/risks/{id}/close` | — | `RiskDTO` | Cerrar riesgo |
| `PATCH` | `/api/risks/{id}/accept` | — | `RiskDTO` | Aceptar riesgo |
| `POST` | `/api/risks/{id}/policies/{policyId}` | — | `204 No Content` | Vincular política mitigadora |
| `DELETE` | `/api/risks/{id}/policies/{policyId}` | — | `204 No Content` | Desvincular política |

**Filtros disponibles** (`RiskFilter`): `agentId`, `status`, `riskLevel`, `threatId`

El nivel de riesgo (`riskLevel`) se calcula en `RiskService.computeRiskLevel()` a partir de `impacto × probabilidad` siguiendo una matriz de riesgos estándar.

---

## 6. Flujo de una petición HTTP

Este es el recorrido completo de una petición desde que llega al servidor hasta que se devuelve la respuesta. Se usa como ejemplo `POST /api/users`:

```
1. PETICIÓN ENTRANTE
   Cliente envía: POST /api/users
                  Authorization: Bearer <jwt>
                  Body: { "username": "juan", "email": "juan@acme.com", "password": "***" }

2. FILTRO JWT  (JwtAuthFilter)
   - Extrae el token del header Authorization
   - Llama a JwtUtil.validateToken()
   - Si el token es válido, carga el usuario con UserDetailsServiceImpl
   - Registra la identidad en el SecurityContext de Spring

3. SPRING SECURITY
   - Comprueba que el rol del usuario tiene permiso para POST /api/users
   - Si no tiene permiso → devuelve 403 Forbidden

4. CONTROLADOR  (UserController.createUser)
   - Spring deserializa el JSON del body a UserCreateDTO
   - @Valid activa la validación: comprueba @NotBlank, @Email, @Size, etc.
   - Si la validación falla → devuelve 400 Bad Request automáticamente
   - Llama a userService.create(dto)

5. SERVICIO  (UserService.create)
   - Comprueba si el username ya existe con userRepository.existsByUsername()
   - Si existe → lanza DuplicateUsernameException
   - Llama a userMapper.toEntity(dto) para convertir el DTO a UserEntity
   - Hashea la contraseña con passwordEncoder.encode()
   - Llama a userRepository.save(entity) → Hibernate genera el INSERT
   - Llama a userMapper.toDTO(savedEntity) para preparar la respuesta
   - Devuelve UserDTO al controlador

6. CONTROLADOR  (respuesta)
   - Construye ResponseEntity con status 201 Created
   - Añade el header Location: /api/users/{newId}
   - Serializa UserDTO a JSON

7. RESPUESTA AL CLIENTE
   HTTP 201 Created
   Location: /api/users/550e8400-e29b-41d4-a716-446655440000
   Body: { "id": "550e...", "username": "juan", "email": "juan@acme.com", "roles": [] }
```

**¿Qué pasa si el servicio lanza una excepción?**

Existe un `@ControllerAdvice` global que intercepta las excepciones de dominio y las traduce a respuestas HTTP con el código correcto:

| Excepción | Código HTTP |
|---|---|
| `EntityNotFoundException` | `404 Not Found` |
| `DuplicateUsernameException` | `409 Conflict` |
| `AccessDeniedException` | `403 Forbidden` |
| `MethodArgumentNotValidException` | `400 Bad Request` |
| `Exception` (cualquier otra) | `500 Internal Server Error` |

---

## 7. Seguridad y autenticación

### JWT — JSON Web Token

La API es **stateless**: no guarda sesiones en el servidor. Cada petición debe incluir un token JWT en el header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Un JWT tiene tres partes separadas por puntos: `header.payload.signature`

El **payload** contiene el username, los roles y la fecha de expiración. El servidor verifica la firma criptográfica para asegurarse de que no ha sido manipulado.

Hay dos tipos de token:

| Token | Duración | Para qué |
|---|---|---|
| `accessToken` | Corta (ej. 15 minutos) | Autorizar peticiones a la API |
| `refreshToken` | Larga (ej. 7 días) | Obtener un nuevo accessToken sin volver a pedir credenciales |

Uno de los posibles defectos que puede tener la API con este tipo de planteamiento, es que no hay un mecanismo real para invalidar el token antes de que termine su duración (logout). Al no implementar un mecanismo de lista negra, lo que habrá que hacer en el front es que al llamar al endpoint de cierre de sesión, descartar el token.

### Componentes de seguridad

```
SecurityConfig
 └── configura el filtro y las rutas protegidas

JwtAuthFilter   (se ejecuta en cada petición)
 ├── extrae el token del header
 ├── llama a JwtUtil.validateToken()
 └── si es válido → carga UserDetails y actualiza SecurityContext

JwtUtil
 ├── generateToken(userDetails) → genera accessToken
 ├── generateRefreshToken(userDetails) → genera refreshToken
 └── validateToken(token, userDetails) → verifica firma y expiración

UserDetailsServiceImpl
 └── loadUserByUsername(username) → consulta UserRepository → devuelve UserDetails
```

### Control de acceso por roles

Los roles se almacenan en la tabla `roles` y se asignan a usuarios en `users_roles`. En el código, los endpoints se protegen con `@PreAuthorize`:

```java
@PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable UUID id) { ... }

@PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
@PostMapping
public ResponseEntity<UserDTO> createUser(...) { ... }
```

---

## 8. Convenciones de desarrollo

### Nomenclatura de clases

| Tipo | Sufijo | Ejemplo |
|---|---|---|
| Entidad JPA | `Entity` | `UserEntity` |
| Repositorio | `Repository` | `UserRepository` |
| Servicio | `Service` | `UserService` |
| Controlador | `Controller` | `UserController` |
| Mapper | `Mapper` | `UserMapper` |
| Specifications | `Specifications` | `UserSpecifications` |
| DTO de respuesta | `DTO` | `UserDTO` |
| DTO de creación | `CreateDTO` | `UserCreateDTO` |
| DTO de actualización | `UpdateDTO` | `UserUpdateDTO` |
| DTO de resumen (listados) | `SummaryDTO` | `PolicySummaryDTO` |
| DTO de detalle (por ID) | `DetailDTO` | `PolicyDetailDTO` |

### Códigos de respuesta HTTP

| Situación | Código |
|---|---|
| Consulta exitosa (GET) | `200 OK` |
| Recurso creado (POST) | `201 Created` + header `Location` |
| Operación sin cuerpo (DELETE, PATCH) | `204 No Content` |
| Datos de entrada inválidos | `400 Bad Request` |
| Token inválido o ausente | `401 Unauthorized` |
| Sin permisos suficientes | `403 Forbidden` |
| Recurso no encontrado | `404 Not Found` |
| Conflicto (duplicado) | `409 Conflict` |
| Error interno | `500 Internal Server Error` |

### Paginación

Todos los endpoints de listado devuelven `Page<T>` de Spring. El cliente envía los parámetros de paginación como query params:

```
GET /api/users?page=0&size=20&sort=username,asc
```

### Transacciones

- La clase de servicio se anota con `@Transactional(readOnly = true)` a nivel de clase: todas las operaciones de solo lectura son más eficientes así.
- Los métodos que escriben en base de datos se anotan individualmente con `@Transactional` (sin `readOnly`), sobreescribiendo el comportamiento de clase.
- Si un método de servicio lanza una excepción no comprobada (`RuntimeException`), Spring deshace automáticamente todos los cambios de esa transacción.
