package com.duoc.sgf.ms_users.controller;

import com.duoc.sgf.ms_users.model.dto.UserRequestDto;
import com.duoc.sgf.ms_users.model.dto.UserResponseDto;
import com.duoc.sgf.ms_users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios S.D.G.F.", description = "Gestión centralizada de identidades, ciudadanos y operadores del sistema fronterizo.")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Retorna el registro completo de todos los usuarios registrados en el ecosistema.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente.")
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Busca un usuario específico utilizando su identificador único interno.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado."),
            @ApiResponse(responseCode = "404", description = "El usuario no existe.")
    })
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/rut/{rut}")
    @Operation(summary = "Obtener usuario por RUT/Pasaporte", description = "Busca a un ciudadano u operador utilizando su documento de identidad principal.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado."),
            @ApiResponse(responseCode = "404", description = "No existe usuario asociado a este documento.")
    })
    public ResponseEntity<UserResponseDto> findByRut(@PathVariable String rut) {
        return ResponseEntity.ok(userService.findByRut(rut));
    }

    @GetMapping("/role/{role}")
    @Operation(summary = "Filtrar por rol", description = "Retorna un listado de usuarios que poseen un rol específico (ej. ROLE_BORDER_AGENT, ROLE_CITIZEN).")
    @ApiResponse(responseCode = "200", description = "Listado filtrado correctamente.")
    public ResponseEntity<List<UserResponseDto>> findByRole(@PathVariable String role) {
        return ResponseEntity.ok(userService.findByRole(role));
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo registro de usuario en el ecosistema fronterizo.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o documento duplicado.")
    })
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Modifica los datos personales o roles de un usuario existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente."),
            @ApiResponse(responseCode = "404", description = "El usuario a actualizar no existe.")
    })
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id,
                                                  @Valid @RequestBody UserRequestDto request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina a un usuario del sistema o lo inactiva de forma lógica.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado sin contenido de retorno."),
            @ApiResponse(responseCode = "404", description = "El usuario a eliminar no existe.")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}