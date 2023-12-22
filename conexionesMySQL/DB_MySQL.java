package conexionesMySQL;

import java.sql.*;

import genericos.DataTable;
import genericos.DataTableRow;

public class DB_MySQL {

	private String server_name = "localhost";
	private String server_port = "3306";
	private String server_sqlName = "mysql";

	private String url = "jdbc:mysql://localhost:3306/mysql";

	private String user = "root";
	private String password = "1111";

	public String get_server_name() {
		return server_name;
	}

	public void set_server_name(String value) {
		server_name = value;
		recalculaURL();
	}

	public String get_serverPort() {
		return server_port;
	}

	public void set_serverPort(String value) {
		server_port = value;
		recalculaURL();
	}

	public String get_serverSqlName() {
		return server_sqlName;
	}

	public void set_serverSqlName(String value) {
		server_sqlName = value;
		recalculaURL();
	}

	public String get_user() {
		return user;
	}

	public void set_user(String value) {
		user = value;
	}

	public String get_password() {
		return password;
	}

	public void set_password(String value) {
		password = value;
	}

	private void recalculaURL() {
		url = "jdbc:mysql://" + server_name + ":" + server_port + "/" + server_sqlName;
	}

	public DB_MySQL() {
		;
	}

	public DB_MySQL(String serverNameValue, String serverPortValue, String serverSQlNameValue, String userValue,
			String passwordValue) {
		server_name = serverNameValue;
		server_port = serverPortValue;
		server_sqlName = serverSQlNameValue;

		recalculaURL();

		user = userValue;
		password = passwordValue;
	}

	
	
	public Connection getConnection() {
		Connection conn = null;
		try {
			// connection
			conn = DriverManager.getConnection(url, user, password);

			if (conn != null) {
				System.out.println("Conectado a la database mysql");
			}
		} catch (SQLException ex) {
			System.out.println("Exception :" + ex.getMessage());
			ex.printStackTrace();
		}

		return conn;
	}

	public void closeConnection(Connection conn) {
		try {
			conn.close();
			if (conn.isClosed())
				System.out.println("Database mysql cerrada");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DataTable getDataTable(String sql, Connection conn) {
		DataTable t = new DataTable("");
		try {
			Statement sentencia = conn.createStatement();
			ResultSet resultado = sentencia.executeQuery(sql);
			t.load(resultado);

		} catch (SQLException ex) {
			System.out.println("Exception ::" + ex.getMessage());
			ex.printStackTrace();

			t = null;
		}

		return t;
	}

	public DataTable getDataTable_CCP(String sql) {
		DataTable t = new DataTable("");
		try {

			Connection conn = getConnection();
			t = getDataTable(sql, conn);
			closeConnection(conn);

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			ex.printStackTrace();
			t = null;
		}

		return t;
	}

	public Integer ejecutaSQL(String sql, Connection conn) {
		Integer afectados = 0;
		try {
			Statement sentencia = conn.createStatement();
			afectados = sentencia.executeUpdate(sql);
		} catch (SQLException ex) {
			System.out.println("Exception :" + ex.getMessage());
			ex.printStackTrace();
		}
		return afectados;
	}
	
	public Integer ejecutaSQL_CCP(String sql)
	{
		Integer afectados = 0;
		try {
			Connection conn = getConnection();
			afectados = ejecutaSQL(sql, conn);
			closeConnection(conn);
		}
		catch (Exception ex)
		{
			System.out.println("Exception: " + ex.getMessage());
			ex.printStackTrace();
		}
		return afectados;
	}

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		main_getDataTable();
		main_ejecutaComando("PENELOPEX");
		main_getDataTable();
		main_ejecutaComando("PENELOPE");
		main_getDataTable();
	} // end main
	
	private static void main_getDataTable()
	{
		try {
			DB_MySQL myDB = new DB_MySQL();
			DataTable t = myDB.getDataTable_CCP("select * from sakila.actor");

			String txtCabecera = "";
			for (genericos.DataTableColumn columna : t.columnas)
				txtCabecera += columna.nombre + "[" + columna.tipo.toString() + "]\t";
			
			System.out.println(txtCabecera);
			
			int i = 0;
			for (genericos.DataTableRow fila : t.filas) 
			{
				String txtFila = "";
				for (Integer j = 0; j < t.columnas.size(); j++)
					txtFila += fila.itemArray[j].toString() + "\t";

				if (i % 2 == 0 && i % 3 == 0 && i % 5 == 0)
					System.out.println(txtFila);
				i++;
			}
		} catch (Exception ex) {
			System.out.println("Exception:" + ex.getMessage());
			ex.printStackTrace();
		}	
	}

	private static void main_ejecutaComando(String nombrePoner)
	{
		String sql = "update sakila.actor set first_name='" + nombrePoner +"' where actor_id=1";
		DB_MySQL mySQL = new DB_MySQL();
		mySQL.ejecutaSQL_CCP(sql);
	}
} // end class
