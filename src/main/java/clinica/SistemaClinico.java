package clinica;

import java.util.List;
import java.util.Scanner;

// SistemaClinico es el corazón del programa.
// Aquí se conectan todos los gestores, se maneja el login
// y se controla todo lo que el usuario puede hacer desde el menú.
public class SistemaClinico {

    private GestorDoctores  gestorDoctores;
    private GestorPacientes gestorPacientes;
    private GestorCitas     gestorCitas;
    private Administrador   admin;
    private Scanner         scanner;

    // En el constructor creo todos los objetos que el sistema necesita.
    // GestorCitas recibe los otros dos gestores porque los necesita
    // para reconstruir citas al leer el CSV.
    public SistemaClinico() {
        gestorDoctores  = new GestorDoctores();
        gestorPacientes = new GestorPacientes();
        gestorCitas     = new GestorCitas(gestorDoctores, gestorPacientes);
        admin           = new Administrador("admin", "1234");
        scanner         = new Scanner(System.in);
    }

    // Primer método que se llama. Muestra el encabezado,
    // carga los datos que ya estaban guardados y abre el login.
    public void iniciar() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   Sistema de Administración de Citas     ║");
        System.out.println("║         Consultorio Clínico              ║");
        System.out.println("╚══════════════════════════════════════════╝");

        gestorDoctores.cargar();
        gestorPacientes.cargar();
        gestorCitas.cargar();

