package io.github.styxiner.complyx_api.users;

import java.net.URI;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "Gestión de roles de usuario")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService service) {
        this.roleService = service;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los roles con filtros y paginación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Página de roles obtenida con éxito")
    })
    @Parameters({
        @Parameter(name = "page", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
        @Parameter(name = "size", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "20")),
        @Parameter(name = "sort", in = ParameterIn.QUERY, schema = @Schema(type = "string", example = "rolename,asc"))
    })
    public ResponseEntity<Page<RoleDTO>> getAllRoles(
            @ParameterObject RoleFilter filter,
            @Parameter(hidden = true) Pageable pageable) {
        return ResponseEntity.ok(roleService.getAllRoles(filter, pageable));
    }

    @GetMapping("/{roleId}")
    @Operation(summary = "Obtener un rol por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rol encontrado"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable UUID roleId) {
        return ResponseEntity.ok(roleService.getRolesById(roleId));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo rol")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Rol creado con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Ya existe un rol con ese nombre")
    })
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleCreateDTO dto) {
        RoleDTO created = roleService.create(dto);
        URI location = URI.create("/api/roles/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{roleId}")
    @Operation(summary = "Actualizar un rol existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rol actualizado"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    public ResponseEntity<RoleDTO> updateRole(
            @PathVariable UUID roleId,
            @Valid @RequestBody RoleUpdateDTO dto) {
        dto.setId(roleId);
        return ResponseEntity.ok(roleService.update(dto));
    }

    @DeleteMapping("/{roleId}")
    @Operation(summary = "Eliminar un rol")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Rol eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    public ResponseEntity<Void> deleteRole(@PathVariable UUID roleId) {
        roleService.delete(roleId);
        return ResponseEntity.noContent().build();
    }
}