package main;

import java.util.ArrayList;
import model.Product;
import model.Sale;
import java.util.Scanner;
import model.Amount;
import model.Client;
import model.Employee;

public class Shop {

    private double cash = 100.00;
    //Array to ArrayList
    private ArrayList<Product> products = new ArrayList();
    private ArrayList<Sale> sales = new ArrayList<>();

    final static double TAX_RATE = 1.04;

    public Shop() {
        products = new ArrayList();
        sales = new ArrayList<>();
    }

    public static void main(String[] args) {
        Boolean a;
        do {
            a = initSession();
        } while (!a);

        Shop shop = new Shop();

        shop.loadInventory();

        Scanner scanner = new Scanner(System.in);
        int opcion = 0;
        boolean exit = false;

        do {
            System.out.println("\n");
            System.out.println("===========================");
            System.out.println("Menu principal miTienda.com");
            System.out.println("===========================");
            System.out.println("1) Contar caja");
            System.out.println("2) A\u00f1adir producto");
            System.out.println("3) A\u00f1adir stock");
            System.out.println("4) Marcar producto proxima caducidad");
            System.out.println("5) Ver inventario");
            System.out.println("6) Venta");
            System.out.println("7) Ver ventas");
            System.out.println("8) Ver ventas totales");
            System.out.println("9) Eliminar producto");

            System.out.println("10) Salir programa");
            System.out.print("Seleccione una opcion: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    shop.showCash();
                    break;

                case 2:
                    shop.addProduct();
                    break;
                case 3:
                    shop.addStock();
                    break;

                case 4:
                    shop.setExpired();
                    break;

                case 5:
                    shop.showInventory();
                    break;
                case 6:
                    shop.sale();
                    break;
                case 7:
                    shop.showSales();
                    break;
                case 8:
                    shop.showTotalSales();
                    break;
                case 9:
                    shop.deleteProduct();
                    break;
                case 10:
                    exit = true;
                    break;
            }
        } while (!exit);
    }

