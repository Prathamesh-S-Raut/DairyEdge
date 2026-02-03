package com.dairyedge.backend.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class SupplierDTO {

    @NotBlank(message = "Supplier name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;
}
