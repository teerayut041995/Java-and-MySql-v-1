import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
public class Connect {
	public static Connection getConnection(){
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/tutorial_java_basic?useUnicode=yes&characterEncoding=UTF-8","root","abc456");
			//JOptionPane.showMessageDialog(null, "Connected");
			return con;
		} catch (Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null , ex);
			JOptionPane.showMessageDialog(null, "Not Connected");
			return null;
		}
	}
}
