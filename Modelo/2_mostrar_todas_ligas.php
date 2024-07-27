<?php
/**
 * @file
 * Este archivo contiene un script PHP para mostrar todas las ligas en la base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_liga.php');
include('conexion.php');

/**
 * Obtiene la conexión a la base de datos.
 * @return mysqli La conexión a la base de datos.
 */
function obtener_conexion() {
    return conexion_DB();
}

/**
 * Muestra todas las ligas en la base de datos.
 */
function main() {
    // Obtiene la conexión a la base de datos
    $conexion = obtener_conexion();

    // Crea una instancia de la clase liga    
    $objeto_liga = new liga();

    // Obtiene los resultados
    $resultado = $objeto_liga->mostrar_todas_ligas('', $conexion);

    // Cierra la conexión
    mysqli_close($conexion);

    // Envia datos al cliente
    if ($resultado != 0) {
        echo json_encode(array('datos' => $resultado));
    } else {
        echo json_encode(array('datos' => false));
    }
}

// Ejecuta la función principal para mostrar todas las ligas
main();

?>