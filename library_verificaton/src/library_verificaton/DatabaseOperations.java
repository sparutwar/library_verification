package library_verificaton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DatabaseOperations {
	XSSFWorkbook workbook[];
	BufferedWriter errLogBuffer;
	BookNumbersInSheet[] excelBookDetail;
	
	public void initializeWorkBook() {
		workbook = new XSSFWorkbook[Constants.TYPE_OF_BOOKS];
		excelBookDetail = new BookNumbersInSheet[4];
		
		try {
			errLogBuffer = new BufferedWriter(new FileWriter(Constants.errLogFile, true));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			for (int i = 0; i < Constants.TYPE_OF_BOOKS ; i++) {
			workbook[i] = new XSSFWorkbook(new FileInputStream
					(Constants.currentDir+"/"+Constants.BOOK_TYPE[i]+".xlsx"));
			}
		} catch (IOException e) {
			writeErrLog(e.getMessage());
		}
		for (int i = 0; i < Constants.TYPE_OF_BOOKS ; i++) {
			excelBookDetail[i] = new BookNumbersInSheet();
		}
	}
	
	private void writeErrLog(String message) {
		try {
			errLogBuffer.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int intializeBooksEntered() {
		
		for(int i=0;i<Constants.TYPE_OF_BOOKS;i++) {

			HashMap<Integer, Integer> bookDetails = excelBookDetail[i].getHashMap();
			
			XSSFWorkbook localWorkbook = workbook[i];
			XSSFSheet sheet = localWorkbook.getSheet("Sheet1");
			Iterator<Row> iterator = sheet.iterator();
			while (iterator.hasNext()) {
				try {
	                Row currentRow = iterator.next();
	                int rowNum = currentRow.getRowNum();
	                
	                if(rowNum==0) {
	                	continue;
	                }
	                int bookNum = (int)currentRow.getCell
	                		(Constants.COLUMN_BOOK_NUMBER).getNumericCellValue();
	                
	                if(bookDetails.get(bookNum) != null) {
	                	return Constants.ERR_EXCEL_SHEET_DUPLICATE_ENTRY;
	                }
	                
	                bookDetails.put(bookNum, rowNum);
				}
				catch (Exception e) {
					writeErrLog(e.getMessage());
					return Constants.ERR_EXCEL_SHEET_WRONG_DATA_FORMAT;
				}
			}
		}
		return Constants.SUCCESS_EXCEL_SHEET;
	}
	
	/**
	 * 
	 * @param book
	 * @return ERR_DB_INSERT - error while writing in excel
	 * SUCCESS_DB_INSERT - if written date in excel without error
	 * in case of duplicate book number - row number of that entry in excel 
	 */
	int insert(BookDetails book) {
		int typeOfBook = book.bookType;
		XSSFWorkbook localWorkbook = workbook[typeOfBook];
		XSSFSheet sheet = localWorkbook.getSheet("Sheet1");
		
		int bookNumToAdd = book.bookNumber;
		HashMap<Integer, Integer> bookDetails = 
				excelBookDetail[typeOfBook].getHashMap();
		
		if(bookDetails.get(bookNumToAdd) != null) {
			return bookDetails.get(bookNumToAdd);
		}
		
		int rownum = addRow(localWorkbook, sheet, book);
		
		try { 
            // this Writes the workbook gfgcontribute 
            FileOutputStream out = new FileOutputStream
            		(new File(Constants.currentDir+"/"+
            				Constants.BOOK_TYPE[typeOfBook]+".xlsx")); 
            
            localWorkbook.write(out); 
            out.close(); 
            System.out.println("excel written successfully on disk."); 
        }
        catch (Exception e) { 
            e.printStackTrace();
            return Constants.ERR_DB_INSERT;
        } 
		
        bookDetails.put(book.bookNumber, rownum);
        
		return Constants.SUCCESS_DB_INSERT;
	}
	/**
	 * 
	 * @param localWorkbook - select excel from 4 excels of different book type
	 * @param sheet - select sheet
	 * @param book - book details 
	 * @return row number of newly inserted row
	 */
	int addRow(XSSFWorkbook localWorkbook, XSSFSheet sheet, BookDetails book)
	{
		int lastRow = sheet.getLastRowNum();
		System.out.println("last Row number"+lastRow);
		
		Date date = Calendar.getInstance().getTime();
		
		Map<String, Object[]> data = new TreeMap<String, Object[]>(); 
		data.put(lastRow+"", new Object[]{ 
				Constants.BOOK_STATUS[book.statusIndex],
				Constants.USERS[book.userIndex], 
				Constants.BOOK_TYPE[book.bookType], 
				book.cupboard, 
				book.rack,
				book.bookNumber,
				date});
		
		// Iterate over data and write to sheet 
        Set<String> keyset = data.keySet(); 
        
        int rownum = lastRow+1;
        
        for (String key : keyset) { 
            // this creates a new row in the sheet 
            Row row = sheet.createRow(rownum++); 
            Object[] objArr = data.get(key); 
            int cellnum = 0; 
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++); 
                if (obj instanceof String) 
                    cell.setCellValue((String)obj); 
                else if (obj instanceof Integer) 
                    cell.setCellValue((Integer)obj); 
                else if(obj instanceof Date) {
                	CellStyle cellStyle = localWorkbook.createCellStyle();
                	CreationHelper createHelper = localWorkbook.getCreationHelper();
                	cellStyle.setDataFormat(createHelper.createDataFormat()
                			.getFormat("m/d/yy h:mm"));
                	cell.setCellValue(date);
                	cell.setCellStyle(cellStyle);
                }
            } 
        }
        return rownum;
	}
}
