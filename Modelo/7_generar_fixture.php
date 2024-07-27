<?php
/**
 * @file
 * Este archivo contiene un script PHP para mostrar equipos de una categoría en una base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_partido.php');
include('clases/clase_torneo.php');
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
    $equipos = isset($_POST['equipos']) ? $_POST['equipos'] : "";
    
    /*$id_torneo=1;
    $equipos=[4,27,5,24,15,16];//16*/

    // Crea instancias de las clases a ser usadas
    $objeto_torneo = new torneo();
    $objeto_partido = new partido();
    $objeto_equipo = new equipo();

    // Verifica si existe ese torneo
    $verificar = $objeto_torneo->verificar_ID($id_torneo, $conexion);
    if(!empty($verificar)){
        // Verificar si ya existen partidos creados en ese torneo
        $existen_partidos = $objeto_partido->mostrar_partidos($id_torneo,$conexion);
        if(empty($existen_partidos)){
            if(!empty($equipos)){
                $id_categoria=$verificar['id_categoria'];
                // Verficar los id de equipos
                $cont=0;
                $valido=false;
                for($i=0; $i < count($equipos); $i++){
                    $verificar=$objeto_equipo->verificar_ID($equipos[$i],$conexion);
                    if($verificar){
                        $cont++;
                    }
                    if($i==count($equipos)-1){
                        if($i==$cont-1){
                            $valido=true;
                        }
                    }
                }
                if($valido){
                    $num_equipos=count($equipos);
                    $objeto_equipo->registar_estadisticas_equipo($equipos,$id_torneo,$conexion);
                    if($num_equipos%2!=0){
                        $equipos[$num_equipos]=30; //se asigna el id de equipo descansa
                        $num_equipos++;//se aumenta el num de equipos porque es impar
                    }
                    $num_partidos=$num_equipos/2;
                    // Obtiene los resultados
                    $resultado = $objeto_partido->generar_calendario_juego($equipos,$num_equipos);

                    $respuesta = $objeto_partido->insertar_partido($resultado, $num_equipos, $id_torneo, $conexion);
                    if ($respuesta) {
                        echo json_encode(array('success' => true));
                    } else {
                        echo json_encode(array('noInserto' => true));
                    }
                    //echo $base64Data."<br>";
                } else {
                    echo json_encode(array('no_existen_equipo' => true));
                }
            } else {
                echo json_encode(array('noHayDatos' => true));
            }
        } else {
            echo json_encode(array('existen_partidos' => true));
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