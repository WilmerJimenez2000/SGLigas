<?php
/**
 * @file
 * Este archivo contiene un script PHP para mostrar equipos de una categoría en una base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_partido.php');
include('clases/clase_torneo.php');
include('clases/clase_jugador.php');
include('clases/clase_equipo.php');
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
    $id_torneo = isset($_POST['id_torneo']) ? intval(trim($_POST['id_torneo'])) : 0;
    $id_partido = isset($_POST['id_partido']) ? intval(trim($_POST['id_partido'])) : 0;
    $partido = isset($_POST['partido']) ? $_POST['partido'] : "";
    $estado = "jugado";
    $jugadores = isset($_POST['jugadores']) ? $_POST['jugadores'] : "";
    $opcion = isset($_POST['opcion']) ? trim($_POST['opcion']) : "";


    /*$id_partido=3;
    $id_torneo=1;
    $partido[0]=4;      //id_equipo_local
    $partido[1]=1;      //goles_local
    $partido[2]=1;      //goles_visitante
    $partido[3]=16;      //id_equipo_visitante

    $jugadores=[[1234567890,1,0,1,1,0,0],//CI,pj,rojas,amarillas,goles,goles_recibidos,autogoles
    [16,1,0,1,1,0,0]];
    $opcion="jugadores";*/



    // Crea instancias de las clases a ser usadas
    $objeto_torneo = new torneo();
    $objeto_partido = new partido();
    $objeto_jugador= new jugador();
    $objeto_equipo= new equipo();


    // Verifica si existe ese torneo
    $verificar = $objeto_torneo->verificar_ID($id_torneo, $conexion);
    if(!empty($verificar)){
        // Verificar si ya existen partidos creados en ese torneo
        $existen_partidos = $objeto_partido->mostrar_partidos($id_torneo,$conexion);
        if(!empty($existen_partidos)){
            if($opcion=="jugadores"){
                // Verifica si existe ese partido
                $verificar = $objeto_partido->verificar_ID($id_partido, $conexion);
                if(!empty($verificar)){
                    $estado_BD=$verificar['estado'];
                    // Verificar si ya existen partidos creados en ese torneo
                    switch ($estado_BD) {
                        case "porjugar":
                            echo json_encode(array('partido_por_jugar' => true));
                            break;
                        case "jugado":
                            $respuesta=$objeto_jugador->extraer_partidos_jugados($id_torneo,$id_partido,$conexion);
                            if($respuesta){
                                echo json_encode(array('existen_registros' => true));
                            } else {
                                if(!empty($jugadores)){
                                    //Insertar estadisticas de jugadores
                                    $respuesta=$objeto_jugador->insertar_actualizar_estadisticas_jugador($jugadores,$id_torneo,$id_partido,$conexion);
                                    if($respuesta){
                                        echo json_encode(array('success' => true));
                                    } else {
                                        echo json_encode(array('noInserto' => true));
                                    }
                                } else {
                                    echo json_encode(array('noHayDatos' => true));
                                }
                            }
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
            } else {
                if($opcion=="resultados"){
                    // Verifica si existe ese partido
                    $verificar = $objeto_partido->verificar_ID($id_partido, $conexion);
                    if(!empty($verificar)){
                        $estado_BD=$verificar['estado'];
                        // Verificar si ya existen partidos creados en ese torneo
                        switch ($estado_BD) {
                            case "porjugar":
                                if(!empty($partido)){ 
                                    // Actualizar partido en la base de datos
                                    $respuesta=$objeto_partido->modificar_partido($id_partido, $partido, $estado, $conexion);
                                    if ($respuesta) {
                                        $respuesta=$objeto_equipo->actualizar_estadisticas_equipo($partido, $id_torneo, $conexion);
                                        if($respuesta){
                                            echo json_encode(array('success' => true));
                                        } else {
                                            echo json_encode(array('no_insertor_estadistica' => true));
                                        }
                                    } else {
                                        echo json_encode(array('no_modifico_partido' => true));
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
                } else {
                    echo json_encode(array('no_existe_opcion' => true));
                }
            }
        } else {
            echo json_encode(array('no_existen_partidos' => true));
        }
    } else {
        echo json_encode(array('no_existe_torneo' => true));
    }

    // Cierra la conexión
    mysqli_close($conexion);
}

// Ejecuta la función principal para obtener información de equipos de una categoría
main();

?>