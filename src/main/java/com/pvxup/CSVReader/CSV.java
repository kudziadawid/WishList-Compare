package com.pvxup.CSVReader;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class CSV {
	
	private static final char DEFAULT_SEPARATOR = ',';
	private static final char DEFAULT_QUOTE = '"';
	
	String csvFile = "";
	Map<Integer, String> authors = new HashMap<Integer, String>();
	
	public CSV(String csvFile) {
		this.csvFile = csvFile;
	}
	
	
	 public static List<String> parseLine(String csvLine) {

	        List<String> result = new ArrayList<>();

	        //if empty, return!
	        if (csvLine == null || csvLine.isEmpty()) {
	            return result;
	        }

	        StringBuffer curVal = new StringBuffer();
	        boolean inQuotes = false;
	        boolean startCollectChar = false;
	        boolean doubleQuotesInColumn = false;

	        char[] chars = csvLine.toCharArray();

	        for (char ch : chars) {

	            if (inQuotes) {
	            	startCollectChar = true;
	                if (ch == DEFAULT_QUOTE) {
	                    inQuotes = false;
	                    doubleQuotesInColumn = false;
	                } else {

	                    //Fixed : allow "" in custom quote enclosed
	                    if (ch == '\"') {
	                        if (!doubleQuotesInColumn) {
	                            curVal.append(ch);
	                            doubleQuotesInColumn = true;
	                        }
	                    } else {
	                        curVal.append(ch);
	                    }

	                }
	            } else {
	                if (ch == DEFAULT_QUOTE) {

	                    inQuotes = true;
	                    
	                    if (chars[0] != '"') {
	                        curVal.append('"');
	                    }
	                    
	                    if (startCollectChar) {
	                        curVal.append('"');
	                    }
	                    
	                } else if (ch == DEFAULT_SEPARATOR) {
	                	//System.out.println(curVal);
	                    result.add(curVal.toString());

	                    curVal = new StringBuffer();
	                    startCollectChar = false;
	                    
	                } else if (ch == '\r') {
	                	
	                    //ignore LF characters
	                    continue;
	                    
	                } else if (ch == '\n') {
	                	
	                	//the end, break!
	                    break;
	                    
	                } else {
	                    curVal.append(ch);
	                }
	            }

	        }        
	        //System.out.println(curVal);
	        result.add(curVal.toString());
	        
	        return result;
	    }
	 
	 public static String formatToLastName(String author, boolean CSVorWeb) {
			String auth = author;				// we don't want to change param author content
			boolean moreThan1Word = false;		
			int indexOfSpace = auth.indexOf(' ');				
			System.out.println(auth);									   
			if(CSVorWeb==true) { // CSV == true
				if (!moreThan1Word) 
					return auth.substring(0, auth.length());	
				else 
					return auth.substring(indexOfSpace+1, auth.length());	
			} else {
				if (!moreThan1Word) 
					return auth.substring(2, auth.length() -2);	
				else 
					return auth.substring(indexOfSpace+1, auth.length() - 2);
			}
		}
	
	public void CSVAddAuthors(Map<Integer, String> hm) throws FileNotFoundException {
		
		Scanner scanner = new Scanner(new File(csvFile));
		int key = 0;
		List<String> line = parseLine(scanner.nextLine());
        while (scanner.hasNext()) {
           // line = parseLine(scanner.nextLine());
           
            if (!(this.authors.containsValue(formatToLastName(parseLine(scanner.nextLine()).get(2),false)))) {
				this.authors.put(key, formatToLastName(parseLine(scanner.nextLine()).get(2),false));
				key++;
            }
            //System.out.println("Last name: " + formatToLastName(line.get(2)));
            if (hm.containsValue(formatToLastName(line.get(2),false)))
            	System.out.println(line.get(2) + " is on the wishlist");
            		
        }
        scanner.close();

    }
}