    public static boolean initSession() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Dame el nombre del empleado");
        String name = sc.nextLine();
        System.out.println("Dime el id");
        int idEmpleado = sc.nextInt();
        sc.nextLine();
        System.out.println("Dime la contraseña");
        String password = sc.nextLine();
        Employee empl = new Employee(name);
        Boolean checkPassword = empl.login(idEmpleado, password);
        if (checkPassword) {
            return true;
        }
        System.out.println("Usuario o contraseña incorrecta");
        return false;
    }

    /**
     * Employee log in
     */
    /**
     * load initial inventory to shop
     */
    public void loadInventory() {
        addProduct(new Product("Manzana", new Amount(20.00), new Amount(10.00), true, 10));
        addProduct(new Product("Pera", new Amount(40.00), new Amount(20.00), true, 20));
        addProduct(new Product("Hamburguesa", new Amount(60.00), new Amount(30.00), true, 30));
        addProduct(new Product("Fresa", new Amount(10.00), new Amount(5.00), true, 20));
    }

    /**
     * show current total cash
     */
    private void showCash() {
        System.out.println("Dinero actual: " + cash);
    }

    /**
     * add a new product to inventory getting data from console
     */
    public void addProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nombre: ");
        String name = scanner.nextLine();
        for (Product product : products) {
            if (product != null && product.getName().equalsIgnoreCase(name)) {
                System.out.println("El producto existe");
                return;
            }
        }
        System.out.println("Precio de venta: ");
        double publicPrice = scanner.nextDouble();
        System.out.print("Precio mayorista: ");
        double wholesalerPrice = scanner.nextDouble();
        System.out.print("Stock: ");
        int stock = scanner.nextInt();

        addProduct(new Product(name, new Amount(publicPrice), new Amount(wholesalerPrice), true, stock));

        System.out.println("Se ha creado correctamente: " + name);
    }

    /**
     * add stock for a specific product
     */
    public void addStock() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleccione un nombre de producto: ");
        String name = scanner.next();
        Product product = findProduct(name);

        if (product != null) {
            System.out.print("Seleccione la cantidad a a\u00f1adir: ");
            int stock = scanner.nextInt();
            product.setStock(product.getStock() + stock);
            System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getStock());

        } else {
            System.out.println("No se ha encontrado el producto con nombre " + name);
        }
    }

    /**
     * set a product as expired
     */
    private void setExpired() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleccione un nombre de producto: ");
        String name = scanner.next();

        Product product = findProduct(name);

        if (product == null) {
            System.out.println("Producto no encontrado");
            return;
        }

        product.expire();
        System.out.println("El producto " + name + " ha sido marcado como caducado " + product.getPublicPrice().getValue());
    }

    /**
     * show all inventory
     */
    public void showInventory() {
        System.out.println("Contenido actual de la tienda:");
        for (Product product : products) {
            if (product != null) {
                System.out.println(product);
            }
        }
    }

    /**
     * make a sale of products to a client
     */
    public void sale() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Realizar venta, escribir nombre cliente");
        String client = sc.nextLine();

        Amount totalAmount = new Amount(0.0);
        String name = "";
        ArrayList<Product> soldProducts = new ArrayList<>();

        while (!name.equals("0")) {
            System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
            name = sc.nextLine();

            if (name.equals("0")) {
                break;
            }
            Product product = findProduct(name);
            boolean productAvailable = false;

            if (product != null && product.isAvailable()) {
                productAvailable = true;
                totalAmount.add(product.getPublicPrice());
                soldProducts.add(product); //Pasmos todo a arrayList
                product.setStock(product.getStock() - 1);
                // if no more stock, set as not available to sale
                if (product.getStock() == 0) {
                    product.setAvailable(false);
                }
                System.out.println("Producto a\u00f1adido con Ã©xito");
            }

            if (!productAvailable) {
                System.out.println("Producto no encontrado o sin stock");
            }
        }
        //Agregamos cliente;
        Client cl = new Client(name);
        totalAmount.multiply(TAX_RATE); //CalculamoRs la cantidad total que ha de pagar

        if (cl.pay(totalAmount)) {
            cash += totalAmount.getValue();
            addSale(new Sale(cl, soldProducts, totalAmount));
            System.out.println("Venta realizada con exito, total: " + totalAmount.getValue());
            System.out.println("Dinero que le queda al cliente  " + cl.getBalance());
        } else {
            cash += totalAmount.getValue();
            addSale(new Sale(cl, soldProducts, totalAmount));
            System.out.println("Venta realizada con exito, total: " + totalAmount.getValue());
            System.out.println("Dinero que le queda al cliente  " + cl.getBalance());
        }
    }

    /**
     * show all sales
     */
    private void showSales() {
        System.out.println("Lista de ventas:");
        for (Sale sale : sales) {
            if (sale != null) {
                System.out.println(sale);
            }
        }
    }

    public void showTotalSales() {
        System.out.println("Ventas totales " + sales.size());
    }

    public void deleteProduct() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Dime el nombre del producto");
        String prod = scanner.nextLine();
        Product pr = findProduct(prod);
        if (pr == null) {
            System.out.println("Produco no encontrado");
            return;
        }
        products.remove(pr);
        System.out.println("Se ha borrado el producto " + pr.getName() + " correctamente");
    }

    public void addSale(Sale sale) {
        sales.add(sale);
    }

    /**
     * add a product to inventory
     *
     * @param product
     */
    public void addProduct(Product product) {
        products.add(product);
    }

    /**
     * check if inventory is full or not
     *
     * @return true if inventory is full
     */
    /**
     * find product by name
     *
     * @param name
     * @return product found by name
     */
    public Product findProduct(String name) {
        for (Product p : products) {
            if (p != null && p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

}
