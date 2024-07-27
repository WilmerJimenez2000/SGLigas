<?php
include('conexion.php');

$conexion = conexion_DB();

// Obtener la información de todos los registros de la tabla 'usuarios'
$sql = "SELECT * FROM alineacion";
$resultado = mysqli_query($conexion, $sql);

if ($resultado) {
    $datos = array();
    while ($fila = mysqli_fetch_assoc($resultado)) {
        $datos[] = $fila;
    }
    mysqli_free_result($resultado);

    // Cerrar la conexión
    mysqli_close($conexion);

    echo json_encode(array('datos' => $datos));
} else {
    // Error al ejecutar la consulta
    echo json_encode(array('error' => 'Error al obtener datos de la tabla usuarios.'));
}
?>
