package me.roybailey.data.schema;


import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.JUnitBDDSoftAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
public class ProductTest {

    @Rule
    public TestName name = new TestName();

    @Rule
    public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();

    @Before
    public void banner() {
        log.info("------------------------------------------------------------");
        log.info(this.getClass().getSimpleName()+"."+name.getMethodName()+"()");
        log.info("------------------------------------------------------------");
    }


    @Test
    public void testDtoBuilders() {

        ProductDto productDto = ProductDto.builder()
                .name("test product")
                .description("test description")
                .brand("test brand")
                .price(BigDecimal.TEN)
                .category(ImmutableList.of("one", "two", "three"))
                .build();

        softly.then(productDto)
                .hasNoNullFieldsOrPropertiesExcept("id");
    }
}
