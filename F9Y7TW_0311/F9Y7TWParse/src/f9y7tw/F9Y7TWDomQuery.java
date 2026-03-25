package f9y7tw;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class F9Y7TWDomQuery {

    public static void main(String[] args)
            throws SAXException, IOException, ParserConfigurationException {

        File xmlFile = new File("F9Y7TW_XML.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

        System.out.println("Azok a szakácsok, akiknek a végzettségeik között van szakközépiskola:\n");

        NodeList nodeList = doc.getElementsByTagName("szakacs");

        for (int i = 0; i < nodeList.getLength(); i++) {

            Node nNode = nodeList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element elem = (Element) nNode;

                for (int j = 0; j < elem.getElementsByTagName("vegzettseg").getLength(); j++) {

                    Node node3 = elem.getElementsByTagName("vegzettseg").item(j);
                    String edu1 = node3.getTextContent();

                    if ("Szakközépiskola".equals(edu1)) {

                        String id = elem.getAttribute("szkod");
                        String eid = elem.getAttribute("e_sz");

                        String work = "Ez a szakács a(z) " + eid + " azonosítójú étteremben dolgozik";

                        Node node1 = elem.getElementsByTagName("nev").item(0);
                        String name = node1.getTextContent();

                        Node node2 = elem.getElementsByTagName("reszleg").item(0);
                        String department = node2.getTextContent();

                        String edu2 = "";

                        for (int k = 0; k < elem.getElementsByTagName("vegzettseg").getLength(); k++) {

                            Node nodeTemp = elem.getElementsByTagName("vegzettseg").item(k);

                            if (k == elem.getElementsByTagName("vegzettseg").getLength() - 1) {
                                edu2 += nodeTemp.getTextContent();
                            } else {
                                edu2 += nodeTemp.getTextContent() + ", ";
                            }
                        }

                        System.out.println("Szakács ID: " + id);
                        System.out.println("Név: " + name);
                        System.out.println("Reszleg: " + department);
                        System.out.println("Végzettségek: " + edu2);
                        System.out.println(work + "\n");

                        break;
                    }
                }
            }
        }

        System.out.println("Azok az éttermek, amik 5 csillagosak:\n");

        nodeList = doc.getElementsByTagName("etterem");

        for (int i = 0; i < nodeList.getLength(); i++) {

            Node nNode = nodeList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element elem = (Element) nNode;

                Node node5 = elem.getElementsByTagName("csillag").item(0);
                String stars = node5.getTextContent();

                if ("5".equals(stars)) {

                    String id = elem.getAttribute("ekod");

                    String name = elem.getElementsByTagName("nev").item(0).getTextContent();
                    String city = elem.getElementsByTagName("varos").item(0).getTextContent();
                    String street = elem.getElementsByTagName("utca").item(0).getTextContent();
                    String number = elem.getElementsByTagName("hazszam").item(0).getTextContent();

                    String adr = city + ", " + street + " utca " + number + ".";

                    System.out.println("Étterem ID: " + id);
                    System.out.println("Név: " + name);
                    System.out.println("Cím: " + adr);
                    System.out.println("Csillagok: " + stars + "\n");
                }
            }
        }

        System.out.println("Azok a gyakornokok, akik be vannak osztva délutánra:\n");

        nodeList = doc.getElementsByTagName("gyakornok");

        for (int i = 0; i < nodeList.getLength(); i++) {

            Node nNode = nodeList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element elem = (Element) nNode;

                for (int j = 0; j < elem.getElementsByTagName("muszak").getLength(); j++) {

                    Node node4 = elem.getElementsByTagName("muszak").item(j);
                    String shift1 = node4.getTextContent();

                    if ("Délután".equals(shift1)) {

                        String id = elem.getAttribute("gykod");
                        String eid = elem.getAttribute("e_gy");

                        String work = "Ez a gyakornok a(z) " + eid + " azonosítójú étteremben dolgozik.";

                        String name = elem.getElementsByTagName("nev").item(0).getTextContent();
                        String start = elem.getElementsByTagName("kezdete").item(0).getTextContent();
                        String duration = elem.getElementsByTagName("idotartama").item(0).getTextContent();

                        String practical = "Kezdete: " + start + " Időtartama: " + duration;

                        String shift2 = "";

                        for (int k = 0; k < elem.getElementsByTagName("muszak").getLength(); k++) {

                            Node nodeTemp = elem.getElementsByTagName("muszak").item(k);

                            if (k == elem.getElementsByTagName("muszak").getLength() - 1) {
                                shift2 += nodeTemp.getTextContent();
                            } else {
                                shift2 += nodeTemp.getTextContent() + ", ";
                            }
                        }

                        System.out.println("Gyakornok ID: " + id);
                        System.out.println("Név: " + name);
                        System.out.println("Gyakorlat: " + practical);
                        System.out.println("Műszak: " + shift2);
                        System.out.println(work + "\n");

                        break;
                    }
                }
            }
        }
    }
}
