<?php
class torneo{
    /**
    * Inserta un nuevo torneo en la base de datos.
    *
    * @param string $etapa La etapa del torneo.
    * @param string $fecha_inicio La fecha de inicio del torneo.
    * @param string $fecha_fin La fecha de fin del torneo.
    * @param string $grupo El nombre del grupo del torneo ejemplo (GRUPO A) es opcional si la etapa es de grupos.
    * @param int $num_equipos_grupo El número de equipos que hay en cada grupo es opcional si la etapa es de grupos.
    * @param int $id_categoria El ID de la categoría a la que pertenece el torneo.
    * @param mysqli $conexion La conexión a la base de datos.
    *
    * @return bool Devuelve `true` si la inserción fue exitosa, `false` en caso contrario.
    */
    public function insertar_torneo($etapa, $fecha_inicio, $fecha_fin, $grupo, $num_clasificados, $id_categoria, $canchas, $conexion) {
        // Inicializar variable de retorno
        $id_insertado = 0;

        // Dar formato a la fecha acorde a la BD
        $fecha_inicio = date('Y-m-d', strtotime($fecha_inicio));
        $fecha_fin = date('Y-m-d', strtotime($fecha_fin));

        // Convertir el array canchas a una cadena antes de insertarlo
        $canchas_str = implode(',', $canchas);

        // Crear la sentencia preparada
        $sql = "INSERT INTO torneo(etapa, fecha_inicio, fecha_fin, grupo, num_clasificados, id_categoria, canchas) VALUES (?,?,?,?,?,?,?)";
        $stmt = mysqli_prepare($conexion, $sql);

        if($stmt){
            // Vincular parámetros a la sentencia
            mysqli_stmt_bind_param($stmt, "ssssiis", $etapa, $fecha_inicio, $fecha_fin, $grupo, $num_clasificados, $id_categoria, $canchas_str);
            
            // Ejecutar la sentencia
            if(mysqli_stmt_execute($stmt)){
                $id_insertado = mysqli_insert_id($conexion);
            }
        }
        // Cerrar la sentencia preparada.
        mysqli_stmt_close($stmt);

        // Devolver `true` si la inserción fue exitosa, `false` en caso contrario.
        return $id_insertado;
    }

    public function mostrar_torneos($id_categoria, $conexion){
        // Inicializar la variable de retorno
        $torneos = array();

        // Crear la sentencia SQL para verificar la existencia del ID de la liga.
        $sql="SELECT DISTINCT torneo.*
            FROM torneo
            JOIN partidos ON torneo.id_torneo = partidos.id_torneo
            WHERE torneo.id_categoria = ?;";
        $stmt = mysqli_prepare($conexion, $sql);

        if ($stmt) {
            // Vincular parámetro a la sentencia
            mysqli_stmt_bind_param($stmt, "i", $id_categoria);

            // Ejecutar la sentencia
            if (mysqli_stmt_execute($stmt)) {
                // Obtener resultados
                $resultado = mysqli_stmt_get_result($stmt);
                // Verificar si hay resultados en la consulta.
                if (mysqli_num_rows($resultado) > 0) {
                    while ($fila = mysqli_fetch_assoc($resultado)) {
                        // Agregar cada torneo al array de torneos.
                        $torneos[] = $fila; 
                    }
                } 
            } 
        }

        // Cerrar la sentencia preparada
        mysqli_stmt_close($stmt);

        // Devolver el ID del torneo y categoria (array vacio si no se encontró).
        return $torneos;
    }

    /**
    * Verifica la existencia de un torneo y devuelve su ID y categoría asociada.
    *
    * @param int $id_torneo El ID del torneo a verificar.
    * @param resource $conexion La conexión a la base de datos.
    *
    * @return array Un array asociativo con el ID del torneo y la categoría. Array vacío si no se encuentra el torneo.
    */
    public function verificar_ID($id_torneo, $conexion) {
        // Inicializar la variable de retorno
        $respuesta = array();

        // Crear la sentencia SQL para verificar la existencia del ID de la liga.
        $sql = "SELECT id_torneo, id_categoria FROM torneo WHERE id_torneo = ?";
        $stmt = mysqli_prepare($conexion, $sql);

        if ($stmt) {
            // Vincular parámetro a la sentencia
            mysqli_stmt_bind_param($stmt, "i", $id_torneo);

            // Ejecutar la sentencia
            if (mysqli_stmt_execute($stmt)) {
                // Obtener resultados
                $resultado = mysqli_stmt_get_result($stmt);
                // Verificar si hay resultados en la consulta.
                if (mysqli_num_rows($resultado) > 0) {
                    while ($fila = mysqli_fetch_assoc($resultado)) {
                        // Agregar cada categoría al array de categorías.
                        $respuesta = $fila; 
                    }
                } 
            } 
        }

        // Cerrar la sentencia preparada
        mysqli_stmt_close($stmt);

        // Devolver el ID del torneo y categoria (array vacio si no se encontró).
        return $respuesta;
    }

    function verificar_estadisticas_torneo($id_torneo,$conexion){
        // Inicializar la variable de retorno
        $respuesta = false;

        // Crear la sentencia SQL para verificar la existencia del CI  de jugador.
        $sql = "SELECT * FROM estadistica_equipo WHERE id_torneo = ?";
        $stmt = mysqli_prepare($conexion, $sql);

        if ($stmt) {
            // Vincular parámetro a la sentencia
            mysqli_stmt_bind_param($stmt, "i", $id_torneo);

            // Ejecutar la sentencia
            if (mysqli_stmt_execute($stmt)) {
                // Obtener resultados
                $resultado = mysqli_stmt_get_result($stmt);
                if (mysqli_num_rows($resultado) > 0) { 
                    $respuesta = true;
                }
            } 
        }

        // Cerrar la sentencia preparada
        mysqli_stmt_close($stmt);

        // Devolver el CI del jugador (0 si no se encontró).
        return $respuesta;

    }
}
?>