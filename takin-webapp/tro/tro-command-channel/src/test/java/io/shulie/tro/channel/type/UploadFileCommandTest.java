package io.shulie.tro.channel.type;


import org.junit.Test;

public class UploadFileCommandTest {


    @Test
    public void test(){
        UploadFileCommand fileCommand = new UploadFileCommand();
        System.out.println(fileCommand.getId());
        System.out.println(fileCommand.getDesc());
    }

}