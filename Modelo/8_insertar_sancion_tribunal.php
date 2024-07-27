<?php
/**
 * @file
 * Este archivo contiene un script PHP para manejar la inserción de categorías en una base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_sanciones.php');
include('clases/clase_partido.php');
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
    $id_partido = isset($_POST['id_partido']) ? intval(trim($_POST['id_partido'])) : 0;
    $tribunal_local = isset($_POST['informe_local']) ? trim($_POST['informe_local']) : "";
    $tribunal_visitante = isset($_POST['informe_visitante']) ? trim($_POST['informe_visitante']) : "";

    /*$id_partido = 2;
    $tribunal_local = "TA #18 05-35";
    $tribunal_visitante = "TA #50 TR#15 texto vario";*/

    // Crea instancias de las clases a ser usadas
    $objeto_sanciones = new sanciones();
    $objeto_partido = new partido();

    // Verifica si existe ese ID de partido
    $verificar = $objeto_partido->verificar_ID($id_partido, $conexion);
    if (!empty($verificar)){
        $estado=$verificar['estado'];
        // Verificar el esatdo del partido
        if($estado==='jugado'){
            $verificar=$objeto_sanciones->verificar_sancion($id_partido, $conexion);
            if($verificar>0){
                if(!empty($tribunal_local) & !empty($tribunal_visitante)){ 
                    // Inseratar sanciones en la base de datos
                    $respuesta=$objeto_sanciones->actualizar_tribunal($id_partido, $tribunal_local, $tribunal_visitante, $conexion);
                    if ($respuesta) {
                        echo json_encode(array('success' => true));
                    } else {
                        echo json_encode(array('noInserto' => true));
                    }
                } else {
                    echo json_encode(array('noHayDatos' => true));
                }
            } else {
                echo json_encode(array('no_existe_sancion' => true));
            }
        } else {
            echo json_encode(array('partido_no_jugado' => true));
        }
    } else {
        echo json_encode(array('no_existe_partido' => true));
    }

    // Cierra la conexión a la base de datos
    mysqli_close($conexion);
}

// Ejecuta la función principal
main();

?>