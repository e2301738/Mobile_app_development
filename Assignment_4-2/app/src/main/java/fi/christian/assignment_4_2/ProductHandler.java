package fi.christian.assignment_4_2;

import java.util.ArrayList;

public class ProductHandler {
    private static ArrayList<Product> productList = new ArrayList<>();

    public static void addProduct(Product product) {
        productList.add(product);
    }

    public static boolean isDuplicate(String id) {
        for (Product product : productList) {
            if (product.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static String getSummary() {
        StringBuilder summaryBuilder = new StringBuilder();
        for (Product product : productList) {
            summaryBuilder.append(product.toString()).append("\n");
        }
        return summaryBuilder.toString();
    }
}
