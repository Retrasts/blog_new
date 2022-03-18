package com.retrast.blog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retrast.blog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IBlogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IBlog.class);
        IBlog iBlog1 = new IBlog();
        iBlog1.setId(1L);
        IBlog iBlog2 = new IBlog();
        iBlog2.setId(iBlog1.getId());
        assertThat(iBlog1).isEqualTo(iBlog2);
        iBlog2.setId(2L);
        assertThat(iBlog1).isNotEqualTo(iBlog2);
        iBlog1.setId(null);
        assertThat(iBlog1).isNotEqualTo(iBlog2);
    }
}
