package org.KovD;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Aggregates.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String connectionString = "mongodb+srv://kovacsd435:------@bookdb2026.mbjbb7u.mongodb.net/";
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
                            String szKod = doc.getString("sz_kod");
                            doc.put("_id", szKod);
                            doc.remove("sz_kod");

                            if (doc.containsKey("eletkor")) {
                                String korStr = doc.getString("eletkor");
                                doc.put("eletkor", Integer.parseInt(korStr));
                            }

                            return doc;
                        })
                        .collect(Collectors.toList());

                szakacsColl.insertMany(formattedSzakacsok);

                System.out.println("--- a) Összes étterem lekérdezése ---");
                List<Document> ettermek = etteremColl.find().into(new ArrayList<>());
                for (Document doc : ettermek) {
                    System.out.println(doc.toJson());
                }

                System.out.println("\n--- b) Étterem lekérdezése (ekod: e3) ---");
                    Document e3 = etteremColl.find(eq("_id", "e3")).first();
                    if (e3 != null) {
                        System.out.println(e3.toJson());
                    }
                System.out.println("\n--- c) Szakácsok, akik idősebbek 35 évnél ---");
                    List<Document> szakacsok = szakacsColl.find(gt("eletkor", 35)).into(new ArrayList<>());
                    for (Document doc : szakacsok) {
                        System.out.println(doc.toJson());
                    }
                    
                System.out.println("\n--- d) Szakács (40 éves) és étterme (JOIN) ---");
                    List<Document> eredmeny = szakacsColl.aggregate(Arrays.asList(
                        match(eq("eletkor", 40)),
                        lookup("ettermek", "e_sz", "_id", "etterem_info")
                    )).into(new ArrayList<>());

                    for (Document doc : eredmeny) {
                        System.out.println(doc.toJson());
                    }
                
                    System.out.println("\n--- e) Szakácsok átlagos életkora ---");
                    Document atlag = szakacsColl.aggregate(Arrays.asList(
                        group(null, avg("atlagEletkor", "$eletkor"))
                    )).first();

                    if (atlag != null) {
                        System.out.println("Átlagéletkor: " + atlag.get("atlagEletkor"));
                    }

                    etteremColl.updateOne(
                        eq("_id", "e2"), 
                        new Document("$set", new Document("nev", "Új Étterem Név"))
                    );
                    System.out.println("Étterem neve frissítve.");
                    etteremColl.deleteOne(eq("_id", "e4"));
                    System.out.println("e4 kódú étterem törölve.");
                    szakacsColl.deleteMany(lt("eletkor", 35));
                    System.out.println("35 évnél fiatalabb szakácsok eltávolítva.");
                

            } catch (IOException e) {
                System.err.println("Hiba a fájl beolvasásakor: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Hiba az adatbázis művelet során: " + e.getMessage());
            }


        }


    }
}