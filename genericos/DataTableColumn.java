package genericos;

public class DataTableColumn {
	
	public String nombre;
	public Class<?> tipo;
	
	public DataTableColumn(String nombreColumna, Class<?> tipoColumna)
	{
		nombre = nombreColumna;
		tipo = tipoColumna;
	}
}
