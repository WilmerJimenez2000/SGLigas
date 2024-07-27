El numero 1. corresponde a todos los archivos php que sean de usuario
El numero 2. corresponde a todos los archivos php que sean de ligas
El numero 3. corresponde a todos los archivos php que sean de categoria
El numero 4. corresponde a todos los archivos php que sean de equipo
El numero 5. corresponde a todos los archivos php que sean de jugadores
El numero 6. corresponde a todos los archivos php que sean de torneo
El numero 7. corresponde a todos los archivos php que sean de partido
El numero 8. corresponde a todos los archivos php que sean de sancion

Array de partidos
// Se asigan los valores respectivos al array jornada
                        for($j=0;$j<$num_partidos;$j++){
                            $jornada[$j][0]=$j+1;
                            $jornada[$j][1]=""; // Nombre equipo local
                            $jornada[$j][2]=""; // Escudo equipo local
                            $jornada[$j][3]=$resultado[$i][$k]; // Id equipo local
                            $jornada[$j][4]="0"; // Goles equipo local
                            $jornada[$j][5]="0"; // Goles equipo visitante
                            $jornada[$j][6]=$resultado[$i][$k+1]; // Id equipo visitante
                            $jornada[$j][7]=""; // Nombre equipo visitante
                            $jornada[$j][8]=""; // Escudo equipo visitante
                            $k=$k+2;
                            $jornada[$j][9]="1970-01-01"; // Fecha del partido
                            $jornada[$j][10]="00:00"; // Hora del partido
                            $jornada[$j][11]="vocal"; 
                            $jornada[$j][12]="veedor";
                            $jornada[$j][13]="cancha";
                        }