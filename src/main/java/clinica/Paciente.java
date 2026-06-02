package clinica;

// Paciente también hereda de Persona. A diferencia del Doctor,
// no necesita atributos extra: solo id y nombre son suficientes.
public class Paciente extends Persona {

    public Paciente(String id, String nombre) {
        super(id, nombre); // Persona se encarga de guardar id y nombre
    }

    // La versión de mostrarInfo() para Paciente es más sencilla
    // porque no tiene especialidad ni otros datos adicionales
    @Override
    public void mostrarInfo() {
        System.out.println("  Paciente: " + nombre + " | ID: " + id);
    }
}
