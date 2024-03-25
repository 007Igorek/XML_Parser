package ru.netology;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.plaf.synth.SynthTextAreaUI;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fileName = "data.xml";
        List<Employee> list = parseXML(fileName);

        String json = listToJson(list);
        try (FileWriter fileWriter = new FileWriter("data2.json")) {
            fileWriter.write(json);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static List<Employee> parseXML(String fileName) {
        List<Employee> list = new ArrayList<>();
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(fileName);
            NodeList employeeNodes = doc.getDocumentElement().getElementsByTagName("employee");

            for (int i = 0; i < employeeNodes.getLength(); ++i) {
                Element employeeInfo = (Element) employeeNodes.item(i);
                long id = Long.parseLong(employeeInfo.getElementsByTagName("id").item(0).getTextContent());
                String firstName = employeeInfo.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = employeeInfo.getElementsByTagName("lastName").item(0).getTextContent();
                String country = employeeInfo.getElementsByTagName("country").item(0).getTextContent();
                int age = Integer.parseInt(employeeInfo.getElementsByTagName("age").item(0).getTextContent());

                Employee emp = new Employee(id, firstName, lastName, country, age);
                list.add(emp);
            }
        }
        catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    private static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        Gson gson = new GsonBuilder().create();

        return gson.toJson(list, listType);
    }

}