<?php
/**
 * @file
 * Este archivo contiene un script PHP para manejar la inserción de usuarios en una base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_usuario.php');
include('conexion.php');

/**
 * Obtiene la conexión a la base de datos.
 * @return mysqli La conexión a la base de datos.
 */
function obtener_conexion() {
    return conexion_DB();
}

/**
 * Inserta un nuevo usuario en la base de datos.
 */
function main() {
    // Obtiene la conexión a la base de datos
    $conexion = obtener_conexion();

    // Obtiene los datos ingresados por parte del cliente
    $correo_usuario = isset($_POST['correo']) ? trim($_POST['correo']) : "";
    $nombre = isset($_POST['nombre']) ? trim($_POST['nombre']) : "";
    $contrasena = isset($_POST['password']) ? trim($_POST['password']) : "";
    $tipo = 'hincha';

    /*$correo_usuario = "carlos2@gmail.com";
    $nombre = "andres";
    $contrasena="1234";*/

    // Verifica que los datos no estén vacíos
    if (!empty($correo_usuario) && !empty($nombre) && !empty($contrasena)) {
        // Crea una instancia de la clase Usuario
        $objeto_usuario = new usuario();

        // Consulta para verificar que ese usuario no existe
        $existe_usuario = $objeto_usuario->verificar_usuario($correo_usuario, $conexion);
        if (empty($existe_usuario)) {
            // Encripta el password
            $pass_fuerte = password_hash($contrasena, PASSWORD_DEFAULT);

            // Consulta para insertar usuario
            $resultado = $objeto_usuario->insertar_usuario($correo_usuario, $nombre, $pass_fuerte, $tipo, $conexion);
            // Verifica que se insertó el usuario
            if ($resultado) {
                echo json_encode(array('success' => true));
            } else {
                echo json_encode(array('noInserto' => true));
            }
        } else {
            echo json_encode(array('success' => false)); // El usuario ya existe y no se puede insertar
        }
    } else {
        // Al menos una de las variables está vacía
        echo json_encode(array('noHayDatos' => true));
    }

    // Cierra la conexión a la base de datos
    mysqli_close($conexion);
}

// Ejecuta la función principal para insertar un nuevo usuario
main();
?>