package com.retrast.blog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retrast.blog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ILabelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ILabel.class);
        ILabel iLabel1 = new ILabel();
        iLabel1.setId(1L);
        ILabel iLabel2 = new ILabel();
        iLabel2.setId(iLabel1.getId());
        assertThat(iLabel1).isEqualTo(iLabel2);
        iLabel2.setId(2L);
        assertThat(iLabel1).isNotEqualTo(iLabel2);
        iLabel1.setId(null);
        assertThat(iLabel1).isNotEqualTo(iLabel2);
    }
}
