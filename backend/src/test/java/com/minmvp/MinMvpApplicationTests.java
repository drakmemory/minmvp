package com.minmvp;

import com.minmvp.common.Result;
import com.minmvp.common.PageResult;
import com.minmvp.util.JwtUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * MinMVP Application Tests
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MinMvpApplicationTests {

    @Test
    public void contextLoads() {
        // Verify Spring context loads successfully
    }
    
    @Test
    public void testResultSuccess() {
        Result<String> result = Result.success("test data");
        assertEquals(Integer.valueOf(200), result.getCode());
        assertEquals("test data", result.getData());
    }
    
    @Test
    public void testResultError() {
        Result<?> result = Result.error("error message");
        assertEquals(Integer.valueOf(500), result.getCode());
        assertEquals("error message", result.getMsg());
    }
    
    @Test
    public void testResultErrorWithCode() {
        Result<?> result = Result.error(401, "unauthorized");
        assertEquals(Integer.valueOf(401), result.getCode());
        assertEquals("unauthorized", result.getMsg());
    }
}
