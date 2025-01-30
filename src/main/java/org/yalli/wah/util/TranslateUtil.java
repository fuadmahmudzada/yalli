package org.yalli.wah.util;

import java.util.HashMap;
import java.util.Map;

public class TranslateUtil {


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
}
