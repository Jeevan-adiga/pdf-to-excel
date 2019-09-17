/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pdf.extract.pdf_to_excel;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * This is a simple text extraction example to get started. For more advance
 * usage, see the ExtractTextByArea and the DrawPrintTextLocations examples in
 * this subproject, as well as the ExtractText tool in the tools subproject.
 *
 * @author Tilman Hausherr
 */
public class ExtractTextSimple {
	private ExtractTextSimple() {
		// example class should not be instantiated
	}

	/**
	 * This will print the documents text page by page.
	 *
	 * @param args The command line arguments.
	 *
	 * @throws IOException If there is an error parsing or extracting the document.
	 */
	public static void main(String[] args) throws IOException {

		try (PDDocument document = PDDocument.load(new File("./src/main/resources/demo file.pdf"))) {
			AccessPermission ap = document.getCurrentAccessPermission();
			if (!ap.canExtractContent()) {
				throw new IOException("You do not have permission to extract text");
			}

			// extract data from pdf as text
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setSortByPosition(true);			
			String text = null;
			for (int p = 1; p <= document.getNumberOfPages(); ++p) {
				stripper.setStartPage(p);
				stripper.setEndPage(p);
				text = stripper.getText(document).trim();
			}
			
			// use regex to extract text
			Pattern venderNameRegex = Pattern.compile("Vendor Name:(.*)\\n");
			Pattern serviceRegex = Pattern.compile("Service:(.*)\\n");
			
			String venderName = null;
			String serviceName = null;

			// fetch vender text
			Matcher matcher = venderNameRegex.matcher(text);
			while(matcher.find()) {
				venderName = matcher.group(1);
			}
	
			// fetch service text
			matcher = serviceRegex.matcher(text);
			while(matcher.find()) {
				serviceName = matcher.group(1);
			}
			
			System.out.println("Extracted Info:"+venderName+" , "+serviceName);
		}
	}

}