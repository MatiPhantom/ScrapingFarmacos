package com.scraping.farmacos.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Producto {
    private String nombre;
    private String precio;
    private String laboratorio;
    private String codigoDigemid;
    private String fuente;
}
