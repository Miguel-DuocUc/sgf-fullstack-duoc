package com.duoc.sgf.ms_users.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para registrar o actualizar un usuario en el S.D.G.F.")
public class UserRequestDto {

    @NotBlank(message = "El RUT o pasaporte es obligatorio")
    @Schema(description = "Documento de identidad principal", example = "19283746-5")
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre(s) del usuario", example = "Miguel")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Schema(description = "Apellido(s) del usuario", example = "González")
    private String lastName;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    @Schema(description = "Correo electrónico institucional o personal", example = "miguel.gonzalez@estado.sgf")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener mínimo 6 caracteres")
    @Schema(description = "Contraseña de acceso (será encriptada por el sistema)", example = "Seguridad2026!")
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @Schema(description = "Rol de acceso en el ecosistema", example = "ROLE_BORDER_AGENT")
    private String role;

    @Schema(description = "Estado operativo de la cuenta", example = "ACTIVO")
    private String status;
}