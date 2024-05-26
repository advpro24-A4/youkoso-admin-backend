package id.ac.ui.cs.advprog.youkosoadmin.models.primary.factory;

import id.ac.ui.cs.advprog.youkosoadmin.exceptions.BadRequestException;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Shipment;
import org.springframework.stereotype.Component;

@Component
public class ShipmentFactoryImpl implements ShipmentFactory {

    @Override
    public Shipment createShipment(Long orderId, String shipmentMethod, String trackingNumber, String shipmentStatus) {
        validateTrackingNumber(shipmentMethod, trackingNumber);
        return new Shipment(orderId, shipmentMethod, trackingNumber, shipmentStatus);
    }

    private void validateTrackingNumber(String shipmentMethod, String trackingNumber) {
        switch (shipmentMethod) {
            case "JTE":
                if (!trackingNumber.matches("^JTE-\\d{12}$")) {
                    throw new IllegalArgumentException("Invalid JTE tracking number");
                }
                break;
            case "Go-bek":
                if (!trackingNumber.matches("^GBK-[A-Z0-9]{12}$")) {
                    throw new BadRequestException("Invalid Go-bek tracking number");
                }
                break;
            case "SiWuzz":
                if (!trackingNumber.matches("^SWZ-[A-Z]{12}$")) {
                    throw new BadRequestException("Invalid SiWuzz tracking number");
                }
                break;
            default:
                throw new BadRequestException("Invalid shipment method");
        }
    }

    public String generateTrackingNumber(String shipmentMethod) {
        return switch (shipmentMethod) {
            case "JTE" -> "JTE-" + generateRandomDigits();
            case "Go-bek" -> "GBK-" + generateRandomAlphanumeric();
            case "SiWuzz" -> "SWZ-" + generateRandomUppercase();
            default -> throw new BadRequestException("Invalid shipment method");
        };
    }

    private String generateRandomDigits() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            result.append((int) (Math.random() * 10));
        }
        return result.toString();
    }

    private String generateRandomAlphanumeric() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            result.append(characters.charAt((int) (Math.random() * characters.length())));
        }
        return result.toString();
    }

    private String generateRandomUppercase() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            result.append(characters.charAt((int) (Math.random() * characters.length())));
        }
        return result.toString();
    }
}
