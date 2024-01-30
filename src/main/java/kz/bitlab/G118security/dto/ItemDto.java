package kz.bitlab.G118security.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ItemDto {
    private String name;
    private String price;
    private String description;
    private Date createdAt;
    private Date updatedAt;

}
