package kz.bitlab.G118security.client;

import kz.bitlab.G118security.dto.ItemDto;
import kz.bitlab.G118security.model.Item;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "chaserClient", url = "${chaser.manager}")
public interface ChaserClient {

    @GetMapping
    List<ItemDto> getItems();
}
