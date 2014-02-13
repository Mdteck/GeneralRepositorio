package pe.com.nextel.bean;

public class CuentaDTO {
	
	private String idCuenta;
	private HorarioDTO horario;
	private String nombre;
	
	
	public HorarioDTO getHorario() {
		return horario;
	}
	public void setHorario(HorarioDTO horario) {
		this.horario = horario;
	}
	public String getIdCuenta() {
		return idCuenta;
	}
	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	

}
