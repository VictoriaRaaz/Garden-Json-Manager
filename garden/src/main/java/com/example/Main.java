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
        // Ruta del archivo .json
        String ruta = "jardineria.json";
        boolean a = true;
        Scanner sc = new Scanner(System.in);

        do {
            List<Producto> productos = convertirJsonAObjeto(ruta);

            System.out.println("\n--- Menú Tienda de Jardinería ---");
            System.out.println("1. Mostrar todos los productos.");
            System.out.println("2. Añadir producto.");
            System.out.println("3. Modificar precio del producto.");
            System.out.println("4. Eliminar producto.");
            System.out.println("5. Buscar por ID.");
            System.out.println("6. Salir.");
            System.out.print("Seleccione una opción: ");

            int opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("\n--- Listado de Inventario ---");
                    if (productos.isEmpty()) {
                        System.out.println("El inventario está vacío.");
                    } else {
                        // Recorremos la lista cargada en memoria
                        for (Producto p : productos) {
                            System.out.println("ID: " + p.getId() + " | Nombre: " + p.getNombre() +
                                    " | Precio: " + p.getPrecio() + "€ | Stock: " + p.getStock());
                        }
                    }
                    break;

                case 2:
                    System.out.println("\n--- Añadir Nuevo Producto ---");
                    System.out.print("Introduce el ID: ");
                    int idNuevo = validarEntero(sc);
                    sc.nextLine(); // Limpiar buffer

                    System.out.print("Introduce el nombre: ");
                    String nombreNuevo = sc.nextLine();

                    System.out.print("Introduce el precio: ");
                    double precioNuevo = validarDouble(sc);

                    System.out.print("Introduce el stock: ");
                    int stockNuevo = validarEntero(sc);

                    añadirProducto(productos, idNuevo, nombreNuevo, precioNuevo, stockNuevo);
                    guardar(ruta, productos); 
                    break;

                case 3:
                    System.out.println("\n--- Modificar Producto ---");
                    System.out.print("ID del producto a modificar: ");
                    int idMod = validarEntero(sc);
                    sc.nextLine(); // Limpiar el buffer importante después de un nextInt()

                    System.out.print("Nuevo precio (deja en blanco para no cambiar): ");
                    String inputPrecio = sc.nextLine();

                    System.out.print("Nuevo stock (deja en blanco para no cambiar): ");
                    String inputStock = sc.nextLine();

                    modificarProducto(productos, idMod, inputPrecio, inputStock);
                    guardar(ruta, productos);
                    break;

                case 4:
                    System.out.println("\n--- Eliminar Producto ---");
                    System.out.print("Introduce el ID del producto a borrar: ");
                    int idEliminar = validarEntero(sc);

                    eliminarProducto(productos, idEliminar);
                    guardar(ruta, productos); // Actualizar fichero [cite: 156]
                    break;

                case 5:
                    System.out.println("\n--- Búsqueda de Producto ---");
                    System.out.print("Introduce el ID a buscar: ");
                    int idBuscar = validarEntero(sc);
                    buscarPorId(productos, idBuscar); // Solo consulta, no cambia el fichero [cite: 115]
                    break;

                case 6:
                    System.out.println("Saliendo del sistema...");
                    a = false;
                    break;

                default:
                    System.out.println("Opción no válida.");
                    break;
            }

        } while (a);
        sc.close();
    }


    public static void eliminarProducto(List<Producto> productos, int id) {
        // Buscamos el objeto y lo eliminamos de la lista en memoria [cite: 83-112]
        boolean eliminado = productos.removeIf(p -> p.getId() == id);
        if (eliminado) {
            System.out.println("Producto con ID " + id + " eliminado con éxito.");
        } else {
            System.out.println("No se encontró ningún producto con ese ID.");
        }
    }

    public static void buscarPorId(List<Producto> productos, int id) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                System.out.println("Producto encontrado:");
                System.out.println("ID: " + p.getId());
                System.out.println("Nombre: " + p.getNombre());
                System.out.println("Precio: " + p.getPrecio() + "€");
                System.out.println("Stock: " + p.getStock());
                return;
            }
        }
        System.out.println("Producto no encontrado.");
    }


    public static int validarEntero(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Error. Introduce un número entero: ");
            sc.next();
        }
        return sc.nextInt();
    }

    public static double validarDouble(Scanner sc) {
        while (!sc.hasNextDouble()) {
            System.out.print("Error. Introduce un precio válido (ej: 10,50): ");
            sc.next();
        }
        return sc.nextDouble();
    }


    public static List<Producto> convertirJsonAObjeto(String ruta) throws Exception {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(ruta)) {
            List<Producto> productos = gson.fromJson(reader, new TypeToken<List<Producto>>() {
            }.getType());
            return productos != null ? productos : new ArrayList<>();
        }
    }

    public static void modificarProducto(List<Producto> productos, int idBuscado, String inputPrecio,
            String inputStock) {
        boolean encontrado = false;

        for (Producto p : productos) {
            if (p.getId() == idBuscado) {
                encontrado = true;

                // Si el usuario escribio algo en precio, lo actualizamos
                if (!inputPrecio.trim().isEmpty()) {
                    try {
                        double nPrecio = Double.parseDouble(inputPrecio.replace(",", "."));
                        p.setPrecio(nPrecio);
                        System.out.println("Precio actualizado.");
                    } catch (NumberFormatException e) {
                        System.out.println("Formato de precio incorrecto. No se cambió.");
                    }
                }

                // Si el usuario escribio algo en stock, lo actualizamos
                if (!inputStock.trim().isEmpty()) {
                    try {
                        int nStock = Integer.parseInt(inputStock.trim());
                        p.setStock(nStock);
                        System.out.println("Stock actualizado.");
                    } catch (NumberFormatException e) {
                        System.out.println("Formato de stock incorrecto. No se cambió.");
                    }
                }

                if (inputPrecio.isEmpty() && inputStock.isEmpty()) {
                    System.out.println("No se realizaron cambios.");
                }
                break;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontró el producto con ID: " + idBuscado);
        }
    }

    public static void añadirProducto(List<Producto> productos, int id, String nombre, double precio, int stock) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                System.out.println("Error: Ya existe un producto con ID " + id);
                return;
            }
        }
        productos.add(new Producto(id, nombre, precio, stock));
        System.out.println("Producto añadido en memoria.");
    }

    public static void guardar(String rutaJson, List<Producto> productos) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(rutaJson)) {
            gson.toJson(productos, writer);
        }
    }
}