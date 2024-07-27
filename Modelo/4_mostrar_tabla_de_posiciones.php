<?php
/**
 * @file
 * Este archivo contiene un script PHP para mostrar equipos de una categoría en una base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_partido.php');
include('clases/clase_torneo.php');
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
 * Obtiene la información de los equipos de una categoría y la devuelve en formato JSON.
 */
function main() {
    // Obtiene la conexión a la base de datos
    $conexion = obtener_conexion();

    // Obtiene los datos ingresados por parte del cliente
    $id_torneo = isset($_POST['id_torneo']) ? trim($_POST['id_torneo']) : "";
    //$id_torneo=1;

    // Crea instancias de las clases a ser usadas
    $objeto_torneo = new torneo();
    $objeto_equipo = new equipo();

    // Verifica si existe ese torneo
    $verificar = $objeto_torneo->verificar_ID($id_torneo, $conexion);
    if(!empty($verificar)){
        $resultado = $objeto_equipo->mostrar_tabla_de_posiciones($id_torneo, $conexion);
        if(!empty($resultado)){
            echo json_encode(array('tabla' => $resultado));
        } else {
            echo json_encode(array('no_existe_partidos_equipo' => true));
        }
    } else {
        echo json_encode(array('no_existe_torneo' => true));
    }

    // Cierra la conexión
    mysqli_close($conexion);
}

// Ejecuta la función principal para obtener información de equipos de una categoría
main();

?>