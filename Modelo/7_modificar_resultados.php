<?php
/**
 * @file
 * Este archivo contiene un script PHP para mostrar equipos de una categoría en una base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_partido.php');
include('clases/clase_torneo.php');
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
 * Obtiene la información de los equipos de una categoría y la devuelve en formato JSON.
 */
function main() {
    // Obtiene la conexión a la base de datos
    $conexion = obtener_conexion();

    // Obtiene los datos ingresados por parte del cliente
    $id_torneo = isset($_POST['id_torneo']) ? trim($_POST['id_torneo']) : "";
    $num_jornada = isset($_POST['num_jornada']) ? trim($_POST['num_jornada']) : "";
    $partidos = isset($_POST['partidos']) ? $_POST['partidos'] : "";
    $jugadores = isset($_POST['jugadores']) ? $_POST['jugadores'] : "";
    $opcion = isset($_POST['opcion']) ? trim($_POST['opcion']) : "";

    // Crea instancias de las clases a ser usadas
    $objeto_torneo = new torneo();
    $objeto_partido = new partido();
    $objeto_equipo = new equipo();
    $objeto_jugador= new jugador();

    //$id_torneo=1;
    //$num_jornada=2;

    /*$jugadores=[[1234567890,1,0,1,1,0,0,1],
    [16,1,0,1,1,0,0,1]];*/

    /*$opcion="resultados";

    
    $partidos[0][0]=1;
    $partidos[0][1]=""; // Nombre equipo local
    $partidos[0][2]=""; // Escudo equipo local
    $partidos[0][3]=27; // Id equipo local
    $partidos[0][4]=1; // Goles equipo local
    $partidos[0][5]=3; // Goles equipo visitante
    $partidos[0][6]=24; // Id equipo visitante
    $partidos[0][7]=""; // Nombre equipo visitante
    $partidos[0][8]=""; // Escudo equipo visitante
    $partidos[0][9]="1970-01-01"; // Fecha del partido
    $partidos[0][10]="00:00"; // Hora del partido
    $partidos[0][11]="vocal"; 
    $partidos[0][12]="veedor";
    $partidos[0][13]="canchaasdasdasds";*/

    /*$partidos[1][0]=2;
    $partidos[1][1]=""; // Nombre equipo local
    $partidos[1][2]=""; // Escudo equipo local
    $partidos[1][3]=5; // Id equipo local
    $partidos[1][4]="2"; // Goles equipo local
    $partidos[1][5]="1"; // Goles equipo visitante
    $partidos[1][6]=27; // Id equipo visitante
    $partidos[1][7]=""; // Nombre equipo visitante
    $partidos[1][8]=""; // Escudo equipo visitante
    $partidos[1][9]="1970-01-01"; // Fecha del partido
    $partidos[1][10]="00:00"; // Hora del partido
    $partidos[1][11]="vocal"; 
    $partidos[1][12]="veedor";
    $partidos[1][13]="cancha";

    $partidos[2][0]=3;
    $partidos[2][1]=""; // Nombre equipo local
    $partidos[2][2]=""; // Escudo equipo local
    $partidos[2][3]=4; // Id equipo local
    $partidos[2][4]="2"; // Goles equipo local
    $partidos[2][5]="2"; // Goles equipo visitante
    $partidos[2][6]=16; // Id equipo visitante
    $partidos[2][7]=""; // Nombre equipo visitante
    $partidos[2][8]=""; // Escudo equipo visitante
    $partidos[2][9]="1970-01-01"; // Fecha del partido
    $partidos[2][10]="00:00"; // Hora del partido
    $partidos[2][11]="vocal"; 
    $partidos[2][12]="veedor";
    $partidos[2][13]="cancha";*/

    /*$partidos[2][0]=4;
    $partidos[2][1]=""; // Nombre equipo local
    $partidos[2][2]=""; // Escudo equipo local
    $partidos[2][3]=9; // Id equipo local
    $partidos[2][4]="2"; // Goles equipo local
    $partidos[2][5]="2"; // Goles equipo visitante
    $partidos[2][6]=20; // Id equipo visitante
    $partidos[2][7]=""; // Nombre equipo visitante
    $partidos[2][8]=""; // Escudo equipo visitante
    $partidos[2][9]="1970-01-01"; // Fecha del partido
    $partidos[2][10]="00:00"; // Hora del partido
    $partidos[2][11]="vocal"; 
    $partidos[2][12]="veedor";
    $partidos[2][13]="cancha";

    $partidos[2][0]=5;
    $partidos[2][1]=""; // Nombre equipo local
    $partidos[2][2]=""; // Escudo equipo local
    $partidos[2][3]=14; // Id equipo local
    $partidos[2][4]="2"; // Goles equipo local
    $partidos[2][5]="2"; // Goles equipo visitante
    $partidos[2][6]=17; // Id equipo visitante
    $partidos[2][7]=""; // Nombre equipo visitante
    $partidos[2][8]=""; // Escudo equipo visitante
    $partidos[2][9]="1970-01-01"; // Fecha del partido
    $partidos[2][10]="00:00"; // Hora del partido
    $partidos[2][11]="vocal"; 
    $partidos[2][12]="veedor";
    $partidos[2][13]="cancha";

    $partidos[2][0]=6;
    $partidos[2][1]=""; // Nombre equipo local
    $partidos[2][2]=""; // Escudo equipo local
    $partidos[2][3]=15; // Id equipo local
    $partidos[2][4]="2"; // Goles equipo local
    $partidos[2][5]="2"; // Goles equipo visitante
    $partidos[2][6]=16; // Id equipo visitante
    $partidos[2][7]=""; // Nombre equipo visitante
    $partidos[2][8]=""; // Escudo equipo visitante
    $partidos[2][9]="1970-01-01"; // Fecha del partido
    $partidos[2][10]="00:00"; // Hora del partido
    $partidos[2][11]="vocal"; 
    $partidos[2][12]="veedor";
    $partidos[2][13]="cancha";*/


    // Verifica si existe ese torneo
    $verificar = $objeto_torneo->verificar_ID($id_torneo, $conexion);
    if(!empty($verificar)){
        // Verificar si ya existen partidos creados en ese torneo
        $existen_partidos = $objeto_partido->mostrar_partidos($id_torneo,$conexion);
        if(!empty($existen_partidos)){
            if($opcion=="jugadores"){
                if(!empty($num_jornada) & !empty($jugadores)){
                    //Insertar estadisticas de jugadores
                    $respuesta=$objeto_jugador->insertar_actualizar_estadisticas_jugador($jugadores,$id_torneo,$conexion);
                    if($respuesta){
                        echo json_encode(array('success' => true));
                    } else {
                        echo json_encode(array('noInserto' => true));
                    }
                } else {
                    echo json_encode(array('noHayDatos' => true));
                }
            } else {
                if($opcion=="resultados"){
                    if(!empty($num_jornada) & !empty($partidos)){
                        // Inicializar el array de equipos
                        $equipos = [];
    
                        // Iterar sobre los datos obtenidos y construir el array de equipos
                        foreach ($partidos as $partido) {
                            $equipoLocal = [
                                'id_equipo' => $partido[3],
                                'gf' => intval($partido[4]),
                                'gc' => intval($partido[5]),
                            ];
                    
                            $equipoVisitante = [
                                'id_equipo' => $partido[6],
                                'gf' => intval($partido[5]),
                                'gc' => intval($partido[4]),
                            ];
                        
                            $equipos[] = $equipoLocal;
                            $equipos[] = $equipoVisitante;
                        }
                        // Actualizar estadisticas de equipo
                        $respuesta=$objeto_equipo->actualizar_estadisticas_equipo($equipos, $id_torneo, $conexion);
                        if ($respuesta) {
                            // Actualiza partidos en la base de datos
                            $resultado=$objeto_partido->modificar_resultado($id_torneo,$num_jornada,$partidos, $conexion);
                            if ($resultado) {
                                echo json_encode(array('success' => true));
                            } else {
                                echo json_encode(array('noInserto' => true));
                            }
                        } else {
                            echo json_encode(array('noInsertoResultados' => true));
                        }
                    } else {
                        echo json_encode(array('noHayDatos' => true));
                    }
                } else {
                    if(!empty($num_jornada) & !empty($partidos) & !empty($jugadores)){
                        //Insertar estadisticas de jugadores
                        $respuesta=$objeto_jugador->insertar_actualizar_estadisticas_jugador($jugadores,$id_torneo,$conexion);
                        if($respuesta){
                            // Inicializar el array de equipos
                            $equipos = [];
        
                            // Iterar sobre los datos obtenidos y construir el array de equipos
                            foreach ($partidos as $partido) {
                                $equipoLocal = [
                                    'id_equipo' => $partido[3],
                                    'gf' => intval($partido[4]),
                                    'gc' => intval($partido[5]),
                                ];
                            
                                $equipoVisitante = [
                                    'id_equipo' => $partido[6],
                                    'gf' => intval($partido[5]),
                                    'gc' => intval($partido[4]),
                                ];
                            
                                $equipos[] = $equipoLocal;
                                $equipos[] = $equipoVisitante;
                            }
                            // Actualizar estadisticas de equipo
                            $respuesta=$objeto_equipo->actualizar_estadisticas_equipo($equipos, $id_torneo, $conexion); 
                            if ($respuesta) {
                                // Actualiza partidos en la base de datos
                                $resultado=$objeto_partido->modificar_resultado($id_torneo,$num_jornada,$partidos, $conexion);
                                if ($resultado) {
                                    echo json_encode(array('success' => true));
                                } else {
                                    echo json_encode(array('noInserto' => true));
                                }
                            } else {
                                echo json_encode(array('noInsertoResultados' => true));
                            }
                        } else {
                            echo json_encode(array('noInsertoJugadores' => true));
                        }
                    } else {
                        echo json_encode(array('noHayDatos' => true));
                    }
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