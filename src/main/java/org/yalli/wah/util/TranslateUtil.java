package org.yalli.wah.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.validator.internal.xml.mapping.MappingXmlParser;
import org.yalli.wah.model.exception.InvalidInputException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class TranslateUtil {
    private static final String ALPHANUMERICS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static  Map<String, String> countryMap = new HashMap<>();
    private static  Map<String, String> cityMap = new HashMap<>();

    static {
        countryMap.put("Türkiyə", "Türkiye");
        countryMap.put("Azərbaycan", "Azerbaijan");
        countryMap.put("Rusiya", "Russia");
        countryMap.put("Almaniya", "Germany");
        countryMap.put("ABŞ", "USA");
        countryMap.put("Ukrayna", "Ukraine");
        countryMap.put("Böyük Britaniya", "United Kingdom");
        countryMap.put("Kanada", "Canada");
        countryMap.put("Fransa", "France");
        countryMap.put("İsrail", "Israel");
        countryMap.put("Gürcüstan", "Georgia");
        countryMap.put("İtaliya", "Italy");
        countryMap.put("Avstraliya", "Australia");
        countryMap.put("İspaniya", "Spain");
        countryMap.put("Niderland", "The Netherlands");

    }

    static{
    
        cityMap.put("İsmayıllı", "Ismayilli");
        cityMap.put("Kəlbəcər", "Kalbajar");
        cityMap.put("Kürdəmir", "Kurdamir");
        cityMap.put("Qax", "Gakh");
        cityMap.put("Qazax", "Gazakh");
        cityMap.put("Qəbələ", "Gabala");
    
        cityMap.put("Tekirdağ", "Tekirdag");
        cityMap.put("Tokat", "Tokat");
        cityMap.put("Trabzon", "Trabzon");
        cityMap.put("Tunceli", "Tunceli");



    }

    //Olkeni ingilise cevirir
    public static String getTranslation(String word) {
        return countryMap.get(word);
    }

    //Seherin Azerbaycanca olani Ingilise cevir
    public static String getCityTranslation(String word){
        if (cityMap.get(word) == null){
            throw new InvalidInputException("City isn't available in our platform right now");
        } else {
            return cityMap.get(word);
        }
    }
    //Seherin ingilisce olani azerbaycanca
    public static String getCityInAzerbajani(String word){
        if(word !=null){
            for(Map.Entry<String, String> entry: cityMap.entrySet() ){
                if(entry.getValue().equals(word)){
                    return entry.getKey();
                }
            }
            throw new InvalidInputException("City isn't available in our platform right now");
        } else {
            throw new InvalidInputException("City name cannnot be null");
        }
    }

    public static String getAzerbaijani(String word) {
        if (word != null) {
            for (Map.Entry<String, String> entry : countryMap.entrySet()) {
                if (entry.getValue().equals(word)) {
                    return entry.getKey();
                }
            }
        } else {
            throw new InvalidInputException("INPUT_IS_EMPTY");
        }

        throw new InvalidInputException("THERE_IS_NOT_COUNTRY");
    }


    public static String getLink(String link) {
        String inEnglish = link.toLowerCase().replaceAll("ə", "e")
                .replaceAll("ğ", "g")
                .replaceAll("ı", "i")
                .replaceAll("ç", "c")
                .replaceAll("ö", "o")
                .replaceAll("ü", "u")
                .replaceAll("q", "g")
                .replaceAll(" ", "-");
        StringBuilder suffix = new StringBuilder("-");
        Random ran = new Random();
        for (int i = 0; i < 15; i++) {
            int index = ran.nextInt(0, ALPHANUMERICS.length());
            if (ran.nextInt(1000) % 2 == 0) {
                suffix.append(Character.toLowerCase(ALPHANUMERICS.charAt(index)));
            } else {
                suffix.append(ALPHANUMERICS.charAt(index));
            }
        }
        return inEnglish + suffix;

    }
}
