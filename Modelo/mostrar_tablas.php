<?php
include('conexion.php');

$conexion = conexion_DB();

// Obtener la lista de tablas
$sql = "SHOW TABLES";
$resultado = mysqli_query($conexion, $sql);

if ($resultado) {
    $tablas = array();
    while ($fila = mysqli_fetch_row($resultado)) {
        $tablas[] = $fila[0];
    }
    mysqli_free_result($resultado);
    
    // Cerrar la conexión
    mysqli_close($conexion);

    echo json_encode(array('tablas' => $tablas));
} else {
    // Error al ejecutar la consulta
    echo json_encode(array('error' => 'Error al obtener las tablas de la base de datos.'));
}
?>
