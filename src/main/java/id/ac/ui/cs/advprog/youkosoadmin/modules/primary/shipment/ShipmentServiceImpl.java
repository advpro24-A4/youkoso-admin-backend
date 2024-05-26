package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.shipment;

import id.ac.ui.cs.advprog.youkosoadmin.exceptions.BadRequestException;
import id.ac.ui.cs.advprog.youkosoadmin.exceptions.NotFoundException;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Order;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Shipment;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.factory.ShipmentFactory;
import id.ac.ui.cs.advprog.youkosoadmin.modules.primary.order.IOrderRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipmentServiceImpl implements IShipmentService {
    private final IShipmentRepository shipmentRepository;
    private final ShipmentFactory shipmentFactory;
    private final IOrderRepository orderRepository;

    @Autowired
    public ShipmentServiceImpl(IOrderRepository orderRepository, IShipmentRepository shipmentRepository, ShipmentFactory shipmentFactory) {
        this.orderRepository = orderRepository;
        this.shipmentRepository = shipmentRepository;
        this.shipmentFactory = shipmentFactory;
    }

    @Override
    @Transactional("primaryTransactionManager")
    public Shipment createShipment(Long orderId, String shipmentMethod) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        if (order.getShipment() != null) {
            throw new BadRequestException("Shipment already exists");
        }


        if (!order.getStatus().equals("WAITING_SHIPMENT")) {
            throw new BadRequestException("You can only create shipment for order with status WAITING_SHIPMENT");
        }

        String trackingNumber = shipmentFactory.generateTrackingNumber(shipmentMethod);
        Shipment shipment = shipmentFactory.createShipment(orderId, shipmentMethod, trackingNumber, "PENDING");

        order.setShipment(shipment);
        order.setStatus("SHIPPED");
        orderRepository.save(order);

        shipment = shipmentRepository.save(shipment);
        return shipment;
    }

    @Override
    @Transactional("primaryTransactionManager")
    public Shipment arriveShipment(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        Shipment shipment = order.getShipment();

        if (shipment == null) {
            throw new BadRequestException("Shipment not found");
        }

        if (!order.getStatus().equals("SHIPPED")) {
            throw new BadRequestException("You can only arrive shipment for order with status SHIPPED");
        }

        if (shipment.getShipmentStatus().equals("ARRIVED")) {
            throw new BadRequestException("Shipment already arrived");
        }

        shipment.setShipmentStatus("ARRIVED");
        shipment = shipmentRepository.save(shipment);

        order.setStatus("ARRIVED");
        orderRepository.save(order);

        return shipment;
    }
}
