<?php
class equipo{
    // Funcion para crear una nueva liga
    /*public function crear_equipo($nombre_equipo,$fecha_fundacion,$presidente,$colores,$escudo,$estado,$id_categoria,$conexion){
        // Dar formato a la fecha acorde a la BD
        $fecha_fundacion = date('Y-m-d', strtotime($fecha_fundacion));
        // Crear la sentencia sql
        $sql = "INSERT INTO equipo(nombre_equipo, fecha_fundacion, presidente, colores, escudo, id_categoria, estado) VALUES (?,?,?,?,?,?,?)";
        // Crear la sentencia preparada
        $stmt = mysqli_prepare($conexion, $sql);
        // Asignar valores a los parámetros
        mysqli_stmt_bind_param($stmt, "ssssbis", $nombre_equipo, $fecha_fundacion, $presidente, $colores, $escudo, $id_categoria, $estado);
        // Enviar el dato BLOB
        mysqli_stmt_send_long_data($stmt, 4, $escudo);
        // Retornar valor
        $respuesta=mysqli_stmt_execute($stmt);
        // Cerrar la sentencia preparada
        mysqli_stmt_close($stmt);
        return $respuesta;
    } */

    public function crear_equipo($nombre_equipo,$fecha_fundacion,$presidente,$colores,$escudo,$estado,$id_categoria,$conexion){
        // Dar formato a la fecha acorde a la BD
        $fecha_fundacion = date('Y-m-d', strtotime($fecha_fundacion));
        // Crear la sentencia sql
        $sql = "INSERT INTO equipo(nombre_equipo, fecha_fundacion, presidente, colores, escudo, id_categoria, estado) VALUES (?,?,?,?,?,?,?)";
        // Crear la sentencia preparada
        $stmt = mysqli_prepare($conexion, $sql);
        // Asignar valores a los parámetros
        mysqli_stmt_bind_param($stmt, "sssssis", $nombre_equipo, $fecha_fundacion, $presidente, $colores, $escudo, $id_categoria, $estado);

        // Retornar valor
        $respuesta=mysqli_stmt_execute($stmt);
        // Cerrar la sentencia preparada
        mysqli_stmt_close($stmt);
        return $respuesta;
    } 

    public function verificar_num_equipos($id_categoria,$conexion){
        // Inicializar variable de retorno
        $respuesta=false;

        // Obtener el límite de equipos para la categoría
        $sqlLimiteEquipos = "SELECT num_equipos FROM categoria WHERE id_categoria = ?";
        $stmtLimiteEquipos = mysqli_prepare($conexion, $sqlLimiteEquipos);
        mysqli_stmt_bind_param($stmtLimiteEquipos, "i", $id_categoria);
        mysqli_stmt_execute($stmtLimiteEquipos);
        mysqli_stmt_bind_result($stmtLimiteEquipos, $limiteEquipos);
        mysqli_stmt_fetch($stmtLimiteEquipos);
        // Cerrar la sentencia preparada
        mysqli_stmt_close($stmtLimiteEquipos);

        // Verificar si el límite de equipos en la categoría se ha alcanzado
        $sqlVerificacion = "SELECT COUNT(*) as numEquipos FROM equipo WHERE id_categoria = ?";
        $stmtVerificacion = mysqli_prepare($conexion, $sqlVerificacion);
        mysqli_stmt_bind_param($stmtVerificacion, "i", $id_categoria);
        mysqli_stmt_execute($stmtVerificacion);
        mysqli_stmt_bind_result($stmtVerificacion, $numEquipos);
        mysqli_stmt_fetch($stmtVerificacion);
        // Cerrar la sentencia preparada
        mysqli_stmt_close($stmtVerificacion);

        if ($numEquipos >= $limiteEquipos) {
            $respuesta=true;
        } 

        return $respuesta;
    }

