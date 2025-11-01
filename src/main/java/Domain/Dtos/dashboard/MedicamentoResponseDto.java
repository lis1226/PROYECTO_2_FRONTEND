package Domain.Dtos.dashboard;

public class MedicamentoResponseDto {
    private String codigo;
    private String descripcion;
    private String presentacion;

    public MedicamentoResponseDto() {}
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }
}
