package com.retrast.blog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retrast.blog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IMenuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IMenu.class);
        IMenu iMenu1 = new IMenu();
        iMenu1.setId(1L);
        IMenu iMenu2 = new IMenu();
        iMenu2.setId(iMenu1.getId());
        assertThat(iMenu1).isEqualTo(iMenu2);
        iMenu2.setId(2L);
        assertThat(iMenu1).isNotEqualTo(iMenu2);
        iMenu1.setId(null);
        assertThat(iMenu1).isNotEqualTo(iMenu2);
    }
}
