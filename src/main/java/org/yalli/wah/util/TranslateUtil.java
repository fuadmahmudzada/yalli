package org.yalli.wah.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.validator.internal.xml.mapping.MappingXmlParser;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TranslateUtil {
    private static final String ALPHANUMERICS= "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String getTranslation(String word){
        Map<String, String> countryMap = new HashMap<>();
        countryMap.put("Turkey", "Türkiyə");
        countryMap.put("Russia", "Rusiya");
        countryMap.put("Germany", "Almaniya");
        countryMap.put("USA", "ABŞ");
        countryMap.put("Ukraine", "Ukrayna");
        countryMap.put("United Kingdom", "Böyük Britaniya");
        countryMap.put("Canada", "Kanada");
        countryMap.put("France", "Fransa");
        countryMap.put("Israel", "İsrail");
        countryMap.put("Georgia", "Gürcüstan");
        countryMap.put("Italy", "İtaliya");
        countryMap.put("Australia", "Avstraliya");
        countryMap.put("Spain", "İspaniya");
        countryMap.put("Netherlands", "Niderland");
        countryMap.put("Austria", "Avstriya");
        countryMap.put("Sweden", "İsveç");
        countryMap.put("Belgium", "Belçika");
        countryMap.put("Norway", "Norveç");
        countryMap.put("Finland", "Finlandiya");
        countryMap.put("Hungary", "Macarıstan");
        countryMap.put("Poland", "Polşa");
        countryMap.put("Greece", "Yunanıstan");
        countryMap.put("Slovakia", "Slovakiya");
        countryMap.put("Lithuania", "Litva");
        countryMap.put("Latvia", "Latviya");
        countryMap.put("Estonia", "Estoniya");
        countryMap.put("Kazakhstan", "Qazaxıstan");
        countryMap.put("UAE", "BƏƏ");
        countryMap.put("Japan", "Yaponiya");
        countryMap.put("Iran", "İran");
        countryMap.put("Saudi Arabia", "Səudiyyə Ərəbistanı");
        countryMap.put("Belarus", "Belarus");
        countryMap.put("Moldova", "Moldova");
        countryMap.put("Kyrgyzstan", "Qırğızıstan");
        countryMap.put("Tajikistan", "Tacikistan");
        countryMap.put("Turkmenistan", "Türkmənistan");
        countryMap.put("Uzbekistan", "Özbəkistan");
        countryMap.put("Malaysia", "Malayziya");
        countryMap.put("Singapore", "Sinqapur");
        countryMap.put("Brazil", "Braziliya");
        countryMap.put("Argentina", "Argentina");
        countryMap.put("Mexico", "Meksika");
        countryMap.put("Vietnam", "Vietnam");
        countryMap.put("Bali (Indonesia)", "Bali (İndoneziya)");
        countryMap.put("Switzerland", "İsveçrə");
        countryMap.put("Portugal", "Portuqaliya");
        countryMap.put("South Korea", "Cənubi Koreya");
        return countryMap.get(word);
    }


    public static String getLink(String link){
        String inEnglish =  link.toLowerCase().replaceAll("ə", "e")
                .replaceAll("ğ", "g")
                .replaceAll("ı", "i")
                .replaceAll("ç", "c")
                .replaceAll("ö", "o")
                .replaceAll("ü", "u")
                .replaceAll("q", "g")
                .replaceAll(" ", "-");
        StringBuilder suffix = new StringBuilder("-");
        Random ran = new Random();
        for(int i=0;i<15;i++){
            int index = ran.nextInt(0, ALPHANUMERICS.length());
            if(ran.nextInt(1000)%2==0){
                suffix.append(Character.toLowerCase(ALPHANUMERICS.charAt(index)));
            } else {
                suffix.append(ALPHANUMERICS.charAt(index));
            }
        }
        return inEnglish + suffix;

    }
}
