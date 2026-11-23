import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Calculadora {

    public static void main(String[] args) throws IOException {
        HttpServer servidor = HttpServer.create(new InetSocketAddress("127.0.0.1", 8080), 0);
        servidor.createContext("/", Calculadora::atender);
        servidor.start();
        System.out.println("Calculadora abierta en http://localhost:8080");
    }

    static void atender(HttpExchange intercambio) throws IOException {
        String ruta = intercambio.getRequestURI().getPath();
        if (!ruta.equals("/") && !ruta.equals("/index.html")) {
            responder(intercambio, 404, "Pagina no encontrada");
            return;
        }
        Map<String, String> datos = leerParametros(intercambio.getRequestURI().getRawQuery());
        String resultado = "";
        if (datos.containsKey("operacion")) {
            resultado = calcular(datos);
        }
        String pagina = Files.readString(Path.of("index.html")).replace("{{resultado}}", resultado);
        responder(intercambio, 200, pagina);
    }

    static String calcular(Map<String, String> datos) {
        Integer a = leerNumero(datos.get("a"));
        Integer b = leerNumero(datos.get("b"));
        if (a == null || b == null) {
            return "Error: los numeros tienen que ser enteros entre -99999 y 99999";
        }
        switch (datos.get("operacion")) {
            case "suma":
                return a + " + " + b + " = " + sumar(a, b);
            case "resta":
                return a + " - " + b + " = " + restar(a, b);
            case "multiplicacion":
                return a + " x " + b + " = " + multiplicar(a, b);
            case "potencia":
                return a + " ^ " + b + " = " + potencia(a, b);
            default:
                return "Operacion no valida";
        }
    }

    static Integer leerNumero(String texto) {
        if (texto == null || !texto.matches("-?\\d{1,5}")) {
            return null;
        }
        return Integer.parseInt(texto);
    }

    static long sumar(int a, int b) {
        return (long) a + b;
    }

    static long restar(int a, int b) {
        return (long) a - b;
    }

    static long multiplicar(int a, int b) {
        return (long) a * b;
    }

    static String potencia(int base, int exponente) {
        if (exponente >= 0) {
            return BigInteger.valueOf(base).pow(exponente).toString();
        }
        if (base == 0) {
            return "no existe (0 con exponente negativo)";
        }
        BigDecimal divisor = new BigDecimal(BigInteger.valueOf(base).pow(-exponente));
        return BigDecimal.ONE.divide(divisor, MathContext.DECIMAL64).toString();
    }

    static Map<String, String> leerParametros(String consulta) {
        Map<String, String> datos = new HashMap<>();
        if (consulta == null) {
            return datos;
        }
        for (String par : consulta.split("&")) {
            String[] partes = par.split("=", 2);
            String valor = partes.length > 1 ? URLDecoder.decode(partes[1], StandardCharsets.UTF_8) : "";
            datos.put(URLDecoder.decode(partes[0], StandardCharsets.UTF_8), valor.trim());
        }
        return datos;
    }

    static void responder(HttpExchange intercambio, int codigo, String texto) throws IOException {
        byte[] bytes = texto.getBytes(StandardCharsets.UTF_8);
        intercambio.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        intercambio.sendResponseHeaders(codigo, bytes.length);
        try (OutputStream salida = intercambio.getResponseBody()) {
            salida.write(bytes);
        }
    }
}
