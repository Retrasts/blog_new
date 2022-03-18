package com.retrast.blog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retrast.blog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IUserRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IUserRole.class);
        IUserRole iUserRole1 = new IUserRole();
        iUserRole1.setId(1L);
        IUserRole iUserRole2 = new IUserRole();
        iUserRole2.setId(iUserRole1.getId());
        assertThat(iUserRole1).isEqualTo(iUserRole2);
        iUserRole2.setId(2L);
        assertThat(iUserRole1).isNotEqualTo(iUserRole2);
        iUserRole1.setId(null);
        assertThat(iUserRole1).isNotEqualTo(iUserRole2);
    }
}
