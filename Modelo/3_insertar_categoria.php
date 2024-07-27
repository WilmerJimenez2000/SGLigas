<?php
/**
 * @file
 * Este archivo contiene un script PHP para manejar la inserción de categorías en una base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_categoria.php');
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
 * Obtiene los datos ingresados por el cliente y realiza la inserción de la categoría.
 */
function main() {
    // Obtiene la conexión a la base de datos
    $conexion = obtener_conexion();

    // Obtiene los datos ingresados por parte del cliente
    $nombre = isset($_POST['categoria']) ? trim($_POST['categoria']) : "";
    $num_equipos = isset($_POST['num_equipos']) ? trim($_POST['num_equipos']) : "";
    $id_liga = isset($_POST['id_liga']) ? trim($_POST['id_liga']) : "";

    /*$nombre='Maxima';
    $num_equipos=4;
    $id_liga=63;*/

    // Crea instancias de las clases a ser usadas
    $objeto_liga = new liga();
    $objeto_categoria = new categoria();

    // Verifica si existe ese ID de liga
    $id_liga = $objeto_liga->verificar_ID($id_liga, $conexion);
    if ($id_liga > 0) {
        // Verifica que los datos no estén vacíos
        if (!empty($nombre) && !empty($num_equipos) && !empty($id_liga)) {
            // Verifica que no exista una categoría con el mismo nombre en la liga
            $verificar = $objeto_categoria->verificar_nombre_categoria($nombre, $id_liga, $conexion);
            if (empty($verificar)) {
                // Inserta la nueva categoría en la base de datos
                $resultado = $objeto_categoria->insertar_categoria($nombre, $num_equipos, $id_liga, $conexion);
                if ($resultado) {
                    echo json_encode(array('success' => true));
                } else {
                    echo json_encode(array('noInserto' => true));
                }
            } else {
                echo json_encode(array('existe_nombre' => true));
            }
        } else {
            // Al menos una de las variables está vacía
            echo json_encode(array('noHayDatos' => true));
        }
    } else {
        echo json_encode(array('no_existe_liga' => true));
    }

    // Cierra la conexión a la base de datos
    mysqli_close($conexion);
}

// Ejecuta la función principal
main();
?>