    /**
    * Muestra la información de un equipo a partir de su ID.
    *
    * @param int $id_equipo El ID del equipo que se desea mostrar.
    * @param mysqli $conexion La conexión a la base de datos.
    *
    * @return mixed Un arreglo asociativo con la información del equipo si se encuentra, o 0 si no se encontró ningún equipo con el ID especificado.
    */
    public function mostrar_equipo($id_equipo, $conexion) {
        // Inicializar variable de retorno
        $equipo = array();

        // Crear la sentencia SQL para obtener la información del equipo por su ID.
        $sql = "SELECT * FROM equipo WHERE id_equipo=?";
        $stmt = mysqli_prepare($conexion, $sql);

        if ($stmt) {
            // Vincular el parámetro a la sentencia
            mysqli_stmt_bind_param($stmt, "i", $id_equipo);

            // Ejecutar la sentencia
            if (mysqli_stmt_execute($stmt)) {
                // Ejecutar la sentencia SQL y obtener los resultados.
                $resultado = mysqli_stmt_get_result($stmt);

                // Verificar si hay resultados en la consulta.
                if (mysqli_num_rows($resultado) > 0) {
                    while ($fila = mysqli_fetch_assoc($resultado)) {
                        // Agregar cada categoría al array de categorías.
                        $equipo[] = $fila; 
                    }
                }  
            }
        }

        // Cerrar la sentencia preparada
        mysqli_stmt_close($stmt);

        // Devolver la información del equipo si se encuentra (array vacio si no encontro).
        return $equipo;
    }


    /**
    * Obtiene todos los equipos pertenecientes a una categoría específica.
    *
    * Esta función consulta la base de datos para recuperar todos los equipos que pertenecen
    * a una categoría identificada por su ID.
    *
    * @param int $id_categoria El ID de la categoría de la que se desean obtener los equipos.
    * @param mysqli $conexion La conexión a la base de datos.
    * @return array Un arreglo de equipos que pertenecen a la categoría especificada.
    */
    public function mostrar_equipos_categoria($id_categoria, $conexion) {
        // Inicializar variable de retorno
        $equipo = array();

        // Crear la sentencia SQL para obtener los equipos de la categoría
        $sql = "SELECT * FROM equipo WHERE id_categoria=?";
        $stmt = mysqli_prepare($conexion, $sql);

        if ($stmt) {
            // Vincular el parámetro a la sentencia
            mysqli_stmt_bind_param($stmt, "i", $id_categoria);

            // Ejecutar la sentencia
            if (mysqli_stmt_execute($stmt)) {
                // Ejecutar la sentencia SQL y obtener los resultados.
                $resultado = mysqli_stmt_get_result($stmt);

                // Verificar si hay resultados en la consulta.
                if (mysqli_num_rows($resultado) > 0) {
                    while ($fila = mysqli_fetch_assoc($resultado)) {
                        // Agregar cada categoría al array de categorías.
                        $equipo[] = $fila; 
                    }
                }  
            }
        }

        // Cerrar la sentencia preparada
        mysqli_stmt_close($stmt);

        // Devolver la información del equipo si se encuentra (array vacio si no encontro).
        return $equipo;
    }

    public function mostrar_equipos_ligas($id_liga,$conexion){
        // Inicializar variable de retorno
        $equipos = array();

        // Crear la sentencia SQL para obtener los equipos de la categoría
        $sql = "SELECT * FROM equipo
                WHERE id_categoria IN (SELECT id_categoria FROM categoria WHERE id_liga = ?);";
        $stmt = mysqli_prepare($conexion, $sql);

        if ($stmt) {
            // Vincular el parámetro a la sentencia
            mysqli_stmt_bind_param($stmt, "i", $id_liga);

            // Ejecutar la sentencia
            if (mysqli_stmt_execute($stmt)) {
                // Ejecutar la sentencia SQL y obtener los resultados.
                $resultado = mysqli_stmt_get_result($stmt);

                // Verificar si hay resultados en la consulta.
                if (mysqli_num_rows($resultado) > 0) {
                    while ($fila = mysqli_fetch_assoc($resultado)) {
                        // Agregar cada categoría al array de categorías.
                        $equipos[] = $fila; 
                    }
                }  
            }
        }

        // Cerrar la sentencia preparada
        mysqli_stmt_close($stmt);

        // Devolver la información del equipo si se encuentra (array vacio si no encontro).
        return $equipos;
    }

