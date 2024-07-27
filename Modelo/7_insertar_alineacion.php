<?php
/**
 * @file
 * Este archivo contiene un script PHP para mostrar equipos de una categoría en una base de datos.
 */

// Incluye las clases y archivos necesarios
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
 * Obtiene la información de los equipos de una categoría y la devuelve en formato JSON.
 */
function main() {
    // Obtiene la conexión a la base de datos
    $conexion = obtener_conexion();

    // Obtiene los datos ingresados por parte del cliente
    $id_partido = isset($_POST['id_partido']) ? intval(trim($_POST['id_partido'])) : 0;
    $alineacion = isset($_POST['alineacion']) ? $_POST['alineacion'] : "";

    /*$id_partido=4;
    $alineacion='[{"equipo":"Local","escudo":"escudo1","estado":"Invisible","fondo":"No","nombre":"Jugador i"},
    {"equipo":"Local","escudo":"escudo2","estado":"Invisible","fondo":"No","nombre":"Jugador i"},
    {"equipo":"Local","escudo":"escudo3","estado":"Invisible","fondo":"No","nombre":"Jugador i"},
    {"equipo":"Local","escudo":"https://s3-sgligas.s3.amazonaws.com/uploads_jugadores/jugador.png","estado":"Visible","fondo":"No","nombre":"Jugador1"},
    {"equipo":"Local","escudo":"escudo5","estado":"Invisible","fondo":"No","nombre":"Jugador i"},
    {"equipo":"Local","escudo":"escudo6","estado":"Invisible","fondo":"No","nombre":"Jugador i"},
    {"equipo":"Local","escudo":"escudo7","estado":"Invisible","fondo":"No","nombre":"Jugador i"},
    {"equipo":"Local","escudo":"escudo8","estado":"Invisible","fondo":"No","nombre":"Jugador i"},
    {"equipo":"Local","escudo":"escudo9","estado":"Invisible","fondo":"No","nombre":"Jugador i"},
    {"equipo":"Local","escudo":"escudo10","estado":"Invisible","fondo":"No","nombre":"Jugador i"},
    {"equipo":"Local","escudo":"escudo11","estado":"Invisible","fondo":"No","nombre":"Jugador i"},
    {"equipo":"Local","escudo":"escudo12","estado":"Invisible","fondo":"No","nombre":"Jugador i"},
    {"equipo":"Local","escudo":"escudo13","estado":"Invisible","fondo":"No","nombre":"Jugador i"}]';*/

    // Crea instancias de las clases a ser usadas
    $objeto_partido = new partido();

    // Verifica si existe ese partido
    $verificar = $objeto_partido->verificar_ID($id_partido, $conexion);
    if(!empty($verificar)){
        $estado_BD=$verificar['estado'];
        // Verificar el estado del partido
        switch ($estado_BD) {
            case "porjugar":
                if(!empty($alineacion)){
                    $verfificar_id_partido=$objeto_partido->verificar_existencia_alineacion($id_partido, $conexion);
                    if(!empty($verfificar_id_partido)){
                        echo json_encode(array('existe_alienacion' => true));
                    } else {
                        $respuesta=$objeto_partido->insertar_alineacion($alineacion,$id_partido,$conexion);
                        if($respuesta){
                            echo json_encode(array('success' => true));
                        } else {
                            echo json_encode(array('noInserto' => true));
                        }
                    }
                } else {
                    echo json_encode(array('noHayDatos' => true));
                }
                break;
            case "jugado":
                echo json_encode(array('partido_jugado' => true));                
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