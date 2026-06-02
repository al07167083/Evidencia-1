package clinica;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// GestorDoctores se encarga de todo lo relacionado con los doctores:
// guardarlos en memoria, buscarlos y persistirlos en un archivo CSV.
// Implementa Guardable para cumplir con el contrato de guardar/cargar.
public class GestorDoctores implements Guardable {

    // Lista donde vivo los doctores mientras el programa está corriendo
    private List<Doctor> doctores = new ArrayList<>();

    // Ruta del archivo donde se guardan los datos
    private static final String RUTA = "db/doctores.csv";

    // Agrega un doctor a la lista en memoria
    public void agregarDoctor(Doctor doctor) {
        doctores.add(doctor);
    }

    // Recorre la lista buscando el doctor con ese ID.
    // Si no lo encuentra regresa null, y en SistemaClinico
    // uso eso para avisarle al usuario que no existe.
    public Doctor buscarDoctor(String id) {
        for (Doctor d : doctores) {
            if (d.getId().equalsIgnoreCase(id)) {
                return d;
            }
        }
        return null;
    }

    // Devuelve la lista completa, la uso para mostrar
    // los doctores disponibles al crear una cita
    public List<Doctor> listarDoctores() {
        return doctores;
    }

    // Escribe todos los doctores en el archivo CSV.
    // Cada línea tiene el formato: id,nombre,especialidad
    // Si hay algún problema con el archivo no cierro el programa,
    // solo muestro el error y el sistema sigue funcionando.
    @Override
    public void guardar() {
        try {
            // Si la carpeta db no existe todavía, la creo
            File carpeta = new File("db");
            if (!carpeta.exists()) carpeta.mkdirs();

            BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA));
            for (Doctor d : doctores) {
                bw.write(d.getId() + "," + d.getNombre() + "," + d.getEspecialidad());
                bw.newLine();
            }
            bw.close();

        } catch (IOException e) {
            System.out.println("[Error] No se pudo guardar doctores.csv: " + e.getMessage());
        }
    }

    // Lee el archivo CSV y reconstruye los objetos Doctor en memoria.
    // Si el archivo no existe todavía no pasa nada, simplemente
    // empieza con la lista vacía (se crea la primera vez que se guarda).
    @Override
    public void cargar() {
        doctores.clear();

        File archivo = new File(RUTA);
        if (!archivo.exists()) return;

        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;

            while ((linea = br.readLine()) != null) {
                // Separo cada línea en sus tres partes
                String[] partes = linea.split(",", 3);
                if (partes.length == 3) {
                    doctores.add(new Doctor(
                        partes[0].trim(),
                        partes[1].trim(),
                        partes[2].trim()
                    ));
                }
            }
            br.close();

        } catch (IOException e) {
            System.out.println("[Error] No se pudo cargar doctores.csv: " + e.getMessage());
        }
    }
}
