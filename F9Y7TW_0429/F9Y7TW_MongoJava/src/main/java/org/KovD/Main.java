package org.KovD;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Arrays;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String connectionString = "mongodb+srv://kovacsd435:db12@bookdb2026.mbjbb7u.mongodb.net/";
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {

            MongoDatabase database = mongoClient.getDatabase("vendeglatas");
            MongoCollection<Document> collection = database.getCollection("ettermek");
            try {
                database.getCollection("ettermek").drop();
                database.getCollection("szakacsok").drop();
                MongoCollection<Document> etteremColl = database.getCollection("ettermek");
                String ettermekRaw = Files.readString(Paths.get("ettermek.json"));

                List<Document> ettermekList = Document.parse(ettermekRaw).getList("TempList", Document.class);
                if (ettermekList != null && !ettermekList.isEmpty()) {
                    etteremColl.insertMany(ettermekList);
                }

                // 2. Szakácsok beolvasása és mentése
                MongoCollection<Document> szakacsColl = database.getCollection("szakacsok");
                String szakacsokRaw = Files.readString(Paths.get("szakacsok.json"));

                List<Document> szakacsokList = Document.parse(szakacsokRaw).getList("TempList", Document.class);
                if (szakacsokList != null && !szakacsokList.isEmpty()) {
                    szakacsColl.insertMany(szakacsokList);
                }

                System.out.println("Sikeres importálás!");
//-----------------------------------------------3.----------------------------------------------------
                List<Document> formattedEtteremList = ettermekList.stream()
                        .map(doc -> {
                            String ekod = doc.getString("ekod");
                            doc.put("_id", ekod);
                            doc.remove("ekod");
                            return doc;
                        })
                        .collect(Collectors.toList());

                etteremColl.insertMany(formattedEtteremList);

                List<Document> formattedSzakacsok = szakacsokList.stream()
                        .map(doc -> {
                            // Az azonosító átnevezése
                            String szKod = doc.getString("sz_kod");
                            doc.put("_id", szKod);
                            doc.remove("sz_kod");

                            // Életkor konvertálása String-ből Integer-be, ha létezik a mező
                            if (doc.containsKey("eletkor")) {
                                String korStr = doc.getString("eletkor");
                                doc.put("eletkor", Integer.parseInt(korStr));
                            }

                            return doc;
                        })
                        .collect(Collectors.toList());

                szakacsColl.insertMany(formattedSzakacsok);

            } catch (IOException e) {
                System.err.println("Hiba a fájl beolvasásakor: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Hiba az adatbázis művelet során: " + e.getMessage());
            }


        }


    }
}