package io.shulie.tro.channel.type;

/**
 * @author: HengYu
 * @className: UploadFileCommand
 * @date: 2020/12/29 10:20 下午
 * @description: 上传文件命令对象
 */
public class UploadFileCommand extends Command {

    public UploadFileCommand() {
        this.setId(this.getClass().getName());
        this.setDesc("upload file command");
    }

}