<?php
include('clases/clase_equipo.php');
include('clases/clase_categoria.php');
include('clases/clase_liga.php');
include('conexion.php');

$conexion = conexion_dB();  

// Obtener los datos ingresador por parte del cliente
$nombre_equipo=isset($_POST['nombre_equipo']) ? trim($_POST['nombre_equipo']) : "";
$fecha_fundacion=isset($_POST['fecha_fundacion']) ? trim($_POST['fecha_fundacion']) : "";
$presidente=isset($_POST['presidente']) ? trim($_POST['presidente']) : "";
$colores=isset($_POST['color']) ? trim($_POST['color']) : "";
$escudo=isset($_POST['escudo']) ? trim($_POST['escudo']) : "";
$estado="activo";
$id_categoria=isset($_POST['id_categoria']) ? intval(trim($_POST['id_categoria'])) : 0;
$id_liga = isset($_POST['id_liga']) ? intval(trim($_POST['id_liga'])) : 0;

/*$nombre_equipo="Liga de U";
$fecha_fundacion="28/09/2023";
$presidente="carlos";
$colores="blanco";
$escudo="asdadsadasdadas";
$estado="activo";
$id_categoria=92;
$id_liga=78;*/

// Crea instancias de las clases a ser usadas
$objeto_categoria = new categoria();
$objeto_liga = new liga();
$objeto_equipo = new equipo();

$verificar=$objeto_liga->verificar_ID($id_liga,$conexion);
if($verificar){
    $categoria=$objeto_categoria->obtener_categorias($id_liga,$conexion);

    $verificar=false;
    foreach ($categoria as $elemento) {
        if ($elemento["id_categoria"] === $id_categoria) {
            $verificar = true;
            break; // Terminar el bucle cuando se encuentra el resultado
        }
    }
    if($verificar){
        // Verificar que los datos no esten vacios
        if (!empty($nombre_equipo) && !empty($fecha_fundacion) && !empty($presidente) && !empty($colores) && !empty($escudo)) {
            // Verificar si existe el nombre de equipo
            $verificar=$objeto_equipo->verificar_nombre_equipo($nombre_equipo,$id_liga,$conexion);
            if(!$verificar){
                $resultado=$objeto_equipo->crear_equipo($nombre_equipo,$fecha_fundacion,$presidente,$colores,$escudo,$estado,$id_categoria,$conexion);
                // Cerrar la conexión
                mysqli_close($conexion);
                // Verificar que se inserto el equipo
                if ($resultado) {
                    echo json_encode(array('success' => true));
                } else {
                    echo json_encode(array('noInserto' => true));
                }
            } else {
                echo json_encode(array('existe_nombre' => true));
            }
        }
        else {
            // Al menos una de las variables está vacía
            echo json_encode(array('noHayDatos' => true));
        }
    } else {
        // No existe id categoria
        echo json_encode(array('no_existe_categoria' => true));
    }

    
} else {
    // No existe id liga
    echo json_encode(array('no_existe_liga' => true));
}


?>