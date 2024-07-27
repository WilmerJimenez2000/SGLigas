<?php
/**
 * @file
 * Este archivo contiene un script PHP para manejar la inserción de categorías en una base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_equipo.php');
include('clases/clase_jugador.php');
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
    $CI = isset($_POST['CI']) ? trim($_POST['CI']) : "";
    $nombre = isset($_POST['nombre']) ? trim($_POST['nombre']) : "";
    $posicion = isset($_POST['posicion']) ? trim($_POST['posicion']) : "";
    $fecha_nacimiento = isset($_POST['fecha_nacimiento']) ? trim($_POST['fecha_nacimiento']) : "";
    $foto = isset($_POST['foto']) ? trim($_POST['foto']) : "";
    $estatura = isset($_POST['estatura']) ? trim($_POST['estatura']) : "";
    $num_camiseta = isset($_POST['num_camiseta']) ? trim($_POST['num_camiseta']) : "";
    $id_equipo = isset($_POST['id_equipo']) ? trim($_POST['id_equipo']) : "";
    $estado = "activo";

    /*$CI="1534567896";
    $nombre="Pele";
    $posicion="defensor";
    $fecha_nacimiento="30-04-1992";
    $foto="aa";
    $estatura="187";//la estatura en cm
    $id_equipo=4;
    $num_camiseta=2;*/

    // Crea instancias de las clases a ser usadas
    $objeto_equipo = new equipo();
    $objeto_jugador = new jugador();

    // Verifica si existe ese ID de liga
    $id_equipo = $objeto_equipo->verificar_ID($id_equipo, $conexion);
    if ($id_equipo > 0) {
        // Verificar si la CI de ese jugador no existe
        $verificar=$objeto_jugador->verificar_CI($CI,$conexion);
        if(empty($verificar)){
            // Verifica que los datos no estén vacíos
            if (!empty($nombre) && !empty($posicion) && !empty($fecha_nacimiento) && !empty($foto) && !empty($estatura) && !empty($num_camiseta)) {
                // Inserta el nuevo torneo en la base de datos
                $verificar=$objeto_jugador->verificar_camiseta($id_equipo, $num_camiseta, $conexion);
                if($verificar == 0){
                    $resultado = $objeto_jugador->insertar_jugador($CI, $nombre, $posicion, $foto, $fecha_nacimiento, $estatura, $id_equipo, $estado, $num_camiseta, $conexion);
                    if ($resultado) {
                        echo json_encode(array('success' => true));
                    } else {
                        echo json_encode(array('noInserto' => true));
                    }
                } else {
                    // Al menos una de las variables está vacía
                    echo json_encode(array('exite_numero_camiseta' => true));
                }
            } else {
                // Al menos una de las variables está vacía
                echo json_encode(array('noHayDatos' => true));
            }
        } else {
            echo json_encode(array('existe_jugador' => true));
        }
        
    } else {
        echo json_encode(array('no_existe_equipo' => true));
    }

    // Cierra la conexión a la base de datos
    mysqli_close($conexion);
}

// Ejecuta la función principal
main();

?>