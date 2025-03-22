package Repository;

import Domain.Package;
import Util.Point;

import java.sql.*;
import java.util.ArrayList;

public class PackageRepo {
    public ArrayList<Package> getAll() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite")) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Package");
            ResultSet rs = ps.executeQuery();
            ArrayList<Package> packages = new ArrayList<>();
            while (rs.next()) {
                packages.add(new Package(
                        rs.getInt(1),       // id
                        rs.getString(2),    // recipient
                        rs.getString(3),    // address
                        new Point(rs.getFloat(4), rs.getFloat(5)), // location
                        rs.getBoolean(6))); // status
            }
            return packages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(int id) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite")) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Package WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(Package p) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite")) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Package(Recipient, Address, LocationX, LocationY, Status) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, p.getRecipient());
            ps.setString(2, p.getAddress());
            ps.setFloat(3, p.getLocation().getX());
            ps.setFloat(4, p.getLocation().getY());
            ps.setBoolean(5, p.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Package p) {
        remove(p.getId());
        add(p);
    }
}
