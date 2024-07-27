<?php
/**
 * @file
 * Este archivo contiene un script PHP para mostrar información detallada de un jugador en una base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_jugador.php');
include('conexion.php');

/**
 * Obtiene la conexión a la base de datos.
 * @return mysqli La conexión a la base de datos.
 */
function obtener_conexion() {
    return conexion_DB();
}

/**
 * Obtiene información detallada de un equipo y la devuelve en formato JSON.
 */
function main() {
    // Obtiene la conexión a la base de datos
    $conexion = obtener_conexion();

    // Obtiene el ID del equipo ingresado por parte del cliente
    $CI = isset($_POST['CI']) ? trim($_POST['CI']) : "";
    
    //$CI = 1234567890;

    // Crea una instancia de la clase Jugador
    $objeto_jugador = new jugador();

    // Verifica si existe esa CI
    $verificar=$objeto_jugador->verificar_CI($CI,$conexion);
    if (!empty($verificar)) {
        // Obtiene los resultados para mostrar la informacion del jugador
        $resultado = $objeto_jugador->mostrar_informacion_jugador($CI, $conexion);
        if (!empty($resultado)) {
            // Devuelve la información detallada de un jugador en formato JSON
            //echo '<img src="' . $resultado['foto'] . '" alt="Imagen subida">';
            echo json_encode(array('datos' => $resultado));
        } else {
            echo json_encode(array('datos' => false));
        }
    } else {
        // Al menos una de las variables está vacía
        echo json_encode(array('no_existe_CI' => true));
    }
    // Cierra la conexión a la base de datos
    mysqli_close($conexion);
}

// Ejecuta la función principal para obtener información detallada de un jugador
main();
?>