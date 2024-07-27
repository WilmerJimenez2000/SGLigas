<?php
/**
 * @file
 * Este archivo contiene un script PHP para actualizar los datos de usuarios en una base de datos.
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
 * Actualiza los datos de un usuario registrado en la base de datos.
 */
function main() {
    // Obtiene la conexión a la base de datos
    $conexion = obtener_conexion();

    // Obtiene los datos ingresados por parte del cliente
    $correo_usuario = isset($_POST['correo']) ? trim($_POST['correo']) : "";
    $nombre = isset($_POST['nombre']) ? trim($_POST['nombre']) : "";
    $tipo = isset($_POST['tipo']) ? trim($_POST['tipo']) : "";

    /*$correo_usuario = "carlos2@gmail.com";
    $nombre = "Carlos";
    $tipo="hincha";
    $contrasena="12345";*/

    // Verifica que los datos no estén vacíos
    if (!empty($correo_usuario) && !empty($nombre) && !empty($tipo)) {
        // Crea una instancia de la clase Usuario
        $objeto_usuario = new usuario();

        // Consulta para verificar que ese usuario existe
        $existe_usuario = $objeto_usuario->verificar_usuario($correo_usuario, $conexion);
        if (!empty($existe_usuario)) {
            // Consulta para actualizar los datos del usuario
            $resultado = $objeto_usuario->actualizar_usuario($nombre, $tipo, "", $correo_usuario, $conexion);

            // Verifica que se actualizo los datos del usuario
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

// Ejecuta la función principal para actualizar los datos un nuevo usuario
main();
?>