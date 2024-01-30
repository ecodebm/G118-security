package kz.bitlab.G118security.service;

import kz.bitlab.G118security.client.ChaserClient;
import kz.bitlab.G118security.dto.ItemDto;
import kz.bitlab.G118security.mapper.ItemMapper;
import kz.bitlab.G118security.model.Item;
import kz.bitlab.G118security.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    private final ChaserClient chaserClient;

    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    public Item create(Item item) {
        String mark = calculateMark(item.getPoint());
        item.setMark(mark);
        return itemRepository.save(item);
    }

    private String calculateMark(Integer point) {
        if (point == null) {
            return null;
        }
        switch (point) {
            case 1:
            case 2:
                return "B";
            case 3:
                return "NB";
            case 4:
            case 5:
                return "G";
            default:
                return null;
        }
    }

    public Item editItem(Item item) {
        if (item == null) {
            return null;
        }

        Item oldItem = getItemById(item.getId());

        if (oldItem == null) {
            return null;
        }

        if (!item.getPoint().equals(oldItem.getPoint())) {
            String mark = calculateMark(item.getPoint());
            item.setMark(mark);
        }

        return itemRepository.save(item);
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    public void deleteItemById(Long id) {
        itemRepository.deleteById(id);
    }

    public Item editMark(Long id, Integer point) {
        Item item = getItemById(id);
        String mark = calculateMark(point);
        item.setPoint(point);
        item.setMark(mark);
        return itemRepository.save(item);
    }

    public List<Item> createAllItems() {
        log.info("Save items from chaser is started!");
        List<ItemDto> dtoList = chaserClient.getItems();
        List<Item> items = ItemMapper.INSTANCE.toEntityList(dtoList);
        List<Item> itemsList = itemRepository.saveAll(items);
        log.info("Items from chaser added successfully");
        return itemsList;
    }
}
