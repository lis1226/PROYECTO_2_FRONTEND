package Domain.Dtos.receta;


public class RecetaItemDto {
    private Long id; // puede ser null en frontend al crear
    private String medicamentoCodigo;
    private int cantidad;

    public RecetaItemDto() {}
    public RecetaItemDto(String medicamentoCodigo, int cantidad) {
        this.medicamentoCodigo = medicamentoCodigo;
        this.cantidad = cantidad;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMedicamentoCodigo() { return medicamentoCodigo; }
    public void setMedicamentoCodigo(String medicamentoCodigo) { this.medicamentoCodigo = medicamentoCodigo; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}

