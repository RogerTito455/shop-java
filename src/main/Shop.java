package main;

import model.Product;
import model.Sale;
import java.util.Scanner;
import model.Amount;

public class Shop {

    private double cash = 100.00;
    private Product[] inventory;
    private int numberProducts;
    private Sale[] sales;
    private int numberSales;

    final static double TAX_RATE = 1.04;

    public Shop() {
        inventory = new Product[10];
        sales = new Sale[10];
    }

    public static void main(String[] args) {
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
                case 10:
                    exit = true;
                    break;
            }
        } while (!exit);
    }

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
        if (isInventoryFull()) {
            System.out.println("No se pueden a\u00f1adir mas productos");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nombre: ");
        String name = scanner.nextLine();
        for (Product product : inventory) {
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
            // ask for stock
            System.out.print("Seleccione la cantidad a a\u00f1adir: ");
            int stock = scanner.nextInt();
            // update stock product
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
        for (Product product : inventory) {
            if (product != null) {
                System.out.println(product);
            }
        }
    }

    /**
     * make a sale of products to a client
     */
    public void sale() {
        // ask for client name
        Scanner sc = new Scanner(System.in);
        System.out.println("Realizar venta, escribir nombre cliente");
        String client = sc.nextLine();

        // sale product until input name is not 0
        Amount totalAmount = new Amount(0.0);
        String name = "";
        Product[] soldProducts = new Product[10];
        int count = 0;

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
                soldProducts[count] = product;
                count++;

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
        Product[] finalProducts = new Product[count];
        for (int i = 0; i < count; i++) {
            finalProducts[i] = soldProducts[i];
        }
        // show cost total
        totalAmount.multiply(TAX_RATE);
        cash += totalAmount.getValue();

        addSale(new Sale(client, finalProducts, totalAmount));


        System.out.println("Venta realizada con exito, total: " + totalAmount.getValue());
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
        System.out.println("Ventas totales " + numberSales);
    }

    //Agregar y asegurar no sobrepasar la cantidad máxima
    public boolean isSalesFull() {
        if (numberSales == 10) {
            return true;
        }
        return false;
    }

    public void addSale(Sale sale) {
        if (isSalesFull()) {
            System.out.println("No se pueden añadir más productos");
            return;
        }
        sales[numberSales] = sale;
        numberSales++;
    }

    /**
     * add a product to inventory
     *
     * @param product
     */
    public void addProduct(Product product) {
        if (isInventoryFull()) {
            System.out.println("No se pueden a\u00f1adir mas productos, se ha alcanzado el maximo de " + inventory.length);
            return;
        }
        inventory[numberProducts] = product;
        numberProducts++;
    }

    /**
     * check if inventory is full or not
     *
     * @return true if inventory is full
     */
    public boolean isInventoryFull() {
        if (numberProducts == 10) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * find product by name
     *
     * @param name
     * @return product found by name
     */
    public Product findProduct(String name) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null && inventory[i].getName().equals(name)) {
                return inventory[i];
            }
        }
        return null;
    }

}
