<?php

include 'conexion.php';

// Recuperar el correo y la contraseña desde la solicitud POST
$correoUsuario = $_POST['correo'];
$contrasena = $_POST['contrasena'];

// Encriptar la contraseña (opcional)
$contrasenaEncriptada = password_hash($contrasena, PASSWORD_DEFAULT);

// Actualizar la contraseña en la base de datos
$conexion = Conexion_DB();
$consulta = "UPDATE usuarios SET contraseña = '$contrasenaEncriptada' WHERE correo = '$correoUsuario'";
$resultado = mysqli_query($conexion, $consulta);

if ($resultado) {
    // La contraseña se actualizó correctamente
    echo "Contraseña actualizada correctamente";
} else {
    // Error al actualizar la contraseña
    echo "Error al actualizar la contraseña";
}

// Cerrar la conexión a la base de datos
mysqli_close($conexion);

?>
