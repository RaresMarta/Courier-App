package Service;

import Domain.Package;
import Domain.Courier;
import Domain.Observer;
import Repository.PackageRepo;

import java.util.List;
import java.util.function.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PService {
    PackageRepo pRepo;
    private final List<Observer> observers = new ArrayList<>();

    public PService(PackageRepo pRepo) {
        this.pRepo = pRepo;
    }

    public void addObserver(Observer o) {
        observers.add(o);
    }

    private void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }

    public ArrayList<Package> getPackages() {
        return pRepo.getAll();
    }

    public ArrayList<Package> getUndelivered() {
        return new ArrayList<>(pRepo.getAll().stream().filter((Package p) -> (!p.getStatus())).toList());
    }

    public void addPackage(Package p) {
        pRepo.add(p);
        notifyObservers();
    }

    public void removePackage(int id) {
        pRepo.remove(id);
        notifyObservers();
    }

    public void updatePackage(Package p) {
        pRepo.update(p);
        notifyObservers();
    }

    public ArrayList<Package> getCourierPackages(Courier c) {
        // Get undelivered packages assigned to the given courier
        Predicate<Package> pred = (Package p) -> !p.getStatus() && p.belongsTo(c);
        return new ArrayList<>(pRepo.getAll().stream().filter(pred).toList());
    }

    public ArrayList<String> getAllStreets(ArrayList<Package> packages) {
        // Set of all unique streets in the given list of packages
        return new ArrayList<>(packages.stream().map(Package::getAddress).collect(Collectors.toSet()));
    }
}
