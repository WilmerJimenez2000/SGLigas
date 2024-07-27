<?php
/**
 * @file
 * Este archivo contiene un script PHP para mostrar las ligas de un presidente en la base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_liga.php');
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
 * Muestra las ligas de un presidente en la base de datos.
 */
function main() {
    // Obtiene la conexión a la base de datos
    $conexion = obtener_conexion();

    // Obtiene los datos ingresados por parte del cliente
    $correo_admin = isset($_POST['correo_admin']) ? trim($_POST['correo_admin']) : "";
    //$correo_admin='carlospnppm@hotmail.com';
    //$correo_admin='Martha Navarrete';

    // Crea instancias de las clases a ser usadas
    $objeto_usuario = new usuario();
    $objeto_liga = new liga();

    // Verifica si existe ese correo
    $verificar = $objeto_usuario->verificar_usuario($correo_admin, $conexion);
    if (!empty($verificar)){
        // Verifica si el correo pertenece a un presidente
        if ($verificar['tipo_usuario'] == "presidente") {
            // Obtiene los resultados
            $resultado = $objeto_liga->mostrar_liga_presidente($correo_admin, $conexion);

            // Envia datos al cliente
            if (!empty($resultado)) {
                echo json_encode(array('datos' => $resultado));
            } else {
                echo json_encode(array('datos' => false));
            }
        } else {
            echo json_encode(array('no_es_presidente' => true));
        }
        
    } else {
        echo json_encode(array('no_existe_usuario' => true));
    }

    // Cierra la conexión
    mysqli_close($conexion);
}

// Ejecuta la función principal para mostrar las ligas de un presidente
main();
?>