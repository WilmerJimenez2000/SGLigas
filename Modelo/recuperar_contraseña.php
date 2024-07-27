<?php
include 'conexion.php';

// Recuperar el correo del usuario desde la solicitud POST
$correoUsuario = $_POST['correoUsuario'];

// Validar y procesar el correo del usuario
if (!empty($correoUsuario)) {
    // Verificar si el correo está registrado en la base de datos
    $conexion = Conexion_DB();
    $consulta = "SELECT * FROM usuarios WHERE correo = '$correoUsuario'";
    $resultado = mysqli_query($conexion, $consulta);

    if (mysqli_num_rows($resultado) > 0) {
        // El correo está registrado, obtener el nombre de usuario
        $usuario = mysqli_fetch_assoc($resultado);
        $nombreUsuario = $usuario['nombre'];

        // Realizar la lógica de recuperación de contraseña aquí
        // Por ejemplo, generar un código de verificación y almacenarlo en la base de datos o sistema de almacenamiento
        
        // Generar un código de verificación aleatorio
        $codigoVerificacion = generarCodigoVerificacion();
        
        // Almacenar el código de verificación en la base de datos o sistema de almacenamiento
        // ...
        
        // Envío de correo electrónico con el código de verificación
        enviarCorreoRecuperacionContraseña($correoUsuario, $nombreUsuario, $codigoVerificacion);

        // Crear un arreglo con los datos a devolver en la respuesta JSON
        $response = array(
            'success' => true,
            'message' => 'Se ha enviado un correo con instrucciones para recuperar tu contraseña.',
            'correoUsuario' => $correoUsuario,
            'codigoVerificacion' => $codigoVerificacion
        );
    } else {
        // El correo no está registrado en la base de datos
        // Crear un arreglo con los datos a devolver en la respuesta JSON
        $response = array(
            'success' => false,
            'message' => 'El correo proporcionado no está registrado.'
        );
    }

    // Imprimir la respuesta en formato JSON
    echo json_encode($response);

    // Cerrar la conexión a la base de datos
    mysqli_close($conexion);
} else {
    // No se proporcionó el correo del usuario
    // Crear un arreglo con los datos a devolver en la respuesta JSON
    $response = array(
        'success' => false,
        'message' => 'No se ha proporcionado el correo del usuario.'
    );

    // Imprimir la respuesta en formato JSON
    echo json_encode($response);
}

// Función para generar un código de verificación
function generarCodigoVerificacion() {
    // Implementa tu lógica para generar un código de verificación aquí
    // Puedes utilizar funciones como rand(), mt_rand(), uniqid(), etc.
    
    // Ejemplo básico: Generar una cadena de caracteres aleatoria de longitud 6
    $caracteres = '0123456789';
    $codigoVerificacion = '';
    for ($i = 0; $i < 6; $i++) {
        $codigoVerificacion .= $caracteres[rand(0, strlen($caracteres) - 1)];
    }
    
    return $codigoVerificacion;
}

// Función para enviar el correo de recuperación de contraseña
function enviarCorreoRecuperacionContraseña($correoDestino, $nombreUsuario, $codigoVerificacion) {
    // Implementa tu lógica de envío de correo electrónico aquí
    // Puedes utilizar bibliotecas o servicios como PHPMailer, SwiftMailer, SendGrid, etc.
    // Recuerda configurar los detalles del servidor de correo saliente y personalizar el mensaje
    
    $asunto = 'Recuperación de contraseña';
    $mensaje = "Estimado/a $nombreUsuario,\r\n\r\n";
    $mensaje .= "Hemos recibido una solicitud para restablecer la contraseña de tu cuenta. Por favor, utiliza el siguiente código de verificación: $codigoVerificacion\r\n\r\n";
    $mensaje .= "Si no has solicitado esta acción, te recomendamos que tomes las medidas necesarias para proteger tu cuenta, como cambiar tu contraseña y habilitar la autenticación de dos factores.\r\n\r\n";
    $mensaje .= "Si tienes alguna pregunta o necesitas asistencia adicional, no dudes en contactarnos. Estamos aquí para ayudarte.\r\n\r\n";
    $mensaje .= "Atentamente:\r\n";
    $mensaje .= "SGLigas\r\n";
    $cabeceras = 'From: tu_correo@example.com' . "\r\n" .
                 'Reply-To: tu_correo@example.com' . "\r\n" .
                 'X-Mailer: PHP/' . phpversion();
    
    mail($correoDestino, $asunto, $mensaje, $cabeceras);
}
?>
