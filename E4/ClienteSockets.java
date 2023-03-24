package E4;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ClienteSockets {

    private static final int _PUERTO = 1234;

    public static void main(String[] args) {
        // Leemos el primer parámetro, donde debe ir la dirección
        // IP del servidor
        InetAddress ipServidor = null;
        try {
            ipServidor = InetAddress.getByName(args[0]);
        } catch (UnknownHostException uhe) {
            System.err.println("Host no encontrado : " + uhe);
            System.exit(-1);
        }

        // Determinamos el numero de paquetes a enviar
        int noPaquetes = Integer.parseInt(args[1]);

        // Para cada uno de los argumentos...
        for (int n = 1; n <= noPaquetes; n++) {

            // Por cada valor a procesar configuramos las clases
            // para envio y recepción de datos
            Socket socketCliente = null;
            DataInputStream datosRecepcion = null;
            DataOutputStream datosEnvio = null;

            // Comenzamos proceso de comunicación
            try {

                // Simulamos obtencion de variable
                double temperatura = Math.round((Math.random() * 40 + 16) * 100.0) / 100.0;
                int humedad = (int) Math.random() * 1 + 99;
                double Co2 = Math.round((Math.random() * 200 + 50000) * 100.0) / 100.0;

                // Establecemos los valores para el paquete segun protocolo
                char tipo = 's'; // s para string
                String data = "$Temp|" + temperatura + "#Hum|" + humedad + "%#Co2|" + Co2 + "$";
                byte[] dataInBytes = data.getBytes(StandardCharsets.UTF_8);

                // Creamos el Socket
                socketCliente = new Socket(ipServidor, _PUERTO);

                // Extraemos los streams de entrada y salida
                datosRecepcion = new DataInputStream(socketCliente.getInputStream());
                datosEnvio = new DataOutputStream(socketCliente.getOutputStream());

                // Escribimos datos en el flujo segun protocolo
                datosEnvio.writeChar(tipo);
                datosEnvio.writeInt(dataInBytes.length);
                datosEnvio.write(dataInBytes);

                // Leemos el resultado final
                String resultado = datosRecepcion.readUTF();

                // Indicamos en pantalla
                System.out.println("Solicitud = " + data + "\tResultado = " + resultado);

                // y cerramos los streams y el socket
                datosRecepcion.close();
                datosEnvio.close();

            } catch (Exception e) {
                System.err.println("Se ha producido la excepcion : " + e);
            }
            try {
                if (socketCliente != null)
                    socketCliente.close();
            } catch (IOException ioe) {
                System.err.println("Error al cerrar el socket : " + ioe);
            }
        }
    }
}
