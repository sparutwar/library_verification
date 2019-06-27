package library_verificaton;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Verification extends JFrame implements ActionListener, KeyListener{

	GridBagLayout gl ;
	JComboBox<String> cbBookType;
	JComboBox<String> cbUsers;
	JComboBox<String> cbStatus;
	
	JTextField txtCupboard;
	JTextField txtRack;
	JTextField txtBookNumber;
	JButton btnInsert;
	JLabel lblError;

	ExcelOperations dbop;
	
	
	public static void main(String[] args) {
		Verification vf = new Verification();
		vf.dbop = new ExcelOperations();
		
		vf.dbop.initializeWorkBook();
		vf.dbop.intializeBooksEntered();
		
		try {
			Constants.errLogFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		vf.setGUI();
	}
	
	GridBagConstraints getGc(int x, int y) {
		GridBagConstraints gc;
		gc      = new GridBagConstraints();
		
		gc.gridx = x;
		gc.gridy = y;
		gc.fill=GridBagConstraints.BOTH;
		
		return gc;
	}
	
	void setGUI(){
//		Font fontTemp = new Font(Font.SANS_SERIF, Font.BOLD, 24);
		Helper.setUIFont (new javax.swing.plaf.FontUIResource("Serif",Font.BOLD,24));
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setTitle("Library Verification");
		gl 	= new GridBagLayout();
		setLayout(gl);

		int y =0;
		
		add(new JLabel("Status:"), getGc(0, y));
		cbStatus = new JComboBox(Constants.BOOK_STATUS);
		add(cbStatus, getGc(1,y));
		y++;
		
		add(new JLabel("User:"), getGc(0, y));
		cbUsers = new JComboBox(Constants.USERS);
		add(cbUsers,getGc(1,y));
		y++;
		
		add(new JLabel("type of book:"), getGc(0, y));
		cbBookType = new JComboBox(Constants.BOOK_TYPE);
		add(cbBookType,getGc(1,y));
		y++;
		
		add(new JLabel("cupboard:"), getGc(0, y));
		txtCupboard = new JTextField(20);
		add(txtCupboard,getGc(1,y));
		y++;
		
		add(new JLabel("Rack:"), getGc(0, y));
		txtRack = new JTextField(20);		
		add(txtRack,getGc(1,y));
		y++;
		
		add(new JLabel("BookNumber:"), getGc(0, y));
		txtBookNumber = new JTextField(20);
		add(txtBookNumber,getGc(1,y));
		y++;
		
		add(new JLabel(""), getGc(0,y));
		btnInsert = new JButton("Insert");
		GridBagConstraints gc1 = getGc(1, y);
		add(btnInsert,gc1);
		y++;
		
		lblError = new JLabel("");
		
		GridBagConstraints gcLblError = getGc(0, y);
		gcLblError.gridwidth =2;

		add(lblError, gcLblError);
		
		btnInsert.addActionListener(this);
		txtBookNumber.addKeyListener(this);
		
		setVisible(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Helper h = new Helper();
		
		BookDetails data = h.dataValidation(txtCupboard.getText(), txtRack.getText(),
				txtBookNumber.getText(), cbBookType.getSelectedIndex(), 
				cbUsers.getSelectedIndex(), cbStatus.getSelectedIndex());
		//TODO
		
		if(data.errCode==Constants.ERR_DATA_VALIDATION) {
			lblError.setForeground(Color.RED);
			lblError.setText(data.ERR_DATA_VALIDATION_DESC);
			return;
		}
		
		lblError.setForeground(Color.GREEN);

		int err_code = dbop.insert(data);
		
		if(err_code==Constants.ERR_DB_INSERT) {
			lblError.setForeground(Color.RED);
			lblError.setText("Excel Sheet Error:");
			return;
		}
		
		if(err_code!=Constants.SUCCESS_DB_INSERT) {
			lblError.setForeground(Color.RED);
			lblError.setText("Duplicate Entry in Excel at row "+err_code);
			return;
		}
		
		if(err_code==Constants.SUCCESS_DB_INSERT) {
			lblError.setForeground(Color.GREEN);
			lblError.setText("Successfully inserted book number:"+data.bookNumber
				+" in cupboard:"+data.cupboard);
		}
		
		txtBookNumber.setText("");
		txtBookNumber.requestFocus();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_ENTER){
			btnInsert.doClick();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	
}
