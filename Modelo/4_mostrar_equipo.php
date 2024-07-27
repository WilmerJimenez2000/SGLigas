<?php
/**
 * @file
 * Este archivo contiene un script PHP para mostrar información detallada de un equipo en una base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_equipo.php');
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
    $id_equipo = isset($_POST['id_equipo']) ? trim($_POST['id_equipo']) : "";
    //$id_equipo = 1;

    // Verifica que los datos no estén vacíos
    if (!empty($id_equipo)) {
        // Crea una instancia de la clase Equipo
        $objeto_equipo = new equipo();

        // Obtiene los resultados para mostrar los equipos
        $resultado = $objeto_equipo->mostrar_equipo($id_equipo, $conexion);
        if (!empty($resultado)) {
            // Devuelve la información detallada del equipo en formato JSON
            echo json_encode(array('datos' => $resultado));
        } else {
            echo json_encode(array('datos' => false));
        }
    } else {
        // Al menos una de las variables está vacía
        echo json_encode(array('noHayDatos' => true));
    }
    // Cierra la conexión a la base de datos
    mysqli_close($conexion);
}

// Ejecuta la función principal para obtener información detallada de un equipo
main();

?>