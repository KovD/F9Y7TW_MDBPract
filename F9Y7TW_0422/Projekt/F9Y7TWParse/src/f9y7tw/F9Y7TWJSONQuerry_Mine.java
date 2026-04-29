package f9y7tw;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.util.Set;

public class JSON1Query {

    public static void main(String[] args) throws Exception {

        ObjectMapper m = new ObjectMapper();
        JsonNode root = m.readTree(new File("JSON1.json"));
        JsonNode schemaNode = m.readTree(new File("JSON1_SCHEMA.json"));

        JsonSchema schema = JsonSchemaFactory
                .getInstance(SpecVersion.VersionFlag.V4)
                .getSchema(schemaNode);

        Set<ValidationMessage> errors = schema.validate(root);

        if (errors.isEmpty()) {
            System.out.println("Valid JSON\n");
        } else {
            System.out.println("Hibás JSON:");
            errors.forEach(e -> System.out.println(e.getMessage()));
        }


        JsonNode vendeglatas = root.get("vendeglatas");
        JsonNode luxusbarok = vendeglatas.get("luxusbar");
        JsonNode patronok = vendeglatas.get("patron");
        JsonNode dolgozok = vendeglatas.get("dolgozo");
        JsonNode beszallitok = vendeglatas.get("besszallitoceg");
        JsonNode termekek = vendeglatas.get("termek");
        JsonNode dolgozik = vendeglatas.get("dolgozik");

        System.out.println("=== 1. Patronok (Vendégek) adatai ===");
        for (JsonNode p : patronok) {
            String vNev = p.get("nev").get("vezeteknev").asText();
            String kNev = p.get("nev").get("keresztnev").asText();
            String kor = p.get("kor").asText();
            String email = p.get("patreon_email_cim").asText();
            
            System.out.println("Név: " + vNev + " " + kNev + " | Kor: " + kor + " | E-mail: " + email);
        }

        System.out.println("\n=== 2. BÁROK ÉS DOLGOZÓIK ===");
        for (JsonNode bar : luxusbarok) {
            String bKod = bar.get("_BarID").asText();
            String bNev = bar.get("nev").asText();

            System.out.println("\nBár: " + bNev + " [" + bKod + "]");
            System.out.println("---------------------------");

            for (JsonNode dKapcsolat : dolgozik) {
                String kapcsBarId = dKapcsolat.get("_b_k").asText();
                // A JSON-ben b1 helyett 1b szerepel a kapcsolótáblában, ezt itt áthidaljuk
                if (kapcsBarId.equals(bKod) || (bKod.equals("b1") && kapcsBarId.equals("1b"))) {
                    String dId = dKapcsolat.get("_b_d").asText();
                    
                    for (JsonNode d : dolgozok) {
                        if (d.get("_SzemID").asText().equals(dId)) {
                            System.out.println("- " + d.get("nev").get("vezeteknev").asText() + " " + d.get("nev").get("keresztnev").asText());
                        }
                    }
                }
            }
        }

        System.out.println("\n=== 3. Átlagos termék ár ===");
        double osszeg = 0;
        int db = 0;
        for (JsonNode t : termekek) {
            osszeg += Double.parseDouble(t.get("Ar_db").asText());
            db++;
        }
        System.out.println("AVG: " + (osszeg / db) + " Ft");

        System.out.println("\n=== 4. Feladat: Budapesti luxusbárok ===");
        for (JsonNode b : luxusbarok) {
            if (b.get("varos").asText().equals("Budapest")) {
                System.out.println("- " + b.get("nev").asText());
            }
        }

        System.out.println("\n=== 5. Feladat: Melyik cég mit szállít? (JOIN) ===");
        for (JsonNode t : termekek) {
            String szKod = t.get("_SzallID").asText();
            String tNev = t.get("termeknev").asText();

            String szNev = "";
            for(JsonNode sz : beszallitok) {
                if(sz.get("_SzallID").asText().equals(szKod)) szNev = sz.get("nev").asText();
            }

            System.out.println(szNev + " szállítja: " + tNev);
        }

        System.out.println("\n=== 6. Feladat: JSON adatok manipulációja ===");
        for (JsonNode t : termekek) {
            ObjectNode obj = (ObjectNode) t;
            obj.put("elerheto", true);
            obj.remove("kezdoar");
        }
        System.out.println("JSON fa módosítva (elerheto hozzáadva, kezdoar törölve).");

        System.out.println("\n=== 7. Feladat: VIP Termék (Legdrágább) ===");
        String maxTermekNev = "";
        double maxAr = 0;
        
        for (JsonNode t : termekek) {
            double aktualisAr = Double.parseDouble(t.get("Ar_db").asText());
            if (aktualisAr > maxAr) {
                maxAr = aktualisAr;
                maxTermekNev = t.get("termeknev").asText();
            }
        }
        System.out.println("Legdrágább termék: " + maxTermekNev + " (" + maxAr + " Ft)");

        System.out.println("\n=== 8. Feladat: Új JSON fájl készítése és mentése ===");
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode ujLista = mapper.createArrayNode();

        for (JsonNode sz : beszallitok) {
            String szKod = sz.get("_SzallID").asText();
            ArrayNode beszallitottTermekek = mapper.createArrayNode();
            
            for (JsonNode t : termekek) {
                if (t.get("_SzallID").asText().equals(szKod)) {
                    beszallitottTermekek.add(t.get("termeknev").asText());
                }
            }
            
            ObjectNode csomopont = mapper.createObjectNode();
            csomopont.put("beszallito_nev", sz.get("nev").asText());
            csomopont.set("termekek", beszallitottTermekek);
            ujLista.add(csomopont);
        }

        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("uj_beszallitok.json"), ujLista);
        System.out.println("Fájl kiírva: uj_beszallitok.json");
    }
}