package Api.Exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class WebApiExceptionTest {
    @Test
    void ConstructorsTest(){
        //simple not null test
        Exception e = new WebApiException();
        assertNotNull(e, "The default constructor should never return null");

        //simply test if the message is carried
        e = new WebApiException("Simple Test");
        assertTrue(e.getMessage().equals("Simple Test"));
    }
}