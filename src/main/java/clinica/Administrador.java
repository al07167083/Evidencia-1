package clinica;

// Esta clase representa al usuario con acceso al sistema.
// La idea es que solo alguien con el usuario y contraseña correctos
// pueda entrar y usar el programa.
public class Administrador {

    private String username;
    private String password;

    public Administrador(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Compara lo que escribió el usuario con las credenciales guardadas.
    // Regresa true si coinciden, false si no.
    // Lo uso en el login para decidir si dejo pasar o no.
    public boolean validarCredenciales(String u, String p) {
        return username.equals(u) && password.equals(p);
    }
}
