/*
 * Copyright 2010 KAT Software LLC. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
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
