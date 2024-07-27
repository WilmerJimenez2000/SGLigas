<?php
class categoria{
    /**
    * Inserta una nueva categoría en la base de datos.
    *
    * @param string $nombre El nombre de la categoría a insertar.
    * @param int $num_equipos El número de equipos en la categoría.
    * @param int $id_liga El ID de la liga a la que se asociará la categoría.
    * @param mysqli $conexion La conexión a la base de datos.
    *
    * @return bool `true` si la inserción fue exitosa, `false` en caso contrario.
    */
    public function insertar_categoria($nombre, $num_equipos, $id_liga, $conexion) {
        // Inicializar variable de retorno
        $respuesta = false;

        // Crear la sentencia preparada
        $sql = "INSERT INTO categoria(nombre, num_equipos, id_Liga) VALUES (?,?,?)";
        $stmt = mysqli_prepare($conexion, $sql);

        if($stmt){
            // Vincular parámetros a la sentencia
            mysqli_stmt_bind_param($stmt, "sii", $nombre, $num_equipos, $id_liga);
            
            // Ejecutar la sentencia
            if(mysqli_stmt_execute($stmt)){
                $respuesta = true;
            }
        }
        // Cerrar la sentencia preparada.
        mysqli_stmt_close($stmt);

        // Devolver `true` si la inserción fue exitosa, `false` en caso contrario.
        return $respuesta;
    }

    /**
    * Verifica si existe una categoría con el mismo nombre en una liga específica.
    *
    * @param string $nombre El nombre de la categoría a verificar.
    * @param int $id_liga El ID de la liga en la que se realiza la verificación.
    * @param mysqli $conexion La conexión a la base de datos.
    *
    * @return bool `true` si existe una categoría con el mismo nombre en la liga, `false` si no existe.
    */
    public function verificar_nombre_categoria($nombre, $id_liga, $conexion) {
        // Inicializar variable de retorno
        $existe = false;

        // Crear la sentencia SQL con una consulta preparada
        $sql = "SELECT nombre FROM categoria WHERE id_Liga= ? AND nombre= ?";
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

    /**
    * Obtiene todas las categorías asociadas a un ID de liga específico.
    *
    * @param int $id_liga El ID de la liga para la cual se obtienen las categorías.
    * @param mysqli $conexion La conexión a la base de datos.
    *
    * @return array Un array de categorías asociadas a la liga. Cada categoría es un arreglo asociativo con las claves: 'id_categoria', 'nombre', y 'num_equipos'.
    */
    function obtener_categorias($id_liga, $conexion) {
        // Inicializar variable de retorno
        $categorias = array();

        // Crear la sentencia SQL para obtener las categorías asociadas a la liga.
        $sql = "SELECT id_categoria, nombre, num_equipos FROM categoria WHERE id_Liga=?";
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
                        $categorias[] = $fila; 
                    }
                }  
            }
        }

        // Cerrar la sentencia preparada
        mysqli_stmt_close($stmt);

        // Devolver un array de categorías asociadas a la liga (array vacío si no encontro).
        return $categorias;
    }

    /**
    * Verifica la existencia de un ID de categoría en la base de datos.
    *
    * @param int $id_categoria El ID de la categoría a verificar.
    * @param mysqli $conexion La conexión a la base de datos.
    *
    * @return int Devuelve el ID de la categoría si existe, 0 si no se encontró.
    */
    public function verificar_ID($id_categoria, $conexion) {
        // Inicializar la variable de retorno
        $respuesta = 0;

        // Crear la sentencia SQL para verificar la existencia del ID de la liga.
        $sql = "SELECT id_categoria FROM categoria WHERE id_categoria = ?";
        $stmt = mysqli_prepare($conexion, $sql);

        if ($stmt) {
            // Vincular parámetro a la sentencia
            mysqli_stmt_bind_param($stmt, "i", $id_categoria);

            // Ejecutar la sentencia
            if (mysqli_stmt_execute($stmt)) {
                // Obtener resultados
                $resultado = mysqli_stmt_get_result($stmt);
                if (mysqli_num_rows($resultado) > 0) { 
                    $respuesta = mysqli_fetch_assoc($resultado)['id_categoria'];
                }
            } 
        }

        // Cerrar la sentencia preparada
        mysqli_stmt_close($stmt);

        // Devolver el ID del torneo (0 si no se encontró).
        return $respuesta;
    }

}
?>