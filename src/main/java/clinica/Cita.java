package clinica;

// Cita es la clase más importante del sistema porque es la que
// une a un Doctor con un Paciente en una fecha y hora específica.
// Sin esta clase, el sistema no tendría sentido.
public class Cita {

    private String   id;
    private String   fecha;   // formato DD/MM/AAAA
    private String   hora;    // formato HH:MM
    private String   motivo;
    private Doctor   doctor;   // referencia al doctor asignado
    private Paciente paciente; // referencia al paciente que acude

    public Cita(String id, String fecha, String hora, String motivo,
                Doctor doctor, Paciente paciente) {
        this.id       = id;
        this.fecha    = fecha;
        this.hora     = hora;
        this.motivo   = motivo;
        this.doctor   = doctor;
        this.paciente = paciente;
    }

    // Getters para que GestorCitas pueda leer los datos al guardar en CSV
    public String   getId()        { return id; }
    public String   getFecha()     { return fecha; }
    public String   getHora()      { return hora; }
    public String   getMotivo()    { return motivo; }
    public Doctor   getDoctor()    { return doctor; }
    public Paciente getPaciente()  { return paciente; }

    // Muestra toda la información de la cita de forma ordenada.
    // Aprovecho el polimorfismo llamando mostrarInfo() del Doctor
    // y del Paciente, cada uno imprime lo que le corresponde.
    public void mostrarInfo() {
        System.out.println("----------------------------------------");
        System.out.println("  Cita ID : " + id);
        System.out.println("  Fecha   : " + fecha + "  Hora: " + hora);
        System.out.println("  Motivo  : " + motivo);
        doctor.mostrarInfo();
        paciente.mostrarInfo();
        System.out.println("----------------------------------------");
    }
}
