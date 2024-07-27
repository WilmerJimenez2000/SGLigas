<?php
/**
 * @file
 * Este archivo contiene un script PHP para manejar la obtención de ligas con sus categorías en una base de datos.
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
 * Obtiene todas las ligas activas con sus categorías y las devuelve en formato JSON.
 */
function obtener_ligas_con_categorias() {
    // Obtiene la conexión a la base de datos
    $conexion = obtener_conexion();

    // Crea una instancia de la clase liga
    $objeto_liga = new liga();

    // Obtiene todas las ligas activas con sus categorías
    $ligas_con_categorias = $objeto_liga->obtener_ligas_con_categorias($conexion);
    echo json_encode(array('ligas' => $ligas_con_categorias));

    // Cierra la conexión a la base de datos
    mysqli_close($conexion);
}

// Ejecuta la función principal para obtener ligas con categorías
obtener_ligas_con_categorias();
?>
