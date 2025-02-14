package com.mycar.business.controllers.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @Schema(
            description = "User email",
            example = "usuario_ejemplo"
    )
    private String email;

    @Schema(
            description = "User password",
            example = "contraseña_segura"
    )
    private String password;
}
