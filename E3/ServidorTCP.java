package E3;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServidorTCP {
    // * Configuramos la IP y el Puerto para el servidor
    private static final int _PUERTO = 1234;
    private static final int _BACKLOG = 50;

    public static void main(String[] args) throws UnknownHostException {
        // ? Usamos un manejador de formato para el long del servidor
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        // * Primero indicamos la direccion IP local
        try {
            // ? si se usa Localhost
            System.out.println("IP de LocalHost = " + InetAddress.getLocalHost().toString());
            System.out.println("Puerto = " + _PUERTO);
        } catch (UnknownHostException uhe) {
            System.err.println("No se pudo saber la direccion IP local : " + uhe);
        }
        // * Abrimos un "Socket de Servidor" TCP en el puerto 1234
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(_PUERTO, _BACKLOG); // <-- si usas localhost
            // serverSocket = new ServerSocket(_PUERTO, _BACKLOG, ip); // <-- si usa ip
            // especifica
        } catch (IOException ioe) {
            System.err.println("Error al abrir el socket de servidor : " + ioe);
            System.exit(-1);
        }

        int entrada;
        long salida;

        // ? Bucle infinito
        boolean flag = true;
        while (flag) {
            try {
                // * Esperamos a que alguien se conecte a nuestro socket
                Socket socketPeticion = serverSocket.accept();

                // * Extraemos los flujos de entrada y de salida
                DataInputStream datosEntrada = new DataInputStream(socketPeticion.getInputStream());
                DataOutputStream datosSalida = new DataOutputStream(socketPeticion.getOutputStream());

                // * Podemos extraer informacion del socket
                // * N° de puerto remoto
                int puertoRemitente = socketPeticion.getPort();
                // * Direccion de IP remota
                InetAddress ipRemitente = socketPeticion.getInetAddress();

                // ? Leemos datos de la peticion
                entrada = datosEntrada.readInt();

                // ? Hacemos el calculo correspondiente
                salida = (long) entrada * (long) entrada;

                // ? Escribimos el resultado
                datosSalida.writeLong(salida);

                // * Cerramos los flujos
                datosEntrada.close();
                datosSalida.close();
                socketPeticion.close();

                // * Registramos en salida el LOG
                System.out.println(formatter.format(new Date()) +
                        "Cliente = " + ipRemitente + ":" + puertoRemitente +
                        "\tEntrada = " + entrada
                        + "\tSalida = " + salida);
            } catch (Exception e) {
                System.err.println("Se ha producido la excepcion : " + e);
                flag = false;
            }
        }
    }
}
