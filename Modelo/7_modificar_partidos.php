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
    //$id_partido = isset($_POST['id_partido']) ? trim($_POST['id_partido']) : "";
    //$partido = isset($_POST['partido']) ? $_POST['partido'] : "";
    $estado = "porjugar";

    $id_partido=50;

    // Crea instancias de las clases a ser usadas
    $objeto_partido = new partido();

    $partido[0]="2024-07-16";  //fecha
    $partido[1]="16:00";       //hora
    $partido[2]="Cancha 1";     //cancha
    $partido[3]="Jose Puentes";         //vocal
    $partido[4]="Rodrio Basquez";      //veedor
    
    // Verifica si existe ese torneo
    $verificar = $objeto_partido->verificar_ID($id_partido, $conexion);
    if(!empty($verificar)){
        $estado_BD=$verificar['estado'];
        // Verificar si ya existen partidos creados en ese torneo
        if($estado_BD==='jugado' || $estado_BD==='pospuesto'){
            if(!empty($partido)){ 
                // Actualizar partido en la base de datos
                $respuesta=$objeto_partido->modificar_partido($id_partido, $partido, $estado, $conexion);
                if ($respuesta) {
                    echo json_encode(array('success' => true));
                } else {
                    echo json_encode(array('noInserto' => true));
                }
            } else {
                echo json_encode(array('noHayDatos' => true));
            }
        } else {
            if($estado=="jugador"){
                echo json_encode(array('partido_jugado' => true));
            } else {
                echo json_encode(array('partido_programado' => true));
            }
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