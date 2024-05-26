package id.ac.ui.cs.advprog.youkosoadmin.models.primary.factory;

import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Shipment;

public interface ShipmentFactory {
     Shipment createShipment(Long orderId, String shipmentMethod, String trackingNumber, String shipmentStatus);
     String generateTrackingNumber(String shipmentMethod);
}
