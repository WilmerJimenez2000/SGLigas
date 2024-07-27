<?php
/**
 * @file
 * Este archivo contiene un script PHP para gestionar la inserción de ligas en una base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_liga.php');
include('clases/clase_usuario.php');
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
    $nombre_liga = isset($_POST['nombre_liga']) ? trim($_POST['nombre_liga']) : "";
    $fecha_fundacion = isset($_POST['fecha_fundacion']) ? trim($_POST['fecha_fundacion']) : "";
    $direccion = isset($_POST['direccion']) ? trim($_POST['direccion']) : "";
    $correo_admin = isset($_POST['correo_admin']) ? trim($_POST['correo_admin']) : "";
    $estado="activo";

    /*$nombre_liga = "WILSON MONGE3";
    $fecha_fundacion = "1980-01-10";
    $direccion = "Sebastián Moreno E2 -136 y Eloy Alfaro";
    $correo_admin = "carlospnppm@hotmail.com";*/

    // Verifica que los datos no estén vacíos
    if (!empty($nombre_liga) && !empty($fecha_fundacion) && !empty($direccion) && !empty($correo_admin)) {
        // Crea instancias de las clases a ser usadas
        $objeto_usuario = new usuario();
        $objeto_liga = new liga();

        // Verifica si existe ese correo
        $verificar = $objeto_usuario->verificar_usuario($correo_admin, $conexion);
        if (!empty($verificar)) {
            // Verifica si el usuario es presidente
            if ($verificar['tipo_usuario'] == "presidente") {
                // Verifica si el correo no tiene una liga registrada
                $verificar = $objeto_liga->mostrar_liga_presidente($correo_admin, $conexion);

                if (empty($verificar)) {
                    // Verifica si existe una liga con el nombre ingresado
                    $verificar = $objeto_liga->mostrar_todas_ligas($nombre_liga, $conexion);

                    if (empty($verificar)) {
                        // Obtiene los resultados
                        $resultado = $objeto_liga->insertar_liga($nombre_liga, $fecha_fundacion, $direccion, $correo_admin, $conexion);

                        if ($resultado) {
                            // Obtiene los datos de la liga insertada
                            $liga_insertada = $objeto_liga->mostrar_liga_presidente($correo_admin, $conexion);

                            // Enviar datos al cliente
                            echo json_encode(array('success' => true, 'datos' => $liga_insertada));
                        } else {
                            echo json_encode(array('noInserto' => true));
                        }
                    } else {
                        // Existe una liga registrada con ese nombre
                        echo json_encode(array('existe_nombre' => true));
                    }
                } else {
                    // Existe una liga registrada con el correo ingresado
                    echo json_encode(array('existe_registro' => true));
                }
            } else {
                echo json_encode(array('no_es_presidente' => true));
            }
        } else {
            echo json_encode(array('no_existe_usuario' => true));
        }
    } else {
        // Al menos una de las variables está vacía
        echo json_encode(array('noHayDatos' => true));
    }

    // Cierra la conexión a la base de datos
    mysqli_close($conexion);
}

// Ejecuta la función principal para insertar una nueva liga
main();
?>