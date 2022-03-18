package com.retrast.blog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retrast.blog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IClassifyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IClassify.class);
        IClassify iClassify1 = new IClassify();
        iClassify1.setId(1L);
        IClassify iClassify2 = new IClassify();
        iClassify2.setId(iClassify1.getId());
        assertThat(iClassify1).isEqualTo(iClassify2);
        iClassify2.setId(2L);
        assertThat(iClassify1).isNotEqualTo(iClassify2);
        iClassify1.setId(null);
        assertThat(iClassify1).isNotEqualTo(iClassify2);
    }
}
