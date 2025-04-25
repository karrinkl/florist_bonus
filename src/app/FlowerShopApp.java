package app;

import model.Flower;
import service.BouquetCreator;
import util.FlowerDataLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class FlowerShopApp extends JFrame {
    private JTextField budgetField;
    private JTextArea outputArea;
    private JComboBox<String> sortComboBox;
    private JTextField typeField;

    private List<Flower> availableFlowers;
    private BouquetCreator creator = new BouquetCreator();

    public FlowerShopApp() {
        setTitle("Цветочница");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);
        setLayout(new BorderLayout());

        try {
            availableFlowers = FlowerDataLoader.loadFlowersFromDB();
        } catch (Exception e) {
            showError("Ошибка загрузки цветов из БД: " + e.getMessage());
            return;
        }

        JPanel topPanel = new JPanel(new GridLayout(4, 2));
        budgetField = new JTextField();
        sortComboBox = new JComboBox<>(new String[]{"Цена", "Название"});
        typeField = new JTextField();

        topPanel.add(new JLabel("Бюджет:"));
        topPanel.add(budgetField);
        topPanel.add(new JLabel("Сортировка:"));
        topPanel.add(sortComboBox);
        topPanel.add(new JLabel("Тип цветка для подсчёта:"));
        topPanel.add(typeField);

        JButton createButton = new JButton("Создать букет");
        createButton.addActionListener(this::handleCreateBouquet);
        topPanel.add(createButton);

        add(topPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        setVisible(true);
    }

    private void handleCreateBouquet(ActionEvent e) {
        outputArea.setText("");
        int budget;

        try {
            budget = Integer.parseInt(budgetField.getText());
        } catch (NumberFormatException ex) {
            showError("Введите корректную сумму.");
            return;
        }

        try {
            List<Flower> bouquet = creator.createBouquet(availableFlowers, budget);
            String sortBy = (String) sortComboBox.getSelectedItem();
            creator.sortBouquet(bouquet, sortBy);

            outputArea.append("Состав букета:\n");
            bouquet.forEach(f -> outputArea.append(f + "\n"));

            String type = typeField.getText().trim();
            if (!type.isEmpty()) {
                long count = creator.countFlowersByType(bouquet, type);
                outputArea.append("\nКоличество цветов типа '" + type + "': " + count + "\n");
            }

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FlowerShopApp::new);
    }
}
