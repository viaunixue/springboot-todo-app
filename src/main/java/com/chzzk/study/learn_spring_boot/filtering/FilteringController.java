package com.chzzk.study.learn_spring_boot.filtering;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class FilteringController {

//    @GetMapping("/filtering")
//    public SomeBean filtering() {
//        return new SomeBean("value1", "value2", "value3");
//    }

    @GetMapping("/dynamic-filtering")
    public MappingJacksonValue dynamicFiltering() {

        SomeBean someBean = new SomeBean("value1", "value2", "value3");

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(someBean);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field1", "field3");
        FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }

//    @GetMapping("/filtering-list")
//    public List<SomeBean> filteringList() {
//
//        return Arrays.asList(new SomeBean("value1", "value2", "value3"),
//                new SomeBean("value4", "value5", "value6"));
//    }

    @GetMapping("/dynamic-filtering-list")
    public MappingJacksonValue dynamicFilteringList() {

        List<SomeBean> someBean = Arrays.asList(new SomeBean("value1", "value2", "value3"),
                new SomeBean("value4", "value5", "value6"));

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(someBean);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field2", "field3");
        FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);
        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }
}
