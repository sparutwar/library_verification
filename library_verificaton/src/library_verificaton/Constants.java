package library_verificaton;

import java.io.File;

public interface Constants {
	int ERR_DATA_VALIDATION = -1;
	int TYPE_OF_BOOKS = 4;
	int MAX_CUPBOARD = 500;
	int MAX_RACK = 6;
	
	final static String currentDir = System.getProperty("user.dir");
	
	String BOOK_TYPE[] = {"GL","BB","SW", "WB"};
	
	String USERS[] = {"Admin","MRC","YMK","ASA","ANP","DDL","SPS",
			"SPA","SMS","AJP","RSK"};
	
	int TOTAL_BOOK_COUNT[] = {40000,16000,3400,1530};
	String BOOK_STATUS[] = {"Available", "Issue", "Paid Fine", "Write off"};
	
	File errLogFile = new File(currentDir+"/errLog.txt");
	int ERR_EXCEL_SHEET_DUPLICATE_ENTRY = -1;
	int ERR_EXCEL_SHEET_WRONG_DATA_FORMAT =-2;
	int SUCCESS_EXCEL_SHEET =0;
	
	int ERR_DB_INSERT = -1;
	
	int SUCCESS_DB_INSERT =0;
//	int ERR_DB_INSERT_DUPLICATE_ENTRY = -2;
	int COLUMN_BOOK_NUMBER = 5;
	
}
