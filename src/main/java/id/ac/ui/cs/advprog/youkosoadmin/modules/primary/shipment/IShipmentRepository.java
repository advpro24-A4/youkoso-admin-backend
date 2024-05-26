package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.shipment;

import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IShipmentRepository extends JpaRepository<Shipment, Long> {
}