        login();
    }

    // El login repite hasta que el usuario ingrese las credenciales correctas.
    // Si escribe algo que no es texto válido, capturo la excepción
    // y el programa no se cierra, solo pide los datos de nuevo.
    private void login() {
        boolean acceso = false;

        while (!acceso) {
            try {
                System.out.println("\n--- Inicio de Sesión ---");
                System.out.print("Identificador : ");
                String usuario = scanner.nextLine().trim();

                System.out.print("Contraseña    : ");
                String contrasena = scanner.nextLine().trim();

                if (admin.validarCredenciales(usuario, contrasena)) {
                    System.out.println("\n Acceso concedido. Bienvenido, " + usuario + ".");
                    acceso = true;
                    menuPrincipal();
                } else {
                    System.out.println("[Error] Credenciales incorrectas, intente de nuevo.");
                }

            } catch (Exception e) {
                System.out.println("[Error] Entrada no válida: " + e.getMessage());
            }
        }
    }

    // El menú principal se queda en bucle hasta que el usuario elige Salir.
    // Si escribe algo que no sea un número, capturo el NumberFormatException
    // y el programa no truena, solo pide la opción de nuevo.
    private void menuPrincipal() {
        boolean activo = true;

        while (activo) {
            try {
                System.out.println("\n╔══════════════════════════════╗");
                System.out.println("║        Menú Principal        ║");
                System.out.println("╠══════════════════════════════╣");
                System.out.println("║  1. Dar de alta Doctor       ║");
                System.out.println("║  2. Dar de alta Paciente     ║");
                System.out.println("║  3. Crear Cita               ║");
                System.out.println("║  4. Ver Citas                ║");
                System.out.println("║  5. Salir                    ║");
                System.out.println("╚══════════════════════════════╝");
                System.out.print("Opción: ");

                String entrada = scanner.nextLine().trim();
                int opcion = Integer.parseInt(entrada);

                if (opcion < 1 || opcion > 5) {
                    System.out.println("[Error] Opción no válida. Ingrese un número del 1 al 5.");
                    continue;
                }

                switch (opcion) {
                    case 1: altaDoctor();   break;
                    case 2: altaPaciente(); break;
                    case 3: crearCita();    break;
                    case 4: verCitas();     break;
                    case 5:
                        System.out.println("\n Hasta luego. Sesión cerrada.");
                        activo = false;
                        break;
                }

            } catch (NumberFormatException e) {
                // El usuario escribió letras en lugar de un número
                System.out.println("[Error] Debe ingresar un número del 1 al 5.");
            } catch (Exception e) {
                System.out.println("[Error] Ocurrió un error inesperado: " + e.getMessage());
            }
        }
    }

    // Pide los datos del doctor, los valida y los guarda.
    // Si el usuario falla 3 veces en algún campo, leerCampo()
    // regresa null y volvemos al menú sin hacer nada.
    private void altaDoctor() {
        System.out.println("\n--- Dar de Alta Doctor ---");
        try {
            String id = leerCampo("ID del doctor");
            if (id == null) return;

            String nombre = leerCampo("Nombre completo");
            if (nombre == null) return;

            String especialidad = leerCampo("Especialidad");
            if (especialidad == null) return;

            Doctor doctor = new Doctor(id, nombre, especialidad);
            gestorDoctores.agregarDoctor(doctor);
            gestorDoctores.guardar();
            System.out.println(" Doctor registrado correctamente.");

        } catch (Exception e) {
            System.out.println("[Error] No se pudo registrar el doctor: " + e.getMessage());
        }
    }

    // Similar a altaDoctor() pero más sencillo porque
    // el paciente solo necesita ID y nombre.
    private void altaPaciente() {
        System.out.println("\n--- Dar de Alta Paciente ---");
        try {
            String id = leerCampo("ID del paciente");
            if (id == null) return;

            String nombre = leerCampo("Nombre completo");
            if (nombre == null) return;

            Paciente paciente = new Paciente(id, nombre);
            gestorPacientes.agregarPaciente(paciente);
            gestorPacientes.guardar();
            System.out.println(" Paciente registrado correctamente.");

        } catch (Exception e) {
            System.out.println("[Error] No se pudo registrar el paciente: " + e.getMessage());
        }
    }

    // Aquí es donde se une todo: pido los datos de la cita,
    // muestro los doctores y pacientes disponibles, y verifico
    // que el doctor y paciente que eligió el usuario realmente existan.
    private void crearCita() {
        System.out.println("\n--- Crear Cita ---");
        try {
            String id = leerCampo("ID de la cita");
            if (id == null) return;

            // Valido que la fecha tenga el formato correcto con regex
            String fecha = leerCampoFormato(
                "Fecha (DD/MM/AAAA)",
                "^\\d{2}/\\d{2}/\\d{4}$",
                "Formato incorrecto. Use DD/MM/AAAA."
            );
            if (fecha == null) return;

            // Lo mismo para la hora
            String hora = leerCampoFormato(
                "Hora (HH:MM)",
                "^\\d{2}:\\d{2}$",
                "Formato incorrecto. Use HH:MM."
            );
            if (hora == null) return;

            String motivo = leerCampo("Motivo de la cita");
            if (motivo == null) return;

            // Antes de pedir el ID del doctor, muestro cuáles hay disponibles
            List<Doctor> doctores = gestorDoctores.listarDoctores();
            if (doctores.isEmpty()) {
                System.out.println("[Aviso] No hay doctores registrados. Registre un doctor primero.");
                return;
            }
            System.out.println("\nDoctores disponibles:");
            for (Doctor d : doctores) {
                System.out.println("  " + d.getId() + " - " + d.getNombre()
                                 + " (" + d.getEspecialidad() + ")");
            }
            System.out.print("ID del Doctor  : ");
            String idDoctor = scanner.nextLine().trim();

            // Igual para los pacientes
            List<Paciente> pacientes = gestorPacientes.listarPacientes();
            if (pacientes.isEmpty()) {
                System.out.println("[Aviso] No hay pacientes registrados. Registre un paciente primero.");
                return;
            }
            System.out.println("\nPacientes disponibles:");
            for (Paciente p : pacientes) {
                System.out.println("  " + p.getId() + " - " + p.getNombre());
            }
            System.out.print("ID del Paciente: ");
            String idPaciente = scanner.nextLine().trim();

            // Busco los objetos por ID para verificar que existen
            Doctor   doctor   = gestorDoctores.buscarDoctor(idDoctor);
            Paciente paciente = gestorPacientes.buscarPaciente(idPaciente);

            // Si alguno no se encontró, aviso y regreso al menú
            if (doctor == null || paciente == null) {
                System.out.println("[Error] Doctor o Paciente no encontrado. Verifique los IDs.");
                return;
            }

            Cita cita = new Cita(id, fecha, hora, motivo, doctor, paciente);
            gestorCitas.agregarCita(cita);
            gestorCitas.guardar();
            System.out.println(" Cita creada correctamente.");

        } catch (Exception e) {
            System.out.println("[Error] No se pudo crear la cita: " + e.getMessage());
        }
    }

    // Muestra todas las citas guardadas.
    // Si no hay ninguna, le aviso al usuario en lugar de
    // mostrar una lista vacía sin explicación.
    private void verCitas() {
        System.out.println("\n--- Lista de Citas ---");
        try {
            List<Cita> citas = gestorCitas.listarCitas();

            if (citas.isEmpty()) {
                System.out.println("  No hay citas registradas.");
            } else {
                System.out.println("Total de citas: " + citas.size());
                for (Cita c : citas) {
                    c.mostrarInfo();
                }
            }

        } catch (Exception e) {
            System.out.println("[Error] No se pudieron mostrar las citas: " + e.getMessage());
        }
    }

    // Pide un campo de texto al usuario y lo reintenta hasta 3 veces
    // si lo deja vacío. Regresa null si agota los intentos
    // para que el método que lo llama pueda volver al menú.
    private String leerCampo(String etiqueta) {
        int intentos = 0;

        while (intentos < 3) {
            System.out.print(etiqueta + ": ");
            try {
                String valor = scanner.nextLine().trim();
                if (!valor.isEmpty()) {
                    return valor;
                }
                System.out.println("[Error] El campo '" + etiqueta + "' no puede estar vacío.");
            } catch (Exception e) {
                System.out.println("[Error] Entrada no válida.");
            }
            intentos++;
        }

        System.out.println("[Aviso] Demasiados intentos fallidos. Volviendo al menú.");
        return null;
    }

    // Igual que leerCampo() pero además valida que el texto cumpla
    // con un formato específico usando una expresión regular.
    // Lo uso para fecha y hora para evitar que el usuario escriba
    // cualquier cosa en esos campos.
    private String leerCampoFormato(String etiqueta, String regex, String mensajeError) {
        int intentos = 0;

        while (intentos < 3) {
            System.out.print(etiqueta + ": ");
            try {
                String valor = scanner.nextLine().trim();
                if (valor.matches(regex)) {
                    return valor;
                }
                System.out.println("[Error] " + mensajeError);
            } catch (Exception e) {
                System.out.println("[Error] Entrada no válida.");
            }
            intentos++;
        }

        System.out.println("[Aviso] Demasiados intentos fallidos. Volviendo al menú.");
        return null;
    }
}
