package top.stillmisty.shopback.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/order")
@Tag(name = "管理员-订单管理接口")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {
    
}
