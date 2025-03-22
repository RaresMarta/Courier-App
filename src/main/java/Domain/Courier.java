package Domain;

import java.util.Objects;

public class Courier {
    private int id;
    private String name;
    private String streets;
    private final Point zoneCenter;
    private final float zoneRadius;

    public Courier(int id, String name, String streets, Point zoneCenter, float zoneRadius) {
        this.id = id;
        this.name = name;
        this.streets = streets;
        this.zoneCenter = zoneCenter;
        this.zoneRadius = zoneRadius;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStreets() {
        return streets;
    }

    public Point getZoneCenter() {
        return zoneCenter;
    }

    public float getZoneRadius() {
        return zoneRadius;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Courier courier = (Courier) o;
        return id == courier.id && Float.compare(zoneRadius, courier.zoneRadius) == 0 && Objects.equals(name, courier.name) && Objects.equals(streets, courier.streets) && Objects.equals(zoneCenter, courier.zoneCenter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, streets, zoneCenter, zoneRadius);
    }

    @Override
    public String toString() {
        return "Courier{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", streets='" + streets + '\'' +
                ", zoneCenter=" + zoneCenter +
                ", zoneRadius=" + zoneRadius +
                '}';
    }
}