    /**
    * Verifica la existencia de un ID de equipo en la base de datos.
    *
    * @param int $id_equipo El ID del equipo a verificar.
    * @param mysqli $conexion La conexión a la base de datos.
    *
    * @return int Devuelve el ID del equipo si existe, 0 si no se encontró.
    */
    public function verificar_ID($id_equipo, $conexion) {
        // Inicializar la variable de retorno
        $respuesta = 0;

        // Crear la sentencia SQL para verificar la existencia del ID de la liga.
        $sql = "SELECT id_equipo FROM equipo WHERE id_equipo = ?";
        $stmt = mysqli_prepare($conexion, $sql);

        if ($stmt) {
            // Vincular parámetro a la sentencia
            mysqli_stmt_bind_param($stmt, "i", $id_equipo);

            // Ejecutar la sentencia
            if (mysqli_stmt_execute($stmt)) {
                // Obtener resultados
                $resultado = mysqli_stmt_get_result($stmt);
                if (mysqli_num_rows($resultado) > 0) { 
                    $respuesta = mysqli_fetch_assoc($resultado)['id_equipo'];
                }
            } 
        }

        // Cerrar la sentencia preparada
        mysqli_stmt_close($stmt);

        // Devolver el ID de la liga (0 si no se encontró).
        return $respuesta;
    }

    public function obtener_id_participantes($jornada,$conexion){
        // Inicializar la variable de retorno
        $equipos = array();

        // Obtener id equipos
        $jornada=json_decode(base64_decode($jornada),true);
        $i=0;
        foreach ($jornada as $aux){
            $equipos[$i]=$aux[3];
            $equipos[$i+1]=$aux[6];
            $i=$i+2;
        }
        
        // Devolver un array con los id de equipos.
        return $equipos;
    }

    public function obtener_nombre_escudo_participantes($equipos,$conexion){
        // Inicializar la variable de retorno
        $nombre_escudo = array();

        $j=0;
        for($i=0;$i<count($equipos);$i++){
            $info=$this->mostrar_equipo($equipos[$i],$conexion);
            $nombre_escudo[$j]=$equipos[$i];
            $nombre_escudo[$j+1]=$info[0]['nombre_equipo'];
            $nombre_escudo[$j+2]=$info[0]['escudo'];
            $j=$j+3;
        }

        // Devolver un array con los id, nombre y escudo de equipos.
        return $nombre_escudo;
    }

    public function asignar_nombre_escudo($id_equipo,$nombre_escudo){
        // Inicializar la variable de retorno
        $resultado = array();

        for ($i = 0; $i < count($nombre_escudo); $i += 3) {
            if ($nombre_escudo[$i] == $id_equipo) {
                $resultado[0]=$nombre_escudo[$i+1];
                $resultado[1]=$nombre_escudo[$i+2];
                break;
            }
        }
        // Devolver un array con los id de equipos.
        return $resultado;
    }

    public function registar_estadisticas_equipo($equipos, $id_torneo, $conexion){
        // Inicializar la variable de retorno
        $respuesta=false;

        // Construir la consulta de inserción
        $sql = "INSERT INTO estadistica_equipo (pg, pe, pp, gf, gc, id_equipo, id_torneo)
        VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Preparar la consulta
        $stmt = mysqli_prepare($conexion, $sql);

        // Verificar la preparación de la consulta
        if (!$stmt) {
            die("Error al preparar la consulta: " . mysqli_error($conexion));
        }

        // Iterar sobre los equipos y ejecutar la consulta para cada uno
        $cont=0;

        for($i=0;$i<count($equipos);$i++){
            mysqli_stmt_bind_param($stmt, "iiiiiii", $pg, $pe, $pp, $gf, $gc, $id_equipo, $id_torneo);
            $id_equipo=$equipos[$i];
            $pg=0; 
            $pe=0; 
            $pp=0; 
            $gf=0; 
            $gc=0;
            if (mysqli_stmt_execute($stmt)) {
                $cont++;
            } 
        }

        if($cont==count($equipos)){
            $respuesta=true;
        }

        // Cerrar la consulta de inserción
        mysqli_stmt_close($stmt);

        return $respuesta;
    }

