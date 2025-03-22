package Domain;

import Util.Point;

import java.util.Objects;

public class Package {
    int id;
    String recipient;
    String address;
    Point location;
    boolean status;

    public Package(int id, String recipient, String address, Point location, boolean status) {
        this.id = id;
        this.recipient = recipient;
        this.address = address;
        this.location = location;
        this.status = status;
    }

    public boolean belongsTo(Courier c) {
        // Check if the package belongs to the courier
        return (c.getStreets().contains(this.getAddress()) ||
                (Point.distance(this.location, c.getZoneCenter()) <= c.getZoneRadius()));
    }

    public int getId() {
        return id;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getAddress() {
        return address;
    }

    public Point getLocation() {
        return location;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Package aPackage = (Package) o;
        return id == aPackage.id && status == aPackage.status && Objects.equals(recipient, aPackage.recipient) && Objects.equals(address, aPackage.address) && Objects.equals(location, aPackage.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipient, address, location, status);
    }

    @Override
    public String toString() {
        return "Package{" +
                "id=" + id +
                ", recipient='" + recipient + '\'' +
                ", address='" + address + '\'' +
                ", location=" + location +
                ", status=" + status +
                '}';
    }
}
