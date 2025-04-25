package util;

import model.Flower;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FlowerDataLoader {
    public static List<Flower> loadFlowersFromDB() throws Exception {
        List<Flower> flowers = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT name, price FROM flowers";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                flowers.add(new Flower(name, price));
            }
        }

        return flowers;
    }
}