    /*public function registar_estadisticas_equipo($equipos, $id_torneo, $conexion){
        // Inicializar la variable de retorno
        $respuesta=false;

        // Construir la consulta de inserción
        $sql = "INSERT INTO estadistica_equipo (pg, pe, pp, gf, gc, id_equipo, id_torneo)
        VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Preparar la consulta
        $stmt = mysqli_prepare($conexion, $sql);

        // Verificar la preparación de la consulta
        if (!$stmt) {
            die("Error al preparar la consulta: " . mysqli_error($conexion));
        }

        // Iterar sobre los equipos y ejecutar la consulta para cada uno
        $cont=0;
        foreach ($equipos as $equipo) {
            mysqli_stmt_bind_param($stmt, "iiiiiii", $pg, $pe, $pp, $gf, $gc, $id_equipo, $id_torneo);

            $id_equipo = $equipo['id_equipo'];
            $gf = $equipo['gf'];
            $gc = $equipo['gc'];

            // Calcular pg, pe, pp basado en el resultado del partido
            $pg = ($gf > $gc) ? 1 : 0;
            $pe = ($gf === $gc) ? 1 : 0;
            $pp = ($gf < $gc) ? 1 : 0;

            if (mysqli_stmt_execute($stmt)) {
                $cont++;
            } 
        }

        if($cont==count($equipos)){
            $respuesta=true;
        }

        // Cerrar la consulta de inserción
        mysqli_stmt_close($stmt);

        return $respuesta;
    }*/

    /*public function actualizar_estadisticas_equipo($equipos, $id_torneo, $conexion){
        // Inicializar la variable de retorno
        $respuesta=false;

        // Construir la consulta de actualización
        $sql = "UPDATE estadistica_equipo 
                SET 
                    pg = CASE 
                        WHEN id_equipo = ? AND ? > ? THEN pg + 1
                        ELSE pg
                    END,
                    pe = CASE 
                        WHEN id_equipo = ? AND ? = ? THEN pe + 1
                        ELSE pe
                    END,
                    pp = CASE 
                        WHEN id_equipo = ? AND ? < ? THEN pp + 1
                        ELSE pp
                    END,
                    gf = gf + ?,
                    gc = gc + ?
                WHERE id_equipo = ? AND id_torneo = ?";
        // Iterar sobre los equipos y ejecutar la consulta para cada uno
        $cont=0;
        foreach ($equipos as $equipo) {
            $id_equipo = $equipo['id_equipo'];
            $gf = $equipo['gf'];
            $gc = $equipo['gc'];
            // Preparar la consulta
            $stmt = mysqli_prepare($conexion, $sql);

            mysqli_stmt_bind_param($stmt, "iiiiiiiiiiiii", $id_equipo,$gf,$gc, $id_equipo,$gf,$gc,$id_equipo,$gf,$gc,$gf,$gc, $id_equipo, $id_torneo);



            // Ejecutar la consulta
            if (mysqli_stmt_execute($stmt)) {
                $cont++;
            } 
             // Cerrar la consulta
            mysqli_stmt_close($stmt);
        }

        if($cont==count($equipos)){
            $respuesta=true;
        }
        return $respuesta;
    }*/


