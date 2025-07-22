package com.scraping.farmacos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.scraping.farmacos.model.Producto;
import com.scraping.farmacos.service.ProductoService;
import com.scraping.farmacos.tools.ExcelExporter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public String index(Model modelo) {

        List<Producto> listFarmacos = productoService.getProductos();
        modelo.addAttribute("farmacos", listFarmacos);

        return "home/index";
    }

    @PostMapping
    public void exportarExcel(HttpServletResponse response) {
        try {
            ExcelExporter.exportar(productoService.getProductos(), response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
