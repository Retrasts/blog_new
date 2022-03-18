package com.retrast.blog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retrast.blog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IUser.class);
        IUser iUser1 = new IUser();
        iUser1.setId(1L);
        IUser iUser2 = new IUser();
        iUser2.setId(iUser1.getId());
        assertThat(iUser1).isEqualTo(iUser2);
        iUser2.setId(2L);
        assertThat(iUser1).isNotEqualTo(iUser2);
        iUser1.setId(null);
        assertThat(iUser1).isNotEqualTo(iUser2);
    }
}
