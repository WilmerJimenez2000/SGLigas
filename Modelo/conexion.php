<?php
/**
 * @file
 * Este archivo contiene funciones para establecer la conexión a una base de datos y configurar encabezados CORS.
 */

/**
 * Establece encabezados CORS para permitir solicitudes desde cualquier origen.
 */
function configurar_CORS() {
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept');
    header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE');
}
configurar_CORS();
/**
 * Establece una conexión a la base de datos y devuelve la instancia de conexión.
 * @return mysqli|false La instancia de conexión a la base de datos o false en caso de error.
 */
function conexion_DB() {
    $server = "brzdpx5hq52w25hlgmwk-mysql.services.clever-cloud.com"; 
    $user = "ubkfifhwkdhvld6s"; 
    //$pass = "26VWkDo2fBwaxMJ1T8sa"; 

    $pass = "XZ923BQyATpQOZ87r72s"; 
    $db = "brzdpx5hq52w25hlgmwk"; 

    $conectar = mysqli_connect($server, $user, $pass, $db);
    if (!$conectar) {
        die("Error de conexión: " . mysqli_connect_error());
        return false;
    }

    return $conectar;
}
