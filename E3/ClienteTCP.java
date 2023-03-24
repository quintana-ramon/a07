package E3;

import java.io.*;
import java.net.*;

class ClienteTCP {
    private static final int _PUERTO = 1234;

    public static void main(String[] args) {
        // ? Leemos el primer parametro, donde debe ir la direccion
        // ? IP del servidor
        InetAddress ipServidor = null;
        try {
            ipServidor = InetAddress.getByName(args[0]);
        } catch (UnknownHostException uhe) {
            System.err.println("Host no encontrado : " + uhe);
            System.exit(-1);
        }

        // * Para cada uno de los argumentos...
        for (int n = 1; n < args.length; n++) {

            // ? Por cada valor a procesar configuramos las clases
            // ? Para envio y recepción de datos
            Socket socketCliente = null;
            DataInputStream datosRecepcion = null;
            DataOutputStream datosEnvio = null;

            // * Comenzamos proceso de comunicacion
            try {
                // ? Convertimos el texto en número
                int numero = Integer.parseInt(args[n]);

                // * Creamos el socket
                socketCliente = new Socket(ipServidor, _PUERTO);

                // ? Extraemos los streams de entrada y salida
                datosRecepcion = new DataInputStream(socketCliente.getInputStream());
                datosEnvio = new DataOutputStream(socketCliente.getOutputStream());

                // * Lo escribimos
                datosEnvio.writeInt(numero);

                // * Leemos el resultado final
                long resultado = datosRecepcion.readLong();

                // * Indicamos en pantalla
                System.out.println("Solicitud = " + numero + "\tResultado = " + resultado);

                // * y Cerramos los streams y el socket
                datosRecepcion.close();
                datosEnvio.close();

            } catch (Exception e) {
                System.err.println("Se ha producido una excepcion : " + e);
            }
            try {
                if (socketCliente != null) {
                    socketCliente.close();
                }
            } catch (IOException ioe) {
                System.err.println("Error al cerrar el socket : " + ioe);
            }
        }
    }
}