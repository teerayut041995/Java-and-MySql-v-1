import java.awt.BorderLayout;
import java.util.Date;
import java.util.Vector;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
public class Main extends JFrame {
	private Connection con = null;

	private JPanel contentPane;
	private JTextField text_name;
	private JTextField text_price;
	private JLabel lbl_show_image;
	private String imgPath = null;
	private JTextField textID;
	private JTable table;
	private JDateChooser text_date;
	private JTable JTable_Products;
	private Date addDate = null;
	private JComboBox cbbCategory;
	private JComboBox cbbSelectCateggory;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 742, 524);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		text_name = new JTextField();
		text_name.setBounds(23, 133, 255, 34);
		contentPane.add(text_name);
		text_name.setColumns(10);
		
		text_price = new JTextField();
		text_price.setColumns(10);
		text_price.setBounds(23, 194, 255, 34);
		contentPane.add(text_price);
		
		JLabel lblNewLabel = new JLabel("\u0E0A\u0E37\u0E48\u0E2D\u0E2A\u0E34\u0E19\u0E04\u0E49\u0E32");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(23, 112, 162, 27);
		contentPane.add(lblNewLabel);
		
		JLabel label = new JLabel("\u0E23\u0E32\u0E04\u0E32");
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
		label.setBounds(23, 167, 162, 27);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("\u0E27\u0E31\u0E19\u0E17\u0E35\u0E48");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		label_1.setBounds(23, 230, 162, 27);
		contentPane.add(label_1);
		
		text_date = new JDateChooser();
		text_date.setDateFormatString("yyyy-MM-dd");
		text_date.setBounds(23, 255, 255, 34);
		contentPane.add(text_date);
		
		lbl_show_image = new JLabel("New label");
		lbl_show_image.setBackground(Color.WHITE);
		lbl_show_image.setBounds(23, 300, 255, 114);
		lbl_show_image.setOpaque(true);
		contentPane.add(lbl_show_image);
		ImageIcon ImageDefault = new ImageIcon(Main.class.getResource("/images/upload-image.png"));
		Image img = ImageDefault.getImage();
		Image newImage = img.getScaledInstance(lbl_show_image.getWidth(), lbl_show_image.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(newImage);
		lbl_show_image.setIcon(image);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.IMAGE", "jpg","gif","png");
				fileChooser.addChoosableFileFilter(filter);
				int result = fileChooser.showSaveDialog(null);
				if(result == JFileChooser.APPROVE_OPTION){
					File selectedFile = fileChooser.getSelectedFile();
					String path = selectedFile.getAbsolutePath();
					lbl_show_image.setIcon(ResizeImage(path));
					imgPath = path;
					System.out.println(imgPath);
					
				} else if (result == JFileChooser.CANCEL_OPTION){
					System.out.println("No Data");
				} else {
					System.out.println("Error....");
				}
			}
		});
		btnBrowse.setBounds(23, 425, 255, 23);
		contentPane.add(btnBrowse);
		
		JButton btnInsert = new JButton("Insert");
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ItemCategory item = (ItemCategory)cbbCategory.getSelectedItem();

				if(text_name.getText() == null || text_price.getText() == null || text_date.getDate() == null || imgPath == null || item.getId() == 0){
					JOptionPane.showMessageDialog(null, "กรุณากรอกข้อมูลให้ครบทุกช่อง");
				} else {
					con = Connect.getConnection();
					try {
						PreparedStatement ps = con.prepareStatement("INSERT INTO products (name,price,add_date,image,category_id) value (?,?,?,?,?)");
						ps.setString(1, text_name.getText());
						ps.setString(2, text_price.getText());
						
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						String addDate = dateFormat.format(text_date.getDate());
						ps.setString(3, addDate);
						InputStream is = new FileInputStream(new File(imgPath));
						ps.setBlob(4,is);
						ps.setInt(5, item.getId());
						ps.executeUpdate();
						getData();
						//Show_Products_In_JTable();
						JOptionPane.showMessageDialog(null, "Data Insert Complate...");
					} catch (Exception ex) {
						
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
				}
			}
		});
		btnInsert.setBounds(290, 425, 125, 23);
		contentPane.add(btnInsert);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ItemCategory item = (ItemCategory)cbbCategory.getSelectedItem();
				if(text_name.getText() == null || text_price.getText() == null || text_date.getDate() == null || textID.getText() == null || item.getId() == 0){
					JOptionPane.showMessageDialog(null, "กรุณากรอกข้อมูลให้ครบทุกช่อง");
				} else {
					con = Connect.getConnection();
					if(imgPath == null){
						try {
							String sql = "UPDATE products set name = ?, price = ?,add_date = ? , category_id = ? WHERE id = ?";
							PreparedStatement ps = con.prepareStatement(sql);
							ps.setString(1, text_name.getText());
							ps.setString(2, text_price.getText());
							
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							String addDate = dateFormat.format(text_date.getDate());
							ps.setString(3, addDate);
							ps.setInt(4, item.getId());
							ps.setInt(5, Integer.parseInt((textID.getText())));
							ps.executeUpdate();
							getData();
							JOptionPane.showMessageDialog(null, "Data Update Complate...");
						} catch (Exception ex) {
							
							JOptionPane.showMessageDialog(null, ex.getMessage());
						}
					} else {
						try{
							String sql = "UPDATE products set name = ?, price = ?,add_date = ?,image = ? WHERE id = ?";
							PreparedStatement ps = con.prepareStatement(sql);
							ps.setString(1, text_name.getText());
							ps.setString(2, text_price.getText());
							
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							String addDate = dateFormat.format(text_date.getDate());
							ps.setString(3, addDate);
							InputStream is = new FileInputStream(new File(imgPath));
							ps.setBlob(4,is);
							ps.setInt(5, Integer.parseInt((textID.getText())));
							ps.executeUpdate();
							JOptionPane.showMessageDialog(null, "Data Update Complate...");
						} catch (Exception ex){
							JOptionPane.showMessageDialog(null, ex.getMessage());
						}
						
					}
					
				}
			}
		});
		btnUpdate.setBounds(418, 425, 141, 23);
		contentPane.add(btnUpdate);
		
		textID = new JTextField();
		textID.setEnabled(false);
		textID.setBounds(26, 23, 86, 20);
		contentPane.add(textID);
		textID.setColumns(10);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!textID.getText().equals("")){
					int selectOption = JOptionPane.showConfirmDialog(null, "คุณต้องการลบข้อมูลนี้หรือไม่?", "ยืนยันการลบ",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
					// 0=yes, 1=no, 2=cancel
					if(selectOption == 0){
						con = Connect.getConnection();
						try {
							String sql = "DELETE FROM products WHERE id = ?";
							PreparedStatement ps = con.prepareStatement(sql);
							int id = Integer.parseInt(textID.getText());
							ps.setInt(1,id);
							ps.executeUpdate();
							getData();
							resetForm();
							JOptionPane.showMessageDialog(null, "Data Delete Complate...");
			
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage());
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Product not delete : Enter the product id");
				}
			}
		});
		btnDelete.setBounds(562, 425, 141, 23);
		contentPane.add(btnDelete);
		
		table = new JTable();
		table.setBounds(319, 62, 1, 1);
		contentPane.add(table);
		
		JTable_Products = new JTable();
		JTable_Products.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				DefaultTableModel model = (DefaultTableModel)JTable_Products.getModel();
				int selectedRowIndex = JTable_Products.getSelectedRow();
				int index = Integer.parseInt(model.getValueAt(selectedRowIndex, 0).toString());
				ShowItem(index);
			}
		});
		JTable_Products.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
			},
			new String[] {
				"ID", "\u0E0A\u0E37\u0E48\u0E2D\u0E2A\u0E34\u0E19\u0E04\u0E49\u0E32", "\u0E1B\u0E23\u0E30\u0E40\u0E20\u0E17\u0E2A\u0E34\u0E19\u0E04\u0E49\u0E32", "\u0E23\u0E32\u0E04\u0E32", "\u0E27\u0E31\u0E19\u0E17\u0E35\u0E48\u0E40\u0E1E\u0E34\u0E48\u0E21"
			}
		));
		JTable_Products.setBounds(288, 74, 415, 345);
		contentPane.add(JTable_Products);
		
		
		cbbCategory = new JComboBox(setComboBoxCategory());
		cbbCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox comboBox = (JComboBox)e.getSource();
				ItemCategory item = (ItemCategory)comboBox.getSelectedItem();
		        //System.out.println( item.getId() + " : " + item.getDescription() );
				//JOptionPane.showMessageDialog(null,"Hey");
			}
		});
		//setComboBoxCategory();
		cbbCategory.setBounds(23, 75, 255, 27);
		
		contentPane.add(cbbCategory);
		
		JLabel label_2 = new JLabel("\u0E1B\u0E23\u0E30\u0E40\u0E20\u0E17\u0E2A\u0E34\u0E19\u0E04\u0E49\u0E32");
		label_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		label_2.setBounds(23, 54, 162, 27);
		contentPane.add(label_2);
		
		cbbSelectCateggory = new JComboBox(setComboBoxCategory());
		cbbSelectCateggory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ItemCategory item = (ItemCategory)cbbSelectCateggory.getSelectedItem();
				if(item.getId() == 0){
					getData();
				} else {
					getCategoryData(item.getId());
				}
				
		        
			}
		});
		cbbSelectCateggory.setBounds(451, 29, 252, 34);
		contentPane.add(cbbSelectCateggory);
		
		JLabel label_3 = new JLabel("\u0E40\u0E25\u0E37\u0E2D\u0E01\u0E14\u0E39\u0E15\u0E32\u0E21\u0E1B\u0E23\u0E30\u0E40\u0E20\u0E17\u0E2A\u0E34\u0E19\u0E04\u0E49\u0E32");
		label_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		label_3.setBounds(290, 26, 162, 27);
		contentPane.add(label_3);
		
		JButton btnClearForm = new JButton("\u0E25\u0E49\u0E32\u0E07\u0E1F\u0E2D\u0E23\u0E4C\u0E21");
		btnClearForm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetForm();
			}
		});
		btnClearForm.setBounds(23, 459, 255, 23);
		contentPane.add(btnClearForm);
		getData();
		//Show_Products_In_JTable();
	}
	public Vector setComboBoxCategory(){
		Vector model = new Vector();
		model.addElement( new ItemCategory(0, "เลือกประเภทสินค้า" ) );
        String sql = "SELECT * FROM category_products ORDER BY id ASC";
        try {
            con = Connect.getConnection();
            Statement s = con.prepareStatement(sql);
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
            	model.addElement( new ItemCategory(Integer.parseInt(rs.getString("id")), rs.getString("category_name") ) );
            	//cbbCategory.addItem(rs.getString("category_name"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return model;
	}
	public ImageIcon ResizeImage(String imgPath){
		ImageIcon MyImage = new ImageIcon(imgPath);
		Image img = MyImage.getImage();
		Image newImage = img.getScaledInstance(lbl_show_image.getWidth(), lbl_show_image.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(newImage);
		return image;
	}
	public ImageIcon ShowImage(byte[] pic){
		ImageIcon MyImage = null;
		MyImage = new ImageIcon(pic);
		Image img = MyImage.getImage();
		Image newImage = img.getScaledInstance(lbl_show_image.getWidth(), lbl_show_image.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(newImage);
		return image;
	}

    public void getData() {

        DefaultTableModel dm = (DefaultTableModel)JTable_Products.getModel();
        dm.setRowCount(0);
        String sql = "SELECT * FROM products p INNER JOIN category_products cate ON p.category_id = cate.id ORDER BY p.id ASC";
        try {
            con = Connect.getConnection();
            Statement s = con.prepareStatement(sql);
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                //GET VALUES
                String id = rs.getString("id");
                String name = rs.getString("name");
                String categoryProduct = rs.getString("category_name");
                String price = rs.getString("price");
                String add_date = rs.getString("add_date");
                dm.addRow(new String[]{id, name, categoryProduct , price, add_date});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void getCategoryData(int categoryID) {

        DefaultTableModel dm = (DefaultTableModel)JTable_Products.getModel();
        dm.setRowCount(0);
        String sql = "SELECT * FROM products p INNER JOIN category_products cate ON p.category_id = cate.id WHERE p.category_id = ? ORDER BY p.id ASC";
        try {
            con = Connect.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,categoryID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //GET VALUES
                String id = rs.getString("id");
                String name = rs.getString("name");
                String categoryProduct = rs.getString("category_name");
                String price = rs.getString("price");
                String add_date = rs.getString("add_date");
                dm.addRow(new String[]{id, name, categoryProduct , price, add_date});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void ShowItem(int index){
    	
    	String sql = "SELECT * FROM products WHERE id = ?";
    	try {
            con = Connect.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,index);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
            	textID.setText(rs.getString("id"));
            	text_name.setText(rs.getString("name"));
            	text_price.setText(rs.getString("price"));
            	lbl_show_image.setIcon(ShowImage(rs.getBytes("image")));
            	cbbCategory.setSelectedIndex(Integer.parseInt(rs.getString("category_id")));
            	try {
                  Date addDate = null;
                   addDate = new SimpleDateFormat("yyyy-MM-dd").parse((String)rs.getString("add_date"));
                   text_date.setDate(addDate);
               } catch (ParseException ex) {
                   Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void resetForm(){
    	textID.setText("");
    	text_name.setText("");
    	text_price.setText("");
    	text_date.setCalendar(null);
    	cbbCategory.setSelectedIndex(0);
    	ImageIcon ImageDefault = new ImageIcon(Main.class.getResource("/images/upload-image.png"));
		Image img = ImageDefault.getImage();
		Image newImage = img.getScaledInstance(lbl_show_image.getWidth(), lbl_show_image.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(newImage);
		lbl_show_image.setIcon(image);
    }
    class ItemCategory
    {
        private int id;
        private String category_name;
 
        public ItemCategory(int id, String categoryName)
        {
            this.id = id;
            this.category_name = categoryName;
        }
 
        public int getId()
        {
            return id;
        }
 
        public String getCategoryName()
        {
            return category_name;
        }
 
        public String toString()
        {
            return category_name;
        }
    }
}