    public function actualizar_estadisticas_equipo($partido, $id_torneo, $conexion){
        // Inicializar la variable de retorno
        $respuesta=false;

        // Construir la consulta de actualización
        $sql = "UPDATE estadistica_equipo 
                SET 
                    pg = CASE 
                        WHEN id_equipo = ? AND ? > ? THEN pg + 1
                        ELSE pg
                    END,
                    pe = CASE 
                        WHEN id_equipo = ? AND ? = ? THEN pe + 1
                        ELSE pe
                    END,
                    pp = CASE 
                        WHEN id_equipo = ? AND ? < ? THEN pp + 1
                        ELSE pp
                    END,
                    gf = gf + ?,
                    gc = gc + ?
                WHERE id_equipo = ? AND id_torneo = ?";

        $cont=0;
        for($i=0; $i<2; $i++){
            if($i==0){
                $id_equipo = $partido[0];
                $gf = $partido[1];
                $gc = $partido[2];
            } else {
                $id_equipo = $partido[3];
                $gf = $partido[2];
                $gc = $partido[1];
            }
            $stmt = mysqli_prepare($conexion, $sql);
            if($stmt){
                mysqli_stmt_bind_param($stmt, "iiiiiiiiiiiii", $id_equipo,$gf,$gc, $id_equipo,$gf,$gc,$id_equipo,$gf,$gc,$gf,$gc, $id_equipo, $id_torneo);
                if (mysqli_stmt_execute($stmt)) {
                    $cont++;
                } 
            }
        }
        if($cont==2){
            $respuesta=true;
        }
        return $respuesta;
    }



    function mostrar_tabla_de_posiciones($id_torneo,$conexion){
        // Inicializar variable de retorno
        $tabla_posicion = array();

        // Crear la sentencia SQL para obtener los equipos de la categoría
        $sql = "SELECT 
                    equipo.nombre_equipo,
                    equipo.escudo,
                    estadistica_equipo.pg,
                    estadistica_equipo.pe,
                    estadistica_equipo.gf,
                    estadistica_equipo.gc,
                    (estadistica_equipo.pg * 3 + estadistica_equipo.pe) AS puntos,
                    (estadistica_equipo.gf - estadistica_equipo.gc) AS gol_diferencia
                FROM estadistica_equipo
                JOIN equipo ON estadistica_equipo.id_equipo = equipo.id_equipo
                WHERE estadistica_equipo.id_torneo = ?
                ORDER BY puntos DESC, gol_diferencia DESC, estadistica_equipo.gf DESC, estadistica_equipo.gc ASC;";
        $stmt = mysqli_prepare($conexion, $sql);

        if ($stmt) {
            // Vincular el parámetro a la sentencia
            mysqli_stmt_bind_param($stmt, "i", $id_torneo);

            // Ejecutar la sentencia
            if (mysqli_stmt_execute($stmt)) {
                // Ejecutar la sentencia SQL y obtener los resultados.
                $resultado = mysqli_stmt_get_result($stmt);

                // Verificar si hay resultados en la consulta.
                if (mysqli_num_rows($resultado) > 0) {
                    while ($fila = mysqli_fetch_assoc($resultado)) {
                        // Agregar cada categoría al array de categorías.
                        $tabla_posicion[] = $fila; 
                    }
                }  
            }
        }

        // Cerrar la sentencia preparada
        mysqli_stmt_close($stmt);

        // Devolver la información del equipo si se encuentra (array vacio si no encontro).
        return $tabla_posicion;
    }
    
    public function verificar_nombre_equipo($nombre, $id_liga, $conexion) {
        // Inicializar variable de retorno
        $existe = false;

        // Crear la sentencia SQL con una consulta preparada
        $sql = "SELECT e.nombre_equipo
                FROM equipo e
                INNER JOIN categoria c ON e.id_categoria = c.id_categoria
                INNER JOIN liga l ON c.id_liga = l.id_liga
                WHERE l.id_liga = ? AND e.nombre_equipo = ?;";
        $stmt = mysqli_prepare($conexion, $sql);

        if ($stmt) {
            // Vincular el parámetro a la sentencia
            mysqli_stmt_bind_param($stmt, "is", $id_liga,$nombre);

            // Ejecutar la sentencia
            if (mysqli_stmt_execute($stmt)) {
                // Ejecutar la sentencia SQL y obtener los resultados.
                $resultado = mysqli_stmt_get_result($stmt);

                // Verificar si hay resultados en la consulta.
                if (mysqli_num_rows($resultado) > 0) {
                    $existe = true;
                } 
            }
        }

        // Cerrar la sentencia preparada
        mysqli_stmt_close($stmt);
        
        // Devolver `true` si existe una categoría con el mismo nombre en la liga, `false` en caso contrario.
        return $existe;
    }
}
?>