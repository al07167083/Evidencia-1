package clinica;

// Persona es una clase abstracta porque tanto el Doctor como el Paciente
// comparten id y nombre, entonces decidí no repetir ese código en cada uno.
// No se puede crear un objeto Persona directamente, solo Doctor o Paciente.
public abstract class Persona {

    // Uso "protected" para que las subclases puedan acceder
    // directamente sin necesitar un getter
    protected String id;
    protected String nombre;

    public Persona(String id, String nombre) {
        this.id     = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    // Dejo este método abstracto porque Doctor y Paciente
    // muestran su información de forma diferente.
    // Cada subclase lo implementa a su manera (polimorfismo).
    public abstract void mostrarInfo();
}
