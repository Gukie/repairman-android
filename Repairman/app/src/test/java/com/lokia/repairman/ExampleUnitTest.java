package com.lokia.repairman;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lokia.repairman.model.ResponseObject;
import com.lokia.repairman.model.User;
import com.lokia.repairman.utils.RepairmanConverter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testConverter(){
        String str = "{\"content\":{\"gmtCreated\":\"\",\"gmtModified\":\"\",\"id\":15,\"password\":\"gbvcchhyh\",\"phone\":\"15152854456\",\"sex\":\"女\",\"username\":\"夜\"},\"msg\":\"\",\"resultCode\":1}";
//        String str = "{\"content\":\"\",\"msg\":\"\",\"resultCode\":1}";
//        String str = "{\"content\":{},\"msg\":\"\",\"resultCode\":1}";
//        String str = "{\"content\":\"{\"\"gmtCreated\":\"\",\"gmtModified\":\"\",\"id\":15,\"password\":\"gbvcchhyh\",\"phone\":\"15152854456\",\"sex\":\"女\",\"username\":\"夜\"\"}\",\"msg\":\"\",\"resultCode\":1}";
        ResponseObject<User> responseObject = RepairmanConverter.readValue(str,new TypeReference<ResponseObject<User>>(){});
//        ResponseObject responseObject = RepairmanConverter.readValue(str,ResponseObject.class);
       User user = responseObject.getContent();
        assertNotNull(user);
    }
}