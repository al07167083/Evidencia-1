package clinica;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Funciona igual que GestorDoctores pero para pacientes.
// Lo separé en su propia clase para que cada gestor
// maneje únicamente su propio tipo de datos.
public class GestorPacientes implements Guardable {

    private List<Paciente> pacientes = new ArrayList<>();
    private static final String RUTA = "db/pacientes.csv";

    public void agregarPaciente(Paciente paciente) {
        pacientes.add(paciente);
    }

    // Busca un paciente por ID, regresa null si no existe.
    // Lo uso en crearCita() para validar que el paciente exista
    // antes de registrar la cita.
    public Paciente buscarPaciente(String id) {
        for (Paciente p : pacientes) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    // Devuelve la lista completa para mostrarla al crear una cita
    public List<Paciente> listarPacientes() {
        return pacientes;
    }

    // Guarda todos los pacientes en CSV con el formato: id,nombre
    // Si hay error de escritura lo muestra pero no cierra el programa
    @Override
    public void guardar() {
        try {
            File carpeta = new File("db");
            if (!carpeta.exists()) carpeta.mkdirs();

            BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA));
            for (Paciente p : pacientes) {
                bw.write(p.getId() + "," + p.getNombre());
                bw.newLine();
            }
            bw.close();

        } catch (IOException e) {
            System.out.println("[Error] No se pudo guardar pacientes.csv: " + e.getMessage());
        }
    }

    // Lee el CSV y reconstruye los objetos Paciente.
    // Si el archivo no existe todavía simplemente no carga nada.
    @Override
    public void cargar() {
        pacientes.clear();

        File archivo = new File(RUTA);
        if (!archivo.exists()) return;

        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",", 2);
                if (partes.length == 2) {
                    pacientes.add(new Paciente(
                        partes[0].trim(),
                        partes[1].trim()
                    ));
                }
            }
            br.close();

        } catch (IOException e) {
            System.out.println("[Error] No se pudo cargar pacientes.csv: " + e.getMessage());
        }
    }
}
