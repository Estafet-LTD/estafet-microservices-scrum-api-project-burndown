package com.estafet.microservices.api.project.burndown.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GenerateProjectBurndownSchema {

	public static void main(String[] args) throws IOException {
		File create = new File("target", "create-project-burndown-db.ddl");
		File drop = new File("target", "drop-project-burndown-db.ddl");
		create.delete();
		drop.delete();
		new ClassPathXmlApplicationContext("project-burndown-entity-application-context.xml").close();
		appendSemicolon(create);
		appendSemicolon(drop);
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
