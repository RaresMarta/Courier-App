package Service;

import Domain.Courier;
import Repository.CourierRepo;
import Repository.PackageRepo;

import java.util.ArrayList;

public class CService {
    CourierRepo cRepo;

    public CService(CourierRepo cRepo) {
        this.cRepo = cRepo;
    }

    public ArrayList<Courier> getCouriers() {
        return cRepo.getAll();
    }

    public void addCourier(Courier c) {
        cRepo.addCourier(c);
    }

    public void removeCourier(int id) {
        cRepo.remove(id);
    }
}
