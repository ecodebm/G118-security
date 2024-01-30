package kz.bitlab.G118security.mapper;

import kz.bitlab.G118security.dto.ItemDto;
import kz.bitlab.G118security.model.Item;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true))
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    List<Item> toEntityList(List<ItemDto> dtoList);

    @Mapping(source = "price", target = "price", qualifiedByName = "price")
    Item toEntity(ItemDto dto);

    default LocalDateTime mapToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    @Named("price")
    default BigDecimal mapToBigDecimal(String price) {
        if (price == null) {
            return null;
        }
        price = price.replaceAll("[a-zA-Zа-яА-Я\\s]", "");
        return new BigDecimal(price);
    }

}