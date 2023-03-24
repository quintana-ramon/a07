package E4;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServidorSockets {

    // Configuramos el puerto para el servidor
    private static final int _PUERTO = 1234;
    private static final int _BACKLOG = 50;

    public static void main(String[] args) throws UnknownHostException {

        // Usamos un manejador de formato para el log del servidor
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SS");

        // Primero indicamos la dirección IP local
        try {
            // Si se usa localhost:
            System.out.println("IP de LocalHost = " + InetAddress.getLocalHost().toString());
            System.out.println("Puerto = " + _PUERTO);
        } catch (UnknownHostException uhe) {
            System.err.println("No puedo saber la dirección IP local : " + uhe);
        }

        // Abrimos un "Socket de Servidor" TCP en el puerto 1234.
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(_PUERTO, _BACKLOG);
        } catch (IOException ioe) {
            System.err.println("Error al abrir el socket de servidor : " + ioe);
            System.exit(-1);
        }

        // Bucle infinito
        boolean flag = true;
        while (flag) {
            try {
                // Esperamos a que alguien se conecte a nuestro Socket
                Socket socketPeticion = serverSocket.accept();

                // Extraemos los flujos de entrada y de salida
                DataInputStream datosEntrada = new DataInputStream(
                        new BufferedInputStream(socketPeticion.getInputStream()));
                DataOutputStream datosSalida = new DataOutputStream(socketPeticion.getOutputStream());

                // Podemos extraer informacion del socket
                // N° de puerto remoto
                int puertoRemitente = socketPeticion.getPort();
                // Dirección de IP remota
                InetAddress ipRemitente = socketPeticion.getInetAddress();

                // Leemos el tipo de dato del mensaje
                char tipDato = datosEntrada.readChar();

                // Obtenemos la longitud del mensaje
                int longitud = datosEntrada.readInt();

                if (tipDato == 's') {

                    // Arreglo de bytes que contienen la data del mensaje
                    byte[] bytesDatos = new byte[longitud];

                    // Bandera para determinar que el contenido de la data ha sido leido
                    boolean finData = false;

                    // Usamos StringBuilder para armar el mensaje en la data del paquete
                    StringBuilder dataEnMensaje = new StringBuilder(longitud);

                    // Inicializamos un acumulador de datos leidos
                    int totalBytesLeidos = 0;

                    // Comenzamos lectura hasta llegar al final de los datos
                    while (!finData) {
                        // Leemos los bytes de la data
                        int bytesActualesLeidos = datosEntrada.read(bytesDatos);

                        // Actualizamos el contador de lectura de datos
                        totalBytesLeidos = bytesActualesLeidos + totalBytesLeidos;

                        // Construimos el mensaje con StringBuilder
                        if (totalBytesLeidos <= longitud) {
                            dataEnMensaje
                                    .append(new String(bytesDatos, 0, bytesActualesLeidos, StandardCharsets.UTF_8));
                        } else {
                            dataEnMensaje.append(new String(bytesDatos, 0,
                                    longitud - totalBytesLeidos + bytesActualesLeidos, StandardCharsets.UTF_8));
                        }

                        // Determinamos si toda la data ha sido leida
                        if (dataEnMensaje.length() >= longitud)
                            finData = true;
                    }

                    // Registramos en salida el log
                    System.out.println(formatter.format(new Date()) + "\tCliente = " + ipRemitente + ":"
                            + puertoRemitente + "\tEntrada = " + dataEnMensaje.toString() + "\tSalida = " + "OK");
                }

                // Escribimos el resultado
                datosSalida.writeUTF("OK");

                // Cerramos los flujos
                datosEntrada.close();
                datosSalida.close();
                socketPeticion.close();

            } catch (Exception e) {
                System.err.println("Se ha producido la excepción : " + e);
                flag = false;
            }
        }
    }
}
