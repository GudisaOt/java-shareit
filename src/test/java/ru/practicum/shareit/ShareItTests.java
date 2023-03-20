package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@SpringBootTest
class ShareItTests {

	@Autowired
	private  ItemMapper mapper;
	@Test
	void contextLoads() {
	}

	@Test
	public void shouldMappingItemToDtoTest() {
		Item item = Item.builder()
				.name("сладкий рулет")
				.description("очень сладкий рулет")
				.available(true)
				.owner(User.builder()
						.name("Пекарь")
						.email("povar@mail.com")
						.build())
				.build();

		ItemDto itemDto = mapper.toItemDto(item);

		Assertions.assertNotNull(itemDto);
		Assertions.assertEquals(item.getName(), itemDto.getName());
		Assertions.assertEquals(item.getDescription(), itemDto.getDescription());
	}

}
