package com.fokkog.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.fokkog.web.rest.TestUtil;

public class TableOfPrizesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TableOfPrizes.class);
        TableOfPrizes tableOfPrizes1 = new TableOfPrizes();
        tableOfPrizes1.setId(1L);
        TableOfPrizes tableOfPrizes2 = new TableOfPrizes();
        tableOfPrizes2.setId(tableOfPrizes1.getId());
        assertThat(tableOfPrizes1).isEqualTo(tableOfPrizes2);
        tableOfPrizes2.setId(2L);
        assertThat(tableOfPrizes1).isNotEqualTo(tableOfPrizes2);
        tableOfPrizes1.setId(null);
        assertThat(tableOfPrizes1).isNotEqualTo(tableOfPrizes2);
    }
}
