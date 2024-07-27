<?php
/**
 * @file
 * Este archivo contiene un script PHP para mostrar equipos de una categoría en una base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_partido.php');
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
 * Obtiene la información de los equipos de una categoría y la devuelve en formato JSON.
 */
function main() {
    // Obtiene la conexión a la base de datos
    $conexion = obtener_conexion();

    // Obtiene los datos ingresados por parte del cliente
    $id_partido = isset($_POST['id_partido']) ? intval(trim($_POST['id_partido'])) : 0;
    $id_torneo = isset($_POST['id_torneo']) ? intval(trim($_POST['id_torneo'])) : 0;
  


    // Crea instancias de las clases a ser usadas
    $objeto_partido = new partido();
    $objeto_jugador= new jugador();

    // Verifica si existe ese partido
    $verificar = $objeto_partido->verificar_ID($id_partido, $conexion);
    if(!empty($verificar)){
        $estado_BD=$verificar['estado'];
        // Verificar el estado del partido
        switch ($estado_BD) {
            case "jugado":
                $respuesta=$objeto_jugador->extraer_partidos_jugados($id_torneo,$id_partido,$conexion);
                if($respuesta){
                    echo json_encode(array('existen_registros' => true));
                } else {
                    echo json_encode(array('no_existen_registros' => true));
                }
                break;
            case "porjugar":
                echo json_encode(array('partido_por_jugar' => true));
                break;
            case "programar":
                echo json_encode(array('partido_no_programado' => true));
                break;
            case "posponer":
                echo json_encode(array('partido_pospuesto' => true));
                break;
            default:
                echo json_encode(array('error' => true));
        }
    } else {
        echo json_encode(array('no_existe_partido' => true));
    }
    // Cierra la conexión
    mysqli_close($conexion);
}

// Ejecuta la función principal para obtener información de equipos de una categoría
main();

?>