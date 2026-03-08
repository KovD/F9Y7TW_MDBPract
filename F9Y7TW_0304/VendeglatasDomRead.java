package app.konrads.vendeglatas;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

@SuppressWarnings("ALL")
public class VendeglatasDomRead {
    public static void main(String[] args) {
        var filePath = "./F9Y7TW_XML1";
        var file = new File(filePath);

        Document document = null;
        try {
            document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(file);
        } catch (Exception e) {
            System.err.println("Hiba az XML olvasásakor: " + e.getMessage());
            return;
        }
        document.normalize();

        var root = document.getDocumentElement();
        if (root == null) {
            System.err.println("Hiba: Nincs gyökérelem.");
            return;
        }
        System.out.println("Gyökérelem: " + root.getTagName() + "\n");

        readLuxusbar(root);
        readPatron(root);
        readDolgozo(root);
        readBeszallitoceg(root);
        readTermek(root);
        readKezbesit(root);
        readBejelentve(root);
        readDolgozik(root);
    }

    public static void readLuxusbar(Element element) {
        var barok = element.getElementsByTagName("luxusbar");
        for (int i = 0; i < barok.getLength(); i++) {
            var bar = (Element) barok.item(i);
            System.out.println("--- Luxusbár ---");
            System.out.println("BarID: " + bar.getAttribute("BarID"));
            System.out.println("Név: " + bar.getElementsByTagName("nev").item(0).getTextContent());
            System.out.println("Város: " + bar.getElementsByTagName("varos").item(0).getTextContent());
            System.out.println();
        }
    }

    public static void readPatron(Element element) {
        var patronok = element.getElementsByTagName("patron");
        for (int i = 0; i < patronok.getLength(); i++) {
            var patron = (Element) patronok.item(i);
            System.out.println("--- Patrónus ---");
            System.out.println("PatID: " + patron.getAttribute("PatID"));
            System.out.println("Ig. szám: " + patron.getElementsByTagName("ig.szam").item(0).getTextContent());
            System.out.println("Szül. idő: " + patron.getElementsByTagName("szulido").item(0).getTextContent());
            System.out.println("Lakcím: " + patron.getElementsByTagName("lakcim").item(0).getTextContent());
            System.out.println("Email: " + patron.getElementsByTagName("patreon_email_cim").item(0).getTextContent());
            System.out.println("Kor: " + patron.getElementsByTagName("kor").item(0).getTextContent());

            var nevNode = (Element) patron.getElementsByTagName("nev").item(0);
            var tit = nevNode.getElementsByTagName("titulus").item(0).getTextContent();
            var ker = nevNode.getElementsByTagName("keresztnev").item(0).getTextContent();
            var vez = nevNode.getElementsByTagName("vezeteknev").item(0).getTextContent();
            System.out.println("Név: " + tit + " " + vez + " " + ker);
            System.out.println();
        }
    }

    public static void readDolgozo(Element element) {
        var dolgozok = element.getElementsByTagName("dolgozo");
        for (int i = 0; i < dolgozok.getLength(); i++) {
            var dolgozo = (Element) dolgozok.item(i);
            System.out.println("--- Dolgozó ---");
            System.out.println("SzemID: " + dolgozo.getAttribute("SzemID"));
            System.out.println("Ig. szám: " + dolgozo.getElementsByTagName("ig.szam").item(0).getTextContent());
            System.out.println("Szül. idő: " + dolgozo.getElementsByTagName("szulido").item(0).getTextContent());
            System.out.println("Lakcím: " + dolgozo.getElementsByTagName("lakcim").item(0).getTextContent());
            System.out.println("Email: " + dolgozo.getElementsByTagName("dolgozo_email_cim").item(0).getTextContent());
            System.out.println("Kor: " + dolgozo.getElementsByTagName("kor").item(0).getTextContent());

            var nevNode = (Element) dolgozo.getElementsByTagName("nev").item(0);
            var ker = nevNode.getElementsByTagName("keresztnev").item(0).getTextContent();
            var vez = nevNode.getElementsByTagName("vezeteknev").item(0).getTextContent();
            System.out.println("Név: " + vez + " " + ker);
            System.out.println();
        }
    }

    public static void readBeszallitoceg(Element element) {
        var cegek = element.getElementsByTagName("besszallitoceg");
        for (int i = 0; i < cegek.getLength(); i++) {
            var ceg = (Element) cegek.item(i);
            System.out.println("--- Beszállítócég ---");
            System.out.println("SzallID: " + ceg.getAttribute("SzallID"));
            System.out.println("Név: " + ceg.getElementsByTagName("nev").item(0).getTextContent());
            System.out.println("Telephely: " + ceg.getElementsByTagName("telephely").item(0).getTextContent());
            System.out.println();
        }
    }

    public static void readTermek(Element element) {
        var termekek = element.getElementsByTagName("termek");
        for (int i = 0; i < termekek.getLength(); i++) {
            var termek = (Element) termekek.item(i);
            System.out.println("--- Termék ---");
            System.out.println("TermID: " + termek.getAttribute("TermID"));
            System.out.println("SzallID: " + termek.getAttribute("SzallID"));
            System.out.println("Terméknév: " + termek.getElementsByTagName("termeknev").item(0).getTextContent());
            System.out.println("Ár/db: " + termek.getElementsByTagName("Ar_db").item(0).getTextContent());
            System.out.println("Allergén: " + termek.getElementsByTagName("allergen").item(0).getTextContent());
            System.out.println("Kezdőár: " + termek.getElementsByTagName("kezdoar").item(0).getTextContent());
            System.out.println("Mennyiség: " + termek.getElementsByTagName("mennyiseg").item(0).getTextContent());
            System.out.println();
        }
    }

    public static void readKezbesit(Element element) {
        var kezbesitesek = element.getElementsByTagName("kezbesit");
        for (int i = 0; i < kezbesitesek.getLength(); i++) {
            var kezbesit = (Element) kezbesitesek.item(i);
            System.out.println("--- Kézbesít ---");
            System.out.println("b_K: " + kezbesit.getAttribute("b_K"));
            System.out.println("k_b: " + kezbesit.getAttribute("k_b"));
            System.out.println("Dátum: " + kezbesit.getElementsByTagName("datum").item(0).getTextContent());
            System.out.println();
        }
    }

    public static void readBejelentve(Element element) {
        var bejelentesek = element.getElementsByTagName("bejelentve");
        for (int i = 0; i < bejelentesek.getLength(); i++) {
            var bejelentve = (Element) bejelentesek.item(i);
            System.out.println("--- Bejelentve kapcsolótábla ---");
            System.out.println("b_k: " + bejelentve.getAttribute("b_k"));
            System.out.println("b_p: " + bejelentve.getAttribute("b_p"));
            System.out.println();
        }
    }

    public static void readDolgozik(Element element) {
        var dolgoznak = element.getElementsByTagName("dolgozik");
        for (int i = 0; i < dolgoznak.getLength(); i++) {
            var dolgozik = (Element) dolgoznak.item(i);
            System.out.println("--- Dolgozik kapcsolótábla ---");
            System.out.println("b_k: " + dolgozik.getAttribute("b_k"));
            System.out.println("b_d: " + dolgozik.getAttribute("b_d"));
            System.out.println();
        }
    }
}