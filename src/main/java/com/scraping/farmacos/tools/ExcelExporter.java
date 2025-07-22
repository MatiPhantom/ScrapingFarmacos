package com.scraping.farmacos.tools;

public class ExcelExporter {
    public void exportar(List<ProductoInfo> productos, String nombreArchivo) {
        Workbook workbook = new XSSFWorkbook();
        Sheet hoja = workbook.createSheet("Productos");

        // Cabeceras
        Row filaCabecera = hoja.createRow(0);
        String[] cabeceras = {"Nombre", "Laboratorio", "Precio", "Código DIGEMID", "Fuente"};
        for (int i = 0; i < cabeceras.length; i++) {
            Cell celda = filaCabecera.createCell(i);
            celda.setCellValue(cabeceras[i]);
        }

        // Datos
        int filaNum = 1;
        for (ProductoInfo producto : productos) {
            Row fila = hoja.createRow(filaNum++);
            fila.createCell(0).setCellValue(producto.getNombre());
            fila.createCell(1).setCellValue(producto.getLaboratorio());
            fila.createCell(2).setCellValue(producto.getPrecio());
            fila.createCell(3).setCellValue(producto.getCodigoDIGEMID());
            fila.createCell(4).setCellValue(producto.getFuente());
        }

        // Autoajustar columnas
        for (int i = 0; i < cabeceras.length; i++) {
            hoja.autoSizeColumn(i);
        }

        // Guardar archivo
        try (FileOutputStream fileOut = new FileOutputStream(nombreArchivo)) {
            workbook.write(fileOut);
            workbook.close();
            System.out.println("Archivo Excel exportado exitosamente: " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
