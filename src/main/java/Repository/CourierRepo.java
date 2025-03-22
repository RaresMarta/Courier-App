package Repository;

import Domain.Courier;
import Domain.Point;

import java.sql.*;
import java.util.ArrayList;

public class CourierRepo {
    public ArrayList<Courier> getAll() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite")) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Courier");
            ResultSet rs = ps.executeQuery();
            ArrayList<Courier> couriers = new ArrayList<>();
            while (rs.next()) {
                couriers.add(new Courier(
                        rs.getInt(1),    // id
                        rs.getString(2), // name
                        rs.getString(3), // streets
                        new Point(rs.getFloat(4), rs.getFloat(5)), // zoneCenter
                        rs.getFloat(5))); // zoneRadius
            }
            return couriers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(int id) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite")) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Courier WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addCourier(Courier courier) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite")) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Courier(name, streets, zoneCenterX, zoneCenterY, zoneRadius) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, courier.getName());
            ps.setString(2, courier.getStreets());
            ps.setObject(3, courier.getZoneCenter().getX());
            ps.setObject(4, courier.getZoneCenter().getY());
            ps.setFloat(5, courier.getZoneRadius());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Courier courier) {
        remove(courier.getId());
        addCourier(courier);
    }
}
