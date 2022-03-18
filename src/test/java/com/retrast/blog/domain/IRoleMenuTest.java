package com.retrast.blog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retrast.blog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IRoleMenuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IRoleMenu.class);
        IRoleMenu iRoleMenu1 = new IRoleMenu();
        iRoleMenu1.setId(1L);
        IRoleMenu iRoleMenu2 = new IRoleMenu();
        iRoleMenu2.setId(iRoleMenu1.getId());
        assertThat(iRoleMenu1).isEqualTo(iRoleMenu2);
        iRoleMenu2.setId(2L);
        assertThat(iRoleMenu1).isNotEqualTo(iRoleMenu2);
        iRoleMenu1.setId(null);
        assertThat(iRoleMenu1).isNotEqualTo(iRoleMenu2);
    }
}
