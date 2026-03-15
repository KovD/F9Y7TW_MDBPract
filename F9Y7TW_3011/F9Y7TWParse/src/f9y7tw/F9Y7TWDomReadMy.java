package f9y7tw;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class F9Y7TWDomReadMy {
    public static void main(String[] args)
            throws SAXException, IOException, ParserConfigurationException {

        File xmlFile = new File("F9Y7TW_XML1.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

        NodeList nList = doc.getElementsByTagName("luxusbar");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("\nCurrent Element: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;
                String id = elem.getAttribute("BarID");
                String nev = elem.getElementsByTagName("nev").item(0).getTextContent();
                String varos = elem.getElementsByTagName("varos").item(0).getTextContent();

                System.out.println("\n--- Luxusbárkok ---");
                System.out.println("ID: " + id);
                System.out.println("Név: " + nev);
                System.out.println("Város: " + varos);
            }
        }

        NodeList patronList = doc.getElementsByTagName("patron");

        for (int i = 0; i < patronList.getLength(); i++) {
            Node node = patronList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                String id = elem.getAttribute("PatID");
                String email = elem.getElementsByTagName("patreon_email_cim").item(0).getTextContent();

                Element nevElem = (Element) elem.getElementsByTagName("nev").item(0);
                String vnev = nevElem.getElementsByTagName("vezeteknev").item(0).getTextContent();
                String knev = nevElem.getElementsByTagName("keresztnev").item(0).getTextContent();

                System.out.println("\n--- Patrónus ---");
                System.out.println("ID: " + id);
                System.out.println("Név: " + vnev + " " + knev);
                System.out.println("Email: " + email);
            }
        }

        NodeList dolgozoList = doc.getElementsByTagName("dolgozo");

        for (int i = 0; i < dolgozoList.getLength(); i++) {
            Node node = dolgozoList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                String id = elem.getAttribute("SzemID");
                String kor = elem.getElementsByTagName("kor").item(0).getTextContent();

                Element nevElem = (Element) elem.getElementsByTagName("nev").item(0);
                String vnev = nevElem.getElementsByTagName("vezeteknev").item(0).getTextContent();
                String knev = nevElem.getElementsByTagName("keresztnev").item(0).getTextContent();

                System.out.println("\n--- Dolgozó ---");
                System.out.println("ID: " + id);
                System.out.println("Név: " + vnev + " " + knev);
                System.out.println("Kor: " + kor);
            }
        }

        NodeList cegList = doc.getElementsByTagName("besszallitoceg");

        for (int i = 0; i < cegList.getLength(); i++) {
            Node node = cegList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                String id = elem.getAttribute("SzallID");
                String nev = elem.getElementsByTagName("nev").item(0).getTextContent();
                String telephely = elem.getElementsByTagName("telephely").item(0).getTextContent();

                System.out.println("\n--- Beszállító cég ---");
                System.out.println("ID: " + id);
                System.out.println("Név: " + nev);
                System.out.println("Telephely: " + telephely);
            }
        }

        NodeList termekList = doc.getElementsByTagName("termek");

        for (int i = 0; i < termekList.getLength(); i++) {
            Node node = termekList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                String id = elem.getAttribute("TermID");
                String szallId = elem.getAttribute("SzallID");
                String nev = elem.getElementsByTagName("termeknev").item(0).getTextContent();
                String ar = elem.getElementsByTagName("Ar_db").item(0).getTextContent();

                System.out.println("\n--- Termék ---");
                System.out.println("ID: " + id + " (Beszállító: " + szallId + ")");
                System.out.println("Név: " + nev);
                System.out.println("Ár/db: " + ar + " Ft");
            }
        }
    }
}