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
import java.util.List;
import java.util.stream.Collectors;

public class Main_mine {
    public static void main(String[] args) {
        String connectionString = "mongodb+srv://kovacsd435:---------@bookdb2026.mbjbb7u.mongodb.net/";
        
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase("vendeglatas");
            
            // Kollekciók ürítése
            database.getCollection("luxusbar").drop();
            database.getCollection("termek").drop();
            database.getCollection("dolgozo").drop();
            
            MongoCollection<Document> barColl = database.getCollection("luxusbar");
            MongoCollection<Document> termekColl = database.getCollection("termek");
            MongoCollection<Document> dolgozoColl = database.getCollection("dolgozo");

            try {
                // 1. Luxusbárok beolvasása és formázása
                String barRaw = Files.readString(Paths.get("luxusbar.json"));
                List<Document> barList = Document.parse(barRaw).getList("TempList", Document.class);
                if (barList != null && !barList.isEmpty()) {
                    List<Document> formattedBar = barList.stream().map(doc -> {
                        if (doc.containsKey("_BarID")) {
                            doc.put("_id", doc.getString("_BarID"));
                            doc.remove("_BarID");
                        }
                        return doc;
                    }).collect(Collectors.toList());
                    barColl.insertMany(formattedBar);
                }

                // 2. Termékek beolvasása (Ár konvertálása Integer-be)
                String termekRaw = Files.readString(Paths.get("termek.json"));
                List<Document> termekList = Document.parse(termekRaw).getList("TempList", Document.class);
                if (termekList != null && !termekList.isEmpty()) {
                    List<Document> formattedTermek = termekList.stream().map(doc -> {
                        if (doc.containsKey("_TermID")) {
                            doc.put("_id", doc.getString("_TermID"));
                            doc.remove("_TermID");
                        }
                        if (doc.containsKey("Ar_db") && doc.get("Ar_db") instanceof String) {
                            doc.put("Ar_db", Integer.parseInt(doc.getString("Ar_db")));
                        }
                        return doc;
                    }).collect(Collectors.toList());
                    termekColl.insertMany(formattedTermek);
                }

                // 3. Dolgozók beolvasása (Kor konvertálása Integer-be)
                String dolgozoRaw = Files.readString(Paths.get("dolgozo.json"));
                List<Document> dolgozoList = Document.parse(dolgozoRaw).getList("TempList", Document.class);
                if (dolgozoList != null && !dolgozoList.isEmpty()) {
                    List<Document> formattedDolgozo = dolgozoList.stream().map(doc -> {
                        if (doc.containsKey("_SzemID")) {
                            doc.put("_id", doc.getString("_SzemID"));
                            doc.remove("_SzemID");
                        }
                        if (doc.containsKey("kor") && doc.get("kor") instanceof String) {
                            doc.put("kor", Integer.parseInt(doc.getString("kor")));
                        }
                        return doc;
                    }).collect(Collectors.toList());
                    dolgozoColl.insertMany(formattedDolgozo);
                }

                System.out.println("Sikeres importálás!\n");

                // --- LEKÉRDEZÉSEK ---
                
                System.out.println("--- a) Összes luxusbár ---");
                List<Document> barok = barColl.find().into(new ArrayList<>());
                for (Document doc : barok) {
                    System.out.println(doc.toJson());
                }

                System.out.println("\n--- b) Termék lekérdezése (ID: t1) ---");
                Document t1 = termekColl.find(eq("_id", "t1")).first();
                if (t1 != null) {
                    System.out.println(t1.toJson());
                }

                System.out.println("\n--- c) Dolgozók, akik idősebbek 30 évnél ---");
                List<Document> idosDolgozok = dolgozoColl.find(gt("kor", 30)).into(new ArrayList<>());
                for (Document doc : idosDolgozok) {
                    System.out.println(doc.toJson());
                }

                System.out.println("\n--- d) Bárok száma városonként (Csoportosítás) ---");
                List<Document> varosStat = barColl.aggregate(Arrays.asList(
                    group("$varos", sum("barokSzama", 1))
                )).into(new ArrayList<>());
                for (Document doc : varosStat) {
                    System.out.println(doc.toJson());
                }

                System.out.println("\n--- e) Termékek átlagára ---");
                Document atlagAr = termekColl.aggregate(Arrays.asList(
                    group(null, avg("atlagAr", "$Ar_db"))
                )).first();
                if (atlagAr != null) {
                    System.out.println("Átlagár: " + atlagAr.get("atlagAr") + " Ft");
                }

                // --- MÓDOSÍTÁSOK ---
                
                termekColl.updateOne(
                    eq("_id", "t2"), 
                    new Document("$set", new Document("termeknev", "Prémium Kézműves Gin"))
                );
                System.out.println("\nt2 kódú termék neve frissítve.");

                dolgozoColl.deleteOne(eq("_id", "d3"));
                System.out.println("d3 kódú dolgozó törölve.");

                dolgozoColl.deleteMany(lt("kor", 25));
                System.out.println("25 évnél fiatalabb dolgozók eltávolítva.");

            } catch (IOException e) {
                System.err.println("Hiba a fájl beolvasásakor: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Hiba az adatbázis művelet során: " + e.getMessage());
            }
        }
    }
}