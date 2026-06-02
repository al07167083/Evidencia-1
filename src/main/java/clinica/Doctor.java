package clinica;

// Doctor hereda de Persona, así no tengo que volver a declarar
// id y nombre aquí. Solo agrego lo que es exclusivo del doctor: la especialidad.
public class Doctor extends Persona {

    private String especialidad;

    // Llamo a super() para que Persona inicialice id y nombre,
    // y aquí solo me encargo de la especialidad
    public Doctor(String id, String nombre, String especialidad) {
        super(id, nombre);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    // Aquí implemento mostrarInfo() para el Doctor.
    // Muestra los tres datos que lo identifican dentro del sistema.
    @Override
    public void mostrarInfo() {
        System.out.println("  Doctor  : " + nombre
                         + " | Especialidad: " + especialidad
                         + " | ID: " + id);
    }
}
