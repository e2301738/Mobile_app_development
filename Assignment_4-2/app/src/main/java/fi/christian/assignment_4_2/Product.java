package fi.christian.assignment_4_2;

public class Product {
    private String id, name;
    private double unitPrice;
    private int amount;

    public Product(String id, String name, double unitPrice, int amount) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.amount = amount;
    }

    public double getTotalValue() {
        return unitPrice * amount;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ID: ").append(id).append("\n");
        stringBuilder.append("Name: ").append(name).append("\n");
        stringBuilder.append("Unit Price: ").append(unitPrice).append("€").append("\n");
        stringBuilder.append("Quantity: ").append(amount).append("\n");
        stringBuilder.append("Total Value: ").append(String.format("%.2f", getTotalValue())).append("€\n");
        return stringBuilder.toString();
    }
}
