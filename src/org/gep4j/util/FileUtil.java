package org.gep4j.util;

import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class FileUtil {

	public static TableModel loadToTableModel(String fileName, Object[] columns) {
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		
		FileReader reader = null;
		try {
			reader = new FileReader(fileName);
			BufferedReader buf = new BufferedReader(reader);
			String line;
			line = buf.readLine();
			while (line != null && line.trim().length() > 0) {
				String data[] = line.split(",");
				model.addRow(data);
				line = buf.readLine();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				reader.close();
			} catch (Exception e) {} 
		}
		return model;
	}

}
