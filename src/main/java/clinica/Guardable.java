package clinica;

// Esta interfaz la creé para que los tres gestores (doctores, pacientes y citas)
// tengan que implementar los mismos dos métodos: guardar y cargar.
// Así me aseguro de que ninguno se quede sin persistencia de datos.
public interface Guardable {

    // Guarda la lista actual en el archivo CSV
    void guardar();

    // Carga los datos del archivo CSV a la lista en memoria
    void cargar();
}
