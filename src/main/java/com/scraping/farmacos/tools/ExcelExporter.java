package com.scraping.farmacos.tools;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.scraping.farmacos.model.Producto;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class ExcelExporter {
    public static void exportar(List<Producto> productos, HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=farmacos.xlsx");
        // Crear el Workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet hoja = workbook.createSheet("Productos");

        // Cabeceras
        Row filaCabecera = hoja.createRow(0);
        String[] cabeceras = { "Nombre", "Laboratorio", "Precio", "Código DIGEMID", "Fuente" };
        for (int i = 0; i < cabeceras.length; i++) {
            Cell celda = filaCabecera.createCell(i);
            celda.setCellValue(cabeceras[i]);
        }

        // Datos
        int filaNum = 1;
        for (Producto producto : productos) {
            Row fila = hoja.createRow(filaNum++);
            fila.createCell(0).setCellValue(producto.getNombre() != null ? producto.getNombre() : "");
            fila.createCell(1).setCellValue(producto.getLaboratorio() != null ? producto.getLaboratorio() : "");
            fila.createCell(2).setCellValue(producto.getPrecio() != null ? producto.getPrecio() : "");
            fila.createCell(3).setCellValue(producto.getCodigoDigemid() != null ? producto.getCodigoDigemid() : "");
            fila.createCell(4).setCellValue(producto.getFuente() != null ? producto.getFuente() : "");
        }

        // Autoajustar columnas
        for (int i = 0; i < cabeceras.length; i++) {
            hoja.autoSizeColumn(i);
        }

        // Guardar archivo
        try {
            workbook.write(response.getOutputStream());
            workbook.close();
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

    }

}
