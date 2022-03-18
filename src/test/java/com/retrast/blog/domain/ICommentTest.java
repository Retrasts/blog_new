package com.retrast.blog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retrast.blog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ICommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IComment.class);
        IComment iComment1 = new IComment();
        iComment1.setId(1L);
        IComment iComment2 = new IComment();
        iComment2.setId(iComment1.getId());
        assertThat(iComment1).isEqualTo(iComment2);
        iComment2.setId(2L);
        assertThat(iComment1).isNotEqualTo(iComment2);
        iComment1.setId(null);
        assertThat(iComment1).isNotEqualTo(iComment2);
    }
}
