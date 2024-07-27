<?php
class sanciones{
    
    public function insertar_sancion($id_partido, $informe_local, $informe_visitante, $arbitro, $conexion) {
        // Inicializar variable de retorno
        $respuesta = false;

        // Crear la sentencia preparada
        $sql = "INSERT INTO sanciones(id_partidos, informe_local, informe_visitante, nombre_arbitro) VALUES (?,?,?,?)";
        $stmt = mysqli_prepare($conexion, $sql);

        if($stmt){
            mysqli_stmt_bind_param($stmt, "isss", $id_partido, $informe_local, $informe_visitante,$arbitro);
            $respuesta=mysqli_stmt_execute($stmt);
        }
        // Cerrar la sentencia preparada.
        mysqli_stmt_close($stmt);
        // Devolver `true` si la inserción fue exitosa, `false` en caso contrario.
        return $respuesta;
    }

    public function actualizar_tribunal($id_partido, $tribunal_local, $tribunal_visitante, $conexion) {
        // Inicializar variable de retorno
        $respuesta = false;

        // Crear la sentencia preparada
        $sql = "UPDATE sanciones SET tribunal_local = ?, tribunal_visitante = ? WHERE id_partidos = ?;";
        $stmt = mysqli_prepare($conexion, $sql);

        if($stmt){
            mysqli_stmt_bind_param($stmt, "ssi", $tribunal_local, $tribunal_visitante, $id_partido);
            $respuesta=mysqli_stmt_execute($stmt);
        }
        // Cerrar la sentencia preparada.
        mysqli_stmt_close($stmt);
        // Devolver `true` si la inserción fue exitosa, `false` en caso contrario.
        return $respuesta;
    }

    public function verificar_sancion($id_partido,$conexion){
        // Inicializar la variable de retorno
        $respuesta = 0;

        // Crear la sentencia SQL para verificar la existencia del ID de la liga.
        $sql = "SELECT id_partidos FROM sanciones WHERE id_partidos = ?";
        $stmt = mysqli_prepare($conexion, $sql);

        if ($stmt) {
            // Vincular parámetro a la sentencia
            mysqli_stmt_bind_param($stmt, "i", $id_partido);

            // Ejecutar la sentencia
            if (mysqli_stmt_execute($stmt)) {
                // Obtener resultados
                $resultado = mysqli_stmt_get_result($stmt);
                if (mysqli_num_rows($resultado) > 0) { 
                    $respuesta = mysqli_fetch_assoc($resultado)['id_partidos'];
                }
            } 
        }

        // Cerrar la sentencia preparada
        mysqli_stmt_close($stmt);

        // Devolver el ID del partido dentro de la tabla sancion (0 si no se encontró).
        return $respuesta;
    }


}
?>