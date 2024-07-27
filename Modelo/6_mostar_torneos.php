<?php
/**
 * @file
 * Este archivo contiene un script PHP para mostrar todas las ligas en la base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_torneo.php');
include('clases/clase_categoria.php');
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
    $id_categoria = isset($_POST['id_categoria']) ? trim($_POST['id_categoria']) : "";
    //$id_categoria=1;

    // Crea una instancia de la clase liga    
    $objeto_torneo = new torneo();
    $objeto_categoria = new categoria();

    // Verifica si existe ese ID de liga
    $id_categoria = $objeto_categoria->verificar_ID($id_categoria, $conexion);
    if ($id_categoria > 0) {
        // Obtiene los resultados
        $resultado = $objeto_torneo->mostrar_torneos($id_categoria, $conexion);
        // Envia datos al cliente
        if ($resultado != 0) {
            echo json_encode(array('datos' => $resultado));
        } else {
            echo json_encode(array('datos' => false));
        }
    } else {
        echo json_encode(array('no_existe_categoria' => true));
    }
    // Cierra la conexión
    mysqli_close($conexion);
    
}

// Ejecuta la función principal para mostrar todas las ligas
main();

?>