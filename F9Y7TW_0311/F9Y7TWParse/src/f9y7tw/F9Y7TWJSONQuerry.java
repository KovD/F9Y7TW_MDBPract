package f9y7tw;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

public class F9Y7TWJSONQuerry {
    public static void main(String[] args) throws Exception {
        ObjectMapper m = new ObjectMapper();

        JsonNode root = m.readTree(new File("JSON.json"));

        JsonNode vendeglatas = root.get("vendeglatas");
        JsonNode foszakacsok = vendeglatas.get("foszakacs");
        JsonNode szakacsok = vendeglatas.get("szakacs");
        JsonNode ettermek = vendeglatas.get("etterem");

        System.out.println("--- Főszakácsok adatai ---");

        if (foszakacsok != null && foszakacsok.isArray()) {
            for (JsonNode szakacs : foszakacsok) {
                String nev = szakacs.get("nev").asText();
                String kor = szakacs.get("eletkor").asText();
                String suli = szakacs.get("vegzettseg").asText();

                System.out.println("Név: " + nev + " | Kor: " + kor + " | Végzettség: " + suli);
            }
        }

        System.out.println("=== ÉTTERMEK ÉS SZAKÁCSAIK ===");

        for (JsonNode etterem : ettermek) {
            String eKod = etterem.get("_ekod").asText();
            String eNev = etterem.get("nev").asText();

            System.out.println("\nÉtterem: " + eNev + " [" + eKod + "]");
            System.out.println("---------------------------");

            for (JsonNode szakacs : szakacsok) {
                if (szakacs.get("_e_sz").asText().equals(eKod)) {
                    System.out.println("- " + szakacs.get("nev").asText() + " (" + szakacs.get("reszleg").asText() + ")");
                }
            }
        }
    }
}