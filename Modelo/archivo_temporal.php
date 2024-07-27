<?php
/**
 * @file
 * Este archivo contiene funciones para establecer la conexión a una base de datos,
 * configurar encabezados CORS y mostrar todas las tablas y valores de una tabla específica,
 * así como eliminar filas específicas de la tabla estadistica_jugador.
 */

/**
 * Establece encabezados CORS para permitir solicitudes desde cualquier origen.
 */
function configurar_CORS() {
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept');
    header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE');
}
configurar_CORS();

/**
 * Establece una conexión a la base de datos y devuelve la instancia de conexión.
 * @return mysqli|false La instancia de conexión a la base de datos o false en caso de error.
 */
function conexion_DB() {
    $server = "brzdpx5hq52w25hlgmwk-mysql.services.clever-cloud.com"; 
    $user = "ubkfifhwkdhvld6s"; 
    $pass = "XZ923BQyATpQOZ87r72s"; 
    $db = "brzdpx5hq52w25hlgmwk"; 

    $conectar = mysqli_connect($server, $user, $pass, $db);
    if (!$conectar) {
        die("Error de conexión: " . mysqli_connect_error());
        return false;
    }

    return $conectar;
}

/**
 * Muestra todas las tablas de la base de datos.
 */
function mostrar_tablas() {
    $conexion = conexion_DB();

    // Verificar la conexión
    if ($conexion->connect_error) {
        die("Conexión fallida: " . $conexion->connect_error);
    }

    // Consulta para obtener todas las tablas de la base de datos actual
    $sql = "SHOW TABLES";
    $resultado = $conexion->query($sql);

    if ($resultado->num_rows > 0) {
        echo "<h2>Tablas en la base de datos:</h2>";
        echo "<ul>";
        while ($fila = $resultado->fetch_array()) {
            echo "<li>" . $fila[0] . "</li>";
        }
        echo "</ul>";
    } else {
        echo "No se encontraron tablas en la base de datos.";
    }

    // Cerrar la conexión
    $conexion->close();
}

/**
 * Muestra todos los valores de la tabla estadistica_jugador.
 */
function mostrar_estadisticas_jugador() {
    $conexion = conexion_DB();

    // Verificar la conexión
    if ($conexion->connect_error) {
        die("Conexión fallida: " . $conexion->connect_error);
    }

    // Consulta para obtener todos los valores de la tabla estadistica_jugador
    $sql = "SELECT * FROM estadistica_jugador";
    $resultado = $conexion->query($sql);

    if ($resultado->num_rows > 0) {
        echo "<h2>Valores en la tabla estadistica_jugador:</h2>";
        echo "<table border='1'>";
        echo "<tr>";
        // Obtener los nombres de las columnas
        while ($campo = $resultado->fetch_field()) {
            echo "<th>" . $campo->name . "</th>";
        }
        echo "</tr>";

        // Obtener los valores de las filas
        while ($fila = $resultado->fetch_assoc()) {
            echo "<tr>";
            foreach ($fila as $valor) {
                echo "<td>" . htmlspecialchars($valor) . "</td>";
            }
            echo "</tr>";
        }
        echo "</table>";
    } else {
        echo "No se encontraron registros en la tabla estadistica_jugador.";
    }

    // Cerrar la conexión
    $conexion->close();
}

/**
 * Elimina los registros de la tabla estadistica_jugador donde id_torneo es 46.
 */
function eliminar_jugadores_torneo_46() {
    $conexion = conexion_DB();

    // Verificar la conexión
    if ($conexion->connect_error) {
        die("Conexión fallida: " . $conexion->connect_error);
    }

    // Consulta para eliminar registros
    $sql = "DELETE FROM estadistica_jugador WHERE id_torneo = 41";
    if ($conexion->query($sql) === TRUE) {
        echo "Registros eliminados correctamente.";
    } else {
        echo "Error al eliminar los registros: " . $conexion->error;
    }

    // Cerrar la conexión
    $conexion->close();
}

// Llamada a las funciones para mostrar las tablas, los valores de estadistica_jugador y eliminar registros específicos
mostrar_tablas();
mostrar_estadisticas_jugador();
eliminar_jugadores_torneo_46();
?>
