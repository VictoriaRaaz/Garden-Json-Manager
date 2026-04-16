package com.example;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Main {
    public static void main(String[] args) throws Exception {
        // Ruta de donde se encuentra el archivo .json
        String ruta = "C:\\Users\\victo\\Desktop\\TrabajoAED\\Garden-Json-Manager\\jardineria.json";

        boolean a = true;

        Scanner sc = new Scanner(System.in);

        do {
            List<Producto> productos = convertirJsonAObjeto(ruta);
            System.out.println("--- Menú ---");
            System.out.println("1. Mostrar datos.");
            System.out.println("2. Añadir producto.");
            System.out.println("3. Modificar precio del producto.");
            System.out.println("4. Eliminar prodcuto.");
            System.out.println("5. Buscar por ID.");
            System.out.println("6. Salir.");

            switch (sc.nextInt()) {
                case 1:
                    System.out.println("1. Mostrar datos");
                    break;

                case 2:
                    System.out.println("2. Añadir producto.");

                    int idNuevo;
                    String nombreNuevo;
                    double precioNuevo;
                    int stockNuevo;

                    System.out.print("Introduce el ID del producto: ");
                    while (!sc.hasNextInt()) {
                        System.out.println("Debe introducir un ID válido.");
                        sc.next();
                    }
                    idNuevo = sc.nextInt();
                    sc.nextLine(); // limpiar buffer

                    System.out.print("Introduce el nombre del producto: ");
                    nombreNuevo = sc.nextLine();

                    System.out.print("Introduce el precio del producto: ");
                    while (!sc.hasNextDouble()) {
                        System.out.println("Debe introducir un precio con formato válido.");
                        sc.next();
                    }
                    precioNuevo = sc.nextDouble();

                    System.out.print("Introduce el stock del producto: ");
                    while (!sc.hasNextInt()) {
                        System.out.println("Debe introducir un stock válido.");
                        sc.next();
                    }
                    stockNuevo = sc.nextInt();

                    añadirProducto(productos, idNuevo, nombreNuevo, precioNuevo, stockNuevo);
                    guardar(ruta, productos);
                    break;

                case 3:
                    System.out.println("3. Modificar precio del producto.");

                    double nuevoPrecio;
                    int idBuscado;

                    System.out.print("Introduce el id del producto: ");

                    while (!sc.hasNextInt()) {
                        System.out.println("Debe introducir un precio con formato válido.");
                        sc.next(); // limpia el valor incorrecto
                    }
                    idBuscado = sc.nextInt();

                    System.out.print("Introduce el nuevo precio: ");

                    while (!sc.hasNextDouble()) {
                        System.out.println("Debe introducir un precio con formato válido.");
                        sc.next(); // limpia el valor incorrecto
                    }
                    nuevoPrecio = sc.nextDouble();

                    // guardar al modificar
                    modificarProducto(productos, idBuscado, nuevoPrecio);
                    guardar(ruta, productos);

                    break;
                    
                case 4:
                    System.out.println("4. Eliminar prodcuto.");
                    break;
                
                case 5:
                    System.out.println("5. Buscar por ID.");
                    break;
                    
                case 6:
                    a = false;
                    break;

                default:
                    System.out.println("Opción no válida.");
                    break;
            }

        } while (a);

    }

    // Metodo para convertir el .json en una lista de objetos
    public static List<Producto> convertirJsonAObjeto(String ruta) throws Exception {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(ruta)) {
            List<Producto> productos = gson.fromJson(reader, new TypeToken<List<Producto>>() {
            }.getType());
            return productos != null ? productos : new ArrayList<>();
        }

    }

    // Metodo modificar
    public static void modificarProducto(List<Producto> productos, int idBuscado, double nuevoPrecio) {
        boolean encontrado = false;

        for (Producto p : productos) {
            if (p.getId() == idBuscado) { // Buscamos por ID como en el ejemplo del PDF [cite: 117]
                p.setPrecio(nuevoPrecio); // Aplicamos el cambio en memoria
                encontrado = true;
                System.out.println("Producto actualizado correctamente.");
                break;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontró ningún producto con ID: " + idBuscado);
        }
    }

    // Metodo para añadir producto
    public static void añadirProducto(List<Producto> productos, int id, String nombre, double precio, int stock) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                System.out.println("Ya existe un producto con ese ID.");
                return;
            }
        }

        if (precio < 0) {
            System.out.println("El precio no puede ser negativo.");
            return;
        }

        if (stock < 0) {
            System.out.println("El stock no puede ser negativo.");
            return;
        }

        Producto nuevoProducto = new Producto(id, nombre, precio, stock);
        productos.add(nuevoProducto);
        System.out.println("Producto añadido correctamente.");
    }

    // Metodo guardarn datos
    public static void guardar(String rutaJson, List<Producto> productos) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // PrettyPrinting para que el JSON sea legible
        try (FileWriter writer = new FileWriter(rutaJson)) {
            gson.toJson(productos, writer); // Convierte la lista a JSON y la escribe [cite: 152, 155]
        }
    }

}