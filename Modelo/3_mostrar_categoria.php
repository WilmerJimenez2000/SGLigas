<?php
/**
 * @file
 * Este archivo contiene un script PHP para manejar la obtención de categorías de una liga en una base de datos.
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
 * Obtiene las categorías de una liga y las devuelve en formato JSON.
 */
function obtener_categorias() {
    // Obtiene la conexión a la base de datos
    $conexion = obtener_conexion();

    // Obtiene el ID de la liga ingresado por parte del cliente
    $id_liga = isset($_POST['id_liga']) ? trim($_POST['id_liga']) : "";
    //$id_liga=77;

    // Verifica que los datos no estén vacíos
    if (!empty($id_liga)) {
        // Crea instancias de las clases a ser usadas
        $objeto_liga = new liga();
        $objeto_categoria = new categoria();

        // Verifica si existe el ID de liga ingresado
        $id_liga = $objeto_liga->verificar_ID($id_liga, $conexion);
        if ($id_liga > 0) {
            // Obtiene los resultados
            $resultado = $objeto_categoria->obtener_categorias($id_liga, $conexion);
            echo json_encode(array('datos' => $resultado));//Retorna un array vacio si no hay datos
        } else {
            echo json_encode(array('no_existe_liga' => true));
        }
    } else {
        // Al menos una de las variables está vacía
        echo json_encode(array('noHayDatos' => true));
    }

    // Cierra la conexión a la base de datos
    mysqli_close($conexion);
}

// Ejecuta la función principal para obtener categorías
obtener_categorias();
