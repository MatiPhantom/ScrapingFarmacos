package com.scraping.farmacos.scraper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.scraping.farmacos.model.Producto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InkafarmaScraper {

    public void buscar(String nombreProducto) {

        String url = "https://inkafarma.pe/buscador?keyword="
                + URLEncoder.encode(nombreProducto, StandardCharsets.UTF_8);

        try {
            // Conectando a la URL y obteniendo documentación
            Document doc = Jsoup.connect(url).userAgent(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .timeout(10000)
                    .get();
            log.info("Conectado a la URL: " + url + " con éxito " + doc.title());

            /*
             * Elements nombre= doc.select("data-product-name");
             * Elements precio= doc.select("span.text-strike--1");
             * Elements precioOferta= doc.select("p.label-black--1");
             * 
             * List<Producto> listaProductos= new ArrayList<>();
             * 
             * for(int i=0; i< nombre.size();i++){
             * String titulo= nombre.get(i).text();
             * String precioNormal= precio.get(i).text();
             * String precioDescuento= precioOferta.get(i).text();
             * String link= nombre.get(i).attr("href");
             * 
             * producto.setNombre(titulo);
             * producto.setPrecio(precioNormal);
             * producto.setFuente(link);
             * 
             * log.info("Producto encontrado: " + producto.toString());
             * }
             */

            Elements productos = doc.select("div.card.product");
            log.info("HTML XD: " + doc.html());
            log.info("Productos encontrados: " + productos.size());
            for (Element producto : productos) {
                String nombre = producto.attr("data-product-name");
                String marca = producto.attr("data-product-brand");
                String precioRegular = producto.selectFirst("p.label--2.mb-0.mf.text--1") != null
                        ? producto.selectFirst("p.label--2.mb-0.mf.text--1").text()
                        : "";
                String precioOferta = producto.selectFirst("span.card-monedero") != null
                        ? producto.selectFirst("span.card-monedero").text()
                        : "";
                String urlProduct = "https://inkafarma.pe" + producto.selectFirst("a.link").attr("href");

                System.out.println("Nombre: " + nombre);
                System.out.println("Marca: " + marca);
                System.out.println("Precio regular: " + precioRegular);
                System.out.println("Precio oferta: " + precioOferta);
                System.out.println("URL: " + urlProduct);
                System.out.println("--------");
            }

        } catch (Exception e) {
            log.error("Conexion fallida a la URL: " + url, e);
        }

    }

}
