package genericos;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import com.mysql.cj.result.Field;

public class DataTable {

	public String nombre;
	public ArrayList<DataTableColumn> columnas;
	public ArrayList<DataTableRow> filas;

	public DataTable(String nombreTabla) {
		nombre = nombreTabla;
		columnas = new ArrayList<DataTableColumn>();
		filas = new ArrayList<DataTableRow>();
	}

	public void addColumna(String nombreColumna, Class<?> tipoColumna) {
		DataTableColumn columna = new DataTableColumn(nombreColumna, tipoColumna);
		try {
			columnas.add(columna);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void addFila(Object[] array) throws Exception {
		if (arrayCorrecto(array))
		{
			filas.add(new DataTableRow(array));
		}
		else
			throw new Exception("Array incorrecto");
	}

	private boolean arrayCorrecto(Object[] array) {
		boolean correcto = array.length == columnas.size();
		int i = 0;
		while (correcto && i < columnas.size()) {
			Class<?> tipoClase = columnas.get(i).tipo;
			Class<?> tipoElmento = array[i].getClass();
			String str_tipoClase = tipoClase.toString();
			String str_tipoElemento = tipoElmento.toString();
			correcto &= str_tipoClase.equals(str_tipoElemento);
			i++;
		}
		return correcto;
	}
	
	public DataTableRow getFila(int indiceDeFila) throws Exception
	{
		if (indiceDeFila < filas.size())
			return filas.get(indiceDeFila);
		else
			throw new Exception("Indice de fila incorrecto");
	}
	
	public void load(java.sql.ResultSet rs)
	{
		try {
			ResultSetMetaData md = (ResultSetMetaData)rs.getMetaData();
			Integer numColumnas = md.getColumnCount();
			Field[] f = md.getFields();
			for (int i=1;i<=numColumnas;i++)
			{
				String nombreColumna = md.getColumnName(i);
				String str_tipoColumna = md.getColumnClassName(i);
				Class<?> tipoColumna = Class.forName(str_tipoColumna);
				DataTableColumn nuevaColumna = new DataTableColumn(nombreColumna, tipoColumna);
				columnas.add(nuevaColumna);
			}
			
			//rs.first();
			while (rs.next())
			{				
				Object[] o = new Object[numColumnas];
				for (Integer j=1;j<=numColumnas;j++)
					o[j-1] = rs.getObject(j);
				
				addFila(o);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
