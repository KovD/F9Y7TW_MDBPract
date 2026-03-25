package f9y7tw;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class F9Y7TWDomRead {
    public static void main(String[] args)
            throws SAXException, IOException, ParserConfigurationException {

        File xmlFile = new File("F9Y7TW_XML.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

        NodeList nList = doc.getElementsByTagName("etterem");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("\nCurrent Element: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String id = elem.getAttribute("ekod");

                Node node1 = elem.getElementsByTagName("nev").item(0);
                String name = node1.getTextContent();

                Node node2 = elem.getElementsByTagName("varos").item(0);
                String city = node2.getTextContent();

                Node node3 = elem.getElementsByTagName("utca").item(0);
                String street = node3.getTextContent();

                Node node4 = elem.getElementsByTagName("hazszam").item(0);
                String number = node4.getTextContent();

                Node node5 = elem.getElementsByTagName("csillag").item(0);
                String stars = node5.getTextContent();

                String adr = city + ", " + street + " utca " + number + ".";

                System.out.println("Étterem ID: " + id);
                System.out.println("Név: " + name);
                System.out.println("Cím: " + adr);
                System.out.println("Csillag: " + stars);
            }
        }

        NodeList foszakacsList = doc.getElementsByTagName("foszakacs");

        for (int i = 0; i < foszakacsList.getLength(); i++) {
            Node node = foszakacsList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;

                String fkod = elem.getAttribute("fkod");
                String etteremKod = elem.getAttribute("e_f");

                String nev = elem.getElementsByTagName("nev").item(0).getTextContent();
                String eletkor = elem.getElementsByTagName("eletkor").item(0).getTextContent();
                String vegzettseg = elem.getElementsByTagName("vegzettseg").item(0).getTextContent();

                System.out.println("\nFőszakács ID: " + fkod);
                System.out.println("Étterem kód: " + etteremKod);
                System.out.println("Név: " + nev);
                System.out.println("Életkor: " + eletkor);
                System.out.println("Végzettség: " + vegzettseg);
            }
        }

        NodeList szakacsList = doc.getElementsByTagName("szakacs");

        for (int i = 0; i < szakacsList.getLength(); i++) {
            Node node = szakacsList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;

                String szkod = elem.getAttribute("szkod");
                String etteremKod = elem.getAttribute("e_sz");

                String nev = elem.getElementsByTagName("nev").item(0).getTextContent();
                String reszleg = elem.getElementsByTagName("reszleg").item(0).getTextContent();

                System.out.println("\nSzakács ID: " + szkod);
                System.out.println("Étterem kód: " + etteremKod);
                System.out.println("Név: " + nev);
                System.out.println("Részleg: " + reszleg);
                System.out.println("Végzettségek:");

                NodeList vegzettsegek = elem.getElementsByTagName("vegzettseg");
                for (int j = 0; j < vegzettsegek.getLength(); j++) {
                    System.out.println(" - " + vegzettsegek.item(j).getTextContent());
                }
            }
        }

        NodeList gyakornokList = doc.getElementsByTagName("gyakornok");

        for (int i = 0; i < gyakornokList.getLength(); i++) {
            Node node = gyakornokList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;

                String gykod = elem.getAttribute("gykod");
                String etteremKod = elem.getAttribute("e_gy");

                String nev = elem.getElementsByTagName("nev").item(0).getTextContent();

                Element gyakorlat = (Element) elem.getElementsByTagName("gyakorlat").item(0);
                String kezdete = gyakorlat.getElementsByTagName("kezdete").item(0).getTextContent();
                String idotartam = gyakorlat.getElementsByTagName("idotartama").item(0).getTextContent();

                System.out.println("\nGyakornok ID: " + gykod);
                System.out.println("Étterem kód: " + etteremKod);
                System.out.println("Név: " + nev);
                System.out.println("Gyakorlat kezdete: " + kezdete);
                System.out.println("Időtartam: " + idotartam);
                System.out.println("Műszakok:");

                NodeList muszakok = elem.getElementsByTagName("muszak");
                for (int j = 0; j < muszakok.getLength(); j++) {
                    System.out.println(" - " + muszakok.item(j).getTextContent());
                }
            }
        }

        NodeList vendegList = doc.getElementsByTagName("vendeg");

        for (int i = 0; i < vendegList.getLength(); i++) {
            Node node = vendegList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;

                String vkod = elem.getAttribute("vkod");
                String nev = elem.getElementsByTagName("nev").item(0).getTextContent();
                String eletkor = elem.getElementsByTagName("eletkor").item(0).getTextContent();

                String varos = elem.getElementsByTagName("varos").item(0).getTextContent();
                String utca = elem.getElementsByTagName("utca").item(0).getTextContent();
                String hazszam = elem.getElementsByTagName("hazszam").item(0).getTextContent();

                System.out.println("\nVendég ID: " + vkod);
                System.out.println("Név: " + nev);
                System.out.println("Életkor: " + eletkor);
                System.out.println("Cím: " + varos + ", " + utca + " utca " + hazszam + ".");
            }
        }

        NodeList rendelesList = doc.getElementsByTagName("rendeles");

        for (int i = 0; i < rendelesList.getLength(); i++) {
            Node node = rendelesList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;

                String rkod = elem.getAttribute("rkod");
                String vkod = elem.getAttribute("vkod");
                String ekod = elem.getAttribute("ekod");

                String etel = elem.getElementsByTagName("etel").item(0).getTextContent();
                String osszeg = elem.getElementsByTagName("osszeg").item(0).getTextContent();
                String datum = elem.getElementsByTagName("datum").item(0).getTextContent();

                System.out.println("\nRendelés ID: " + rkod);
                System.out.println("Vendég kód: " + vkod);
                System.out.println("Étterem kód: " + ekod);
                System.out.println("Étel: " + etel);
                System.out.println("Összeg: " + osszeg + " Ft");
                System.out.println("Dátum: " + datum);
            }
        }
    }
}