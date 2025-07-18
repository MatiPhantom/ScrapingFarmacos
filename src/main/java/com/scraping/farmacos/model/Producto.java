package com.scraping.farmacos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    private String nombre;
    private double precio;
    private String laboratorio;
    private String codigoDigemid;
    private String fuente;
}
