<?php
/**
 * @file
 * Este archivo contiene un script PHP para manejar la inserción de categorías en una base de datos.
 */

// Incluye las clases y archivos necesarios
include('clases/clase_torneo.php');
include('clases/clase_categoria.php');
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
    $etapa = isset($_POST['etapa']) ? trim($_POST['etapa']) : "";
    $fecha_inicio = isset($_POST['fecha_inicio']) ? trim($_POST['fecha_inicio']) : "";
    $fecha_fin = isset($_POST['fecha_fin']) ? trim($_POST['fecha_fin']) : "";
    $canchas = isset($_POST['canchas']) ? trim($_POST['canchas']) : "";
    $grupo = isset($_POST['grupo']) ? trim($_POST['grupo']) : "";
    $num_clasificados = isset($_POST['num_clasificados']) ? trim($_POST['num_clasificados']) : "";
    $id_categoria = isset($_POST['id_categoria']) ? trim($_POST['id_categoria']) : "";
    
    /*$etapa =  "Primera etapa";
    $fecha_inicio =  "2023-11-8";
    $fecha_fin =  "2023-11-20";
    $grupo = 2;
    $num_clasificados = [2,2];
    $canchas=['Cancha 1','Chancha 2'];
    $id_categoria = 2;*/

    // Crea instancias de las clases a ser usadas
    $objeto_torneo = new torneo();
    $objeto_categoria = new categoria();

    // Verifica si existe ese ID de liga
    $id_categoria = $objeto_categoria->verificar_ID($id_categoria, $conexion);
    if ($id_categoria > 0) {
        // Verifica que los datos no estén vacíos
        if (!empty($etapa) && !empty($fecha_inicio) && !empty($fecha_fin) && !empty($canchas) && !empty($num_clasificados)) {
            // Verifica que los datos no estén vacíos si es etapa de grupos
            if(!empty($grupo)){
                for($i=0;$i<$grupo;$i++){
                    $resultado[$i] = $objeto_torneo->insertar_torneo($etapa, $fecha_inicio, $fecha_fin, "GRUPO ".$i+1, $num_clasificados[$i], $id_categoria, $canchas, $conexion);
                }
                // Inserta el nuevo torneo en la base de datos
                if (!empty($resultado)) {
                    echo json_encode(array('success' => true,'datos' => $resultado));
                } else {
                    echo json_encode(array('noInserto' => true));
                }            
            }
            else{
                // Inserta el nuevo torneo en la base de datos
                $resultado = $objeto_torneo->insertar_torneo($etapa, $fecha_inicio, $fecha_fin, $grupo, $num_clasificados, $id_categoria, $canchas, $conexion);
                if (!empty($resultado)) {
                    echo json_encode(array('success' => true));
                } else {
                    echo json_encode(array('noInserto' => true));
                }
            }
        } else {
            // Al menos una de las variables está vacía
            echo json_encode(array('noHayDatos' => true));
        }
    } else {
        echo json_encode(array('no_existe_categoria' => true));
    }

    // Cierra la conexión a la base de datos
    mysqli_close($conexion);
}

// Ejecuta la función principal
main();
?>