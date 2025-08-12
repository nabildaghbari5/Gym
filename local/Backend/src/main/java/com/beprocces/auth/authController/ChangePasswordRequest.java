package com.beprocces.auth.authController;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordRequest {

    @NotBlank(message = "Current password cannot be blank")
    private String currentPassword;
    @NotBlank(message = "New password cannot be blank")
    private String newPassword;
    @NotBlank(message = "Confirmation password cannot be blank")
    private String confirmationPassword;
}
