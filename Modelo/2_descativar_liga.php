<?php
/**
 * @file
 * Este archivo contiene un script PHP para eliminar una liga en una base de datos.
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
 * Inserta una nueva liga en la base de datos.
 */
function main() {
    // Obtiene la conexión a la base de datos
    $conexion = obtener_conexion();

    // Obtiene los datos ingresados por parte del cliente
    //$id_liga = isset($_POST['id_liga']) ? trim($_POST['id_liga']) : "";

    $id_liga=82;

    /*$nombre_liga = "WILSON MONGE3";
    $fecha_fundacion = "1980-01-10";
    $direccion = "Sebastián Moreno E2 -136 y Eloy Alfaro";
    $correo_admin = "carlospnppm@hotmail.com";*/

    // Crea instancias de las clases a ser usadas
    $objeto_liga = new liga();

    // Verifica si existe ese ID  de Liga
    $verificar = $objeto_liga->verificar_ID($id_liga, $conexion);
    // Verifica que los datos no estén vacíos
    if (!empty($id_liga)) {
        // Obtiene los resultados
        $resultado = $objeto_liga->desactivar_liga($id_liga, $conexion);

        if ($resultado) {
            // Enviar datos al cliente
            echo json_encode(array('success' => true));
        } else {
            echo json_encode(array('noInserto' => true));
        }                    
    } else {
        // Al menos una de las variables está vacía
        echo json_encode(array('no_existe_liga' => true));
    }

    // Cierra la conexión a la base de datos
    mysqli_close($conexion);
}

// Ejecuta la función principal para insertar una nueva liga
main();
?>