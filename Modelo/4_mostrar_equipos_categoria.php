<?php
/**
 * @file
 * Este archivo contiene un script PHP para mostrar equipos de una categoría en una base de datos.
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
 * Obtiene la información de los equipos de una categoría y la devuelve en formato JSON.
 */
function main() {
    // Obtiene la conexión a la base de datos
    $conexion = obtener_conexion();

    // Obtiene el ID de la categoría ingresado por parte del cliente
    $id_categoria = isset($_POST['id_categoria']) ? trim($_POST['id_categoria']) : "";
    //$id_categoria = 2;

    // Verifica que los datos no estén vacíos
    if (!empty($id_categoria)) {
        // Crea una instancia de la clase Equipo
        $objeto_equipo = new equipo();

        // Obtiene los resultados
        $resultado = $objeto_equipo->mostrar_equipos_categoria($id_categoria, $conexion);

        // Envia los datos al cliente en formato JSON
        if ($resultado != 0) {
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

// Ejecuta la función principal para obtener información de equipos de una categoría
main();

?>