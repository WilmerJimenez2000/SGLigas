<?php
/**
 * @file
 * Este archivo contiene un script PHP para actualizar la contraseña de usuarios en una base de datos.
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
 * Actualizar la contraseña de un usuario registrado en la base de datos.
 */
function main() {
    // Obtiene la conexión a la base de datos
    $conexion = obtener_conexion();

    // Obtiene los datos ingresados por parte del cliente
    $correo_usuario = isset($_POST['correo']) ? trim($_POST['correo']) : "";
    $contrasena_nueva = isset($_POST['password']) ? trim($_POST['password']) : "";

    /*$correo_usuario = "carlos2@gmail.com";
    $contrasena_nueva="12345";*/

    // Verifica que los datos no estén vacíos
    if (!empty($correo_usuario) && !empty($contrasena_nueva)) {
        // Crea una instancia de la clase Usuario
        $objeto_usuario = new usuario();

        // Consulta para verificar que ese usuario existe
        $existe_usuario = $objeto_usuario->verificar_usuario($correo_usuario, $conexion);
        if (!empty($existe_usuario)) {
            // Encripta el password
            $pass_fuerte = password_hash($contrasena_nueva, PASSWORD_DEFAULT);

            // Consulta para actualizar la contraseña del usuario
            $resultado = $objeto_usuario->actualizar_usuario("", "", $pass_fuerte, $correo_usuario, $conexion);
            // Verifica que se actualizo la contraseña
            if ($resultado) {
                echo json_encode(array('success' => true));
            } else {
                echo json_encode(array('noInserto' => true));
            }
        } else {
            echo json_encode(array('success' => false)); // El usuario no existe y no se puede modificar
        }
    } else {
        // Al menos una de las variables está vacía
        echo json_encode(array('noHayDatos' => true));
    }

    // Cierra la conexión a la base de datos
    mysqli_close($conexion);
}

// Ejecuta la función principal para actualizar la contraseña de un usuario
main();
?>