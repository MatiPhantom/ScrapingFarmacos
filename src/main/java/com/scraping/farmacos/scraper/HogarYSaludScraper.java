package com.scraping.farmacos.scraper;

import java.net.URL;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HogarYSaludScraper {

    public void buscar(String nombreProducto){
        try{
            Document doc = Jsoup.connect("https://www.hogarysalud.com.pe/?s="+URLEncoder.encode(nombreProducto,"UTF-8")+"&post_type=product")
                    .userAgent("Mozilla")
                    .get();
        Element producto = doc.selectFirst("div.wd-product");


            
        }catch(Exception e){

        }
        

    }
}
