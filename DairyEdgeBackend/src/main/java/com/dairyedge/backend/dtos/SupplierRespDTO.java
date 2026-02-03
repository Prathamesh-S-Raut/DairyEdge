package com.dairyedge.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRespDTO {

	private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Boolean isActive;
}
