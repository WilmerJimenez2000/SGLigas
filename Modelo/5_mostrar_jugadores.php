<?php
/**
 * @file
 * Este archivo contiene un script PHP para mostrar todas las ligas en la base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_jugador.php');
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
 * Muestra todas las ligas en la base de datos.
 */
function main() {
    // Obtiene la conexión a la base de datos
    $conexion = obtener_conexion();

    // Obtiene los datos ingresados por parte del cliente
    $id_equipo = isset($_POST['id_equipo']) ? trim($_POST['id_equipo']) : "";
    //$id_equipo=3;

    // Crea una instancia de la clase liga    
    $objeto_jugador = new jugador();
    $objeto_equipo = new equipo();

    // Verifica si existe ese ID de liga
    $id_equipo = $objeto_equipo->verificar_ID($id_equipo, $conexion);
    if ($id_equipo > 0) {
        // Obtiene los resultados
        $resultado = $objeto_jugador->mostrar_jugadores($id_equipo, $conexion);
        // Envia datos al cliente
        if ($resultado != 0) {
            echo json_encode(array('datos' => $resultado));
        } else {
            echo json_encode(array('datos' => false));
        }
    } else {
        echo json_encode(array('no_existe_equipo' => true));
    }
    // Cierra la conexión
    mysqli_close($conexion);
    
}

// Ejecuta la función principal para mostrar todas las ligas
main();

?>