package com.helloworld.callshop.engine;


import com.helloworld.callshop.rater.rate.factory.RateFactoriesConfigReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XMLConfigReader implements RateFactoriesConfigReader {

    private static final String RATE_FACTORIES_TAG = "RateFactories";
    private static final String FACTORY_NAME_TAG = "name";

    private Document document;

    public void readConfiguration(String fileName) throws IOException, ParserConfigurationException, SAXException {
        ClassLoader classLoader = XMLConfigReader.class.getClassLoader();
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();


        try (InputStream in = classLoader.getResourceAsStream(fileName)) {
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            document = docBuilder.parse(in);
        }
    }
    @Override
    public List<String> readRateFactoryNames() {
        NodeList factoriesNodes = document.getElementsByTagName(RATE_FACTORIES_TAG);

        if (factoriesNodes.getLength() <= 0) {
            return Collections.emptyList();
        }

        //Si hay varios elementos con el tag RATE_FACTORIES nos quedamos con el primero
        Node rateFactoriesNode = factoriesNodes.item(0);

        NodeList rateFactoriesChildren = rateFactoriesNode.getChildNodes();

        List<String> factoryNameList = new ArrayList<>();

        for (int  i = 0; i< rateFactoriesChildren.getLength(); ++i){
            Node node = rateFactoriesChildren.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)node;
                if (element.getTagName().equals(FACTORY_NAME_TAG)) {
                    factoryNameList.add(element.getTextContent());
                }
            }

        }

        return factoryNameList;

    }
}
