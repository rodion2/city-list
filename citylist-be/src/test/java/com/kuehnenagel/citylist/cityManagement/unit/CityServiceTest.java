package com.kuehnenagel.citylist.cityManagement.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.kuehnenagel.citylist.common.error.ServiceException;
import com.kuehnenagel.citylist.features.citymanagement.City;
import com.kuehnenagel.citylist.features.citymanagement.CityDto;
import com.kuehnenagel.citylist.features.citymanagement.CityMapper;
import com.kuehnenagel.citylist.features.citymanagement.CityRepository;
import com.kuehnenagel.citylist.features.citymanagement.CitySearchDto;
import com.kuehnenagel.citylist.features.citymanagement.CityService;
import com.kuehnenagel.citylist.features.citymanagement.CityValidator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class CityServiceTest {

    @Mock
    private CityMapper mapper;

    @Mock
    private CityValidator validator;

    @Mock
    private CityRepository repository;

    @InjectMocks
    private CityService cityService;

    @Test
    void fetchEmptyPageWhenNoCitiesWereFound() {
        CitySearchDto searchDto = CitySearchDto.builder().pageNumber(0).pageSize(1).sortBy("name").build();

        when(repository.findAll(((Specification<City>) any()), ((PageRequest) any())))
                .thenReturn(new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 1), 1));

        Page<CityDto> cities = cityService.findCities(searchDto);

        verify(repository, times(1)).findAll(((Specification<City>) any()),
                ((PageRequest) any()));
        assertEquals(0, cities.getContent().size());
    }

    @Test
    void successfullyFetchOneCityWhenOnlyOneWasRequested() {
        City city = new City();
        city.setId(1L);
        city.setName("Tokyo");
        city.setPhotoLink("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers.jpg");
        List<City> cityList = List.of(city);
        CitySearchDto searchDto = CitySearchDto.builder().pageNumber(0).pageSize(1).sortBy("name").build();
        CityDto cityDto = CityDto.builder()
                .id(1L)
                .name("Tokyo")
                .photoLink("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers.jpg")
                .build();
        PageImpl<City> page = new PageImpl<>(cityList, PageRequest.of(0, 1), 1);

        when(repository.findAll(((Specification<City>) any()), ((PageRequest) any()))).thenReturn(page);
        when(mapper.map(city)).thenReturn(cityDto);

        Page<CityDto> cities = cityService.findCities(searchDto);

        verify(repository, times(1)).findAll(((Specification<City>) any()),
                ((PageRequest) any())
        );
        assertEquals(1, cities.getContent().size());
        assertEquals(1, cities.getContent().stream().findFirst().get().getId());
        assertEquals("Tokyo", cities.getContent().stream().findFirst().get().getName());
        assertEquals("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers.jpg",
                cities.getContent().stream().findFirst().get().getPhotoLink());
    }

    @Test
    void successfullyFetchThreeCitiesWhenThreeCitiesWereRequested() {
        List<City> cityList = List.of(new City(), new City(), new City());
        CitySearchDto searchDto = CitySearchDto.builder().pageNumber(0).pageSize(1).sortBy("name").build();
        PageImpl<City> page = new PageImpl<>(cityList, PageRequest.of(0, 3), 3);

        when(repository.findAll(((Specification<City>) any()), ((PageRequest) any())))
                .thenReturn(page);
        when(mapper.map(any(City.class))).thenReturn(CityDto.builder().build());

        Page<CityDto> cities = cityService.findCities(searchDto);

        verify(repository, times(1)).findAll(((Specification<City>) any()),
                ((PageRequest) any()));
        assertEquals(3, cities.getContent().size());
    }

    @Test
    void successfullyUpdateCityNameAndPhotoLinkWithBothValuesValid() {
        CityDto updateDto = CityDto.builder()
                .id(1L)
                .name("Kyoto")
                .photoLink("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/SkyscrapersKyoto.jpg")
                .build();

        City city = new City();
        city.setId(1L);
        city.setName("Tokyo");
        city.setPhotoLink("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/SkyscrapersTokyo.jpg");

        City updatedCity = new City();
        city.setId(1L);
        city.setName("Kyoto");
        city.setPhotoLink("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/SkyscrapersKyoto.jpg");

        doNothing().when(validator).validate(updateDto);
        when(repository.findById(1L)).thenReturn(Optional.of(city));
        when(repository.save(any(City.class))).thenReturn(updatedCity);
        when(mapper.map(any(City.class))).thenReturn(updateDto);

        CityDto cityDto = cityService.updateCity(updateDto);

        verify(repository, times(1)).findById(1L);
        verify(repository,times(1)).save(any(City.class));

        assertEquals("Kyoto", cityDto.getName());
        assertEquals("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/SkyscrapersKyoto.jpg",
                cityDto.getPhotoLink());
    }

    @Test
    void cityIsNotUpdatedWhenCityNotFoundErrorOccurs() {
        CityDto updateDto = CityDto.builder()
                .id(1L)
                .name("Kyoto")
                .photoLink("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/SkyscrapersKyoto.jpg")
                .build();

        when(repository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(ServiceException.class, () -> cityService.updateCity(updateDto));

        verify(repository, times(1)).findById(1L);
        verify(repository,times(0)).save(any(City.class));
    }

}
