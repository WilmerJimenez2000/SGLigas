<?php
include('clases/clase_equipo.php');
include('clases/clase_categoria.php');
include('conexion.php');

$conexion = conexion_dB();  

// Obtener los datos ingresador por parte del cliente
$id_categoria=isset($_POST['id_categoria']) ? trim($_POST['id_categoria']) : "";

//$id_categoria=69;

$objeto_categoria = new categoria();

$verificar=$objeto_categoria->verificar_ID($id_categoria,$conexion);
// Verificar que los datos no esten vacios
if ($verificar) {
    // Crear instancia de la clase equipo    
    $objeto_equipo = new equipo();
    $verificar=$objeto_equipo->verificar_num_equipos($id_categoria,$conexion);
    if($verificar){
        echo json_encode(array('limite_equipos' => true));
    } else {
        echo json_encode(array('limite_equipos' => false));
    }
}
else {
    // Al menos una de las variables está vacía
    echo json_encode(array('no_existe_id' => true));
}
?>