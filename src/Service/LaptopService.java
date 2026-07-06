package Service;

import model.Laptop;
import Repository.LaptopRepository;

import java.util.List;

public class LaptopService {

    private final LaptopRepository laptopRepository;

    public LaptopService() {
        this.laptopRepository = new LaptopRepository();
    }

    public List<Laptop> getAllLaptops() {
        return laptopRepository.getAllLaptops();
    }

    public Laptop getLaptopById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        return laptopRepository.getLaptopById(id.trim());
    }

    public boolean addLaptop(Laptop laptop) {
        if (!isLaptopValid(laptop)) {
            return false;
        }

        if (isLaptopIdExists(laptop.getId())) {
            return false;
        }

        laptopRepository.addLaptop(laptop);
        return true;
    }

    public boolean updateLaptop(Laptop laptop) {
        if (!isLaptopValid(laptop)) {
            return false;
        }

        Laptop existingLaptop = laptopRepository.getLaptopById(laptop.getId());
        if (existingLaptop == null) {
            return false;
        }

        laptopRepository.updateLaptop(laptop);
        return true;
    }

    public boolean deleteLaptop(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }

        Laptop existingLaptop = laptopRepository.getLaptopById(id.trim());
        if (existingLaptop == null) {
            return false;
        }

        laptopRepository.deleteLaptop(id.trim());
        return true;
    }

    public boolean isLaptopIdExists(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return laptopRepository.getLaptopById(id.trim()) != null;
    }

    public boolean isLaptopValid(Laptop laptop) {
        if (laptop == null) {
            return false;
        }

        return isNotBlank(laptop.getId())
                && isNotBlank(laptop.getCustomerId())
                && isNotBlank(laptop.getMerk())
                && isNotBlank(laptop.getTipe())
                && isNotBlank(laptop.getSerialNumber())
                && isNotBlank(laptop.getKeluhan());
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
