package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.shipment;

import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Shipment;

public interface IShipmentService {
    Shipment createShipment(Long orderId, String shipmentMethod);
    Shipment arriveShipment(Long orderId);
}
