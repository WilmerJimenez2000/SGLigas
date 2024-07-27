<?php
/**
 * @file
 * Este archivo contiene un script PHP para manejar la autenticación de usuarios en el sistema basado 
 * en los datos ingresados previamente en una base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_usuario.php');
include("conexion.php");

/**
 * Obtiene la conexión a la base de datos.
 * @return mysqli La conexión a la base de datos.
 */
function obtener_conexion() {
    return conexion_DB();
}

/**
 * Realiza la autenticación del usuario y maneja el inicio de sesión.
 */
function main() {
    // Obtiene la conexión a la base de datos
    $conexion = obtener_conexion();

    // Obtiene los datos ingresados por parte del cliente
    $correo_usuario = isset($_POST['correo']) ? trim($_POST['correo']) : "";
    $contrasena = isset($_POST['password']) ? trim($_POST['password']) : "";

    /*$correo_usuario = "adminweb@gmail.com";
    $contrasena="adminweb";*/

    $correo_usuario="carlos2@gmail.com";
    $contrasena="12345";
    
    // Verifica que los datos no estén vacíos
    if (!empty($correo_usuario) && !empty($contrasena)) {
        // Crea una instancia de la clase Usuario
        $objeto_usuario = new usuario();

        // Consulta para verificar si el usuario existe
        $existe_usuario = $objeto_usuario->verificar_usuario($correo_usuario, $conexion);
        if (!empty($existe_usuario)) {
            // Obtiene la contraseña registrada en la BD
            $contrasena_encriptada = $existe_usuario['contraseña'];

            // Comprueba si la contraseña es correcta
            if (password_verify($contrasena, $contrasena_encriptada)) {
                    $response = array('success' => true);    
                    // Inicio de sesión exitoso y verificar si el usuario es presidente
                    if ($existe_usuario['tipo_usuario'] == "presidente") {
                        $response['correoAdmin'] = $existe_usuario['correo'];
                    }
                    echo json_encode($response);
            } else {
                // Contraseña incorrecta
                echo json_encode(array('success' => false));
            }
        } else {
            echo json_encode(array('correo' => false));
        }
    } else {
        // Al menos una de las variables está vacía
        echo json_encode(array('noHayDatos' => true));
    }

    // Cierra la conexión a la base de datos
    mysqli_close($conexion);
}

// Ejecuta la función principal para autenticar al usuario
main();
?>