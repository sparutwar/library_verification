package library_verificaton;

import javax.swing.UIManager;

public class Helper {
	
	String cupBoard ;
	String rack ;
	String bookNumber;
	
	int bookType ;
	int userIndex ;
	int status;
	
	public static void setUIFont (javax.swing.plaf.FontUIResource f){
	    java.util.Enumeration keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	      Object key = keys.nextElement();
	      Object value = UIManager.get (key);
	      if (value instanceof javax.swing.plaf.FontUIResource)
	        UIManager.put (key, f);
	      }
    }
	
	BookDetails checkIntegers(){
		BookDetails temp = new BookDetails();
		
		String cupBoard_str = cupBoard.trim();
		String rack_str = rack.trim();
		String bookNumber_str = bookNumber.trim();
		
		if(cupBoard_str.isEmpty()) 
		{
			temp.ERR_DATA_VALIDATION_DESC = "CupBoard number is Empty";
			temp.errCode = Constants.ERR_DATA_VALIDATION;
			return temp;
		}
		if(rack_str.isEmpty()) {
			temp.ERR_DATA_VALIDATION_DESC = " rack number is Empty";
			temp.errCode = Constants.ERR_DATA_VALIDATION;
			return temp;
		}
		if(bookNumber_str.isEmpty()) {
			temp.ERR_DATA_VALIDATION_DESC = "Book number is Empty";
			temp.errCode = Constants.ERR_DATA_VALIDATION;
			return temp;
		}
		
		if(!cupBoard_str.matches("[0-9]+") ) {
			temp.ERR_DATA_VALIDATION_DESC = "Please check CupBoard number";
			temp.errCode = Constants.ERR_DATA_VALIDATION;
			return temp;
		}
		if(!rack_str.matches("[0-9]+")) {
			temp.ERR_DATA_VALIDATION_DESC = "Please check rack number";
			temp.errCode = Constants.ERR_DATA_VALIDATION;
			return temp;
		}
		if(!bookNumber_str.matches("[0-9]+")) {
			temp.ERR_DATA_VALIDATION_DESC = "Please check book number";
			temp.errCode = Constants.ERR_DATA_VALIDATION;
			return temp;
		}
		
		int cupboard = Integer.parseInt(cupBoard_str);
		int rack = Integer.parseInt(rack_str);
		int bookNumber = Integer.parseInt(bookNumber_str);
		
		temp.userIndex = userIndex;
		temp.bookNumber = bookNumber;
		temp.bookType = bookType;
		temp.cupboard = cupboard;
		temp.rack = rack;
		
		return temp;
	}
	
	/**
	 * 
	 * @param cupBoard
	 * @param rack
	 * @param bookNumber
	 * @param bookType
	 * @param user
	 * @return
	 */
	BookDetails dataValidation(String cupBoard, 
			String rack,String bookNumber, int bookType, int user, int status){
		
		this.cupBoard = cupBoard;
		this.rack = rack;
		this.bookNumber = bookNumber;
		this.bookType = bookType;
		this.userIndex = user;
		this.status = status;
		
		BookDetails temp = checkIntegers();
		
		if(temp.errCode == Constants.ERR_DATA_VALIDATION) {
			return temp;
		}
		
		if(temp.cupboard> Constants.MAX_CUPBOARD) {
			temp.ERR_DATA_VALIDATION_DESC = "Cupboard greater than "+
						Constants.MAX_CUPBOARD;
			
			temp.errCode = Constants.ERR_DATA_VALIDATION;
			return temp;
		}
		
		if(temp.rack>Constants.MAX_RACK || temp.rack<=0) {
			temp.ERR_DATA_VALIDATION_DESC = "rack number greater than "
							+Constants.MAX_RACK;
			
			temp.errCode = Constants.ERR_DATA_VALIDATION;
			return temp;
		}	
		
		if(temp.bookNumber>Constants.TOTAL_BOOK_COUNT[temp.bookType]) {
			temp.ERR_DATA_VALIDATION_DESC = "Book Number is greater than available books";
			temp.errCode = Constants.ERR_DATA_VALIDATION;
			return temp;
		}
		return temp;
	}

}
