package com.estafet.microservices.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class GenerateDDL {

	public static void main(String[] args) throws IOException {
		String microservice = getMicroserviceName();
		File create = new File("create-" + microservice + "-db.ddl");
		File drop = new File("drop-" + microservice + "-db.ddl");
		new ClassPathXmlApplicationContext("generate-ddl-application-context.xml").close();
		appendSemicolon(create);
		appendSemicolon(drop);
	}

	private static String getMicroserviceName() {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File("pom.xml"));
			doc.getDocumentElement().normalize();
			return doc.getElementsByTagName("microservice").item(0).getTextContent();
		} catch (DOMException | ParserConfigurationException | SAXException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void appendSemicolon(File ddl) throws IOException {
		ArrayList<String> lines = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(ddl))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				lines.add(sCurrentLine + ";");
			}
		}
		writeToFile(lines, new File(ddl.getName()));
	}

	private static void writeToFile(List<String> lines, File ddl) throws IOException {
		ddl.delete();
		for (String line : lines) {
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(ddl, true))) {
				bw.write(line);
				bw.newLine();
				bw.flush();
			}
		}
	}
}
