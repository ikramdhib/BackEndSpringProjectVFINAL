package tn.esprit.pidev.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class LinkedInScraper {

    public static List<String> scrapeDescriptions(String linkedinProfileUrl) {
        try {
            List<String> descriptions = new ArrayList<>();
            Document doc = Jsoup.connect(linkedinProfileUrl).get();
            Elements descriptionElements = doc.select("div.show-more-less-html__markup");
            for (Element element : descriptionElements) {
                String description = element.text().trim();
                descriptions.add(description);
            }

            System.out.println("Descriptions extraites : ");
            for (String desc : descriptions) {
                System.out.println(desc);
            }

            return descriptions;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}