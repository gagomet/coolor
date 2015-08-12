package coolor.parcer;


import coolor.colorspaces.CMYK;
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
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Padonag on 24.09.2014.
 */
public class ParseXmlOracals {

    public ParseXmlOracals() {

    }

    public static Map<String, CMYK> getOracalsMap(File xmlSource) {
        if (xmlSource == null) {
            return new TreeMap<String, CMYK>();
        }
        Map<String, CMYK> oracals = new TreeMap<String, CMYK>();
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(xmlSource);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("oracal");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String tempKey = element.getAttribute("id");
                    double C = Double.parseDouble(element.getElementsByTagName("C").item(0).getTextContent());
                    double M = Double.parseDouble(element.getElementsByTagName("M").item(0).getTextContent());
                    double Y = Double.parseDouble(element.getElementsByTagName("Y").item(0).getTextContent());
                    double K = Double.parseDouble(element.getElementsByTagName("K").item(0).getTextContent());
                    CMYK tempValue = new CMYK(C, M, Y, K);
                    oracals.put(tempKey, tempValue);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return oracals;
    }
    public static Map<String, CMYK> getOracalsMap(InputStream xmlSource) {
        if (xmlSource == null) {
            return new TreeMap<String, CMYK>();
        }
        Map<String, CMYK> oracals = new TreeMap<String, CMYK>();
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(xmlSource);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("oracal");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String tempKey = element.getAttribute("id");
                    double C = Double.parseDouble(element.getElementsByTagName("C").item(0).getTextContent());
                    double M = Double.parseDouble(element.getElementsByTagName("M").item(0).getTextContent());
                    double Y = Double.parseDouble(element.getElementsByTagName("Y").item(0).getTextContent());
                    double K = Double.parseDouble(element.getElementsByTagName("K").item(0).getTextContent());
                    CMYK tempValue = new CMYK(C, M, Y, K);
                    oracals.put(tempKey, tempValue);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return oracals;
    }
}
