package com.retrast.blog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retrast.blog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IRole.class);
        IRole iRole1 = new IRole();
        iRole1.setId(1L);
        IRole iRole2 = new IRole();
        iRole2.setId(iRole1.getId());
        assertThat(iRole1).isEqualTo(iRole2);
        iRole2.setId(2L);
        assertThat(iRole1).isNotEqualTo(iRole2);
        iRole1.setId(null);
        assertThat(iRole1).isNotEqualTo(iRole2);
    }
}
