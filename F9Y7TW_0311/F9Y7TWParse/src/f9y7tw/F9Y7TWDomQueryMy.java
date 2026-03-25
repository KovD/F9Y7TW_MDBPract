package f9y7tw;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class F9Y7TWDomQueryMy {

    public static void main(String[] args) throws Exception {

        File xmlFile = new File("F9Y7TW_XML1.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        // --- 1. Lekérdezés: Budapesti luxusbárok ---
        System.out.println("1. Budapesti luxusbárok:\n");
        NodeList barList = doc.getElementsByTagName("luxusbar");

        for (int i = 0; i < barList.getLength(); i++) {
            Node node = barList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                String varos = elem.getElementsByTagName("varos").item(0).getTextContent();

                if ("Budapest".equals(varos)) {
                    String id = elem.getAttribute("BarID");
                    String nev = elem.getElementsByTagName("nev").item(0).getTextContent();
                    System.out.println("ID: " + id + " | Név: " + nev + " | Város: " + varos);
                }
            }
        }

        // --- 2. Lekérdezés: Allergénmentes ("Nincs") termékek ---
        System.out.println("\n2. Allergénmentes termékek:\n");
        NodeList termekList = doc.getElementsByTagName("termek");

        for (int i = 0; i < termekList.getLength(); i++) {
            Node node = termekList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                String allergen = elem.getElementsByTagName("allergen").item(0).getTextContent();

                if ("Nincs".equals(allergen)) {
                    String id = elem.getAttribute("TermID");
                    String nev = elem.getElementsByTagName("termeknev").item(0).getTextContent();
                    String ar = elem.getElementsByTagName("Ar_db").item(0).getTextContent();
                    System.out.println("ID: " + id + " | Termék: " + nev + " | Ár: " + ar + " Ft");
                }
            }
        }

        // --- 3. Lekérdezés: 35 évnél fiatalabb dolgozók ---
        System.out.println("\n3. 35 évnél fiatalabb dolgozók:\n");
        NodeList dolgozoList = doc.getElementsByTagName("dolgozo");

        for (int i = 0; i < dolgozoList.getLength(); i++) {
            Node node = dolgozoList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                int kor = Integer.parseInt(elem.getElementsByTagName("kor").item(0).getTextContent());

                if (kor < 35) {
                    String id = elem.getAttribute("SzemID");
                    Element nevElem = (Element) elem.getElementsByTagName("nev").item(0);
                    String nev = nevElem.getElementsByTagName("vezeteknev").item(0).getTextContent() + " " +
                            nevElem.getElementsByTagName("keresztnev").item(0).getTextContent();

                    System.out.println("ID: " + id + " | Név: " + nev + " | Kor: " + kor + " év");
                }
            }
        }
    }
}