package ch.shkb.guidedcounselling.consultation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootApplication
class CommonAccessTest {

    @Test
    void accessCommon(){
        CommonAccess commonAccess = new CommonAccess();
        assertThat(commonAccess.getUserId().asString()).isEqualTo("12");
    }

}
