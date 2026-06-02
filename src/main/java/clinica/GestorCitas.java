package clinica;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// GestorCitas es un poco diferente a los otros gestores porque
// cuando cargo el CSV necesito reconstruir los objetos Doctor y Paciente.
// Por eso recibe los otros dos gestores en el constructor,
// para poder buscar por ID al momento de leer el archivo.
public class GestorCitas implements Guardable {

    private List<Cita> citas = new ArrayList<>();
    private static final String RUTA = "db/citas.csv";

    // Los necesito para buscar Doctor y Paciente por ID al cargar
    private GestorDoctores  gestorDoctores;
    private GestorPacientes gestorPacientes;

    public GestorCitas(GestorDoctores gestorDoctores, GestorPacientes gestorPacientes) {
        this.gestorDoctores  = gestorDoctores;
        this.gestorPacientes = gestorPacientes;
    }

    public void agregarCita(Cita cita) {
        citas.add(cita);
    }

    public List<Cita> listarCitas() {
        return citas;
    }

    // Guarda las citas en CSV.
    // Para el Doctor y Paciente solo guardo su ID, no el objeto completo,
    // porque en el CSV no puedo guardar objetos Java directamente.
    // Al cargar uso esos IDs para recuperar los objetos desde los gestores.
    @Override
    public void guardar() {
        try {
            File carpeta = new File("db");
            if (!carpeta.exists()) carpeta.mkdirs();

            BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA));
            for (Cita c : citas) {
                // formato: id,fecha,hora,motivo,idDoctor,idPaciente
                bw.write(c.getId()             + "," +
                         c.getFecha()          + "," +
                         c.getHora()           + "," +
                         c.getMotivo()         + "," +
                         c.getDoctor().getId() + "," +
                         c.getPaciente().getId());
                bw.newLine();
            }
            bw.close();

        } catch (IOException e) {
            System.out.println("[Error] No se pudo guardar citas.csv: " + e.getMessage());
        }
    }

    // Al cargar, uso los IDs del CSV para buscar el Doctor y Paciente
    // en sus respectivos gestores y reconstruir el objeto Cita completo.
    // Si alguno de los dos no se encuentra, esa cita se omite.
    @Override
    public void cargar() {
        citas.clear();

        File archivo = new File(RUTA);
        if (!archivo.exists()) return;

        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(",", 6);

                if (p.length == 6) {
                    // Recupero el Doctor y Paciente usando sus IDs guardados
                    Doctor   doctor   = gestorDoctores.buscarDoctor(p[4].trim());
                    Paciente paciente = gestorPacientes.buscarPaciente(p[5].trim());

                    // Solo agrego la cita si ambos existen en el sistema
                    if (doctor != null && paciente != null) {
                        citas.add(new Cita(
                            p[0].trim(),
                            p[1].trim(),
                            p[2].trim(),
                            p[3].trim(),
                            doctor,
                            paciente
                        ));
                    }
                }
            }
            br.close();

        } catch (IOException e) {
            System.out.println("[Error] No se pudo cargar citas.csv: " + e.getMessage());
        }
    }
}
