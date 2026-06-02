package clinica;

// Punto de entrada del programa.
// Solo crea el sistema y lo arranca, nada más.
// Toda la lógica está en SistemaClinico.
public class Main {

    public static void main(String[] args) {
        SistemaClinico sistema = new SistemaClinico();
        sistema.iniciar();
    }
}
